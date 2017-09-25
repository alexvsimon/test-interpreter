package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.ExpressionAST;
import interpretator.api.ast.SequenceAST;
import interpretator.api.lexer.Token;

/**
 *
 * @author alex
 */
/*package-local*/ class SequenceImpl implements SequenceAST {
    private final ExpressionAST arg1;
    private final ExpressionAST arg2;
    private final Token startToken;

    SequenceImpl(Token startToken, ExpressionAST arg1, ExpressionAST arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.startToken = startToken;
    }

    @Override
    public ExpressionAST getStartExpression(){
        return arg1;
    }

    @Override
    public ExpressionAST getEndExpression(){
        return arg2;
    }

    @Override
    public ASTKind getKind() {
        return ASTKind.Sequence;
    }

    @Override
    public Token getFistToken() {
        return startToken;
    }
    
    @Override
    public String toString() {
        return getKind().name();
    }
}
