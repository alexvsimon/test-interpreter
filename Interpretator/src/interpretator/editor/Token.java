package interpretator.editor;

/**
 *
 * @author alex
 */
public class Token {
    private final TokenKind kind;
    private final int startOffset;
    private final int endOffset;
    private final DocumentContext doc;

    public Token(TokenKind kind, int startOffset, int endOffset, DocumentContext doc) {
        this.kind = kind;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.doc = doc;
    }

    public TokenKind getKind(){
        return kind;
    }
    
    public String getText(){
        return doc.getText().subSequence(startOffset, endOffset).toString();
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }
    
    public int[] getStartRowCol() {
        return doc.getRowCol(startOffset);
    }

    public int[] getEndRowCol() {
        return doc.getRowCol(endOffset);
    }
    
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
