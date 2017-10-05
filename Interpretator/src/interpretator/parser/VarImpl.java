package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.ExpressionAST;
import interpretator.api.ast.VarAST;
import interpretator.api.lexer.Token;

/**
 *
 * @author alex
 */
/*package-local*/ final class VarImpl implements VarAST {
    private final Token varToken;
    private final Token id;
    private final ExpressionAST expression;
    private final String name;
            
    /*package-local*/ VarImpl(Token start, Token id, ExpressionAST expression){
        varToken = start;
        this.id = id;
        this.expression = expression;
        name = id.getText();
    }

    @Override
    public String getName() {
        return name;
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
