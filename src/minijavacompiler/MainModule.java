package minijavacompiler;

import minijavacompiler.catbag.FileManager;
import minijavacompiler.catbag.FileManagerImp;
import minijavacompiler.data_structures.SymbolTable;
import minijavacompiler.exceptions.CompilerException;
import minijavacompiler.lexical_parser.LexicalParser;
import minijavacompiler.syntax_analysis.SyntaxParser;

import java.io.FileNotFoundException;
import java.util.Stack;

public class MainModule {
    private final Stack<CompilerException> stackException;
    private boolean errorsDetected;
    private SyntaxParser syntaxParser;
    private LexicalParser lexicalParser;


    public MainModule(String fileName) {
        stackException = new Stack<>();
        try {

            FileManager fileManager = new FileManagerImp(fileName);
            fileManager.validate();
            lexicalParser = new LexicalParser(fileManager);
            lexicalParser.validate();

            SymbolTable.getInstance().flush();
            syntaxParser = new SyntaxParser(lexicalParser, fileManager);

            compile();

            fileManager.invalidate();

        } catch (FileNotFoundException exception) {
            System.out.println("File path not found");
        }
    }

    private void compile() {
        try {
            syntaxParser.start();
            SymbolTable.getInstance().checkSemantic();
        } catch (CompilerException exception) {
//            System.out.println(exception.generateCodeError());
//            System.out.println(exception.generateElegantError());
            stackException.push(exception);
        }

        generateReportAndRestore();
    }

    private void generateReportAndRestore() {
        if (stackException.isEmpty() && !errorsDetected) {
            System.out.println("Compilacion Exitosa");
            System.out.println("[SinErrores]");
        } else if (!stackException.isEmpty()) {
            errorsDetected = true;
            while (!stackException.isEmpty()) {
                CompilerException exception = stackException.pop();
                System.out.println(exception.generateCodeError());
                System.out.println(exception.generateElegantError());
            }
        }
    }
}
