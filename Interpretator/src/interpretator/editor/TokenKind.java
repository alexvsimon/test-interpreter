package interpretator.editor;

/**
 *
 * @author alex
 */
public enum TokenKind {
    Identifier,
    
    Plus, // +
    Minus, // -
    Mul, // *
    Div, // /
    Pow, // ^
    Arrow, // ->
    Eq, // =
    
    Comma, // ,

    LParen, // (
    RParen, // )
    
    LBrace, // {
    RBrace, // }
    
    Var, // var
    Map, // map
    Reduce, // reduce
    Print, // print
    Out, // out
    
    Number,
    String,
    
    WhiteSpace,
    EOF,
    Unknown
}
