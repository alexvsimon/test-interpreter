package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.VariableAST;
import interpretator.api.lexer.Token;

/**
 *
 * @author alex
 */
/*package-local*/ final class VariableImpl implements VariableAST {
    private final Token id;
    private final String name;

    /*package-local*/ VariableImpl(Token id) {
        this.id = id;
        name = id.getText();
    }

    @Override
    public String getName() {
        return name;
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
