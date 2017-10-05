package interpretator.run;

import interpretator.api.ast.AST;
import interpretator.api.ast.ASTKind;
import interpretator.api.ast.BinaryExpressionAST;
import interpretator.api.ast.LambdaAST;
import interpretator.api.ast.NumberAST;
import interpretator.api.ast.UnaryExpressionAST;
import interpretator.api.ast.VariableAST;
import interpretator.api.run.CanceledRuntimeException;
import interpretator.api.run.InterpreterRuntimeException;

/**
 *
 * @author alex
 */
 /*package-local*/ final class DoubleEval {
    private final AST root;
    private DoubleVarsMap vars;
    
    /**
     * Creates interpreter for AST.
     * 
     * @param root root AST is a program or lambda
     */
    /*package-local*/ DoubleEval(AST root) {
        this.root = root;
    }

    /**
     * Interprets double lambda.
     * 
     * @param vars input parameters map
     * @return value
     * @throws CanceledRuntimeException if interpretation is canceled.
     * @throws InterpreterRuntimeException if interpreter finds runtime error such as incompatible type,
            reference on undefined variable, unsupported operations, and other.
     */
    /*package-local*/ double evalDoubleLambda(DoubleVarsMap vars) throws CanceledRuntimeException, InterpreterRuntimeException {
        assert root.getKind() == ASTKind.Lambda;
        this.vars = vars;
        return eval(((LambdaAST) root).getBody());
    }
    
    private double eval(AST ast) {
        if(Thread.interrupted()) {
            throw new CanceledRuntimeException(ast);
        }
        switch (ast.getKind()) {
            case UnaryMinus:
                return evalUnaryExpression((UnaryExpressionAST) ast);
            case Plus:
            case Minus:
            case Mul:
            case Div:
            case Pow:
                return evalExpression((BinaryExpressionAST) ast);
            case Variable:
                return evalVariable((VariableAST) ast);
            case Number:
                return evalNumber((NumberAST) ast);
        }
        throw new InterpreterRuntimeException("Unsupported operation in optimization", ast);
    }
    

    private double evalUnaryExpression(UnaryExpressionAST ast) {
        assert ast.getKind() == ASTKind.UnaryMinus;
        return -eval(ast.getExpression());
    }
    
    private double evalExpression(BinaryExpressionAST ast) {
        double lh = eval(ast.getLeftExpression());
        double rh = eval(ast.getRightExpression());
        switch (ast.getKind()) {
            case Plus: {
                return lh + rh;
            }
            case Minus: {
                return lh - rh;
            }
            case Mul: {
                return lh * rh;
            }
            case Div: {
                return lh / rh;
            }
            case Pow: {
                double closeInt = Math.rint(lh);
                if (lh == closeInt) {
                    int intLH = (int)Math.round(lh);
                    if (intLH == -1) {
                        int pow = (int)Math.round(rh);
                        return pow % 2 == 0 ? 1 : -1;
                    }
                }
                return Math.pow(lh, rh);
            }
            default:
                throw new InterpreterRuntimeException("Unsupported binary operation in optimization", ast);
        }
    }
    
    private double evalVariable(VariableAST ast) {
        return vars.get(ast.getName());
    }
    
    private double evalNumber(NumberAST ast) {
        Number eval = ast.eval();
        if (eval instanceof Integer) {
            return eval.intValue();
        } else {
            return eval.doubleValue();
        }
    }

}
