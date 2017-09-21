package interpretator.parser;

/**
 *
 * @author alex
 */
public class ASTDump {
    private final AST root;
    private final StringBuilder buf = new StringBuilder();
    private int shift;

    public ASTDump(AST root) {
        this.root = root;
    }
    
    public String dump() {
        buf.setLength(0);
        dump(root);
        return buf.toString();
    }
    
    private void dump(AST ast) {
        switch(ast.getKind()) {
            case Programm:
                dumpProgramm((ProgrammAST) ast);
                break;
            case Print:
                dumpPrint((PrintAST) ast);
                break;
            case Out:
                dumpOut((OutAST) ast);
                break;
            case Var:
                dumpVar((VarAST) ast);
                break;
            case Plus:
            case Minus:
            case Mul:
            case Div:
            case Pow:
                dumpExpression((ExpressionAST) ast);
                break;
            case Map:
                dumpMap((MapAST) ast);
                break;
            case Sequence:
                dumpSequence((SequenceAST) ast);
                break;
            case Reduce:
                dumpReduce((ReduceAST) ast);
                break;
            case Lambda:
                dumpLambda((LambdaAST) ast);
                break;
            case Variable:
                dumpVariable((VariableAST) ast);
                break;
            case Number:
                dumpNumber((NumberAST) ast);
                break;
        }
    }
    
    private void dumpProgramm(ProgrammAST ast) {
        for(AST statement : ast.getStatements()) {
            dump(statement);
        }
    }
    
    private void dumpPrint(PrintAST ast) {
        print(ast.getKind().name()+" "+ast.getString());
    }
    
    private void dumpOut(OutAST ast) {
        print(ast.getKind().name());
        shift++;
        dump(ast.getExpression());
        shift--;
    }
    
    private void dumpVar(VarAST ast) {
        print(ast.getKind().name()+" "+ast.getName());
        shift++;
        dump(ast.getExpression());
        shift--;
    }
    
    private void dumpExpression(ExpressionAST ast) {
        print(ast.getKind().name());
        shift++;
        dump(ast.getLeftExpression());
        dump(ast.getRightExpression());
        shift--;
    }
    
    private void dumpMap(MapAST ast) {
        print(ast.getKind().name());
        shift++;
        dump(ast.getInputExpression());
        dump(ast.getLambda());
        shift--;
    }
    
    private void dumpSequence(SequenceAST ast) {
        print(ast.getKind().name());
        shift++;
        dump(ast.getStartExpression());
        dump(ast.getEndExpression());
        shift--;
    }
    
    private void dumpReduce(ReduceAST ast) {
        print(ast.getKind().name());
        shift++;
        dump(ast.getInputExpression());
        dump(ast.getStartExpression());
        dump(ast.getLambda());
        shift--;
    }
    
    private void dumpLambda(LambdaAST ast) {
        StringBuffer tmp = new StringBuffer(ast.getKind().name());
        for(int i = 0; i < ast.getParametersSize(); i++) {
            tmp.append(' ').append(ast.getParameter(i));
        }
        print(tmp.toString());
        shift++;
        dump(ast.getBody());
        shift--;
    }
    
    private void dumpVariable(VariableAST ast) {
        print(ast.getKind().name()+ " "+ast.getName());
    }
    
    private void dumpNumber(NumberAST ast) {
        print(ast.getKind().name()+ " "+ast.getValue());
    }
    
    private void print(String s) {
        for(int i = 0; i < shift; i++) {
            buf.append(' ');
        }
        buf.append(s);
        buf.append('\n');
    }
}
