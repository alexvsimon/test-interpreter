package interpretator.parser;

/**
 *
 * @author alex
 */
public class ReduceAST implements AST{
    private final AST arg1;
    private final AST arg2;
    private final AST lambda;

    ReduceAST(AST arg1, AST arg2, AST lambda) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.lambda = lambda;
    }

    @Override
    public ASTKind getKind() {
        return ASTKind.Reduce;
    }
    
    @Override
    public String toString() {
        return getKind().name();
    }
}
