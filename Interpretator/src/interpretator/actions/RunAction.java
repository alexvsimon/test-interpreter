package interpretator.actions;

import interpretator.api.ast.ProgramAST;
import interpretator.editor.DocumentContext;
import interpretator.editor.Lexer;
import interpretator.Output;
import interpretator.parser.Parser;
import interpretator.parser.ParserError;
import interpretator.run.ASTEval;
import interpretator.api.run.CanceledRuntimeException;
import interpretator.api.run.InterpreterRuntimeException;
import interpretator.ErrorHighlighter;
import interpretator.StatusLine;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Program runner.
 * 
 * <p>Runs program in background thread. It includes sequence of actions:
 * <ul>
 *   <li> Clear output pane.
 *   <li> Lex program.
 *   <li> Parse program.
 *   <li> Interpret program.
 * </ul>
 * <p>Running stars with small delay after each document updating. Action cancels previous runner if it exists.
 *
 * @author alex
 */
public class RunAction {
    private static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(1);
    private ScheduledFuture<?> task;
    private final Object lock = new Object();
    
    private RunAction() {
    }

    /**
     * Single instance of {@code RunAction}.
     * 
     * @return instance of {@code RunAction}.
     */
    public static RunAction getInstance() {
        return RunActionHelper.INSTANCE;
    }
    
    /**
     * Starts program interpretation.
     * 
     * @param doc program to run.
     */
    public void run(DocumentContext doc) {
        synchronized(lock) {
            if (task != null) {
                task.cancel(true);
            }
            EXECUTOR.purge();
            task = EXECUTOR.schedule(new RunnableImpl(doc), 100, TimeUnit.MILLISECONDS);
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
                StatusLine.getInstance().out("Running...");
                long startTime = System.currentTimeMillis();
                if (Thread.interrupted()) {
                    StatusLine.getInstance().out("Canceled");
                    return;
                }
                Output.getInstance().clear();
                Lexer lexer = new Lexer(doc);
                if (Thread.interrupted()) {
                    StatusLine.getInstance().out("Canceled");
                    return;
                }
                Parser parser = new Parser(lexer);
                if (Thread.interrupted()) {
                    StatusLine.getInstance().out("Canceled");
                    return;
                }
                ProgramAST program = parser.parse();
                if (Thread.interrupted()) {
                    StatusLine.getInstance().out("Canceled");
                    return;
                }
                if (parser.getErrors().size() > 0) {
                    ParserError error = parser.getErrors().get(0);
                    int[] rowCol = error.getRowCol();
                    ErrorHighlighter.getInstance().highlihgt(error.getStartOffset(), error.getEndOffset(), doc.getDocumentVersion());
                    Output.getInstance().out(""+rowCol[0]+":"+rowCol[1]+": "+error.getMessage());
                    Output.getInstance().out("\n"+error.getContext());
                    StringBuilder buf = new StringBuilder();
                    for(int i = 0; i < rowCol[1] - 1; i++){
                        buf.append(' ');
                    }
                    buf.append('^');
                    Output.getInstance().out("\n"+buf.toString());
                    StatusLine.getInstance().out("Syntax error");
                    return;
                }
                try {
                    new ASTEval(program).run();
                    long delta = System.currentTimeMillis() - startTime;
                    StatusLine.getInstance().out("Successfully finished. Interpretation time is "+delta+"ms.");
                } catch (InterpreterRuntimeException t) {
                    ErrorHighlighter.getInstance().highlihgt(t.getStartOffset(), t.getEndOffset(), doc.getDocumentVersion());
                    Output.getInstance().out(t.getMessage());
                    StatusLine.getInstance().out("Runtime error");
                } catch (CanceledRuntimeException t) {
                    StatusLine.getInstance().out("Canceled");
                    // skip cancel error
                } catch (Throwable t) {
                    Output.getInstance().out(t.getMessage());
                    StatusLine.getInstance().out("Fatal error");
                    t.printStackTrace(System.err);
                }
            } catch (Throwable th) {
                Output.getInstance().out(th.getMessage());
                StatusLine.getInstance().out("Fatal error");
                th.printStackTrace(System.err);
            }
        }
    }
    
    
}
