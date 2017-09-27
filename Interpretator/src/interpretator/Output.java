package interpretator;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Prints running results or exceptions in output pane.
 * 
 * @author alex
 */
public class Output {
    private javax.swing.JTextPane outputPane;
    
    private Output() {
    }
    
    /**
     * Single instance of {@code Output}.
     * 
     * @return instance of {@code Output}.
     */
    public static Output getInstance() {
        return OutputHelper.INSTANCE;
    }
    
    /**
     * Print string in output pane.
     * 
     * @param s printed string.
     */
    public void out(String s){
        SwingUtilities.invokeLater(() -> {
            Document doc = outputPane.getDocument();
            try {
                doc.insertString(doc.getLength(), s, null);
            } catch (BadLocationException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    /**
     * Clear output pane.
     */
    public void clear() {
        SwingUtilities.invokeLater(() -> {
            try {
                outputPane.getDocument().remove(0, outputPane.getDocument().getLength());
            } catch (BadLocationException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    
    /*package-local*/ void setOutputPane(javax.swing.JTextPane outputPane) {
        this.outputPane = outputPane;
    }

    private static class OutputHelper {
        private static final Output INSTANCE = new Output();

        private OutputHelper() {
        }
    }
}
