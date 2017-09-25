package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.ExpressionAST;
import interpretator.api.ast.BinaryExpressionAST;
import interpretator.api.lexer.Token;

/**
 *
 * @author alex
 */
/*package-local*/ class BinaryExpressionImpl implements BinaryExpressionAST {
    private final ExpressionAST lh;
    private final ExpressionAST rh;
    private final ASTKind kind;
    private final Token op;

    BinaryExpressionImpl(ExpressionAST lh, ExpressionAST rh, Token op, ASTKind kind) {
        this.lh = lh;
        this.rh = rh;
        this.kind = kind;
        this.op = op;
    }

    @Override
    public ExpressionAST getLeftExpression() {
        return lh;
    }

    @Override
    public ExpressionAST getRightExpression() {
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
