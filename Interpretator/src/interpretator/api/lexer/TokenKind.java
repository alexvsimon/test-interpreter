package interpretator.api.lexer;

/**
 * Token kinds.
 * 
 * <p> All possible token kinds according to grama:
 * <ul>
 *   <li> program ::= stmt | program stmt
 *   <li> stmt ::= var identifier = expr | out expr | print "string"
 *   <li> expr ::= expr op expr | (expr) | identifier | { expr, expr } | number | map(expr, identifier -&gt; expr) | reduce(expr, expr, identifier identifier -&gt; expr)
 *   <li> op ::= + | - | * | / | ^
 *   <li> identifier ::= letter (letter | digit)*
 *   <li> number ::= digit (digit)* | digit (digit)* . (digit)*
 *   <li> keyword ::= var | out | print | map | reduce
 * </ul>
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
