package interpretator.api.ast;

/**
 * Sequence expression.
 * 
 *
 * @author alex
 */
public interface SequenceAST extends ExpressionAST {

    /**
     * 
     * @return integer expression of the first sequence element.
     */
    ExpressionAST getEndExpression();

    /**
     * 
     * @return integer expression of the last sequence element.
     */
    ExpressionAST getStartExpression();
    
}
