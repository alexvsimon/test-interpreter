package interpretator.api.ast;

/**
 * Unary expression.
 *
 * @author alex
 */
public interface UnaryExpressionAST extends ExpressionAST {
    
    /**
     * 
     * @return expression.
     */
    ExpressionAST getExpression();
    
}
