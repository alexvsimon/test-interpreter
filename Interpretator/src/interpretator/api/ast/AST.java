package interpretator.api.ast;

import interpretator.editor.Token;

/**
 *
 * @author alex
 */
public interface AST {

    ASTKind getKind();

    Token getFistToken();
}
