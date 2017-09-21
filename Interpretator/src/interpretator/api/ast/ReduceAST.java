package interpretator.api.ast;

/**
 *
 * @author alex
 */
public interface ReduceAST extends ExpressionAST {

    ExpressionAST getInputExpression();

    LambdaAST getLambda();

    ExpressionAST getStartExpression();
    
}
