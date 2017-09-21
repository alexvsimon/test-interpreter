package interpretator.api.ast;

/**
 *
 * @author alex
 */
public interface SequenceAST extends ExpressionAST {

    ExpressionAST getEndExpression();

    ExpressionAST getStartExpression();
    
}
