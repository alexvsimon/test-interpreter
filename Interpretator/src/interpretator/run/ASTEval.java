package interpretator.run;

import interpretator.api.run.InterpreterRuntimeError;
import interpretator.api.run.CanceledRuntimeError;
import interpretator.api.run.SequenceValue;
import interpretator.api.run.DoubleValue;
import interpretator.api.run.IntegerValue;
import interpretator.api.run.Value;
import interpretator.api.ast.AST;
import interpretator.api.ast.ASTKind;
import interpretator.api.ast.BinaryExpressionAST;
import interpretator.api.ast.LambdaAST;
import interpretator.api.ast.MapAST;
import interpretator.api.ast.NumberAST;
import interpretator.api.ast.OutAST;
import interpretator.api.ast.PrintAST;
import interpretator.api.ast.ProgramAST;
import interpretator.api.ast.ReduceAST;
import interpretator.api.ast.SequenceAST;
import interpretator.api.ast.UnaryExpressionAST;
import interpretator.api.ast.VarAST;
import interpretator.api.ast.VariableAST;
import interpretator.api.run.ValueKind;
import interpretator.Output;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author alex
 */
public class ASTEval {

    private final AST root;
    private final Map<String, Value> vars = new HashMap<>();
    private final AtomicBoolean canceled;
    private static final int MAX_OUT_SEQUENCE_LENGTH = 100;
    
    /**
     * Creates interpreter for AST.
     * 
     * @param root root AST is a program or lambda
     * @param canceled execution canceler
     */
    public ASTEval(AST root, AtomicBoolean canceled) {
        this.root = root;
        this.canceled = canceled;
    }

    /**
     * Interprets program
     * 
     * @exception CanceledRuntimeError if interpretation is canceled.
     * @exception InterpreterRuntimeError if interpreter finds runtime error such as incompatible type,
     *            reference on undefined variable, unsupported operations, and other.
     */
    public void run() {
        run(root);
    }

    /**
     * Interprets lambda.
     * 
     * @param vars input parameters
     * @return value
     * @exception CanceledRuntimeError if interpretation is canceled.
     * @exception InterpreterRuntimeError if interpreter finds runtime error such as incompatible type,
     *            reference on undefined variable, unsupported operations, and other.
     */
    public Value evalLambda(Map<String, Value> vars) {
        assert root.getKind() == ASTKind.Lambda;
        this.vars.putAll(vars);
        return eval(((LambdaAST) root).getBody());
    }

    /**
     * Gets value of variable.
     * 
     * @param name variable name.
     * @return value if variable exists.
     */
    public Value getVariable(String name) {
        return vars.get(name);
    }
    
    private void run(AST ast) {
        switch (ast.getKind()) {
            case Program:
                runProgram((ProgramAST) ast);
                break;
            case Print:
                runPrint((PrintAST) ast);
                break;
            case Out:
                runOut((OutAST) ast);
                break;
            case Var:
                runVar((VarAST) ast);
                break;
        }
    }

    private Value eval(AST ast) {
        if (canceled.get()) {
            throw new CanceledRuntimeError(ast);
        }
        switch (ast.getKind()) {
            case UnaryMinus:
                return evalUnaryExpression((UnaryExpressionAST) ast);
            case Plus:
            case Minus:
            case Mul:
            case Div:
            case Pow:
                return evalExpression((BinaryExpressionAST) ast);
            case Map:
                return evalMap((MapAST) ast);
            case Sequence:
                return evalSequence((SequenceAST) ast);
            case Reduce:
                return evalReduce((ReduceAST) ast);
            case Variable:
                return evalVariable((VariableAST) ast);
            case Number:
                return evalNumber((NumberAST) ast);
        }
        return null;
    }

    private void runProgram(ProgramAST ast) {
        for (AST statement : ast.getStatements()) {
            run(statement);
        }
    }

    private void runPrint(PrintAST ast) {
        Output.getInstance().out(ast.getString());
    }

    private void runOut(OutAST ast) {
        outValue(ast, eval(ast.getExpression()));
    }
    
