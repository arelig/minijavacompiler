package minijavacompiler.exceptions;

import minijavacompiler.lexical_parser.Token;

public class SyntaxException extends CompilerException {
    private final Token currentToken;
    private final String expectedTokens;
    private final int line;


    //@todo "Error sintactico en linea N: no se encontro X cuando se esperaba los primeros de la produccion"
    public SyntaxException(Token currentToken, String expectedTokens, int line) {
        this.currentToken = currentToken;
        this.expectedTokens = expectedTokens;
        this.line = line;
    }

    public String generateCodeError() {
        return "[" +
                "Error:" +
                currentToken.getLexeme() +
                "|" +
                line +
                "]";
    }

    public String generateElegantError() {
        return "Error Sintactico en linea " +
                line +
                ": se esperaba " +
                expectedTokens +
                " pero se encontro " +
                " '" +
                currentToken.getLexeme() +
                "' ";
    }
}
