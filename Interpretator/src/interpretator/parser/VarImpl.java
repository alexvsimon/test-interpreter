package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.ExpressionAST;
import interpretator.editor.Token;
import interpretator.api.ast.VarAST;

/**
 *
 * @author alex
 */
/*package-local*/ class VarImpl implements VarAST {
    private final Token varToken;
    private final Token id;
    private final ExpressionAST expression;
            
    VarImpl(Token start, Token id, ExpressionAST expression){
        varToken = start;
        this.id = id;
        this.expression = expression;
    }

    @Override
    public String getName() {
        return id.getText();
    }
    
    @Override
    public ExpressionAST getExpression() {
        return expression;
    }
    
    @Override
    public ASTKind getKind() {
        return ASTKind.Var;
    }

    @Override
    public Token getFistToken() {
        return varToken;
    }

    @Override
    public String toString() {
        return getKind().name() + " " + id.getText();
    }
}
