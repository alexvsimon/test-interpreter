package interpretator.api.run;

/**
 * Common type of all runtime types.
 *
 * @author alex
 */
public interface Value {

    ValueKind getKind();
    
    default double getDouble(){
        throw new UnsupportedOperationException();        
    }

    default int getInteger(){
        throw new UnsupportedOperationException();        
    }
    
    default boolean isDouble(){
        return false;
    }
    
    default boolean isInteger(){
        return false;
    }

    default boolean isSequence(){
        return false;
    }
}
