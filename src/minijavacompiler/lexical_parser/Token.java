package minijavacompiler.lexical_parser;

public class Token {
    private TokenType typeToken;
    private String lexeme;
    private int line;

    public Token() {
    }

    public Token(TokenType typeToken, String lexeme, int line) {
        this.typeToken = typeToken;
        this.lexeme = lexeme;
        this.line = line;
    }

    public TokenType getTokenType() {
        return typeToken;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLine() {
        return line;
    }



    //bue, falleció el clean code
    public String getId(){
        return lexeme;
    }
    public String toString() {
        return "(" +
                typeToken + "," +
                lexeme + "," +
                line +
                ")";
    }

    public boolean equals(Token token) {
        return this.lexeme.equals(token.getLexeme());
    }

}
