package interpretator.parser;

import interpretator.editor.Token;

/**
 *
 * @author alex
 */
public class VarAST implements AST {
    private final Token varToken;
    private final Token id;
    private final AST expression;
            
    VarAST(Token start, Token id, AST expression){
        varToken = start;
        this.id = id;
        this.expression = expression;
    }

    public String getName() {
        return id.getText();
    }
    
    public AST getExpression() {
        return expression;
    }
    
    @Override
    public ASTKind getKind() {
        return ASTKind.Var;
    }

    @Override
    public String toString() {
        return getKind().name() + " " + id.getText();
    }
}
