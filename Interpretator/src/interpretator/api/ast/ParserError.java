package interpretator.api.ast;

/**
 *
 * @author alex
 */
public final class ParserError {
    private final String message;
    private final String context;
    private final int[] rowCol; 
    private final int startOffset;
    private final int endOffset;

    public ParserError(String message, String context, int[] rowCol, int startOffset, int endOffset) {
        this.message = message;
        this.context = context;
        this.rowCol = rowCol;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public String getMessage() {
        return message;
    }

    public String getContext() {
        return context;
    }

    public int[] getRowCol() {
        return rowCol;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }
}
