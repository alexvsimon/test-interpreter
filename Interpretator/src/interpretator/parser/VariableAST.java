package interpretator.parser;

import interpretator.editor.Token;

/**
 *
 * @author alex
 */
public class VariableAST implements AST {
    private final Token id;

    VariableAST(Token id) {
        this.id = id;
    }

    public String getName() {
        return id.getText();
    }
    
    @Override
    public ASTKind getKind() {
        return ASTKind.Variable;
    }
    
    @Override
    public String toString() {
        return getKind().name();
    }
}
