package interpretator.run;

import interpretator.api.ast.Parser;
import interpretator.api.lexer.Lexer;
import interpretator.api.run.Interpretator;
import interpretator.api.run.InterpreterRuntimeException;
import interpretator.api.run.SequenceValue;
import interpretator.api.run.Value;
import interpretator.DocumentContext;
import interpretator.spi.ast.ParserFactory;
import interpretator.spi.lexer.LexerFactory;
import interpretator.spi.run.InterpretatorFactory;
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
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("var res = 2*2", 0));
        Parser parser = ParserFactory.getInstance().getParser(lexer);
        Interpretator run = InterpretatorFactory.getInstance().getInterpretator(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res.isInteger());
        assertEquals(4, res.getInteger());
    }

    @Test
    public void evalSeq() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("var res = {1,10}", 0));
        Parser parser = ParserFactory.getInstance().getParser(lexer);
        Interpretator run = InterpretatorFactory.getInstance().getInterpretator(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res.isSequence());
        SequenceValue seq = (SequenceValue) res;
        assertEquals(10, seq.getSize());
        for(int i = 0; i < seq.getSize(); i++) {
            assertEquals(i+1, seq.getValueAt(i).getInteger());
        }
    }

    @Test
    public void evalSeq0() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("var res = {6,5}", 0));
        Parser parser = ParserFactory.getInstance().getParser(lexer);
        Interpretator run = InterpretatorFactory.getInstance().getInterpretator(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res.isSequence());
        SequenceValue seq = (SequenceValue) res;
        assertEquals(0, seq.getSize());
    }

    @Test
    public void evalReduce0() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("var res = reduce({6,5},7,x y->x+y)", 0));
        Parser parser = ParserFactory.getInstance().getParser(lexer);
        Interpretator run = InterpretatorFactory.getInstance().getInterpretator(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(7, res.getInteger());
    }

    @Test
    public void evalReduce1() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("var res = reduce({5,5},7,x y->x+y)", 0));
        Parser parser = ParserFactory.getInstance().getParser(lexer);
        Interpretator run = InterpretatorFactory.getInstance().getInterpretator(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(12, res.getInteger());
    }

    @Test
    public void evalSeq1() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("var res = {5,5}", 0));
        Parser parser = ParserFactory.getInstance().getParser(lexer);
        Interpretator run = InterpretatorFactory.getInstance().getInterpretator(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res.isSequence());
        SequenceValue seq = (SequenceValue) res;
        assertEquals(1, seq.getSize());
        for(int i = 0; i < seq.getSize(); i++) {
            assertEquals(5, seq.getValueAt(i).getInteger());
        }
    }

    @Test
    public void evalFailedSeq() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("var res = {1,10.}", 0));
        Parser parser = ParserFactory.getInstance().getParser(lexer);
        Interpretator run = InterpretatorFactory.getInstance().getInterpretator(parser.parse());
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
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("var res = map({1,10},x->{1,x})", 0));
        Parser parser = ParserFactory.getInstance().getParser(lexer);
        Interpretator run = InterpretatorFactory.getInstance().getInterpretator(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res.isSequence());
        SequenceValue seq = (SequenceValue) res;
        assertEquals(10, seq.getSize());
        for(int i = 0; i < seq.getSize(); i++) {
            assertTrue(seq.getValueAt(i).isSequence());
        }
    }

    @Test
    public void eval2Plus2Mul2() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("var res = 2+2*2", 0));
        Parser parser = ParserFactory.getInstance().getParser(lexer);
        Interpretator run = InterpretatorFactory.getInstance().getInterpretator(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res.isInteger());
        assertEquals(6, res.getInteger());
    }

    @Test
    public void evalMinus2Pow2Plus2Mul2() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext("var res = -2^2+2*2", 0));
        Parser parser = ParserFactory.getInstance().getParser(lexer);
        Interpretator run = InterpretatorFactory.getInstance().getInterpretator(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res.isInteger());
        assertEquals(8, res.getInteger());
    }

    @Test
    public void evalPi() {
        Lexer lexer = LexerFactory.getInstance().getLexer(new DocumentContext(
                "var n = 10000\n" +
                "var sequence = map({0, n}, i -> (-1)^i / (2.0 * i + 1))\n" +
                "var pi = 4 * reduce(sequence, 0, x y -> x + y)\n" +
                "var delta = pi - 355/113", 0));
        Parser parser = ParserFactory.getInstance().getParser(lexer);
        Interpretator run = InterpretatorFactory.getInstance().getInterpretator(parser.parse());
        run.run();
        Value res = run.getVariable("delta");
        assertEquals(true, res.isDouble());
        assertTrue(Math.abs(res.getDouble()) < 0.0001);
    }
    
}
