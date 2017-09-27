package interpretator.api.run;

import interpretator.api.ast.AST;
import interpretator.api.lexer.Token;

/**
 * Thrown when a interpretation thread is interrupted.
 *
 * <p>{@code CanceledRuntimeException} is <em>unchecked exception</em>.
 * 
 * @author alex
 */
public class CanceledRuntimeException extends RuntimeException {
    private final String message;
    private final String context;
    private final int[] rowCol; 

    public CanceledRuntimeException(AST ast) {
        this.message = "Interpretaion canceled";
        Token token = ast.getFistToken();
        rowCol = token.getStartRowCol();
        context = token.getTokenLine();
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
}