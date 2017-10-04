package interpretator.run;

import interpretator.api.ast.AST;
import interpretator.api.run.Interpretator;
import interpretator.spi.run.InterpretatorFactory;

/**
 *
 * @author alex
 */
public final class InterpretatorFactoryImpl extends InterpretatorFactory {

    @Override
    public Interpretator getInterpretator(AST root) {
        return new ASTEval(root);
    }
    
}
