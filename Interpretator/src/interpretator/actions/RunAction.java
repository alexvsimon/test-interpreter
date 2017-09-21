package interpretator.actions;

import interpretator.editor.DocumentContext;
import interpretator.editor.Lexer;
import interpretator.editor.Token;
import interpretator.editor.TokenKind;
import interpretator.output.Output;
import interpretator.parser.ASTDump;
import interpretator.parser.Parser;
import interpretator.parser.ProgrammAST;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 *
 * @author alex
 */
public class RunAction {
    private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    
    private final DocumentContext doc;

    public RunAction(DocumentContext doc) {
        this.doc = doc;
    }

    public void run() {
        executor.execute(() -> {
            try {
                Lexer lexer = new Lexer(doc);
                //while(true) {
                //    Token token = lexer.nextToken();
                //    Output.getInstance().out(token.toString()+"\n");
                //    if (token.getKind() == TokenKind.EOF) {
                //        break;
                //    }
                //}
                Parser parser = new Parser(lexer);
                ProgrammAST programm = parser.parse();
                ASTDump visitor = new ASTDump(programm);
                visitor.dump();
                Output.getInstance().out(visitor.dump());
                Output.getInstance().out("\n");
            } catch (Throwable th) {
                th.printStackTrace();
            }
        });
    }
    
}
