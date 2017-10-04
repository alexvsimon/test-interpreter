package interpretator.api.run;

/**
 *
 * @author alex
 */
public interface Interpretator {

    /**
     * Gets value of variable.
     *
     * @param name variable name.
     * @return value if variable exists.
     */
    Value getVariable(String name);

    /**
     * Interprets program
     *
     * @throws CanceledRuntimeException if interpretation is canceled.
     * @throws InterpreterRuntimeException if interpreter finds runtime error such as incompatible type,
    reference on undefined variable, unsupported operations, and other.
     */
    void run() throws CanceledRuntimeException, InterpreterRuntimeException;
    
}
