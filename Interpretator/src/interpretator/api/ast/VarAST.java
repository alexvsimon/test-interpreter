package interpretator.api.ast;

/**
 * Variable declaration statement.
 *
 * @author alex
 */
public interface VarAST extends StatementAST {

    /**
     * 
     * @return variable name.
     */
    String getName();

    /**
     * 
     * @return variable initialization expression.
     */
    ExpressionAST getExpression();
    
}
