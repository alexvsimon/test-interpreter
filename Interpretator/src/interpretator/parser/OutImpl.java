package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.AST;
import interpretator.editor.Token;
import interpretator.api.ast.OutAST;

/**
 *
 * @author alex
 */
/*package-local*/ class OutImpl implements OutAST {
    private final Token outToken;
    private final AST expression;
    
    OutImpl(Token start, AST expression){
        outToken = start;
        this.expression = expression;
    }

    @Override
    public AST getExpression() {
        return expression;
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
