package interpretator.parser;

/**
 *
 * @author alex
 */
public class ExpressionAST implements AST {
    private final AST lh;
    private final AST rh;
    private final ASTKind kind;

    ExpressionAST(AST lh, AST rh, ASTKind kind) {
        this.lh = lh;
        this.rh = rh;
        this.kind = kind;
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
