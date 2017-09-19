package interpretator.editor;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
        Lexer lexer = new Lexer(new DocumentContext(""));
        List<Token> tokens = getTokens(lexer);
        assertEquals(1, tokens.size());
        assertEquals(TokenKind.EOF, tokens.get(0).getKind());
    }

    @Test
    public void emptySpaceStream() {
        Lexer lexer = new Lexer(new DocumentContext(" "));
        List<Token> tokens = getTokens(lexer);
        assertEquals(2, tokens.size());
        assertEquals(TokenKind.WhiteSpace, tokens.get(0).getKind());
        assertEquals(TokenKind.EOF, tokens.get(1).getKind());
    }

    @Test
    public void emptyNewLineStream() {
        Lexer lexer = new Lexer(new DocumentContext("\n"));
        List<Token> tokens = getTokens(lexer);
        assertEquals(2, tokens.size());
        assertEquals(TokenKind.WhiteSpace, tokens.get(0).getKind());
        assertEquals(TokenKind.EOF, tokens.get(1).getKind());
    }

    @Test
    public void varStream() {
        Lexer lexer = new Lexer(new DocumentContext("var n = 500"));
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
        Lexer lexer = new Lexer(new DocumentContext("print \"pi = \""));
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
        Lexer lexer = new Lexer(new DocumentContext("out pi"));
        List<Token> tokens = getTokens(lexer);
        assertEquals(4, tokens.size());
        assertEquals(TokenKind.Out, tokens.get(0).getKind());
        assertEquals(TokenKind.WhiteSpace, tokens.get(1).getKind());
        assertEquals(TokenKind.Identifier, tokens.get(2).getKind());
        assertEquals("pi", tokens.get(2).getText());
        assertEquals(TokenKind.EOF, tokens.get(3).getKind());
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
