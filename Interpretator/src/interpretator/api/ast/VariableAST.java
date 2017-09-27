package interpretator.api.ast;

/**
 * Variable reference expression.
 *
 * @author alex
 */
public interface VariableAST extends ExpressionAST {

    /**
     * 
     * @return variable name.
     */
    String getName();
    
}
