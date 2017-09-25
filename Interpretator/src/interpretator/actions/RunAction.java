package interpretator.actions;

import interpretator.api.ast.ProgramAST;
import interpretator.editor.DocumentContext;
import interpretator.editor.Lexer;
import interpretator.output.Output;
import interpretator.parser.Parser;
import interpretator.parser.ParserError;
import interpretator.run.ASTEval;
import interpretator.api.run.CanceledRuntimeError;
import interpretator.api.run.InterpreterRuntimeError;
import interpretator.output.ErrorHighlighter;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author alex
 */
public class RunAction {
    private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    private ScheduledFuture<?> task;
    private AtomicBoolean canceled = new AtomicBoolean(false);
    private final Object lock = new Object();
    
    private RunAction() {
    }

    public static RunAction getInstance() {
        return RunActionHelper.INSTANCE;
    }
    
    public void run(DocumentContext doc) {
        synchronized(lock) {
            if (task != null) {
                canceled.set(true);
                task.cancel(true);
            }
            executor.purge();
            canceled = new AtomicBoolean(false);
            task = executor.schedule(new RunnableImpl(doc, canceled), 100, TimeUnit.MILLISECONDS);
        }
    }

    private static class RunActionHelper {
        private static RunAction INSTANCE = new RunAction();

        private RunActionHelper() {
        }
    }
    
    private static class RunnableImpl implements Runnable {
        private final DocumentContext doc;
        private final AtomicBoolean canceled;
        
        public RunnableImpl(DocumentContext doc, AtomicBoolean canceled) {
            this.doc = doc;
            this.canceled = canceled;
        }

        @Override
        public void run() {
            try {
                if (canceled.get()) {
                    return;
                }
                Output.getInstance().clear();
                Lexer lexer = new Lexer(doc);
                if (canceled.get()) {
                    return;
                }
                Parser parser = new Parser(lexer);
                if (canceled.get()) {
                    return;
                }
                ProgramAST program = parser.parse();
                if (canceled.get()) {
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
                    new ASTEval(program, canceled).run();
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
