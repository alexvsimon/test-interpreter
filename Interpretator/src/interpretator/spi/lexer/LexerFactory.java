package interpretator.spi.lexer;

import interpretator.api.lexer.Lexer;
import interpretator.DocumentContext;
import java.util.ServiceLoader;

/**
 *
 * @author alex
 */
public abstract class LexerFactory {
    public abstract Lexer getLexer(DocumentContext doc);
    
    private static final ServiceLoader<LexerFactory> FACTORY = ServiceLoader.load(LexerFactory.class);
    
    public static LexerFactory getInstance() {
        for(LexerFactory lexerFactory: FACTORY) {
            return lexerFactory;
        }
        return null;
    }
}
