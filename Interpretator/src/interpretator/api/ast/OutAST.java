package interpretator.api.ast;

/**
 *
 * @author alex
 */
public interface OutAST extends StatementAST {

    AST getExpression();
    
}
