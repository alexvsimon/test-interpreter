package interpretator.run;

import interpretator.api.run.IntegerValue;
import interpretator.api.run.ValueKind;

/**
 *
 * @author alex
 */
/*package-local*/ class IntegerImpl implements IntegerValue {

    private final int value;

    /*package-local*/ IntegerImpl(int value) {
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
