package interpretator.api.ast;

/**
 *
 * @author alex
 */
public interface BinaryExpressionAST extends ExpressionAST {

    ExpressionAST getLeftExpression();

    ExpressionAST getRightExpression();
    
}
