package interpretator;

import interpretator.actions.RunAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;

/**
 *
 * @author alex
 */
/*package-local*/ class DocumentListenerImpl implements DocumentListener {

    private final JTextPane editor;

    /*package-local*/ DocumentListenerImpl(JTextPane editor) {
        this.editor = editor;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        update(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        update(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        update(e);
    }

    private void update(DocumentEvent e) {
        Document doc = e.getDocument();
        final int documentVersion = ((VersionedStyledDocument)doc).incrementDocumentVersion();
        doc.render(() -> {
            try {
                Highlighter h = editor.getHighlighter();
                h.removeAllHighlights();
                String text = doc.getText(0, doc.getLength());
                RunAction.getInstance().run(new DocumentContext(text, documentVersion));
            } catch (BadLocationException ex) {
                Logger.getLogger(DocumentListenerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
