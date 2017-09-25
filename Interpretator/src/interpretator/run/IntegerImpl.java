package interpretator.run;

import interpretator.api.run.IntegerValue;
import interpretator.api.run.ValueKind;

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
    public ValueKind getKind() {
        return ValueKind.Integer;
    }

    @Override
    public String toString() {
        return ""+value;
    }

}
