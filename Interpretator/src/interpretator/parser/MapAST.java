package interpretator.parser;

/**
 *
 * @author alex
 */
public class MapAST implements AST{
    private final AST arg1;
    private final AST lambda;

    MapAST(AST arg1, AST lambda) {
        this.arg1 = arg1;
        this.lambda = lambda;
    }

    @Override
    public ASTKind getKind() {
        return ASTKind.Map;
    }
    
    @Override
    public String toString() {
        return getKind().name();
    }
}
