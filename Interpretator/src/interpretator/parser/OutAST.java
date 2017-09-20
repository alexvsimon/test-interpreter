/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpretator.parser;

import interpretator.editor.Token;

/**
 *
 * @author alex
 */
public class OutAST implements AST {
    private final Token outToken;
    private final AST expression;
    
    public OutAST(Token start, AST expression){
        outToken = start;
        this.expression = expression;
    }

    @Override
    public ASTKind getKind() {
        return ASTKind.Out;
    }

    @Override
    public String toString() {
        return getKind().name();
    }
}
