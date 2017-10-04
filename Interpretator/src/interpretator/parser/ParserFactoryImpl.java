package interpretator.parser;

import interpretator.api.ast.Parser;
import interpretator.api.lexer.Lexer;
import interpretator.spi.ast.ParserFactory;

/**
 *
 * @author alex
 */
public final class ParserFactoryImpl extends ParserFactory {

    @Override
    public Parser getParser(Lexer lexer) {
        return new ParserImpl(lexer);
    }
}
