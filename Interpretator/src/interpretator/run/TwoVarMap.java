package interpretator.run;

import interpretator.api.run.VarsMap;
import interpretator.api.run.Value;

/**
 *
 * @author alex
 */
/*package-local*/ class TwoVarMap implements VarsMap {
    private final String parameter1;
    private final Value value1;
    private final String parameter2;
    private final Value value2;
    
    /*package-local*/ TwoVarMap(String parameter1, Value value1,
                                String parameter2, Value value2) {
        this.parameter1 = parameter1;
        this.value1 = value1;
        this.parameter2 = parameter2;
        this.value2 = value2;
    }
    
    @Override
    public Value get(String name) {
        if (parameter1.equals(name)) {
            return value1;
        } else if (parameter2.equals(name)) {
            return value2;
        } 
        return null;
    }
    
    @Override
    public void put(String name, Value value) {
        throw new IllegalArgumentException();
    }
}