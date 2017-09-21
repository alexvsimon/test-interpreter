package interpretator.parser;

/**
 *
 * @author alex
 */
public class SequenceAST implements AST {
    private final AST arg1;
    private final AST arg2;

    SequenceAST(AST arg1, AST arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public AST getStartExpression(){
        return arg1;
    }

    public AST getEndExpression(){
        return arg2;
    }

    @Override
    public ASTKind getKind() {
        return ASTKind.Sequence;
    }
    
    @Override
    public String toString() {
        return getKind().name();
    }
}