    private void outValue(OutAST ast, Value value) {
        switch (value.getKind()) {
            case Integer:
                Output.getInstance().out("" + ((IntegerValue) value).getInteger());
                break;
            case Double:
                Output.getInstance().out("" + ((DoubleValue) value).getDouble());
                break;
            case Sequence:
                Output.getInstance().out("{");
                SequenceValue v = (SequenceValue) value;
                for (int i = 0; i < v.getSize(); i++) {
                    if (canceled.get()) {
                        throw new CanceledRuntimeError(ast);
                    }
                    if (i < MAX_OUT_SEQUENCE_LENGTH) {
                        outValue(ast, v.getValueAt(i));
                    } else {
                        Output.getInstance().out("...");
                        break;
                    }
                    if (i < v.getSize() - 1) {
                        Output.getInstance().out(",");
                    }
                }
                Output.getInstance().out("}");
                break;
        }
    }

    private void runVar(VarAST ast) {
        String name = ast.getName();
        Value v = eval(ast.getExpression());
        vars.put(name, v);
    }

    private Value evalUnaryExpression(UnaryExpressionAST ast) {
        assert ast.getKind() == ASTKind.UnaryMinus;
        Value eval = eval(ast.getExpression());
        switch (eval.getKind()) {
            case Integer:
                return new IntegerImpl(-((IntegerValue) eval).getInteger());
            case Double:
                return new DoubleImpl(-((DoubleValue) eval).getDouble());
            default:
                throw new InterpreterRuntimeError("Unsupported unary minus for sequence", ast);
        }
    }

    private Value evalExpression(BinaryExpressionAST ast) {
        Value lh = eval(ast.getLeftExpression());
        if (lh.getKind() == ValueKind.Sequence) {
            throw new InterpreterRuntimeError("Unsupported binary operation for sequence", ast.getLeftExpression());
        }
        Value rh = eval(ast.getRightExpression());
        if (rh.getKind() == ValueKind.Sequence) {
            throw new InterpreterRuntimeError("Unsupported binary operation for sequence", ast.getRightExpression());
        }
        switch (ast.getKind()) {
            case Plus: {
                if ((lh.getKind() == ValueKind.Integer) &&
                    (rh.getKind() == ValueKind.Integer)) {
                    return new IntegerImpl(
                            ((IntegerValue) lh).getInteger() +
                            ((IntegerValue) rh).getInteger());
                }
                double ldh = (lh.getKind() == ValueKind.Integer)
                        ? ((IntegerValue) lh).getInteger() : ((DoubleValue) lh).getDouble();
                double rdh = (rh.getKind() == ValueKind.Integer)
                        ? ((IntegerValue) rh).getInteger() : ((DoubleValue) rh).getDouble();
                return new DoubleImpl(ldh + rdh);
            }
            case Minus: {
                if ((lh.getKind() == ValueKind.Integer) &&
                    (rh.getKind() == ValueKind.Integer)) {
                    return new IntegerImpl(
                            ((IntegerValue) lh).getInteger() -
                            ((IntegerValue) rh).getInteger());
                }
                double ldh = (lh.getKind() == ValueKind.Integer)
                        ? ((IntegerValue) lh).getInteger() : ((DoubleValue) lh).getDouble();
                double rdh = (rh.getKind() == ValueKind.Integer)
                        ? ((IntegerValue) rh).getInteger() : ((DoubleValue) rh).getDouble();
                return new DoubleImpl(ldh - rdh);
            }
            case Mul: {
                if ((lh.getKind() == ValueKind.Integer) &&
                    (rh.getKind() == ValueKind.Integer)) {
                    return new IntegerImpl(
                            ((IntegerValue) lh).getInteger() *
                            ((IntegerValue) rh).getInteger());
                }
                double ldh = (lh.getKind() == ValueKind.Integer)
                        ? ((IntegerValue) lh).getInteger() : ((DoubleValue) lh).getDouble();
                double rdh = (rh.getKind() == ValueKind.Integer)
                        ? ((IntegerValue) rh).getInteger() : ((DoubleValue) rh).getDouble();
                return new DoubleImpl(ldh * rdh);
            }
            case Div: {
                double ldh = (lh.getKind() == ValueKind.Integer)
                        ? ((IntegerValue) lh).getInteger() : ((DoubleValue) lh).getDouble();
                double rdh = (rh.getKind() == ValueKind.Integer)
                        ? ((IntegerValue) rh).getInteger() : ((DoubleValue) rh).getDouble();
                return new DoubleImpl(ldh / rdh);
            }
            case Pow: {
                if (rh.getKind() != ValueKind.Integer) {
                    throw new InterpreterRuntimeError("Unsupported power operation for double", ast.getRightExpression());
                }
                int pow = ((IntegerValue) rh).getInteger();
                if (pow == 0) {
                    return new IntegerImpl(1);
                }
                boolean minus = pow < 1;
                if (pow < 0) {
                    pow = -pow;
                }
                if ((lh.getKind() == ValueKind.Integer)) {
                    int arg = ((IntegerValue) lh).getInteger();
                    if (arg == -1) {
                        return pow % 2 == 0 ? new IntegerImpl(1) : new IntegerImpl(-1);
                    }
                    int res = arg;
                    for (int i = 1; i < pow; i++) {
                        res = res * arg;
                    }
                    if (minus) {
                        return new DoubleImpl(1.0 / res);
                    } else {
                        return new IntegerImpl(res);
                    }
                } else {
                    double arg = ((DoubleValue) lh).getDouble();
                    double res = arg;
                    for (int i = 1; i < pow; i++) {
                        res = res * arg;
                    }
                    if (minus) {
                        return new DoubleImpl(1.0 / res);
                    } else {
                        return new DoubleImpl(res);
                    }
                }
            }
            default:
                throw new InterpreterRuntimeError("Unsupported binary operation", ast);
        }
    }

