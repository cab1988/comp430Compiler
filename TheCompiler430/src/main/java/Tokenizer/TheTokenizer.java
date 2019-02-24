/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tokenizer;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author Quelyn
 */
public class TheTokenizer {

    // begin static variables
    private static Map<String, Token> TOKEN_MAPPING =
        new HashMap<String, Token>() {{
            put("+", new PlusToken());
            put("-", new MinusToken());
            put("*", new MultToken());
            put("/", new DivToken());
            put("(", new LeftParenToken());
            put(")", new RightParenToken());
            put("{", new LeftCurlyToken());
            put("}", new RightCurlyToken());
            put("if", new IfToken());
            put("else", new ElseToken());
            put("#", new PoundToken());
            put("<", new LessToken());
            put(">", new GreaterToken());
            put("[", new LeftSquareToken());
            put("]", new RightSquareToken());
            put(".", new PeriodToken());
            put("\"", new QuoteToken());
            put("\\", new EscapeToken());
            put(";", new SemiToken());
            put("=", new EqualsToken());
            put(",", new CommaToken());
            put(":", new ColonToken());
            put("%", new PercentToken());
            put("!", new NotToken());
            put("'", new SingleQToken());
        }};
    // end static variables
    
    // begin instance variables
    private char[] input;
    private int inputPos; // position in the input
    private stringToken newString = null;
    private QuoteToken newQuote = new QuoteToken();
    // end instance variables

        
    TheTokenizer(final char[] input) {
        this.input = input;
        inputPos = 0;
    }

    public static boolean isTokenString(final String input) {
        return TOKEN_MAPPING.containsKey(input);
    }
    
    // assumes there is at least one character left
    // returns null if it couldn't parse a number
    private NumberToken tryTokenizeNumber() {
        final int initialInputPos = inputPos;
        String digits = "";

        while (inputPos < input.length &&
               Character.isDigit(input[inputPos])) {
            digits += input[inputPos];
            inputPos++;
        }

        if (digits.length() > 0) {
            return new NumberToken(Integer.parseInt(digits));
        } else {
            // reset position
            inputPos = initialInputPos;
            return null;
        }
    }

    // assumes there is at least one character left
    // returns null if it couldn't parse a variable
    private VariableToken tryTokenizeVariable() {
        final int initialInputPos = inputPos;
        String name = "";

        if (Character.isLetter(input[inputPos])) {
            name += input[inputPos];
            inputPos++;            
            while (inputPos < input.length &&
                   Character.isLetterOrDigit(input[inputPos])) {
                name += input[inputPos];
                inputPos++;
            }
        } else {
            // reset position
            inputPos = initialInputPos;
            return null;
        }

        if (isTokenString(name)) {
            // reset position
            inputPos = initialInputPos;
            return null;
        } else {
            return new VariableToken(name);
        }
    }

    private boolean prefixCharsEqual(final String probe) {
        int targetPos = inputPos;
        int probePos = 0;

        while (targetPos < input.length &&
               probePos < probe.length() &&
               probe.charAt(probePos) == input[targetPos]) {
            probePos++;
            targetPos++;
        }

        return probePos == probe.length();
    }
            
    // returns null if it couldn't parse a token
    private Token tryTokenizeOther() {
        for (final Map.Entry<String, Token> entry : TOKEN_MAPPING.entrySet()) {
            final String key = entry.getKey();
            if (prefixCharsEqual(key)) {
                inputPos += key.length();
                
                if(entry.getValue() instanceof QuoteToken){
                    int k = inputPos;
                    String theString = "";
                    while(input[k]!='\"'){
                    theString += input[k];                
                    k++;
                    }
                
                inputPos = k+1;
                newString = new stringToken(theString);
                }
                return entry.getValue();
            }
        }
        return null;
    }
    
    private void skipWhitespace() {
        while (inputPos < input.length &&
               Character.isWhitespace(input[inputPos])) {
            inputPos++;
        }
    }

    // returns null if there are no more tokens
    public Token tokenizeSingle() throws TokenizerException {
        VariableToken var = null;
        NumberToken num = null;
        Token otherToken = null;

        skipWhitespace();

        if (inputPos >= input.length) {
            return null;
        } else if ((var = tryTokenizeVariable()) != null) {
            return var;
        } else if ((num = tryTokenizeNumber()) != null) {
            return num;
        } else if ((otherToken = tryTokenizeOther()) != null) {
            return otherToken;
        } else {
            throw new TokenizerException("Invalid character " +
                                         input[inputPos] +
                                         " at position " +
                                         inputPos);
        }
    }
    
    public List<Token> tokenize() throws TokenizerException {
        List<Token> list = new ArrayList<Token>();
        Token current = null;
            
        while ((current = tokenizeSingle()) != null) {
            list.add(current);
            
            if(newString !=null){
                list.add(newString);
                list.add(newQuote);
                newString = null;
            }
        }

        return list;
    }
}

