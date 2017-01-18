import java.util.*;
import java.util.regex.*;

public class Lexer {
    String input;
    
    public static enum TokenType {
        NUMBER("-?([0-9]+(\\.[0-9]+)?)"),
        VARIABLE("[a-zA-Z]+"),
        PLUS("[+]"),
        MINUS("[-]"),
        POWER("['^']"),
        WHITESPACE("[ \t\f\r\n]+");
        
        public final String pattern;
        
        private TokenType(String pattern) {
            this.pattern = pattern;
        }
    }
    
    public static class Token {
        TokenType type;
        String value;
        
        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }
        
        @Override
        public String toString() {
            return this.type + " " + this.value;
        }
    }
    
    ArrayList<Token> tokens = new ArrayList<Token>();
    
    public Lexer(String input) {
        this.input = input;
    }
    
    public void scan() {
        StringBuffer patternBuffer = new StringBuffer();
        for (TokenType token: TokenType.values()) {
            patternBuffer.append("(?<" + token.name() + ">" + token.pattern + ")|");
        }
        
        String pattern = patternBuffer.toString();
        
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(this.input);
        
        while (m.find()) {
            if (m.group(TokenType.NUMBER.name()) != null) {
                this.tokens.add(new Token(TokenType.NUMBER, m.group(TokenType.NUMBER.name())));
            } else if (m.group(TokenType.VARIABLE.name()) != null) {
                this.tokens.add(new Token(TokenType.VARIABLE, m.group(TokenType.VARIABLE.name())));
            } else if (m.group(TokenType.PLUS.name()) != null) {
                this.tokens.add(new Token(TokenType.PLUS, m.group(TokenType.PLUS.name())));  
            } else if (m.group(TokenType.MINUS.name()) != null) {
                this.tokens.add(new Token(TokenType.MINUS, m.group(TokenType.MINUS.name())));  
            } else if (m.group(TokenType.POWER.name()) != null) {
                this.tokens.add(new Token(TokenType.POWER, m.group(TokenType.POWER.name())));
            } else if (m.group(TokenType.WHITESPACE.name()) != null) {
                continue;
            }
        }
    }
}
