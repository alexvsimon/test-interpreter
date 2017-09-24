package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.ExpressionAST;
import interpretator.api.ast.LambdaAST;
import interpretator.api.ast.MapAST;
import interpretator.editor.Token;

/**
 *
 * @author alex
 */
/*package-local*/ class MapImpl implements MapAST {
    private final ExpressionAST arg1;
    private final LambdaAST lambda;
    private final Token map;

    MapImpl(Token map, ExpressionAST arg1, LambdaAST lambda) {
        this.map = map;
        this.arg1 = arg1;
        this.lambda = lambda;
    }

    @Override
    public ExpressionAST getInputExpression() {
        return arg1;
    }

    @Override
    public LambdaAST getLambda() {
        return lambda;
    }
    
    @Override
    public ASTKind getKind() {
        return ASTKind.Map;
    }

    @Override
    public Token getFistToken() {
        return map;
    }
    
    @Override
    public String toString() {
        return getKind().name();
    }
}
