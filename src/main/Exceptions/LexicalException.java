package main.Exceptions;

/*
    Error Lexico en linea 2: # no es un simbolo valido.
    [Error:#|2]
    @todo Modificar mensajes de excepcion
 */

public class LexicalException extends CompilerException {
    private final String description;
    private final String lineString;
    private final String lexeme;
    private final int line;
    private final int column;


    public LexicalException(String description, String lineString, String lexeme, int line, int column) {
        this.description = description;
        this.lineString = lineString;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }

    public String generateCodeError() {
        return "[" +
                "Error:" +
                lexeme +
                "|" +
                line +
                "]";
    }

    public String generateElegantError() {
        int offsetDescription = 7 + column;
        return "Error Lexico en linea " +
                line +
                " columna " +
                column +
                " : " +
                lexeme +
                " " +
                description +
                "\n" +
                "Detalle: " +
                lineString +
                "\n" +
                " ".repeat(Math.max(0, offsetDescription)) +
                "^";
    }
}
