package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.ExpressionAST;
import interpretator.editor.Token;
import interpretator.api.ast.LambdaAST;

/**
 *
 * @author alex
 */
/*package-local*/ class LambdaImpl implements LambdaAST {
    private final Token v1;
    private final Token v2;
    private final ExpressionAST function;

    LambdaImpl(Token v1, Token v2, ExpressionAST function) {
        this.v1 = v1;
        this.v2 = v2;
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
                return v1.getText();
            case 1:
                assert v2 != null;
                return v2.getText();
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
