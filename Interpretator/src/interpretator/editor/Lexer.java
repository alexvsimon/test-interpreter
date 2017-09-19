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

    public Lexer(DocumentContext doc) {
        this.doc = doc;
        state = State.INIT;
    }

    private char read() {
        if (offset < doc.getText().length()) {
            return doc.getText().charAt(offset++);
        } else {
            return 0;
        }
    }

    private void unread() {
        if (offset > 0) {
            offset--;
        } else {
            throw new IllegalStateException();
        }
    }
    
    private Token token(TokenKind kind, int start, int end){
        return new Token(kind, start, end, doc);
    }
    
    private String readWord(char c) {
        StringBuilder buf = new StringBuilder();
        buf.append(c);
        while (true) {
            c = read();
            if (c == 0) {
                break;
            }
            if (Character.isJavaIdentifierPart(c)) {
                buf.append(c);
            } else {
                unread();
                break;
            }
        }
        return buf.toString();
    }

    private void readNumber(char c) {
        boolean hasPoint = c == '.';
        loop:while (true) {
            c = read();
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
                    continue loop;
                case '.':
                    if (hasPoint) {
                        unread();
                        break loop;
                    } else {
                        hasPoint = true;
                        continue loop;
                    }
                case 0:
                    break loop;
                default:
                    unread();
                    break loop;
            }
        }
    }
    
    public Token nextToken() {
        while(true) {
            char c = read();
            if (c == 0) {
                return token(TokenKind.EOF, offset, offset);
            }
            if (c == ' ' || c == '\t' || c == '\n') {
                int start = offset-1;
                while (true) {
                    c = read();
                    if (c == ' ' || c == '\t' || c == '\n') {
                       continue; 
                    }
                    if (c != 0) {
                        unread();
                    }
                    return token(TokenKind.WhiteSpace, start, offset);
                }
            }
            switch (state) {
                case EXPR:
                case INIT:
                {
                    int start = offset-1;
                    if (c == 'v' || c == 'o' || c == 'p') {
                        String word = readWord(c);
                        if ("var".equals(word)) {
                            state = State.VAR;
                            return token(TokenKind.Var, start, offset);
                        } else if ("out".equals(word)) {
                            state = State.EXPR;
                            return token(TokenKind.Out, start, offset);
                        } else if ("print".equals(word)) {
                            state = State.PRINT;
                            return token(TokenKind.Print, start, offset);
                        } else {
                            if (state == State.INIT) {
                                return token(TokenKind.Unknown, start, offset);
                            }
                        }
                    } else {
                        if (state == State.INIT) {
                            return token(TokenKind.Unknown, start, offset);
                        }
                    }
                    break;
                }
                case VAR:
                {
                    int start = offset-1;
                    if (Character.isJavaIdentifierStart(c)){
                        String word = readWord(c);
                        state = State.VAR_INIT;
                        return token(TokenKind.Identifier, start, offset);
                    } else {
                        return token(TokenKind.Unknown, start, offset);
                    }
                }
                case VAR_INIT:
                {
                    if (c == '=') {
                        state = State.EXPR;
                        return token(TokenKind.Eq, offset-1, offset);
                    } else {
                        return token(TokenKind.Unknown, offset-1, offset);
                    }
                }
                case PRINT:
                {
                    int start = offset-1;
                    if (c == '"') {
                        while (true) {
                            c = read();
                            if (c == 0) {
                                return token(TokenKind.Unknown, start, offset);
                            }
                            if (c == '"') {
                                state = State.INIT;
                                return token(TokenKind.String, start, offset);
                            }
                        }
                    } else {
                        return token(TokenKind.Unknown, start, offset);
                    }
                }
            }
            assert state == State.EXPR;
            //case EXPR:
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
                case '.':
                {
                    int start = offset-1;
                    readNumber(c);
                    return token(TokenKind.Number, start, offset);
                }
                case '(':
                    return token(TokenKind.LParen, offset-1, offset);
                case ')':
                    return token(TokenKind.RParen,offset-1, offset);
                case '{':
                    return token(TokenKind.LBrace, offset-1, offset);
                case '}':
                    return token(TokenKind.RBrace, offset-1, offset);
                case '+':
                    return token(TokenKind.Plus, offset-1, offset);
                case '-':
                {
                    int start = offset-1;
                    c = read();
                    if (c == '>') {
                        return token(TokenKind.Arrow, start, offset);
                    } else {
                        if (c != 0) { 
                            unread();
                        }
                        return token(TokenKind.Minus, start, offset);
                    }
                }
                case '*':
                    return token(TokenKind.Mul, offset-1, offset);
                case '/':
                    return token(TokenKind.Div, offset-1, offset);
                case '^':
                    return token(TokenKind.Pow, offset-1, offset);
                case ',':
                    return token(TokenKind.Comma, offset-1, offset);
                default:
                {
                    int start = offset-1;
                    if (Character.isJavaIdentifierStart(c)) {
                        String word = readWord(c);
                        if ("map".equals(word)) {
                            return token(TokenKind.Map, start, offset);
                        } else if ("reduce".equals(word)) {
                            return token(TokenKind.Reduce, start, offset);
                        } else {
                            return token(TokenKind.Identifier, start, offset);
                        }
                    } else {
                        return token(TokenKind.Unknown, offset-1, offset);
                    }
                }
            }
        }
    }
    
}
