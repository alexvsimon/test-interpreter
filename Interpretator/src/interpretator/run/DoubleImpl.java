package interpretator.run;

import interpretator.api.run.Value;
import interpretator.api.run.ValueKind;

/**
 *
 * @author alex
 */
/*package-local*/ final class DoubleImpl implements Value {

    private final double value;

    /*package-local*/ DoubleImpl(double value) {
        this.value = value;
    }

    @Override
    public double getDouble() {
        return value;
    }

    @Override
    public boolean isDouble() {
        return true;
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
