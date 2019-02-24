package Tokenizer;


import java.util.List;
import org.hamcrest.CustomMatcher;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Rule;
import org.junit.Test;

public class TokenizerTest {
    // specify null for expected if it's not supposed to tokenize
    
    public static void main(String[] args){
        
    } 
    public void assertTokenizes(final String input,
                                final Token[] expected) {
        final TheTokenizer tokenizer = new TheTokenizer(input.toCharArray());
        try {
            final List<Token> received = tokenizer.tokenize();
            assertTrue("Expected tokenization failure, got: " + received,
                       expected != null);
            assertArrayEquals(expected,
                              received.toArray(new Token[received.size()]));
        } catch (final TokenizerException e) {
            assertTrue(("Unexpected tokenization failure for \"" +
                        input + "\": " + e.getMessage()),
                       expected == null);
        }
    }

    @Test(expected = AssertionError.class)
    public void testErrors(){
        assertTokenizes("@",
                        new Token[]{ new PlusToken()});
    }
    
    @Test
    public void testTokenizeSingleDigitInteger() {
        assertTokenizes("0",
                        new Token[]{ new NumberToken(0) });
    }
    
    @Test
    public void testTokenizeInteger() {
        assertTokenizes("123",
                        new Token[]{ new NumberToken(123) });
    }

    @Test
    public void testTokenizeIntegerLeadingWhitespace() {
        assertTokenizes("  123",
                        new Token[]{ new NumberToken(123) });
    }

    @Test
    public void testTokenizeIntegerTrailingWhitespace() {
        assertTokenizes("123   ",
                        new Token[]{ new NumberToken(123) });
    }

    @Test
    public void testTokenizeIntegerLeadingAndTrailingWhitespace() {
        assertTokenizes("  123  ",
                        new Token[]{ new NumberToken(123) });
    }

    @Test
    public void testTokenizeVariableSingleLetter() {
        assertTokenizes("x",
                        new Token[]{ new VariableToken("x") });
    }

    @Test
    public void testTokenizeVariableMultiLetter() {
        assertTokenizes("foo",
                        new Token[]{ new VariableToken("foo") });
    }

    @Test
    public void testTokenizeVariableStartsWithIf() {
        assertTokenizes("ifx",
                        new Token[]{ new VariableToken("ifx") });
    }

    @Test
    public void testTokenizeIf() {
        assertTokenizes("if",
                        new Token[]{ new IfToken() });
    }

    @Test
    public void testTokenizeSingleChars() {
        assertTokenizes("+-*/(){}",
                        new Token[]{ new PlusToken(),
                                     new MinusToken(),
                                     new MultToken(),
                                     new DivToken(),
                                     new LeftParenToken(),
                                     new RightParenToken(),
                                     new LeftCurlyToken(),
                                     new RightCurlyToken()});
    }
    
    @Test
    public void testNewSingleCharacters() {
        assertTokenizes(",%!.<>[]=:;#'\\",
                        new Token[]{ new CommaToken(),
                                     new PercentToken(),
                                     new NotToken(),
                                     //new QuoteToken(),
                                     new PeriodToken(),
                                     new LessToken(),
                                     new GreaterToken(),
                                     new LeftSquareToken(),
                                     new RightSquareToken(),
                                     new EqualsToken(),
                                     new ColonToken(),
                                     new SemiToken(),
                                     new PoundToken(),
                                     new SingleQToken(),
                                     new EscapeToken()});
    }
    
    
 
       
    @Test
    public void testTokenizeIntermixed() {
        assertTokenizes("*if+foo-",
                        new Token[]{ new MultToken(),
                                     new IfToken(),
                                     new PlusToken(),
                                     new VariableToken("foo"),
                                     new MinusToken() });
    }

    @Test
    public void testTokenizeElse() {
        assertTokenizes("else",
                        new Token[]{ new ElseToken() });
    }

    @Test
    public void testTokenizeIfExpression() {
        assertTokenizes("if (1) { x } else { y }",
                        new Token[]{ new IfToken(),
                                     new LeftParenToken(),
                                     new NumberToken(1),
                                     new RightParenToken(),
                                     new LeftCurlyToken(),
                                     new VariableToken("x"),
                                     new RightCurlyToken(),
                                     new ElseToken(),
                                     new LeftCurlyToken(),
                                     new VariableToken("y"),
                                     new RightCurlyToken() });
    }
}

            
