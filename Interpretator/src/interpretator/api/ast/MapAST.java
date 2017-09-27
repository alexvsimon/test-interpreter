package interpretator.api.ast;

/**
 * Map is a rule to convert one sequence to another.
 *
 * @author alex
 */
public interface MapAST extends ExpressionAST {

    /**
     * 
     * @return expression of input sequence.
     */
    ExpressionAST getInputExpression();

    /**
     * 
     * @return lambda to convert sequence elements.
     */
    LambdaAST getLambda();
    
}
