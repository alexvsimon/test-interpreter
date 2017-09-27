package interpretator.api.ast;

import interpretator.api.run.InterpreterRuntimeException;

/**
 *
 * @author alex
 */
public interface NumberAST extends ExpressionAST {

    String getValue();
    
    /**
     * Integer or Double presentation.
     * 
     * @return Integer or Double presentation of the value
     * @throws InterpreterRuntimeException if value cannot be converted to Integer or Double.
     */
    Number eval() throws InterpreterRuntimeException;
    
}
