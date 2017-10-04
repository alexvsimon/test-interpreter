package interpretator.spi.ast;

import interpretator.api.ast.Parser;
import interpretator.api.lexer.Lexer;
import java.util.ServiceLoader;

/**
 *
 * @author alex
 */
public abstract class ParserFactory {
    
    public abstract Parser getParser(Lexer lexer);
    
    private static final ServiceLoader<ParserFactory> FACTORY = ServiceLoader.load(ParserFactory.class);
    
    public static ParserFactory getInstance() {
        for(ParserFactory parserFactory: FACTORY) {
            return parserFactory;
        }
        return null;
    }
}
