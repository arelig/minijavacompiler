package minijavacompiler.catbag;

/*
   Abstrae al lexico de los detalles de bajo nivel del manejo de archivo fuente
 */
public interface FileManager {

    int nextCharacter();

    String currentLine();

    int line();

    int column();

    void validate();

    void invalidate();
}
