package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.ExpressionAST;
import interpretator.api.ast.AST;
import interpretator.api.ast.LambdaAST;
import interpretator.api.ast.ProgramAST;
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
    private final ProgramImpl program;

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
        program = new ProgramImpl();
    }
    
    private void next() {
        if (index < ts.size()) {
            t = ts.get(index++);
        }
    }
    
    public ProgramAST parse() {
        while(true) {
            switch(t.getKind()) {
                case EOF:
                    return program;
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
                ExpressionAST expr = expression();
                program.add(new VarImpl(startToken, id, expr));
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
            program.add(new PrintImpl(startToken, t));
        } else {
            //TODO: error
        }
        next();
    }

    private void out() {
        Token startToken = t;
        next();
        AST expr = expression();
        program.add(new OutImpl(startToken, expr));
    }

    private ExpressionAST expression() {
        return parsePlusMinus();
    }

    private ExpressionAST parsePlusMinus() {
        ExpressionAST lh = parseMulDiv();
        while(t.getKind() == TokenKind.Plus || t.getKind() == TokenKind.Minus) {
            Token op = t;
            next();
            ExpressionAST rh = parseMulDiv();
            lh = new BinaryExpressionImpl(lh, rh, op.getKind() == TokenKind.Plus ? ASTKind.Plus : ASTKind.Minus);
        }
        return lh;
    }

    private ExpressionAST parseMulDiv() {
        ExpressionAST lh = parsePow();
        while(t.getKind() == TokenKind.Mul || t.getKind() == TokenKind.Div) {
            Token op = t;
            next();
            ExpressionAST rh = parsePow();
            lh = new BinaryExpressionImpl(lh, rh, op.getKind() == TokenKind.Mul ? ASTKind.Mul : ASTKind.Div);
        }
        return lh;
    }
    
    private ExpressionAST parsePow() {
        ExpressionAST lh = parseFunction();
        while(t.getKind() == TokenKind.Pow) {
            next();
            ExpressionAST rh = parseFunction();
            lh = new BinaryExpressionImpl(lh, rh, ASTKind.Pow);
        }
        return lh;
        
    }
    
    private ExpressionAST parseFunction() {
        switch(t.getKind()) {
            case Map:
                return parseMap();
            case Reduce:
                return parseReduce();
            case LParen:
            {
                next();
                ExpressionAST expr = expression();
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
                ExpressionAST arg1 = expression();
                if (t.getKind() == TokenKind.Comma) {
                    next();
                    ExpressionAST arg2 = expression();
                    if (t.getKind() == TokenKind.RBrace) {
                        next();
                        return new SequenceImpl(arg1, arg2);
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
                return new VariableImpl(id);
            }
            case Number:
            {
                Token number = t;
                next();
                return new NumberImpl(number);
            }
            default: 
            {
                next();
                // TODO: error
            }
        }
        return null;
    }

    private ExpressionAST parseMap() {
        next();
        if (t.getKind() == TokenKind.LParen) {
            next();
            ExpressionAST arg1 = expression();
            if (t.getKind() == TokenKind.Comma) {
                next();
                LambdaAST lambda = parseLambda();
                if (t.getKind() == TokenKind.RParen) {
                    next();
                    return new MapImpl(arg1, lambda);
                }
            } else {
                // TODO: error
            }
        } else {
            // TODO: error
        }
        return null;
    }
    
    private ExpressionAST parseReduce() {
        next();
        if (t.getKind() == TokenKind.LParen) {
            next();
            ExpressionAST arg1 = expression();
            if (t.getKind() == TokenKind.Comma) {
                next();
                ExpressionAST arg2 = expression();
                if (t.getKind() == TokenKind.Comma) {
                    next();
                    LambdaAST lambda = parseLambda();
                    if (t.getKind() == TokenKind.RParen) {
                        next();
                        return new ReduceImpl(arg1, arg2, lambda);
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

    private LambdaImpl parseLambda() {
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
                ExpressionAST function = expression();
                return new LambdaImpl(v1, v2, function);
            } else {
                // TODO: error
            }
        } else {
            // TODO: error
        }
        return null;
    }
}
