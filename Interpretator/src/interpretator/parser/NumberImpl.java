package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.NumberAST;
import interpretator.api.lexer.Token;

/**
 *
 * @author alex
 */
/*package-local*/ class NumberImpl implements NumberAST {
    private final Token number;

    NumberImpl(Token number) {
        this.number = number;
    }

    @Override
    public String getValue() {
        return number.getText();
    }

    @Override
    public ASTKind getKind() {
        return ASTKind.Number;
    }

    @Override
    public Token getFistToken() {
        return number;
    }
    
    @Override
    public String toString() {
        return getKind().name();
    }
}
