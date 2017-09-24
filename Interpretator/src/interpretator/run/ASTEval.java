package interpretator.run;

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
import interpretator.output.Output;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author alex
 */
public class ASTEval {

    private final AST root;
    private Map<String, Value> vars = new HashMap<>();

    public ASTEval(AST root) {
        this.root = root;
    }

    public void run() {
        run(root);
    }

    public Value evalLambda(Map<String, Value> vars) {
        assert root.getKind() == ASTKind.Lambda;
        this.vars.putAll(vars);
        return eval(((LambdaAST) root).getBody());
    }

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
        outValue(eval(ast.getExpression()));
    }
    
    private void outValue(Value value) {
        if (value instanceof IntegerValue) {
            Output.getInstance().out("" + ((IntegerValue) value).getInteger());
        } else if (value instanceof DoubleValue) {
            Output.getInstance().out("" + ((DoubleValue) value).getDouble());
        } else if (value instanceof SequenceValue) {
            Output.getInstance().out("{");
            SequenceValue v = (SequenceValue) value;
            for (int i = 0; i < v.getSize(); i++) {
                outValue(v.getValueAt(i));
                if (i < v.getSize() - 1) {
                    Output.getInstance().out(",");
                }
            }
            Output.getInstance().out("}");
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
        if (eval instanceof IntegerValue) {
            return new IntegerImpl(-((IntegerValue) eval).getInteger());
        } else if (eval instanceof DoubleValue) {
            return new DoubleImpl(-((DoubleValue) eval).getDouble());
        } else {
            throw new InterpreterRuntimeError("Unsupported unary minus for sequence", ast);
        }
    }

    private Value evalExpression(BinaryExpressionAST ast) {
        Value lh = eval(ast.getLeftExpression());
        if (!((lh instanceof IntegerValue) ||
              (lh instanceof DoubleValue))) {
            throw new InterpreterRuntimeError("Unsupported binary operation for sequence", ast.getLeftExpression());
        }
        Value rh = eval(ast.getRightExpression());
        if (!((rh instanceof IntegerValue) ||
              (rh instanceof DoubleValue))) {
            throw new InterpreterRuntimeError("Unsupported binary operation for sequence", ast.getRightExpression());
        }
        switch (ast.getKind()) {
            case Plus: {
                if ((lh instanceof IntegerValue) &&
                    (rh instanceof IntegerValue)) {
                    return new IntegerImpl(
                            ((IntegerValue) lh).getInteger()
                            + ((IntegerValue) rh).getInteger());
                }
                double ldh = (lh instanceof IntegerValue)
                        ? ((IntegerValue) lh).getInteger() : ((DoubleValue) lh).getDouble();
                double rdh = (rh instanceof IntegerValue)
                        ? ((IntegerValue) rh).getInteger() : ((DoubleValue) rh).getDouble();
                return new DoubleImpl(ldh + rdh);
            }
            case Minus: {
                if ((lh instanceof IntegerValue) &&
                    (rh instanceof IntegerValue)) {
                    return new IntegerImpl(
                            ((IntegerValue) lh).getInteger()
                            - ((IntegerValue) rh).getInteger());
                }
                double ldh = (lh instanceof IntegerValue)
                        ? ((IntegerValue) lh).getInteger() : ((DoubleValue) lh).getDouble();
                double rdh = (rh instanceof IntegerValue)
                        ? ((IntegerValue) rh).getInteger() : ((DoubleValue) rh).getDouble();
                return new DoubleImpl(ldh - rdh);
            }
            case Mul: {
                if ((lh instanceof IntegerValue) &&
                    (rh instanceof IntegerValue)) {
                    return new IntegerImpl(
                            ((IntegerValue) lh).getInteger()
                            * ((IntegerValue) rh).getInteger());
                }
                double ldh = (lh instanceof IntegerValue)
                        ? ((IntegerValue) lh).getInteger() : ((DoubleValue) lh).getDouble();
                double rdh = (rh instanceof IntegerValue)
                        ? ((IntegerValue) rh).getInteger() : ((DoubleValue) rh).getDouble();
                return new DoubleImpl(ldh * rdh);
            }
            case Div: {
                double ldh = (lh instanceof IntegerValue)
                        ? ((IntegerValue) lh).getInteger() : ((DoubleValue) lh).getDouble();
                double rdh = (rh instanceof IntegerValue)
                        ? ((IntegerValue) rh).getInteger() : ((DoubleValue) rh).getDouble();
                return new DoubleImpl(ldh / rdh);
            }
            case Pow: {
                if (!(rh instanceof IntegerValue)) {
                    throw new IllegalArgumentException();
                }
                int pow = ((IntegerValue) rh).getInteger();
                if (pow == 0) {
                    return new IntegerImpl(1);
                }
                boolean minus = pow < 1;
                if (pow < 0) {
                    pow = -pow;
                }
                if ((lh instanceof IntegerValue)) {
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
        if (arg instanceof SequenceValue) {
            SequenceValue seq = (SequenceValue) arg;
            return new MappedSequenceImpl(seq, ast.getLambda());
        }
        throw new InterpreterRuntimeError("Operator map defined for sequence only", ast);
    }

    private Value evalSequence(SequenceAST ast) {
        Value start = eval(ast.getStartExpression());
        Value end = eval(ast.getEndExpression());
        if ((start instanceof IntegerValue) &&
            (end instanceof IntegerValue)) {
            return new SequenceImpl(((IntegerValue) start).getInteger(), ((IntegerValue) end).getInteger());
        }
        throw new InterpreterRuntimeError("Sequence defined for integer operands", ast);
    }

    private Value evalReduce(ReduceAST ast) {
        Value seq = eval(ast.getInputExpression());
        Value start = eval(ast.getStartExpression());
        if (seq instanceof SequenceValue) {
            LambdaAST lambda = ast.getLambda();
            if (lambda.getParametersSize() != 2) {
                throw new InterpreterRuntimeError("Labda of operator reduse must have 2 parameters", ast);
            }
            String arg1 = lambda.getParameter(0);
            String arg2 = lambda.getParameter(1);
            for (int i = 0; i < ((SequenceValue) seq).getSize(); i++) {
                Map<String, Value> args = new HashMap<>();
                args.put(arg1, start);
                args.put(arg2, ((SequenceValue) seq).getValueAt(i));
                start = new ASTEval(lambda).evalLambda(args);
            }
            return start;
        }
        throw new InterpreterRuntimeError("First argument of operator reduce must be sequence", ast);
    }

    public ASTEval() {
        this.root = null;
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
            return new IntegerImpl(Integer.parseInt(value));
        }
    }
}
