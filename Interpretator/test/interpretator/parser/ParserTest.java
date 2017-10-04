package interpretator.parser;

import interpretator.editor.DocumentContext;
import interpretator.editor.Lexer;
import interpretator.api.ast.ASTKind;
import interpretator.api.ast.PrintAST;
import interpretator.api.ast.ProgramAST;
import interpretator.api.ast.StatementAST;
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
        Lexer lexer = new Lexer(new DocumentContext("", 0));
        Parser parser = new Parser(lexer);
        ProgramAST program = parser.parse();
        List<StatementAST> statements = program.getStatements();
        assertEquals(0, statements.size());
    }    

    @Test
    public void printStream() {
        Lexer lexer = new Lexer(new DocumentContext("print \"pi = \"", 0));
        Parser parser = new Parser(lexer);
        ProgramAST program = parser.parse();
        List<StatementAST> statements = program.getStatements();
        assertEquals(1, statements.size());
        assertEquals(ASTKind.Print, statements.get(0).getKind());
        assertEquals("pi = ", ((PrintAST)statements.get(0)).getString());
        assertEquals("Print pi = \n", new ASTDump(program).dump());
    }    

    @Test
    public void varMapStream() {
        Lexer lexer = new Lexer(new DocumentContext("var sequence = map({0, n}, i -> (-1)^i / (2.0 * i + 1))\n", 0));
        Parser parser = new Parser(lexer);
        ProgramAST program = parser.parse();
        List<StatementAST> statements = program.getStatements();
        assertEquals(1, statements.size());
        assertEquals(ASTKind.Var, statements.get(0).getKind());
        assertEquals("Var sequence\n" +
                     " Map\n" +
                     "  Sequence\n" +
                     "   Number 0\n" +
                     "   Variable n\n" +
                     "  Lambda i\n" +
                     "   Div\n" +
                     "    Pow\n" +
                     "     UnaryMinus\n" +
                     "      Number 1\n" +
                     "     Variable i\n" +
                     "    Plus\n" +
                     "     Mul\n" +
                     "      Number 2.0\n" +
                     "      Variable i\n" +
                     "     Number 1\n", new ASTDump(program).dump());
    }    

    @Test
    public void unaryMinus(){
        Lexer lexer = new Lexer(new DocumentContext("var n = -6*2-5*2\n", 0));
        Parser parser = new Parser(lexer);
        ProgramAST program = parser.parse();
        List<StatementAST> statements = program.getStatements();
        assertEquals(1, statements.size());
        assertEquals(ASTKind.Var, statements.get(0).getKind());
        assertEquals("Var n\n" +
                     " Minus\n" +
                     "  Mul\n" +
                     "   UnaryMinus\n" +
                     "    Number 6\n" +
                     "   Number 2\n" +
                     "  Mul\n" +
                     "   Number 5\n" +
                     "   Number 2\n", new ASTDump(program).dump());
    }

    @Test
    public void pow() {
        Lexer lexer = new Lexer(new DocumentContext("out 2^3^2\n", 0));
        Parser parser = new Parser(lexer);
        ProgramAST program = parser.parse();
        List<StatementAST> statements = program.getStatements();
        assertEquals(1, statements.size());
        assertEquals(ASTKind.Out, statements.get(0).getKind());
        assertEquals("Out\n" +
                     " Pow\n" +
                     "  Number 2\n" +
                     "  Pow\n" +
                     "   Number 3\n" +
                     "   Number 2\n", new ASTDump(program).dump());
    }
    
    @Test
    public void varReduceStream() {
        Lexer lexer = new Lexer(new DocumentContext("var pi = 4 * reduce(sequence, 0, x y -> x + y)\n", 0));
        Parser parser = new Parser(lexer);
        ProgramAST program = parser.parse();
        List<StatementAST> statements = program.getStatements();
        assertEquals(1, statements.size());
        assertEquals(ASTKind.Var, statements.get(0).getKind());
        assertEquals("Var pi\n" +
                     " Mul\n" +
                     "  Number 4\n" +
                     "  Reduce\n" +
                     "   Variable sequence\n" +
                     "   Number 0\n" +
                     "   Lambda x y\n" +
                     "    Plus\n" +
                     "     Variable x\n" +
                     "     Variable y\n", new ASTDump(program).dump());
    }    

    @Test
    public void complicatedStream() {
        Lexer lexer = new Lexer(new DocumentContext(
                "var n = 500\n" +
                "var sequence = map({0, n}, i -> (-1)^i / (2.0 * i + 1))\n" +
                "var pi = 4 * reduce(sequence, 0, x y -> x + y)\n" +
                "print \"pi = \"\n" +
                "out pi", 0));
        Parser parser = new Parser(lexer);
        ProgramAST program = parser.parse();
        List<StatementAST> statements = program.getStatements();
        assertEquals(5, statements.size());
        assertEquals(ASTKind.Var, statements.get(0).getKind());
        assertEquals(ASTKind.Var, statements.get(1).getKind());
        assertEquals(ASTKind.Var, statements.get(2).getKind());
        assertEquals(ASTKind.Print, statements.get(3).getKind());
        assertEquals(ASTKind.Out, statements.get(4).getKind());
        assertEquals("Var n\n" +
                     " Number 500\n" +
                     "Var sequence\n" +
                     " Map\n" +
                     "  Sequence\n" +
                     "   Number 0\n" +
                     "   Variable n\n" +
                     "  Lambda i\n" +
                     "   Div\n" +
                     "    Pow\n" +
                     "     UnaryMinus\n" +
                     "      Number 1\n" +
                     "     Variable i\n" +
                     "    Plus\n" +
                     "     Mul\n" +
                     "      Number 2.0\n" +
                     "      Variable i\n" +
                     "     Number 1\n" +
                     "Var pi\n" +
                     " Mul\n" +
                     "  Number 4\n" +
                     "  Reduce\n" +
                     "   Variable sequence\n" +
                     "   Number 0\n" +
                     "   Lambda x y\n" +
                     "    Plus\n" +
                     "     Variable x\n" +
                     "     Variable y\n" +
                     "Print pi = \n" +
                     "Out\n" +
                     " Variable pi\n", new ASTDump(program).dump());
    }
    
    /**
     * Test checks that parser does not throw exceptions and does not have infinite loop on partly typed txt
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
            Lexer lexer = new Lexer(new DocumentContext(source.substring(0, i), 0));
            Parser parser = new Parser(lexer);
            ProgramAST program = parser.parse();
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
                Lexer lexer = new Lexer(new DocumentContext(buf.toString(), 0));
                Parser parser = new Parser(lexer);
                ProgramAST program = parser.parse();
            }
        }
    }

}
