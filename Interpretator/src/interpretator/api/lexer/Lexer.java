package interpretator.api.lexer;

/**
 *
 * @author alex
 */
public interface Lexer {

    /**
     * Gets next token in document.
     *
     * <p>Last token is EOF.
     *
     * @return next token
     */
    Token nextToken();
    
}
