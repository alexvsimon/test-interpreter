package interpretator;

import javax.swing.text.DefaultStyledDocument;

/**
 * Extended DefaultStyledDocument.
 * 
 * <p> Supports document version.
 *
 * @author alex
 */
public final class VersionedStyledDocument extends DefaultStyledDocument {
    
    private int documentVersion;

    /**
     * Document version is incremented before document changed.
     * 
     * @return document version
     */
    public int getDocumentVersion() {
        return documentVersion;
    }

    /*package-local*/  int incrementDocumentVersion() {
        return ++documentVersion;
    }
}
