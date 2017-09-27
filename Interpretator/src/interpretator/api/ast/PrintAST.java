package interpretator.api.ast;

/**
 * Print string literal statement.
 * 
 * @author alex
 */
public interface PrintAST extends StatementAST {

    /**
     * String is enclosed in double quotes.
     * 
     * <p>Enclosed double quotes are not printed.
     * <p>Substring "\n" is printed as new line char.
     * 
     * @return string to print.
     */
    String getString();
    
}
