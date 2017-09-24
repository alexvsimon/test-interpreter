package interpretator.run;

import interpretator.api.run.SequenceValue;
import interpretator.api.run.Value;

/**
 *
 * @author alex
 */
public class SequenceImpl implements SequenceValue {

    private final int startIndex;
    private final int endIndex;

    public SequenceImpl(int startIndex, int endIndex) {
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
    public String toString() {
        return "Sequence of " + getSize() + " values";
    }

}
