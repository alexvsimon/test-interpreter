/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpretator.parser;

/**
 *
 * @author alex
 */
public enum ASTKind {
    Programm,

    Print,
    Out,
    Var,
    
    Plus,
    Minus,

    Mul,
    Div,
    
    Pow,
    
    Map,
    Sequence,
    Reduce,

    Lambda,
    
    Variable,
    Number,

}
