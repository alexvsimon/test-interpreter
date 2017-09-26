package interpretator.api.run;

/**
 *
 * @author alex
 */
public interface VarsMap {

    Value get(String name);

    void put(String name, Value value);
    
}
