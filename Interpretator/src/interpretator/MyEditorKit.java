package interpretator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 *
 * @author alex
 */
/*package-local*/ class MyEditorKit extends StyledEditorKit {

    @Override
    public ViewFactory getViewFactory() {
        return new NumberedViewFactory();
    }

    /**
     * Idea was taken from the article "Add Line Numbering in the JEditorPane"
     * Developer.com<http://www.developer.com/java/other/article.php/3318421/Add-Line-Numbering-in-the-JEditorPane.htm>
     */
    private static final class NumberedViewFactory implements ViewFactory {

        @Override
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                switch (kind) {
                    case AbstractDocument.ContentElementName:
                        return new LabelView(elem);
                    case AbstractDocument.ParagraphElementName:
                        return new NumberedParagraphView(elem);
                    case AbstractDocument.SectionElementName:
                        return new BoxView(elem, View.Y_AXIS);
                    case StyleConstants.ComponentElementName:
                        return new ComponentView(elem);
                    case StyleConstants.IconElementName:
                        return new IconView(elem);
                }
            }
            return new LabelView(elem);
        }
    }

    private static final class NumberedParagraphView extends ParagraphView {

        // with enough for line numbers 1-999
        private static final short NUMBERS_WIDTH = 25;
        private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
        private static final Color BORDER_COLOR = new Color(224, 224, 224);
        private static final Color TEXT_COLOR = new Color(0, 96, 0);

        public NumberedParagraphView(Element e) {
            super(e);
            short top = 0;
            short left = 0;
            short bottom = 0;
            short right = 0;
            this.setInsets(top, left, bottom, right);
        }

        @Override
        protected void setInsets(short top, short left, short bottom,
                short right) {
            super.setInsets(top, (short) (left + NUMBERS_WIDTH),
                    bottom, right);
        }

        @Override
        public void paintChild(Graphics g, Rectangle r, int n) {
            super.paintChild(g, r, n);
            int previousLineCount = getPreviousLineCount();
            int numberX = r.x - getLeftInset();
            int numberY = r.y + r.height - 5;
            Color old = g.getColor();
            g.setColor(BACKGROUND_COLOR);
            g.fillRect(numberX -5, r.y, getLeftInset(), r.height);
            g.setColor(BORDER_COLOR);
            g.drawLine(numberX + getLeftInset() - 5, r.y, numberX + getLeftInset() - 5, r.y + r.height);
            g.drawLine(numberX - 5, r.y, numberX - 5, r.y + r.height);
            g.setColor(TEXT_COLOR);
            String number = Integer.toString(previousLineCount + n + 1);
            int stringWidth = g.getFontMetrics().stringWidth(number);
            int shift = getLeftInset() - 5 - stringWidth;
            g.drawString(number, numberX + shift, numberY + 2);
            g.setColor(old);
        }

        public int getPreviousLineCount() {
            int lineCount = 0;
            View parent = this.getParent();
            int count = parent.getViewCount();
            for (int i = 0; i < count; i++) {
                if (parent.getView(i) == this) {
                    break;
                } else {
                    lineCount += parent.getView(i).getViewCount();
                }
            }
            return lineCount;
        }
    }
}
