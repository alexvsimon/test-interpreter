package interpretator.run;

import interpretator.api.run.Value;

/**
 *
 * @author alex
 */
/*package-local*/ final class OneVarMap implements VarsMap {
    private final String parameter;
    private final Value value;
    
    /*package-local*/ OneVarMap(String parameter, Value value) {
        this.parameter = parameter;
        this.value = value;
    }
    
    @Override
    public Value get(String name) {
        if (parameter.equals(name)) {
            return value;
        }
        return null;
    }
    
    @Override
    public void put(String name, Value value) {
        throw new IllegalArgumentException();
    }
}
