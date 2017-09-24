package interpretator.run;

import interpretator.api.run.DoubleValue;

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

}
