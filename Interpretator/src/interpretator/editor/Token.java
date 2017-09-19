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

    @Override
    public String toString() {
        if (kind == TokenKind.EOF) {
            return kind.name();
        } else {
            return kind.name()+" "+getText();
        }
    }
}
