package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.ExpressionAST;
import interpretator.api.ast.UnaryExpressionAST;

/**
 *
 * @author as204739
 */
public class UnaryExpressionImpl implements UnaryExpressionAST {

    private final ExpressionAST rh;
    private final ASTKind kind;

    UnaryExpressionImpl(ExpressionAST rh, ASTKind kind) {
        this.rh = rh;
        this.kind = kind;
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
    public String toString() {
        return getKind().name();
    }
}
