package interpretator.api.lexer;

/**
 *
 * @author alex
 */
public interface Token {

    TokenKind getKind();

    String getText();

    int getStartOffset();
    int getEndOffset();

    int[] getStartRowCol();
    int[] getEndRowCol();

    /**
     * Token line of text
     * 
     * @return whole line with token
     */
    String getTokenLine();
    
}
