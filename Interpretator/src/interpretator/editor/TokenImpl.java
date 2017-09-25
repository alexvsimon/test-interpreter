package interpretator.editor;

import interpretator.api.lexer.TokenKind;
import interpretator.api.lexer.Token;

/**
 *
 * @author alex
 */
public class TokenImpl implements Token {
    private final TokenKind kind;
    private final int startOffset;
    private final int endOffset;
    private final DocumentContext doc;

    public TokenImpl(TokenKind kind, int startOffset, int endOffset, DocumentContext doc) {
        this.kind = kind;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.doc = doc;
    }

    @Override
    public TokenKind getKind(){
        return kind;
    }
    
    @Override
    public String getText(){
        return doc.getText().subSequence(startOffset, endOffset).toString();
    }

    @Override
    public int getStartOffset() {
        return startOffset;
    }

    @Override
    public int getEndOffset() {
        return endOffset;
    }
    
    @Override
    public int[] getStartRowCol() {
        return doc.getRowCol(startOffset);
    }

    @Override
    public int[] getEndRowCol() {
        return doc.getRowCol(endOffset);
    }
    
    @Override
    public String getTokenLine() {
        return doc.getLine(getStartRowCol()[0]);
    }

    @Override
    public String toString() {
        if (kind == TokenKind.EOF) {
            return kind.name();
        } else {
            return kind.name()+" "+getText();
        }
    }
}
