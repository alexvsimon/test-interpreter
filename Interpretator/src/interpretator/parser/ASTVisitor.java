package interpretator.parser;

/**
 *
 * @author alex
 */
public class ASTVisitor {
    private final AST root;
    private final StringBuilder buf = new StringBuilder();

    public ASTVisitor(AST root) {
        this.root = root;
    }
    
    public String dump() {
        buf.setLength(0);
        switch(root.getKind()) {
            case Programm:
                dumpProgramm((ProgrammAST) root);
                break;
            case Print:
                break;
            case Out:
                break;
            case Var:
                break;
            case Plus:
                break;
            case Minus:
                break;
            case Mul:
                break;
            case Div:
                break;
            case Pow:
                break;
            case Map:
                break;
            case Sequence:
                break;
            case Reduce:
                break;
            case Lambda:
                break;
            case Variable:
                break;
            case Number:
                break;
        }
        return buf.toString();
    }
    
    private void dumpProgramm(ProgrammAST ast) {
    }
    private void dumpPrint(PrintAST ast) {
    }
    private void dumpPrint(OutAST ast) {
    }
    private void dumpPrint(VarAST ast) {
    }
    private void dumpExpression(ExpressionAST ast) {
    }
}
