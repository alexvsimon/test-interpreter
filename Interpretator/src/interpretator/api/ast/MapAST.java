package interpretator.api.ast;

/**
 *
 * @author alex
 */
public interface MapAST extends ExpressionAST {

    ExpressionAST getInputExpression();

    LambdaAST getLambda();
    
}
