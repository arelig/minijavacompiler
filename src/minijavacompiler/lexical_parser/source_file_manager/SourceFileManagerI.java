package minijavacompiler.lexical_parser.source_file_manager;

/*
   Abstrae al lexico de los detalles de bajo nivel del manejo de archivo fuente
 */
public interface SourceFileManagerI {

    int nextCharacter();

    String currentLine();

    int line();

    int column();

    void validate();

    void invalidate();
}
