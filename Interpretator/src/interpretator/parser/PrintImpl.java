package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.PrintAST;
import interpretator.api.lexer.Token;

/**
 *
 * @author alex
 */
/*package-local*/ class PrintImpl implements PrintAST {
    private final Token stringToken;
    private final Token printToken;

    /*package-local*/ PrintImpl(Token start, Token stringToken){
        printToken = start;
        this.stringToken = stringToken;
    }

    @Override
    public String getString() {
        return stringToken.getText().substring(1, stringToken.getText().length()-1).replace("\\n", "\n");
    }
    
    @Override
    public ASTKind getKind() {
        return ASTKind.Print;
    }

    @Override
    public Token getFistToken() {
        return printToken;
    }

    @Override
    public String toString() {
        return getKind().name();
    }

}
