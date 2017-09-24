package interpretator.actions;

import interpretator.api.ast.ProgramAST;
import interpretator.editor.DocumentContext;
import interpretator.editor.Lexer;
import interpretator.output.Output;
import interpretator.parser.ASTDump;
import interpretator.parser.Parser;
import interpretator.parser.ParserError;
import interpretator.run.ASTEval;
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
                ProgramAST program = parser.parse();
                //ASTDump visitor = new ASTDump(program);
                //visitor.dump();
                //Output.getInstance().out(visitor.dump());
                //Output.getInstance().out("\n");
                if (parser.getErrors().size() > 0) {
                    ParserError error = parser.getErrors().get(0);
                    int[] rowCol = error.getRowCol();
                    Output.getInstance().out("\n"+rowCol[0]+":"+rowCol[1]+": "+error.getMessage());
                    Output.getInstance().out("\n"+error.getContext());
                    StringBuilder buf = new StringBuilder();
                    for(int i = 0; i < rowCol[1] - 2; i++){
                        buf.append(' ');
                    }
                    buf.append('^');
                    Output.getInstance().out("\n"+buf.toString());
                    return;
                }
                Output.getInstance().out("\n");
                new ASTEval(program).run();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        });
    }
    
}
