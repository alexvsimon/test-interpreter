package interpretator.api.ast;

/**
 * Lambda.
 * 
 * @author alex
 */
public interface LambdaAST extends AST {

    /**
     * 
     * @return parameters size.
     */
    int getParametersSize();
       
    /**
     * 
     * @param i parameter number.
     * @return parameter name.
     */
    String getParameter(int i);

    /**
     * 
     * @return lambda body expression.
     */
    ExpressionAST getBody();

}
