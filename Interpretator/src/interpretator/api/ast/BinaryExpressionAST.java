package interpretator.api.ast;

/**
 * Common type of all binary expressions.
 *
 * @author alex
 */
public interface BinaryExpressionAST extends ExpressionAST {

    /**
     * 
     * @return left expression
     */
    ExpressionAST getLeftExpression();

    /**
     * 
     * @return right expression
     */
    ExpressionAST getRightExpression();
    
}
