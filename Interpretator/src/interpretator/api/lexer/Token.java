package interpretator.api.lexer;

/**
 * Program token.
 * 
 * @see TokenKind Program grama.
 * 
 * @author alex
 */
public interface Token {

    /**
     * 
     * @return token kind.
     */
    TokenKind getKind();

    /**
     * 
     * @return token text
     */
    String getText();

    /**
     * 
     * @return token start offset in document.
     */
    int getStartOffset();

    /**
     * 
     * @return token end offset in document.
     */
    int getEndOffset();

    /**
     * 
     * @return token start row and column in document.
     */
    int[] getStartRowCol();

    /**
     * 
     * @return token end row and column in document.
     */
    int[] getEndRowCol();

    /**
     * Token line of text
     * 
     * @return whole line with token
     */
    String getTokenLine();
    
}
