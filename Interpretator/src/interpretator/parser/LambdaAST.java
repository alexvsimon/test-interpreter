package interpretator.parser;

import interpretator.editor.Token;

/**
 *
 * @author alex
 */
public class LambdaAST implements AST {
    private final Token v1;
    private final Token v2;
    private final AST function;

    LambdaAST(Token v1, Token v2, AST function) {
        this.v1 = v1;
        this.v2 = v2;
        this.function = function;
    }

    @Override
    public ASTKind getKind() {
        return ASTKind.Lambda;
    }
    
    @Override
    public String toString() {
        return getKind().name();
    }
}
