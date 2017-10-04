package interpretator.run;

import interpretator.api.run.InterpreterRuntimeException;
import interpretator.api.run.SequenceValue;
import interpretator.api.run.Value;
import interpretator.api.run.ValueKind;
import interpretator.api.ast.LambdaAST;

/**
 *
 * @author alex
 */
/*package-local*/ final class MappedSequenceImpl implements SequenceValue {

    private final SequenceValue mapped;
    private final LambdaAST lambda;
    private boolean numericSequence = ASTEval.OPTIMIZE_DOUBLE_ARITHMETIC;
    private int evals = 0;

    /*package-local*/ MappedSequenceImpl(SequenceValue mapped, LambdaAST lambda) {
        this.mapped = mapped;
        this.lambda = lambda;
    }

    @Override
    public int getSize() {
        return mapped.getSize();
    }

    @Override
    public Value getValueAt(int i) {
        evals++;
        Value value = mapped.getValueAt(i);
        String arg = lambda.getParameter(0);
        if (numericSequence && evals < 10) {
            if (value.getKind() == ValueKind.Sequence) {
                numericSequence = false;
            }
            final ASTEval eval = new ASTEval(lambda);
            final Value res = eval.evalLambda(new OneVarMap(arg, value));
            if (res.getKind() != ValueKind.Double) {
                numericSequence = false;
            }
            if (eval.hasRecursiveLambda()) {
                numericSequence = false;
            }
            return res;
        }
        if (numericSequence) {
            double res;
            switch(value.getKind()) {
                case Integer:
                    res = value.getInteger();
                    break;
                case Double:
                    res = value.getDouble();
                    break;
                default:
                    throw new InterpreterRuntimeException("Sequence has number and sequence elements", lambda);
            }
            res =  new DoubleEval(lambda).evalDoubleLambda(new OneDoubleVarMap(arg, res));
            return new DoubleImpl(res);
        } else {
            return new ASTEval(lambda).evalLambda(new OneVarMap(arg, value));
        }
    }
    
    @Override
    public double getDouble() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getInteger() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ValueKind getKind() {
        return ValueKind.Sequence;
    }

    @Override
    public String toString() {
        return "MappedSequence of " + getSize() + " values";
    }

}
