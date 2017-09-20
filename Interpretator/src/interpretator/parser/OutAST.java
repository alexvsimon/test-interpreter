package interpretator.parser;

import interpretator.editor.Token;

/**
 *
 * @author alex
 */
public class OutAST implements AST {
    private final Token outToken;
    private final AST expression;
    
    OutAST(Token start, AST expression){
        outToken = start;
        this.expression = expression;
    }

    @Override
    public ASTKind getKind() {
        return ASTKind.Out;
    }

    @Override
    public String toString() {
        return getKind().name();
    }
}
