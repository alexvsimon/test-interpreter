package interpretator.parser;

import interpretator.editor.DocumentContext;
import interpretator.editor.Lexer;
import interpretator.editor.Token;
import interpretator.editor.TokenKind;
import interpretator.output.Output;

/**
 *
 * @author alex
 */
public class RunAction {
    private final DocumentContext doc;

    public RunAction(DocumentContext doc) {
        this.doc = doc;
    }

    public void run() {
        Lexer lexer = new Lexer(doc);
        while(true) {
            Token token = lexer.nextToken();
            Output.getInstance().out(token.toString()+"\n");
            if (token.getKind() == TokenKind.EOF) {
                break;
            }
        }
    }
    
}
