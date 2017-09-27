package interpretator.api.ast;

/**
 * Reduce is a rule to covert sequence to one element.
 *
 * @author alex
 */
public interface ReduceAST extends ExpressionAST {

    /**
     * 
     * @return input sequence expression.
     */
    ExpressionAST getInputExpression();

    /**
     * 
     * @return first neutral element expression.
     */
    ExpressionAST getStartExpression();
    
    /**
     * 
     * @return lambda to convert current neutral element and sequence element to next neutral element.
     */
    LambdaAST getLambda();

}
