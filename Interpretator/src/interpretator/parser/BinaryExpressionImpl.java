package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.ExpressionAST;
import interpretator.api.ast.BinaryExpressionAST;

/**
 *
 * @author alex
 */
/*package-local*/ class BinaryExpressionImpl implements BinaryExpressionAST {
    private final ExpressionAST lh;
    private final ExpressionAST rh;
    private final ASTKind kind;

    BinaryExpressionImpl(ExpressionAST lh, ExpressionAST rh, ASTKind kind) {
        this.lh = lh;
        this.rh = rh;
        this.kind = kind;
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
    public String toString() {
        return getKind().name();
    }
}
