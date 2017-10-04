package interpretator.run;

import interpretator.api.run.InterpreterRuntimeException;
import interpretator.api.run.CanceledRuntimeException;
import interpretator.api.run.SequenceValue;
import interpretator.api.run.Value;
import interpretator.api.run.ValueKind;
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
import interpretator.Output;

/**
 *
 * @author alex
 */
public class ASTEval {

    private final AST root;
    private VarsMap vars;
    
    /**
     * maximum of printed sequence elements
     */
    private static final int MAX_OUT_SEQUENCE_LENGTH = 100;
    
    /**
     * use optimized expression evaluator
     */
    /*package-local*/ static final boolean OPTIMIZE_DOUBLE_ARITHMETIC = true;   
    private boolean hasRecursiveLambda = false;
    
    /**
     * Creates interpreter for AST.
     * 
     * @param root root AST is a program or lambda
     */
    public ASTEval(AST root) {
        this.root = root;
    }

    /**
     * Interprets program
     *
     * @throws CanceledRuntimeException if interpretation is canceled.
     * @throws InterpreterRuntimeException if interpreter finds runtime error such as incompatible type,
            reference on undefined variable, unsupported operations, and other.
     */
    public void run() throws CanceledRuntimeException, InterpreterRuntimeException {
        vars = new VarsMapImpl();
        run(root);
    }

    /**
     * Interprets lambda.
     * 
     * @param vars input parameters map
     * @return value
     * @throws CanceledRuntimeException if interpretation is canceled.
     * @throws InterpreterRuntimeException if interpreter finds runtime error such as incompatible type,
            reference on undefined variable, unsupported operations, and other.
     */
    /*package-local*/ Value evalLambda(VarsMap vars) throws CanceledRuntimeException, InterpreterRuntimeException {
        assert root.getKind() == ASTKind.Lambda;
        this.vars = vars;
        return eval(((LambdaAST) root).getBody());
    }
    
