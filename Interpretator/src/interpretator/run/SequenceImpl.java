package interpretator.run;

import interpretator.api.run.SequenceValue;
import interpretator.api.run.Value;
import interpretator.api.run.ValueKind;

/**
 *
 * @author alex
 */
/*package-local*/ final class SequenceImpl implements SequenceValue {

    private final int startIndex;
    private final int endIndex;

    /*package-local*/ SequenceImpl(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public int getSize() {
        return endIndex - startIndex + 1;
    }

    @Override
    public Value getValueAt(int i) {
        if (i < 0 || i > getSize()) {
            throw new IllegalArgumentException();
        }
        return new IntegerImpl(i + startIndex);
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
        return "Sequence of " + getSize() + " values";
    }

}
