package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.editor.Token;
import interpretator.api.ast.NumberAST;

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
    public String toString() {
        return getKind().name();
    }
}
