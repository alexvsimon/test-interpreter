package interpretator.run;

import interpretator.api.run.Value;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author alex
 */
/*package-local*/ class VarsMapImpl implements VarsMap {
    private final Map<String, Value> vars = new HashMap<>();
    
    /*package-local*/ VarsMapImpl(){
    }

    @Override
    public Value get(String name) {
        return vars.get(name);
    }

    @Override
    public void put(String name, Value value) {
        vars.put(name, value);
    }
}
