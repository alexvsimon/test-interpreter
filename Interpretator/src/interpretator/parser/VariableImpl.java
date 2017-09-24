package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.editor.Token;
import interpretator.api.ast.VariableAST;

/**
 *
 * @author alex
 */
/*package-local*/ class VariableImpl implements VariableAST {
    private final Token id;

    VariableImpl(Token id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return id.getText();
    }
    
    @Override
    public ASTKind getKind() {
        return ASTKind.Variable;
    }

    @Override
    public Token getFistToken() {
        return id;
    }
    
    @Override
    public String toString() {
        return getKind().name();
    }
}
