package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.ExpressionAST;
import interpretator.api.ast.LambdaAST;
import interpretator.api.lexer.Token;

/**
 *
 * @author alex
 */
/*package-local*/ class LambdaImpl implements LambdaAST {
    private final Token v1;
    private final String parameter1;
    private final Token v2;
    private final String parameter2;
    private final ExpressionAST function;

    /*package-local*/ LambdaImpl(Token v1, Token v2, ExpressionAST function) {
        this.v1 = v1;
        this.v2 = v2;
        parameter1 = v1.getText();
        if (v2 != null) {
            parameter2 = v2.getText();
        } else {
            parameter2 = null;
        }
        this.function = function;
    }

    @Override
    public int getParametersSize() {
        return v2 != null ? 2 : 1;
    }

    @Override
    public String getParameter(int i) {
        switch(i) {
            case 0:
                return parameter1;
            case 1:
                assert v2 != null;
                return parameter2;
            default:
                throw new ArrayIndexOutOfBoundsException(i);
        }
    }
    
    @Override
    public ExpressionAST getBody(){
        return function;
    }
    
    @Override
    public ASTKind getKind() {
        return ASTKind.Lambda;
    }

    @Override
    public Token getFistToken() {
        return v1;
    }
    
    @Override
    public String toString() {
        return getKind().name();
    }
}
