package interpretator.api.ast;

/**
 * All possible kinds of AST nodes.
 *
 * @author alex
 */
public enum ASTKind {
    Program,

    Print,
    Out,
    Var,
    
    UnaryMinus,

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
