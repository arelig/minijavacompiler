package minijavacompiler.exceptions;

public abstract class CompilerException extends Exception {
    public abstract String generateCodeError();

    public abstract String generateElegantError();

}
