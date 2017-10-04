package interpretator.editor;

import interpretator.DocumentContext;
import interpretator.api.lexer.Lexer;
import interpretator.spi.lexer.LexerFactory;

/**
 *
 * @author alex
 */
public final class LexerFactoryImpl extends LexerFactory {

    @Override
    public Lexer getLexer(DocumentContext doc) {
        return new LexerImpl(doc);
    }
    
}
