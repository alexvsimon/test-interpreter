package interpretator.api.ast;

/**
 *
 * @author alex
 */
public interface UnaryExpressionAST extends ExpressionAST {
    
    ExpressionAST getExpression();
    
}
