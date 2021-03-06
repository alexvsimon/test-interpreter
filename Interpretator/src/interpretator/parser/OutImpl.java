package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.AST;
import interpretator.api.ast.OutAST;
import interpretator.api.lexer.Token;

/**
 *
 * @author alex
 */
/*package-local*/ final class OutImpl implements OutAST {
    private final Token outToken;
    private final AST expression;
    
    /*package-local*/ OutImpl(Token start, AST expression){
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
    public Token getFistToken() {
        return outToken;
    }

    @Override
    public String toString() {
        return getKind().name();
    }
}
