package interpretator.editor;

import interpretator.api.lexer.Token;
import interpretator.api.lexer.TokenKind;

/**
 * Tokenizes document.
 * 
 * @author alex
 */
public class Lexer {
    private final DocumentContext doc;
    private int offset;
    private char c;

    /**
     * 
     * @param doc program snapshot.
     */
    public Lexer(DocumentContext doc) {
        this.doc = doc;
        read();
    }

    private void read() {
        if (offset < doc.getText().length()) {
            c = doc.getText().charAt(offset++);
        } else {
            offset++;
            c = 0;
        }
    }

    private Token token(TokenKind kind, int start, int end){
        return new TokenImpl(kind, start, end, doc);
    }
    
    private void readNumber() {
        boolean hasPoint = false;
        while (true) {
            read();
            switch(c) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    continue;
                case '.':
                    if (hasPoint) {
                        return;
                    } else {
                        hasPoint = true;
                        continue;
                    }
                default:
                    return;
            }
        }
    }
    
    private boolean isWiteSpace(){
        return c == ' ' || c == '\t' || c == '\n';
    }

    private boolean isIdentifier(){
        char cUpper = Character.toUpperCase(c);
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ_".indexOf(cUpper) != -1;
    }

    private boolean isDigit(){
        return "1234567890".indexOf(c) != -1;
    }
    
    /**
     * Gets next token in document.
     * 
     * <p>Last token is EOF.
     * 
     * @return next token
     */
    public Token nextToken() {
        int start = offset-1;
        if (c == 0) {
            return token(TokenKind.EOF, start, start);
        }
        if (isWiteSpace()) {
            read();
            while(isWiteSpace()) {
                read();
            }
            return token(TokenKind.WhiteSpace, start, offset-1);
        }
        if (isIdentifier()) {
            read();
            while(isIdentifier() || isDigit()) {
                read();
            }
            String id = doc.getText().subSequence(start, offset-1).toString();
            if ("var".equals(id)) {
                return token(TokenKind.Var, start, offset-1);
            } else if("out".equals(id)) {
                return token(TokenKind.Out, start, offset-1);
            } else if ("print".equals(id)) {
                return token(TokenKind.Print, start, offset-1);
            } else if ("map".equals(id)) {
                return token(TokenKind.Map, start, offset-1);
            } else if ("reduce".equals(id)) {
                return token(TokenKind.Reduce, start, offset-1);
            } else {
                return token(TokenKind.Identifier, start, offset-1);
            }
        }
        if (isDigit()) {
            readNumber();
            return token(TokenKind.Number, start, offset-1);
        }
        switch(c) {
            case '(':
                read();
                return token(TokenKind.LParen, start, offset-1);
            case ')':
                read();
                return token(TokenKind.RParen, start, offset-1);
            case '{':
                read();
                return token(TokenKind.LBrace, start, offset-1);
            case '}':
                read();
                return token(TokenKind.RBrace, start, offset-1);
            case '+':
                read();
                return token(TokenKind.Plus, start, offset-1);
            case '-':
            {
                read();
                if (c == '>') {
                    read();
                    return token(TokenKind.Arrow, start, offset-1);
                } else {
                    return token(TokenKind.Minus, start, offset-1);
                }
            }
            case '*':
                read();
                return token(TokenKind.Mul, start, offset-1);
            case '/':
                read();
                return token(TokenKind.Div, start, offset-1);
            case '^':
                read();
                return token(TokenKind.Pow, start, offset-1);
            case ',':
                read();
                return token(TokenKind.Comma, start, offset-1);
            case '=':
                read();
                return token(TokenKind.Eq, start, offset-1);
            case '"':
                read();
                while(true) {
                    if (c == 0) {
                        return token(TokenKind.Unknown, start, offset-1);
                    }
                    if (c == '"') {
                        read();
                        return token(TokenKind.String, start, offset-1);
                    }
                    read();
                }
        }
        read();
        return token(TokenKind.Unknown, start, offset-1);
    }    
}