    /**
     * 
     * @return true if lambda call inner lambda
     */
    /*package-local*/ boolean hasRecursiveLambda() {
        return hasRecursiveLambda;
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
        if(Thread.interrupted()) {
            throw new CanceledRuntimeException(ast);
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
        throw new InterpreterRuntimeException("Unsupported operation", ast);
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
                Output.getInstance().out("" + value.getInteger());
                break;
            case Double:
                Output.getInstance().out("" + value.getDouble());
                break;
            case Sequence:
                Output.getInstance().out("{");
                SequenceValue v = (SequenceValue) value;
                for (int i = 0; i < v.getSize(); i++) {
                    if(Thread.interrupted()) {
                        throw new CanceledRuntimeException(ast);
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
                return new IntegerImpl(-eval.getInteger());
            case Double:
                return new DoubleImpl(-eval.getDouble());
            default:
                throw new InterpreterRuntimeException("Unsupported unary minus for sequence", ast);
        }
    }

    private Value evalExpression(BinaryExpressionAST ast) {
        Value lh = eval(ast.getLeftExpression());
        if (lh.getKind() == ValueKind.Sequence) {
            throw new InterpreterRuntimeException("Unsupported binary operation for sequence", ast.getLeftExpression());
        }
        Value rh = eval(ast.getRightExpression());
        if (rh.getKind() == ValueKind.Sequence) {
            throw new InterpreterRuntimeException("Unsupported binary operation for sequence", ast.getRightExpression());
        }
        switch (ast.getKind()) {
            case Plus: {
                if ((lh.getKind() == ValueKind.Integer) &&
                    (rh.getKind() == ValueKind.Integer)) {
                    return new IntegerImpl(lh.getInteger() + rh.getInteger());
                }
                double ldh = (lh.getKind() == ValueKind.Integer) ? lh.getInteger() : lh.getDouble();
                double rdh = (rh.getKind() == ValueKind.Integer) ? rh.getInteger() : rh.getDouble();
                return new DoubleImpl(ldh + rdh);
            }
            case Minus: {
                if ((lh.getKind() == ValueKind.Integer) &&
                    (rh.getKind() == ValueKind.Integer)) {
                    return new IntegerImpl(lh.getInteger() - rh.getInteger());
                }
                double ldh = (lh.getKind() == ValueKind.Integer) ? lh.getInteger() : lh.getDouble();
                double rdh = (rh.getKind() == ValueKind.Integer) ? rh.getInteger() : rh.getDouble();
                return new DoubleImpl(ldh - rdh);
            }
            case Mul: {
                if ((lh.getKind() == ValueKind.Integer) &&
                    (rh.getKind() == ValueKind.Integer)) {
                    int a = lh.getInteger();
                    int b = rh.getInteger();
                    try {
                        return new IntegerImpl(Math.multiplyExact(a,b));
                    } catch (ArithmeticException ex) {
                        // count as double
                    }
                }
                double ldh = (lh.getKind() == ValueKind.Integer) ? lh.getInteger() : lh.getDouble();
                double rdh = (rh.getKind() == ValueKind.Integer) ? rh.getInteger() : rh.getDouble();
                return new DoubleImpl(ldh * rdh);
            }
            case Div: {
                double ldh = (lh.getKind() == ValueKind.Integer) ? lh.getInteger() : lh.getDouble();
                double rdh = (rh.getKind() == ValueKind.Integer) ? rh.getInteger() : rh.getDouble();
                return new DoubleImpl(ldh / rdh);
            }
            case Pow: {
                if (rh.getKind() != ValueKind.Integer) {
                    return new DoubleImpl(Math.pow((lh.getKind() == ValueKind.Integer) ? lh.getInteger() : lh.getDouble(),
                            (rh.getKind() == ValueKind.Integer) ? rh.getInteger() : rh.getDouble()));
                }
                int pow = rh.getInteger();
                if (pow == 0) {
                    return new IntegerImpl(1);
                }
                boolean minus = pow < 1;
                if (pow < 0) {
                    pow = -pow;
                }
                if ((lh.getKind() == ValueKind.Integer)) {
                    int arg = lh.getInteger();
                    if (arg == -1) {
                        return pow % 2 == 0 ? new IntegerImpl(1) : new IntegerImpl(-1);
                    }
                    int res = arg;
                    try {
                        for (int i = 1; i < pow; i++) {
                            res = Math.multiplyExact(res, arg);
                        }
                        if (minus) {
                            return new DoubleImpl(1.0 / res);
                        } else {
                            return new IntegerImpl(res);
                        }
                    } catch (ArithmeticException ex) {
                        // count as double
                    }
                }
                double arg = (lh.getKind() == ValueKind.Integer) ? lh.getInteger() : lh.getDouble();
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
            default:
                throw new InterpreterRuntimeException("Unsupported binary operation", ast);
        }
    }

    private Value evalMap(MapAST ast) {
        Value arg = eval(ast.getInputExpression());
        if (arg.getKind() == ValueKind.Sequence) {
            LambdaAST lambda = ast.getLambda();
            if (lambda.getParametersSize() != 1) {
                throw new InterpreterRuntimeException("Lambda of operator map must have 1 parameter", ast);
            }
            hasRecursiveLambda = true;
            SequenceValue seq = (SequenceValue) arg;
            return new MappedSequenceImpl(seq, lambda);
        }
        throw new InterpreterRuntimeException("Operator map defined for sequence only", ast);
    }

    private Value evalSequence(SequenceAST ast) {
        Value start = eval(ast.getStartExpression());
        Value end = eval(ast.getEndExpression());
        if ((start.getKind() == ValueKind.Integer) &&
            (end.getKind() == ValueKind.Integer)) {
            return new SequenceImpl(start.getInteger(), end.getInteger());
        }
        throw new InterpreterRuntimeException("Sequence defined for integer operands", ast);
    }

    private Value evalReduce(ReduceAST ast) {
        Value seq = eval(ast.getInputExpression());
        Value start = eval(ast.getStartExpression());
        if (seq.getKind() == ValueKind.Sequence) {
            SequenceValue sequense = (SequenceValue) seq;
            LambdaAST lambda = ast.getLambda();
            if (lambda.getParametersSize() != 2) {
                throw new InterpreterRuntimeException("Lambda of operator reduse must have 2 parameters", ast);
            }
            hasRecursiveLambda = true;
            String arg1 = lambda.getParameter(0);
            String arg2 = lambda.getParameter(1);
            boolean numericSequence = OPTIMIZE_DOUBLE_ARITHMETIC;
            for (int i = 0; i < ((SequenceValue) seq).getSize() && i < 10; i++) {
                if(Thread.interrupted()) {
                    throw new CanceledRuntimeException(ast);
                }
                Value currentSeq = sequense.getValueAt(i);
                if (currentSeq.getKind() == ValueKind.Sequence) {
                    numericSequence = false;
                }
                final ASTEval eval = new ASTEval(lambda);
                start = eval.evalLambda(new TwoVarMap(arg1, start, arg2, currentSeq));
                if (eval.hasRecursiveLambda) {
                    numericSequence = false;
                }
            }
            if (numericSequence && (start.getKind() == ValueKind.Double)) {
                double doubleStart = start.getDouble();
                final DoubleEval doubleEval = new DoubleEval(lambda);
                for (int i = 10; i < ((SequenceValue) seq).getSize(); i++) {
                    if(Thread.interrupted()) {
                        throw new CanceledRuntimeException(ast);
                    }
                    Value currentSeq = sequense.getValueAt(i);
                    double currendValue;
                    switch (currentSeq.getKind()) {
                        case Integer:
                            currendValue = currentSeq.getInteger();
                            break;
                        case Double:
                            currendValue = currentSeq.getDouble();
                            break;
                        default:
                            throw new InterpreterRuntimeException("Sequence has number and sequence elements", ast);
                    }
                    doubleStart = doubleEval.evalDoubleLambda(new TwoDoubleVarsMap(arg1, doubleStart, arg2, currendValue));
                }
                start = new DoubleImpl(doubleStart);
            } else {
                final ASTEval astEval = new ASTEval(lambda);
                for (int i = 10; i < ((SequenceValue) seq).getSize(); i++) {
                    if(Thread.interrupted()) {
                        throw new CanceledRuntimeException(ast);
                    }
                    start = astEval.evalLambda(new TwoVarMap(arg1, start, arg2, sequense.getValueAt(i)));
                }
            }
            return start;
        }
        throw new InterpreterRuntimeException("First argument of operator reduce must be sequence", ast);
    }

    private Value evalVariable(VariableAST ast) {
        Value value = vars.get(ast.getName());
        if (value == null) {
            throw new InterpreterRuntimeException("Variable '"+ast.getName()+"' is not declared", ast);
        }
        return value;
    }

    private Value evalNumber(NumberAST ast) {
        Number eval = ast.eval();
        if (eval instanceof Integer) {
            return new IntegerImpl(eval.intValue());
        } else {
            return new DoubleImpl(eval.doubleValue());
        }
    }
}
