package interpretator.run;

import interpretator.api.run.SequenceValue;
import interpretator.api.run.Value;
import interpretator.api.ast.LambdaAST;
import interpretator.api.run.ValueKind;

/**
 *
 * @author alex
 */
/*package-local*/ class MappedSequenceImpl implements SequenceValue {

    private final SequenceValue mapped;
    private final LambdaAST lambda;

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
        Value value = mapped.getValueAt(i);
        assert lambda.getParametersSize() == 1;
        String arg = lambda.getParameter(0);
        return new ASTEval(lambda).evalLambda(new OneVarMap(arg, value));
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
