package interpretator.api.ast;

/**
 *
 * @author alex
 */
public interface LambdaAST extends AST {

    ExpressionAST getBody();

    String getParameter(int i);

    int getParametersSize();
    
}
