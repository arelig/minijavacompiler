package main;

public class Token {
    private TokenType tokenType;
    private String lexeme;
    private int line;

    public Token(TokenType tokenType, String lexeme, int line){
        this.tokenType = tokenType;
        this.lexeme = lexeme;
        this.line = line;
    }

    public TokenType getTokenType(){
        return tokenType;
    }

    public String toString() {
        return "(" +
                tokenType + "," +
                lexeme + "," +
                line +
                ")";
    }

}
