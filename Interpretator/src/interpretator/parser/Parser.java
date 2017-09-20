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
        return parsePlusMinus();
    }

    private AST parsePlusMinus() {
        AST lh = parseMulDiv();
        while(t.getKind() == TokenKind.Plus || t.getKind() == TokenKind.Minus) {
            Token op = t;
            next();
            AST rh = parseMulDiv();
            lh = new ExpressionAST(lh, rh, op.getKind() == TokenKind.Plus ? ASTKind.Plus : ASTKind.Minus);
        }
        return lh;
    }

    private AST parseMulDiv() {
        AST lh = parsePow();
        while(t.getKind() == TokenKind.Mul || t.getKind() == TokenKind.Div) {
            Token op = t;
            next();
            AST rh = parsePow();
            lh = new ExpressionAST(lh, rh, op.getKind() == TokenKind.Mul ? ASTKind.Mul : ASTKind.Div);
        }
        return lh;
    }
    
    private AST parsePow() {
        AST lh = parseFunction();
        while(t.getKind() == TokenKind.Pow) {
            next();
            AST rh = parseFunction();
            lh = new ExpressionAST(lh, rh, ASTKind.Pow);
        }
        return lh;
        
    }
    
    private AST parseFunction() {
        switch(t.getKind()) {
            case Map:
                return parseMap();
            case Reduce:
                return parseReduce();
            case LParen:
            {
                next();
                AST expr = expression();
                if (t.getKind() == TokenKind.RParen) {
                    next();
                    return expr;
                } else {
                    // TODO: error
                }
                break;
            }
            case LBrace:
            {
                next();
                AST arg1 = expression();
                if (t.getKind() == TokenKind.Comma) {
                    next();
                    AST arg2 = expression();
                    if (t.getKind() == TokenKind.RBrace) {
                        next();
                        return new SequenceAST(arg1, arg2);
                    } else {
                    // TODO: error
                    }
                } else {
                    // TODO: error
                }
                break;
            }
            case Identifier:
            {
                Token id = t;
                next();
                return new VariableAST(id);
            }
            case Number:
            {
                Token number = t;
                next();
                return new NumberAST(number);
            }
            default: 
            {
                next();
                // TODO: error
            }
        }
        return null;
    }

    private AST parseMap() {
        next();
        if (t.getKind() == TokenKind.LParen) {
            next();
            AST arg1 = expression();
            if (t.getKind() == TokenKind.Comma) {
                next();
                AST lambda = parseLambda();
                if (t.getKind() == TokenKind.RParen) {
                    next();
                    return new MapAST(arg1, lambda);
                }
            } else {
                // TODO: error
            }
        } else {
            // TODO: error
        }
        return null;
    }
    
    private AST parseReduce() {
        next();
        if (t.getKind() == TokenKind.LParen) {
            next();
            AST arg1 = expression();
            if (t.getKind() == TokenKind.Comma) {
                AST arg2 = expression();
                if (t.getKind() == TokenKind.Comma) {
                    next();
                    AST lambda = parseLambda();
                    if (t.getKind() == TokenKind.RParen) {
                        next();
                        return new ReduceAST(arg1, arg2, lambda);
                    }
                } else {
                // TODO: error
                }
            } else {
                // TODO: error
            }
        } else {
            // TODO: error
        }
        return null;
    }

    private AST parseLambda() {
        Token v1 = null;
        Token v2 = null;
        if (t.getKind() == TokenKind.Identifier) {
            v1 = t;
            next();
            if (t.getKind() == TokenKind.Identifier) {
                v2 = t;
                next();
            }
            if (t.getKind() == TokenKind.Arrow) {
                next();
                AST function = expression();
                return new LambdaAST(v1, v2, function);
            } else {
                // TODO: error
            }
        } else {
            // TODO: error
        }
        return null;
    }
}
