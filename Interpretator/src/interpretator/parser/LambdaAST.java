package interpretator.parser;

import interpretator.editor.Token;

/**
 *
 * @author alex
 */
public class LambdaAST implements AST {
    private final Token v1;
    private final Token v2;
    private final AST function;

    LambdaAST(Token v1, Token v2, AST function) {
        this.v1 = v1;
        this.v2 = v2;
        this.function = function;
    }

    public int getParametersSize() {
        return v2 != null ? 2 : 1;
    }

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
    
    public AST getBody(){
        return function;
    }
    
    @Override
    public ASTKind getKind() {
        return ASTKind.Lambda;
    }
    
    @Override
    public String toString() {
        return getKind().name();
    }
}
