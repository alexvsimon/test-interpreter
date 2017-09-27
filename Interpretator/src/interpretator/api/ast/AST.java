package interpretator.api.ast;

import interpretator.api.lexer.Token;

/**
 * Common AST type.
 *
 * @author alex
 */
public interface AST {

    /**
     * 
     * @return AST node kind
     */
    ASTKind getKind();

    /**
     * 
     * @return token lying under AST node.
     */
    Token getFistToken();
}
