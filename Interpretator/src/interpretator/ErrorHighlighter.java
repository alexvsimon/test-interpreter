package interpretator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
 * Highlights fragment of program.
 * 
 * <p>A fragment is red waved.
 * 
 * @author alex
 */
public class ErrorHighlighter {
    private JTextPane editor;
    
    private ErrorHighlighter(){
    }
    
    /**
     * Single instance of {@code ErrorHighlighter}.
     * 
     * @return instance of {@code ErrorHighlighter}.
     */
    public static ErrorHighlighter getInstance() {
        return ErrorHighlighterHelper.INSTANCE;
    }

    /*package-local*/ void setOutputPane(javax.swing.JTextPane editor) {
        this.editor = editor;
    }
    
    private static class ErrorHighlighterHelper {
        private static final ErrorHighlighter INSTANCE = new ErrorHighlighter();

        private ErrorHighlighterHelper() {
        }
    }

    /**
     * Highlights specified fragment of program.
     * 
     * @param start start offset of program.
     * @param end end offset of program.
     * @param documentVersion document version to highlight
     */
    public void highlight(int start, int end, int documentVersion) {
        SwingUtilities.invokeLater(() -> {
            Document doc = editor.getDocument();
            doc.render(() -> {
                try {
                    Highlighter h = editor.getHighlighter();
                    h.removeAllHighlights();
                    if (((VersionedStyledDocument)doc).getDocumentVersion() == documentVersion) {
                        h.addHighlight(start, end, new ErrorHighlightPainter(editor));
                    }
                } catch (BadLocationException ex) {
                    Logger.getLogger(ErrorHighlighter.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
        
    }
    
    private static class ErrorHighlightPainter implements Highlighter.HighlightPainter {
        private final JTextPane editor;

        private ErrorHighlightPainter(JTextPane editor) {
            this.editor = editor;
        }

        @Override
        public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c) {
            g.setColor(Color.RED);

            try {
                Rectangle start = editor.modelToView(p0);
                Rectangle end = editor.modelToView(p1);

                if (start.x < 0) {
                    return;
                }

                int waveLength = end.x + end.width - start.x;
                if (waveLength > 0) {
                    int[] wf = {0, 0, -1, -1};
                    int[] xArray = new int[waveLength + 1];
                    int[] yArray = new int[waveLength + 1];

                    int yBase = start.y + start.height - 2;
                    for (int i = 0; i <= waveLength; i++) {
                        xArray[i] = start.x + i;
                        yArray[i] = yBase + wf[xArray[i] % 4];
                    }
                    g.drawPolyline(xArray, yArray, waveLength);
                }
            } catch (BadLocationException ex) {
                Logger.getLogger(ErrorHighlighter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
