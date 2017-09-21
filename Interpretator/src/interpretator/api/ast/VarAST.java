package interpretator.api.ast;

/**
 *
 * @author alex
 */
public interface VarAST extends StatementAST {

    ExpressionAST getExpression();

    String getName();
    
}
