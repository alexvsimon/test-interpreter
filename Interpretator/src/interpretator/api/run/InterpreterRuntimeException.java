package interpretator.api.run;

import interpretator.api.ast.AST;
import interpretator.api.lexer.Token;

/**
 * Thrown when interpreter encounters fatal error and cannot continue running.
 * 
 * <p>{@code InterpreterRuntimeException} is <em>unchecked exception</em>.
 * <p>Reasons when exception is thrown:
 * <ul>
 *   <li> Unsupported operation on types.
 *   <li> Reference to undefined variable.
 *   <li> Number cannot be evaluated to internal representation.
 * </ul>
 * @author alex
 */
public final class InterpreterRuntimeException extends RuntimeException {
    private final String message;
    private final String context;
    private final int[] rowCol; 
    private final int startOffset;
    private final int endOffset;

    public InterpreterRuntimeException(String message, AST ast) {
        this.message = message;
        Token token = ast.getFistToken();
        rowCol = token.getStartRowCol();
        context = token.getTokenLine();
        startOffset = token.getStartOffset();
        endOffset = token.getEndOffset();
    }

    @Override
    public String getMessage() {
        StringBuilder buf = new StringBuilder();
        buf.append(""+rowCol[0]+":"+rowCol[1]+": "+message);
        buf.append("\n"+context);
        buf.append("\n");
        for(int i = 0; i < rowCol[1] - 1; i++){
            buf.append(' ');
        }
        buf.append('^');
        return buf.toString();
    }

    /**
     * Start offset.
     * 
     * @return start offset of invalid code fragment.
     */
    public int getStartOffset() {
        return startOffset;
    }

    /**
     * End offset.
     * 
     * @return end offset of invalid code fragment.
     */
    public int getEndOffset() {
        return endOffset;
    }
}
