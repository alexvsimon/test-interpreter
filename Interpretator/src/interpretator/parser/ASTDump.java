package interpretator.parser;

import interpretator.api.ast.AST;
import interpretator.api.ast.BinaryExpressionAST;
import interpretator.api.ast.LambdaAST;
import interpretator.api.ast.MapAST;
import interpretator.api.ast.NumberAST;
import interpretator.api.ast.OutAST;
import interpretator.api.ast.PrintAST;
import interpretator.api.ast.ProgramAST;
import interpretator.api.ast.ReduceAST;
import interpretator.api.ast.SequenceAST;
import interpretator.api.ast.UnaryExpressionAST;
import interpretator.api.ast.VarAST;
import interpretator.api.ast.VariableAST;

/**
 *
 * @author alex
 */
/*package-local*/ class ASTDump {
    private final AST root;
    private final StringBuilder buf = new StringBuilder();
    private int shift;

    /*package-local*/ ASTDump(AST root) {
        this.root = root;
    }
    
    /*package-local*/ String dump() {
        buf.setLength(0);
        dump(root);
        return buf.toString();
    }
    
    private void dump(AST ast) {
        switch(ast.getKind()) {
            case Program:
                dumpProgram((ProgramAST) ast);
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
            case UnaryMinus:
                dumpUnaryExpression((UnaryExpressionAST) ast);
                break;
            case Plus:
            case Minus:
            case Mul:
            case Div:
            case Pow:
                dumpExpression((BinaryExpressionAST) ast);
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
    
    private void dumpProgram(ProgramAST ast) {
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

    private void dumpUnaryExpression(UnaryExpressionAST ast) {
        print(ast.getKind().name());
        shift++;
        dump(ast.getExpression());
        shift--;
    }

    private void dumpExpression(BinaryExpressionAST ast) {
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
        StringBuilder tmp = new StringBuilder(ast.getKind().name());
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
