package interpretator.run;

import interpretator.api.run.Value;

/**
 * Table of runtime variables.
 * 
 * @author alex
 */
/*package-local*/ interface VarsMap {

    Value get(String name);

    void put(String name, Value value);
    
}
