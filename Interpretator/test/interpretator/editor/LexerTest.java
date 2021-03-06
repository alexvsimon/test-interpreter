package interpretator.editor;

import interpretator.DocumentContext;
import interpretator.api.lexer.Lexer;
import interpretator.api.lexer.Token;
import interpretator.api.lexer.TokenKind;
import interpretator.spi.lexer.LexerFactory;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author alex
 */
public class LexerTest {
    
    public LexerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void emptyStream() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("", 0));
        List<Token> tokens = getTokens(lexer);
        assertEquals(1, tokens.size());
        assertEquals(TokenKind.EOF, tokens.get(0).getKind());
    }

    @Test
    public void emptySpaceStream() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext(" ", 0));
        List<Token> tokens = getTokens(lexer);
        assertEquals(2, tokens.size());
        assertEquals(TokenKind.WhiteSpace, tokens.get(0).getKind());
        assertEquals(TokenKind.EOF, tokens.get(1).getKind());
    }

    @Test
    public void emptyNewLineStream() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("\n", 0));
        List<Token> tokens = getTokens(lexer);
        assertEquals(2, tokens.size());
        assertEquals(TokenKind.WhiteSpace, tokens.get(0).getKind());
        assertEquals(TokenKind.EOF, tokens.get(1).getKind());
    }

    @Test
    public void varStream() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("var n = 500", 0));
        List<Token> tokens = getTokens(lexer);
        assertEquals(8, tokens.size());
        assertEquals(TokenKind.Var, tokens.get(0).getKind());
        assertEquals(TokenKind.WhiteSpace, tokens.get(1).getKind());
        assertEquals(TokenKind.Identifier, tokens.get(2).getKind());
        assertEquals("n", tokens.get(2).getText());
        assertEquals(TokenKind.WhiteSpace, tokens.get(3).getKind());
        assertEquals(TokenKind.Eq, tokens.get(4).getKind());
        assertEquals(TokenKind.WhiteSpace, tokens.get(5).getKind());
        assertEquals(TokenKind.Number, tokens.get(6).getKind());
        assertEquals("500", tokens.get(6).getText());
        assertEquals(TokenKind.EOF, tokens.get(7).getKind());
    }
    
    @Test
    public void printStream() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("print \"pi = \"", 0));
        List<Token> tokens = getTokens(lexer);
        assertEquals(4, tokens.size());
        assertEquals(TokenKind.Print, tokens.get(0).getKind());
        assertEquals(TokenKind.WhiteSpace, tokens.get(1).getKind());
        assertEquals(TokenKind.String, tokens.get(2).getKind());
        assertEquals("\"pi = \"", tokens.get(2).getText());
        assertEquals(TokenKind.EOF, tokens.get(3).getKind());
    }

    @Test
    public void outStream() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("out pi", 0));
        List<Token> tokens = getTokens(lexer);
        assertEquals(4, tokens.size());
        assertEquals(TokenKind.Out, tokens.get(0).getKind());
        assertEquals(TokenKind.WhiteSpace, tokens.get(1).getKind());
        assertEquals(TokenKind.Identifier, tokens.get(2).getKind());
        assertEquals("pi", tokens.get(2).getText());
        assertEquals(TokenKind.EOF, tokens.get(3).getKind());
    }

    @Test
    public void expressionStream() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("(-1)^i / (2.0 * i + 1)", 0));
        List<Token> tokens = getTokens(lexer);
        assertEquals(21, tokens.size());
        assertEquals(TokenKind.LParen, tokens.get(0).getKind());
        assertEquals(TokenKind.Minus, tokens.get(1).getKind());
        assertEquals(TokenKind.Number, tokens.get(2).getKind());
        assertEquals("1", tokens.get(2).getText());
        assertEquals(TokenKind.RParen, tokens.get(3).getKind());
        assertEquals(TokenKind.Pow, tokens.get(4).getKind());
        assertEquals(TokenKind.Identifier, tokens.get(5).getKind());
        assertEquals("i", tokens.get(5).getText());
        assertEquals(TokenKind.WhiteSpace, tokens.get(6).getKind());
        assertEquals(TokenKind.Div, tokens.get(7).getKind());
        assertEquals(TokenKind.WhiteSpace, tokens.get(8).getKind());
        assertEquals(TokenKind.LParen, tokens.get(9).getKind());
        assertEquals(TokenKind.Number, tokens.get(10).getKind());
        assertEquals("2.0", tokens.get(10).getText());
        assertEquals(TokenKind.WhiteSpace, tokens.get(11).getKind());
        assertEquals(TokenKind.Mul, tokens.get(12).getKind());
        assertEquals(TokenKind.WhiteSpace, tokens.get(13).getKind());
        assertEquals(TokenKind.Identifier, tokens.get(14).getKind());
        assertEquals("i", tokens.get(14).getText());
        assertEquals(TokenKind.WhiteSpace, tokens.get(15).getKind());
        assertEquals(TokenKind.Plus, tokens.get(16).getKind());
        assertEquals(TokenKind.WhiteSpace, tokens.get(17).getKind());
        assertEquals(TokenKind.Number, tokens.get(18).getKind());
        assertEquals("1", tokens.get(18).getText());
        assertEquals(TokenKind.RParen, tokens.get(19).getKind());
        assertEquals(TokenKind.EOF, tokens.get(20).getKind());
    }
    
    @Test
    public void expressionLabdaStream() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("4*reduce(sequence,0,x y->x+y)", 0));
        List<Token> tokens = getTokens(lexer);
        assertEquals(17, tokens.size());
        assertEquals(TokenKind.Number, tokens.get(0).getKind());
        assertEquals("4", tokens.get(0).getText());
        assertEquals(TokenKind.Mul, tokens.get(1).getKind());
        assertEquals(TokenKind.Reduce, tokens.get(2).getKind());
        assertEquals(TokenKind.LParen, tokens.get(3).getKind());
        assertEquals(TokenKind.Identifier, tokens.get(4).getKind());
        assertEquals("sequence", tokens.get(4).getText());
        assertEquals(TokenKind.Comma, tokens.get(5).getKind());
        assertEquals(TokenKind.Number, tokens.get(6).getKind());
        assertEquals("0", tokens.get(6).getText());
        assertEquals(TokenKind.Comma, tokens.get(7).getKind());
        assertEquals(TokenKind.Identifier, tokens.get(8).getKind());
        assertEquals("x", tokens.get(8).getText());
        assertEquals(TokenKind.WhiteSpace, tokens.get(9).getKind());
        assertEquals(TokenKind.Identifier, tokens.get(10).getKind());
        assertEquals("y", tokens.get(10).getText());
        assertEquals(TokenKind.Arrow, tokens.get(11).getKind());
        assertEquals(TokenKind.Identifier, tokens.get(12).getKind());
        assertEquals("x", tokens.get(12).getText());
        assertEquals(TokenKind.Plus, tokens.get(13).getKind());
        assertEquals(TokenKind.Identifier, tokens.get(14).getKind());
        assertEquals("y", tokens.get(14).getText());
        assertEquals(TokenKind.RParen, tokens.get(15).getKind());
        assertEquals(TokenKind.EOF, tokens.get(16).getKind());
    }

    @Test
    public void expressionSequenceStream() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("{0,n}", 0));
        List<Token> tokens = getTokens(lexer);
        assertEquals(6, tokens.size());
        assertEquals(TokenKind.LBrace, tokens.get(0).getKind());
        assertEquals(TokenKind.Number, tokens.get(1).getKind());
        assertEquals("0", tokens.get(1).getText());
        assertEquals(TokenKind.Comma, tokens.get(2).getKind());
        assertEquals(TokenKind.Identifier, tokens.get(3).getKind());
        assertEquals("n", tokens.get(3).getText());
        assertEquals(TokenKind.RBrace, tokens.get(4).getKind());
        assertEquals(TokenKind.EOF, tokens.get(5).getKind());
    }

    /**
     * Test checks that lexer does not throw exceptions and does not have infinite loop on partly typed txt
     */
    @Test 
    public void typing() {
        String source = "var n = 500\n" +
                        "var sequence = map({0, n}, i -> (-1)^i / (2.0 * i + 1))\n" +
                        "var pi = 4 * reduce(sequence, 0, x y -> x + y)\n" +
                        "print \"pi = \"\n" +
                        "out pi\n";
        // type at the end of file
        for(int i = 0; i < source.length(); i++) {
            Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext(source.substring(0, i), 0));
            List<Token> tokens = getTokens(lexer);
            //System.err.println("Source length="+i+" token stream size="+tokens.size());
        }
        // type line inside of file
        String[] lines = source.split("\n");
        StringBuilder buf = new StringBuilder();
        for(int i = 1; i < lines.length -1; i++) {
            String line = lines[i];
            for(int j = 0; j < line.length(); j++) {
                buf.setLength(0);
                for(int k = 0; k < i; k++) {
                    buf.append(lines[k]).append('\n');
                }
                buf.append(line.substring(0, j)).append('\n');
                for(int k = i + 1; k < lines.length; k++) {
                    buf.append(lines[k]).append('\n');
                }
                Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext(buf.toString(), 0));
                List<Token> tokens = getTokens(lexer);
                //System.err.println("Source length="+buf.length()+" token stream size="+tokens.size());
            }
        }
    }

    private List<Token> getTokens(Lexer lexer) {
        List<Token> res = new ArrayList<>();
        while(true) {
            Token token = lexer.nextToken();
            res.add(token);
            if (token.getKind() == TokenKind.EOF) {
                break;
            }
        }
        return res;
    }
}
