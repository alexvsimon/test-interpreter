package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.NumberAST;
import interpretator.api.lexer.Token;
import interpretator.api.run.InterpreterRuntimeException;

/**
 *
 * @author alex
 */
/*package-local*/ final class NumberImpl implements NumberAST {
    private final Token number;
    private final String value;
    private Number eval;

    /*package-local*/ NumberImpl(Token number) {
        this.number = number;
        this.value = number.getText();
    }

    @Override
    public String getValue() {
        return value;
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

    @Override
    public Number eval() throws InterpreterRuntimeException {
        if (eval == null) {
            if (value.indexOf('.') > 0) {
                try {
                    eval = Double.parseDouble(value);
                } catch (NumberFormatException ex) {
                    throw new InterpreterRuntimeException("Double '"+value+"' is too big", this);
                }
            } else {
                try {
                    eval = Integer.parseInt(value);
                } catch (NumberFormatException ex) {
                    throw new InterpreterRuntimeException("Integer '"+value+"' is too big", this);
                }
            }
        }
        return eval;
    }
}
