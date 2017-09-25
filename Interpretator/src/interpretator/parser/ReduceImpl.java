package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.ExpressionAST;
import interpretator.api.ast.LambdaAST;
import interpretator.api.ast.ReduceAST;
import interpretator.api.lexer.Token;

/**
 *
 * @author alex
 */
/*package-local*/ class ReduceImpl implements ReduceAST {
    private final ExpressionAST arg1;
    private final ExpressionAST arg2;
    private final LambdaAST lambda;
    private final Token reduce;

    ReduceImpl(Token reduce, ExpressionAST arg1, ExpressionAST arg2, LambdaAST lambda) {
        this.reduce = reduce;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.lambda = lambda;
    }

    @Override
    public ExpressionAST getInputExpression() {
        return arg1;
    }

    @Override
    public ExpressionAST getStartExpression() {
        return arg2;
    }
    
    @Override
    public LambdaAST getLambda(){
        return lambda;
    }
    
    @Override
    public ASTKind getKind() {
        return ASTKind.Reduce;
    }

    @Override
    public Token getFistToken() {
        return reduce;
    }
    
    @Override
    public String toString() {
        return getKind().name();
    }
}
