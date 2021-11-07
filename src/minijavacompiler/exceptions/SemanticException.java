package minijavacompiler.exceptions;

public class SemanticException extends CompilerException {

    private final String id;
    private final int line;
    private final String messageError;

    public SemanticException(String id, int line, String messageError) {
        this.id = id;
        this.line = line;
        this.messageError = messageError;
    }

    @Override
    public String generateCodeError() {
        return "[Error:" +
                id +
                "|" +
                line +
                "]";
    }

    @Override
    public String generateElegantError() {
        return "Error Semantico en linea " +
                line +
                " : " +
                messageError;
    }
}
