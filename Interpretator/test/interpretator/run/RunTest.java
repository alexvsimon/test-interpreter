package interpretator.run;

import interpretator.api.run.InterpreterRuntimeException;
import interpretator.api.run.SequenceValue;
import interpretator.api.run.Value;
import interpretator.api.run.ValueKind;
import interpretator.editor.DocumentContext;
import interpretator.editor.Lexer;
import interpretator.parser.Parser;
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
public class RunTest {

    public RunTest() {
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
    public void eval2Mul2() {
        Lexer lexer = new Lexer(new DocumentContext("var res = 2*2"));
        Parser parser = new Parser(lexer);
        ASTEval run = new ASTEval(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res.getKind() == ValueKind.Integer);
        assertEquals(4, res.getInteger());
    }

    @Test
    public void evalSeq() {
        Lexer lexer = new Lexer(new DocumentContext("var res = {1,10}"));
        Parser parser = new Parser(lexer);
        ASTEval run = new ASTEval(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res.getKind() == ValueKind.Sequence);
        SequenceValue seq = (SequenceValue) res;
        assertEquals(10, seq.getSize());
        for(int i = 0; i < seq.getSize(); i++) {
            assertEquals(i+1, seq.getValueAt(i).getInteger());
        }
    }

    @Test
    public void evalSeq0() {
        Lexer lexer = new Lexer(new DocumentContext("var res = {6,5}"));
        Parser parser = new Parser(lexer);
        ASTEval run = new ASTEval(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res.getKind() == ValueKind.Sequence);
        SequenceValue seq = (SequenceValue) res;
        assertEquals(0, seq.getSize());
    }

    @Test
    public void evalSeq1() {
        Lexer lexer = new Lexer(new DocumentContext("var res = {5,5}"));
        Parser parser = new Parser(lexer);
        ASTEval run = new ASTEval(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res.getKind() == ValueKind.Sequence);
        SequenceValue seq = (SequenceValue) res;
        assertEquals(1, seq.getSize());
        for(int i = 0; i < seq.getSize(); i++) {
            assertEquals(5, seq.getValueAt(i).getInteger());
        }
    }

    @Test
    public void evalFailedSeq() {
        Lexer lexer = new Lexer(new DocumentContext("var res = {1,10.}"));
        Parser parser = new Parser(lexer);
        ASTEval run = new ASTEval(parser.parse());
        try {
            run.run();
        } catch (InterpreterRuntimeException e) {
            assertTrue(e.getMessage().contains("Sequence defined for integer operands"));
            return;
        }
        assertTrue("Should be run time exception", false);
    }

    @Test
    public void evalNestedSeq() {
        Lexer lexer = new Lexer(new DocumentContext("var res = map({1,10},x->{1,x})"));
        Parser parser = new Parser(lexer);
        ASTEval run = new ASTEval(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res.getKind() == ValueKind.Sequence);
        SequenceValue seq = (SequenceValue) res;
        assertEquals(10, seq.getSize());
        for(int i = 0; i < seq.getSize(); i++) {
            assertTrue(seq.getValueAt(i).getKind() == ValueKind.Sequence);
        }
    }

    @Test
    public void eval2Plus2Mul2() {
        Lexer lexer = new Lexer(new DocumentContext("var res = 2+2*2"));
        Parser parser = new Parser(lexer);
        ASTEval run = new ASTEval(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res.getKind() == ValueKind.Integer);
        assertEquals(6, res.getInteger());
    }

    @Test
    public void evalMinus2Pow2Plus2Mul2() {
        Lexer lexer = new Lexer(new DocumentContext("var res = -2^2+2*2"));
        Parser parser = new Parser(lexer);
        ASTEval run = new ASTEval(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res.getKind() == ValueKind.Integer);
        assertEquals(8, res.getInteger());
    }

    @Test
    public void evalPi() {
        Lexer lexer = new Lexer(new DocumentContext(
                "var n = 10000\n" +
                "var sequence = map({0, n}, i -> (-1)^i / (2.0 * i + 1))\n" +
                "var pi = 4 * reduce(sequence, 0, x y -> x + y)\n" +
                "var delta = pi - 355/113"));
        Parser parser = new Parser(lexer);
        ASTEval run = new ASTEval(parser.parse());
        run.run();
        Value res = run.getVariable("delta");
        assertEquals(true, res.getKind() == ValueKind.Double);
        assertTrue(Math.abs(res.getDouble()) < 0.0001);
    }
    
}