    private Value evalMap(MapAST ast) {
        Value arg = eval(ast.getInputExpression());
        if (arg.getKind() == ValueKind.Sequence) {
            SequenceValue seq = (SequenceValue) arg;
            return new MappedSequenceImpl(seq, ast.getLambda(), canceled);
        }
        throw new InterpreterRuntimeError("Operator map defined for sequence only", ast);
    }

    private Value evalSequence(SequenceAST ast) {
        Value start = eval(ast.getStartExpression());
        Value end = eval(ast.getEndExpression());
        if ((start.getKind() == ValueKind.Integer) &&
            (end.getKind() == ValueKind.Integer)) {
            return new SequenceImpl(((IntegerValue) start).getInteger(), ((IntegerValue) end).getInteger());
        }
        throw new InterpreterRuntimeError("Sequence defined for integer operands", ast);
    }

    private Value evalReduce(ReduceAST ast) {
        Value seq = eval(ast.getInputExpression());
        Value start = eval(ast.getStartExpression());
        if (seq.getKind() == ValueKind.Sequence) {
            LambdaAST lambda = ast.getLambda();
            if (lambda.getParametersSize() != 2) {
                throw new InterpreterRuntimeError("Labda of operator reduse must have 2 parameters", ast);
            }
            String arg1 = lambda.getParameter(0);
            String arg2 = lambda.getParameter(1);
            for (int i = 0; i < ((SequenceValue) seq).getSize(); i++) {
                if (canceled.get()) {
                    throw new CanceledRuntimeError(ast);
                }
                Map<String, Value> args = new HashMap<>();
                args.put(arg1, start);
                args.put(arg2, ((SequenceValue) seq).getValueAt(i));
                start = new ASTEval(lambda, canceled).evalLambda(args);
            }
            return start;
        }
        throw new InterpreterRuntimeError("First argument of operator reduce must be sequence", ast);
    }

    private Value evalVariable(VariableAST ast) {
        Value value = vars.get(ast.getName());
        if (value == null) {
            throw new InterpreterRuntimeError("Variable '"+ast.getName()+"' is not declared", ast);
        }
        return value;
    }

    private Value evalNumber(NumberAST ast) {
        String value = ast.getValue();
        if (value.indexOf('.') > 0) {
            return new DoubleImpl(Double.parseDouble(value));
        } else {
            try {
                return new IntegerImpl(Integer.parseInt(value));
            } catch (NumberFormatException ex) {
                throw new InterpreterRuntimeError("Integer '"+value+"' is too big", ast);
            }
        }
    }
}
