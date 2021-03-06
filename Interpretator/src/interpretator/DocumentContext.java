package interpretator;

/**
 * Program snapshot.
 * 
 * <p>Contains program text and utility methods for text manipulation.
 *
 * @author alex
 */
public final class DocumentContext {

    private final String text;
    private final int documentVersion;

    public DocumentContext(String text, int documentVersion) {
        this.text = text;
        this.documentVersion = documentVersion;
    }

    /**
     * 
     * @return editor document version at snapshot creation time.
     */
    public int getDocumentVersion() {
        return documentVersion;
    }

    /**
     * Program snapshot text.
     * 
     * @return program text.
     */
    public CharSequence getText() {
        return text;
    }

    /**
     * Counts row and column by offset.
     * 
     * <p>Row and column are 1 based.
     * 
     * @param offset offset in text
     * @return pair of row and column
     */
    public int[] getRowCol(int offset) {
        int row = 1;
        int col = 1;
        for (int i = 0; i < offset && i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '\n':
                    row++;
                    col = 1;
                    break;
                default:
                    col++;
                    break;
            }
        }
        return new int[]{row, col};
    }

    /**
     * Return text row.
     * 
     * <p>Line number is 1 based.
     * 
     * @param line line number
     * @return line string
     */
    public String getLine(int line) {
        int row = 1;
        int lineStart = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                if (row == line) {
                    return text.substring(lineStart, i);
                }
                row++;
                lineStart = i + 1;
            }
        }
        if (row == line) {
            return text.substring(lineStart, text.length());
        }
        return "";
    }
}
