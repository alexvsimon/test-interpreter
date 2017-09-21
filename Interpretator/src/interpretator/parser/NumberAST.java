package interpretator.parser;

import interpretator.editor.Token;

/**
 *
 * @author alex
 */
public class NumberAST implements AST {
    private final Token number;

    NumberAST(Token number) {
        this.number = number;
    }

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
