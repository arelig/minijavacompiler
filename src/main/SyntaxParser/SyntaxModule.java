package main.SyntaxParser;

import main.Exceptions.CompilerException;
import main.LexicalParser.LexicalParser;
import main.Utilities.FileManager;
import main.Utilities.FileManagerImp;

import java.io.FileNotFoundException;
import java.util.Stack;

public class SyntaxModule {
    private final Stack<CompilerException> stackException;
    private boolean errorsDetected;
    private SyntaxParser syntaxParser;
    private LexicalParser lexicalParser;

    public SyntaxModule(String fileName) {
        stackException = new Stack<>();
        try {
            FileManager fileManager = new FileManagerImp(fileName);
            fileManager.validate();
            lexicalParser = new LexicalParser(fileManager);
            lexicalParser.validate();

            syntaxParser = new SyntaxParser(lexicalParser, fileManager);

            parse();

            fileManager.invalidate();

        } catch (FileNotFoundException exception) {
            System.out.println("File path not found");
        }
    }

    private void parse() {
        try {
            syntaxParser.start();
        } catch (CompilerException exception) {
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
            //parse();
        }
    }
}
