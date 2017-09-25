package interpretator.run;

import interpretator.api.run.DoubleValue;
import interpretator.api.run.ValueKind;

/**
 *
 * @author alex
 */
public class DoubleImpl implements DoubleValue {

    private final double value;

    public DoubleImpl(double value) {
        this.value = value;
    }

    @Override
    public double getDouble() {
        return value;
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
