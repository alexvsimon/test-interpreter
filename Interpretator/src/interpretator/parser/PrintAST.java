package interpretator.parser;

import interpretator.editor.Token;

/**
 *
 * @author alex
 */
public class PrintAST implements AST {
    private final Token stringToken;
    private final Token printToken;

    PrintAST(Token start, Token stringToken){
        printToken = start;
        this.stringToken = stringToken;
    }

    public String getString() {
        return stringToken.getText().substring(1, stringToken.getText().length()-1);
    }
    
    @Override
    public ASTKind getKind() {
        return ASTKind.Print;
    }

    @Override
    public String toString() {
        return getKind().name();
    }
}
