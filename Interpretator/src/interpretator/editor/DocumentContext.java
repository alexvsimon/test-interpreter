package interpretator.editor;

/**
 *
 * @author alex
 */
public class DocumentContext {
    private final String text;

    public DocumentContext(String text) {
        this.text = text;
    }
    
    public CharSequence getText() {
        return text;
    }
}
