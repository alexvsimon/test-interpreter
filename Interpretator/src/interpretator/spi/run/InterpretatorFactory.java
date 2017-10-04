package interpretator.spi.run;

import interpretator.api.ast.AST;
import interpretator.api.run.Interpretator;
import java.util.ServiceLoader;

/**
 *
 * @author alex
 */
public abstract class InterpretatorFactory {
    public abstract Interpretator getInterpretator(AST root);
    
    private static final ServiceLoader<InterpretatorFactory> FACTORY = ServiceLoader.load(InterpretatorFactory.class);
    
    public static InterpretatorFactory getInstance() {
        for(InterpretatorFactory interpretatorFactory: FACTORY) {
            return interpretatorFactory;
        }
        return null;
    }
}
