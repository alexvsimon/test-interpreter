package interpretator.output;

import interpretator.MainFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author alex
 */
public class Output {
    private javax.swing.JTextPane outputPane;
    
    private Output() {
    }
    
    public static Output getInstance() {
        return OutputHelper.INSTANCE;
    }
    
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
    
    public void clear() {
        SwingUtilities.invokeLater(() -> {
            try {
                outputPane.getDocument().remove(0, outputPane.getDocument().getLength());
            } catch (BadLocationException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    
    public void setOutputPane(javax.swing.JTextPane outputPane) {
        this.outputPane = outputPane;
    }

    private static class OutputHelper {
        private static final Output INSTANCE = new Output();

        private OutputHelper() {
        }
    }
}
