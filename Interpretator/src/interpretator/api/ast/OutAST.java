package interpretator.api.ast;

/**
 * Out statement.
 *
 * @author alex
 */
public interface OutAST extends StatementAST {

    /**
     * 
     * @return expression to print.
     */
    AST getExpression();
    
}
