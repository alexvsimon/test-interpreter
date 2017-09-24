package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.ExpressionAST;
import interpretator.api.ast.UnaryExpressionAST;
import interpretator.editor.Token;

/**
 *
 * @author as204739
 */
public class UnaryExpressionImpl implements UnaryExpressionAST {

    private final ExpressionAST rh;
    private final ASTKind kind;
    private final Token op;

    UnaryExpressionImpl(ExpressionAST rh, Token op, ASTKind kind) {
        this.rh = rh;
        this.kind = kind;
        this.op = op;
    }

    @Override
    public ExpressionAST getExpression() {
        return rh;
    }
    
    @Override
    public ASTKind getKind() {
        return kind;
    }

    @Override
    public Token getFistToken() {
        return op;
    }

    @Override
    public String toString() {
        return getKind().name();
    }
}
