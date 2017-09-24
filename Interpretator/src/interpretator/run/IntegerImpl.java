package interpretator.run;

import interpretator.api.run.IntegerValue;

/**
 *
 * @author alex
 */
public class IntegerImpl implements IntegerValue {

    private final int value;

    public IntegerImpl(int value) {
        this.value = value;
    }

    @Override
    public int getInteger() {
        return value;
    }

    @Override
    public String toString() {
        return ""+value;
    }

}
