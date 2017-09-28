package interpretator;

import javax.swing.SwingUtilities;

/**
 *
 * @author alex
 */
public class StatusLine  {
    private javax.swing.JLabel message;

    private StatusLine() {
    }
    
    /**
     * Single instance of {@code StatusLine}.
     * 
     * @return instance of {@code StatusLine}.
     */
    public static StatusLine getInstance() {
        return StatusLineHelper.INSTANCE;
    }
    
    /**
     * Print string in status line.
     * 
     * @param s printed string.
     */
    public void out(String s){
        SwingUtilities.invokeLater(() -> {
            message.setText(s);
        });
    }
    
    /**
     * Clear status line.
     */
    public void clear() {
        SwingUtilities.invokeLater(() -> {
            message.setText("");
        });
    }
    
    
    /*package-local*/ void setStatusLine(javax.swing.JLabel message) {
        this.message = message;
    }

    private static class StatusLineHelper {
        private static final StatusLine INSTANCE = new StatusLine();

        private StatusLineHelper() {
        }
    }
}