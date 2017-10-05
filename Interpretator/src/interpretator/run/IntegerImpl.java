package interpretator.run;

import interpretator.api.run.Value;
import interpretator.api.run.ValueKind;

/**
 *
 * @author alex
 */
/*package-local*/ final class IntegerImpl implements Value {

    private final int value;

    /*package-local*/ IntegerImpl(int value) {
        this.value = value;
    }

    @Override
    public int getInteger() {
        return value;
    }

    @Override
    public boolean isInteger() {
        return true;
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
