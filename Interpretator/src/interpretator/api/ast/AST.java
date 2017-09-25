package interpretator.api.ast;

import interpretator.api.lexer.Token;

/**
 *
 * @author alex
 */
public interface AST {

    ASTKind getKind();

    Token getFistToken();
}
