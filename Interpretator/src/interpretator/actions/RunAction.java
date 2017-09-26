package interpretator.actions;

import interpretator.api.ast.ProgramAST;
import interpretator.editor.DocumentContext;
import interpretator.editor.Lexer;
import interpretator.Output;
import interpretator.parser.Parser;
import interpretator.parser.ParserError;
import interpretator.run.ASTEval;
import interpretator.api.run.CanceledRuntimeError;
import interpretator.api.run.InterpreterRuntimeError;
import interpretator.ErrorHighlighter;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author alex
 */
public class RunAction {
    private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    private ScheduledFuture<?> task;
    private final Object lock = new Object();
    
    private RunAction() {
    }

    public static RunAction getInstance() {
        return RunActionHelper.INSTANCE;
    }
    
    public void run(DocumentContext doc) {
        synchronized(lock) {
            if (task != null) {
                task.cancel(true);
            }
            executor.purge();
            task = executor.schedule(new RunnableImpl(doc), 100, TimeUnit.MILLISECONDS);
        }
    }

    private static class RunActionHelper {
        private static RunAction INSTANCE = new RunAction();

        private RunActionHelper() {
        }
    }
    
    private static class RunnableImpl implements Runnable {
        private final DocumentContext doc;
        
        public RunnableImpl(DocumentContext doc) {
            this.doc = doc;
        }

        @Override
        public void run() {
            try {
                if (Thread.interrupted()) {
                    return;
                }
                Output.getInstance().clear();
                Lexer lexer = new Lexer(doc);
                if (Thread.interrupted()) {
                    return;
                }
                Parser parser = new Parser(lexer);
                if (Thread.interrupted()) {
                    return;
                }
                ProgramAST program = parser.parse();
                if (Thread.interrupted()) {
                    return;
                }
                if (parser.getErrors().size() > 0) {
                    ParserError error = parser.getErrors().get(0);
                    int[] rowCol = error.getRowCol();
                    ErrorHighlighter.getInstance().highlihgt(error.getStartOffset(), error.getEndOffset());
                    Output.getInstance().out(""+rowCol[0]+":"+rowCol[1]+": "+error.getMessage());
                    Output.getInstance().out("\n"+error.getContext());
                    StringBuilder buf = new StringBuilder();
                    for(int i = 0; i < rowCol[1] - 1; i++){
                        buf.append(' ');
                    }
                    buf.append('^');
                    Output.getInstance().out("\n"+buf.toString());
                    return;
                }
                try {
                    new ASTEval(program).run();
                } catch (InterpreterRuntimeError t) {
                    ErrorHighlighter.getInstance().highlihgt(t.getStartOffset(), t.getEndOffset());
                    Output.getInstance().out(t.getMessage());
                } catch (CanceledRuntimeError t) {
                    // skip cancel error
                } catch (Throwable t) {
                    Output.getInstance().out(t.getMessage());
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
    
    
}
