package interpretator.run;

import interpretator.api.run.DoubleValue;
import interpretator.api.run.ValueKind;

/**
 *
 * @author alex
 */
/*package-local*/ final class DoubleImpl implements DoubleValue {

    private final double value;

    /*package-local*/ DoubleImpl(double value) {
        this.value = value;
    }

    @Override
    public double getDouble() {
        return value;
    }

    @Override
    public int getInteger() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ValueKind getKind() {
        return ValueKind.Double;
    }
    
    @Override
    public String toString() {
        return ""+value;
    }

}
