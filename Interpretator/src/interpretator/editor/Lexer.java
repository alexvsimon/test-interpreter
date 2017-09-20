package interpretator.editor;

/**
 *
 * @author alex
 */
public class Lexer {
    private enum State {
        INIT,
        VAR,
        VAR_INIT,
        PRINT,
        EXPR,
        
    }

    private final DocumentContext doc;
    private int offset;
    private State state;
    private char c;
    private boolean allowUnaryMinus;

    public Lexer(DocumentContext doc) {
        this.doc = doc;
        state = State.INIT;
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
        return new Token(kind, start, end, doc);
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
                state = State.VAR;
                allowUnaryMinus = false;
                return token(TokenKind.Var, start, offset-1);
            } else if("out".equals(id)) {
                state = State.EXPR;
                allowUnaryMinus = true;
                return token(TokenKind.Out, start, offset-1);
            } else if ("print".equals(id)) {
                state = State.PRINT;
                allowUnaryMinus = false;
                return token(TokenKind.Print, start, offset-1);
            } else if ("map".equals(id)) {
                state = State.EXPR;
                allowUnaryMinus = false;
                return token(TokenKind.Map, start, offset-1);
            } else if ("reduce".equals(id)) {
                state = State.EXPR;
                allowUnaryMinus = false;
                return token(TokenKind.Reduce, start, offset-1);
            } else {
                if (state == State.VAR) {
                    allowUnaryMinus = false;
                    state = State.VAR_INIT;
                    return token(TokenKind.Identifier, start, offset-1);
                } else {
                    allowUnaryMinus = false;
                    state = State.EXPR;
                    return token(TokenKind.Identifier, start, offset-1);
                }
            }
        }
        if (state == State.VAR_INIT) {
            if (c == '=') {
                read();
                allowUnaryMinus = true;
                state = State.EXPR;
                return token(TokenKind.Eq, start, offset-1);
            } else {
                read();
                return token(TokenKind.Unknown, start, offset-1);
            }
        }
        if (state == State.PRINT) {
            if (c == '"') {
                while (true) {
                    read();
                    if (c == 0) {
                        return token(TokenKind.Unknown, start, offset-1);
                    }
                    if (c == '"') {
                        state = State.INIT;
                        read();
                        return token(TokenKind.String, start, offset-1);
                    }
                }
            } else {
                read();
                return token(TokenKind.Unknown, start, offset-1);
            }
        }
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
            {
                readNumber();
                allowUnaryMinus = false;
                return token(TokenKind.Number, start, offset-1);
            }
            case '(':
                read();
                allowUnaryMinus = true;
                return token(TokenKind.LParen, start, offset-1);
            case ')':
                read();
                allowUnaryMinus = false;
                return token(TokenKind.RParen, start, offset-1);
            case '{':
                read();
                allowUnaryMinus = true;
                return token(TokenKind.LBrace, start, offset-1);
            case '}':
                read();
                allowUnaryMinus = false;
                return token(TokenKind.RBrace, start, offset-1);
            case '+':
                read();
                allowUnaryMinus = false;
                return token(TokenKind.Plus, start, offset-1);
            case '-':
            {
                read();
                if (c == '>') {
                    read();
                    allowUnaryMinus = true;
                    return token(TokenKind.Arrow, start, offset-1);
                } else if (allowUnaryMinus && isDigit()){
                    readNumber();
                    allowUnaryMinus = false;
                    return token(TokenKind.Number, start, offset-1);
                } else {
                    allowUnaryMinus = false;
                    return token(TokenKind.Minus, start, offset-1);
                }
            }
            case '*':
                read();
                allowUnaryMinus = false;
                return token(TokenKind.Mul, start, offset-1);
            case '/':
                read();
                allowUnaryMinus = false;
                return token(TokenKind.Div, start, offset-1);
            case '^':
                read();
                allowUnaryMinus = false;
                return token(TokenKind.Pow, start, offset-1);
            case ',':
                read();
                allowUnaryMinus = true;
                return token(TokenKind.Comma, start, offset-1);
        }
        read();
        return token(TokenKind.Unknown, start, offset-1);
    }    
}
