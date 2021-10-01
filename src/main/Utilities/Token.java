package main.Utilities;

public class Token {
    private final TypeToken typeToken;
    private final String lexeme;
    private final int line;

    public Token(TypeToken typeToken, String lexeme, int line) {
        this.typeToken = typeToken;
        this.lexeme = lexeme;
        this.line = line;
    }

    public TypeToken getTokenType() {
        return typeToken;
    }

    public String lexeme() {
        return lexeme;
    }

    public String toString() {
        return "(" +
                typeToken + "," +
                lexeme + "," +
                line +
                ")";
    }

}
