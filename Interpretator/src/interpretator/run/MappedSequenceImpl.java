package interpretator.run;

import interpretator.api.run.SequenceValue;
import interpretator.api.run.Value;
import interpretator.api.ast.LambdaAST;
import interpretator.api.run.ValueKind;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author alex
 */
/*package-local*/ class MappedSequenceImpl implements SequenceValue {

    private final SequenceValue mapped;
    private final LambdaAST lambda;
    private final AtomicBoolean canceled;

    /*package-local*/ MappedSequenceImpl(SequenceValue mapped, LambdaAST lambda, AtomicBoolean canceled) {
        this.mapped = mapped;
        this.lambda = lambda;
        this.canceled = canceled;
    }

    @Override
    public int getSize() {
        return mapped.getSize();
    }

    @Override
    public Value getValueAt(int i) {
        Value value = mapped.getValueAt(i);
        assert lambda.getParametersSize() == 1;
        String arg = lambda.getParameter(0);
        return new ASTEval(lambda, canceled).evalLambda(new OneVarMap(arg, value));
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
