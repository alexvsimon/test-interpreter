package interpretator.run;

/**
 *
 * @author alex
 */
/*package-local*/ final class TwoDoubleVarsMap implements DoubleVarsMap {
    private final String parameter1;
    private final double value1;
    //private final String parameter2;
    private final double value2;
    
    /*package-local*/ TwoDoubleVarsMap(String parameter1, double value1,
                                String parameter2, double value2) {
        this.parameter1 = parameter1;
        this.value1 = value1;
        //this.parameter2 = parameter2;
        this.value2 = value2;
    }
    
    @Override
    public double get(String name) {
        if (parameter1.equals(name)) {
            return value1;
        } else {
            return value2;
        } 
    }
}
