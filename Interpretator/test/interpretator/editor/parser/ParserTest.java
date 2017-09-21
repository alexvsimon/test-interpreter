package interpretator.editor.parser;

import interpretator.editor.DocumentContext;
import interpretator.editor.Lexer;
import interpretator.parser.AST;
import interpretator.parser.ASTKind;
import interpretator.parser.Parser;
import interpretator.parser.PrintAST;
import interpretator.parser.ProgrammAST;
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
public class ParserTest {
    public ParserTest() {
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
        Parser parser = new Parser(lexer);
        ProgrammAST programm = parser.parse();
        List<AST> statements = programm.getStatements();
        assertEquals(0, statements.size());
    }    

    @Test
    public void printStream() {
        Lexer lexer = new Lexer(new DocumentContext("print \"pi = \""));
        Parser parser = new Parser(lexer);
        ProgrammAST programm = parser.parse();
        List<AST> statements = programm.getStatements();
        assertEquals(1, statements.size());
        assertEquals(ASTKind.Print, statements.get(0).getKind());
        assertEquals("pi = ", ((PrintAST)statements.get(0)).getString());
    }    

    @Test
    public void varMapStream() {
        Lexer lexer = new Lexer(new DocumentContext("var sequence = map({0, n}, i -> (-1)^i / (2.0 * i + 1))\n"));
        Parser parser = new Parser(lexer);
        ProgrammAST programm = parser.parse();
        List<AST> statements = programm.getStatements();
        assertEquals(1, statements.size());
        assertEquals(ASTKind.Var, statements.get(0).getKind());
    }    

    @Test
    public void varReduceStream() {
        Lexer lexer = new Lexer(new DocumentContext("var pi = 4 * reduce(sequence, 0, x y -> x + y)\n"));
        Parser parser = new Parser(lexer);
        ProgrammAST programm = parser.parse();
        List<AST> statements = programm.getStatements();
        assertEquals(1, statements.size());
        assertEquals(ASTKind.Var, statements.get(0).getKind());
    }    

    @Test
    public void complicatedStream() {
        Lexer lexer = new Lexer(new DocumentContext(
                "var n = 500\n" +
                "var sequence = map({0, n}, i -> (-1)^i / (2.0 * i + 1))\n" +
                "var pi = 4 * reduce(sequence, 0, x y -> x + y)\n" +
                "print \"pi = \"\n" +
                "out pi"));
        Parser parser = new Parser(lexer);
        ProgrammAST programm = parser.parse();
        List<AST> statements = programm.getStatements();
        assertEquals(5, statements.size());
        assertEquals(ASTKind.Var, statements.get(0).getKind());
        assertEquals(ASTKind.Var, statements.get(1).getKind());
        assertEquals(ASTKind.Var, statements.get(2).getKind());
        assertEquals(ASTKind.Print, statements.get(3).getKind());
        assertEquals(ASTKind.Out, statements.get(4).getKind());
    }    
}