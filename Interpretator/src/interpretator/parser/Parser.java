package interpretator.parser;

import interpretator.editor.Lexer;
import interpretator.editor.Token;
import interpretator.editor.TokenKind;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alex
 */
public class Parser {
    private final List<Token> ts;
    private int index;
    private Token t;
    private final ProgrammAST programm;

    public Parser(Lexer lexer) {
        ts = new ArrayList<>();
        while(true) {
            Token token = lexer.nextToken();
            switch(token.getKind()) {
                case Unknown:
                    //TODO: error
                    continue;
                case WhiteSpace:
                    continue;
            }
            ts.add(token);
            if (token.getKind() == TokenKind.EOF) {
                break;
            }
        }
        next();
        programm = new ProgrammAST();
    }
    
    private void next() {
        if (index < ts.size()) {
            t = ts.get(index++);
        }
    }
    
    public ProgrammAST parse() {
        while(true) {
            switch(t.getKind()) {
                case EOF:
                    return programm;
                case Var:
                    var();
                    break;
                case Print:
                    print();
                    break;
                case Out:
                    out();
                    break;
                default:
                    // TODO: error
                    next();
                    break;
            }
        }
    }
    
    private void var() {
        Token startToken = t;
        next();
        if (t.getKind() == TokenKind.Identifier) {
            Token id = t;
            next();
            if (t.getKind() == TokenKind.Eq) {
                next();
                AST expr = expression();
                programm.add(new VarAST(startToken, id, expr));
            } else {
                //TODO: error
            }
        } else {
            //TODO: error
        }
    }

    private void print() {
        Token startToken = t;
        next();
        if (t.getKind() == TokenKind.String) {
            programm.add(new PrintAST(startToken, t));
        } else {
            //TODO: error
        }
        next();
    }

    private void out() {
        Token startToken = t;
        next();
        AST expr = expression();
        programm.add(new OutAST(startToken, expr));
    }

    private AST expression() {
        Token startToken = t;
        return null;
    }
}
