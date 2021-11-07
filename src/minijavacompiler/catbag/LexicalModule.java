package minijavacompiler.catbag;

import minijavacompiler.exceptions.LexicalException;
import minijavacompiler.lexical_parser.LexicalParser;
import minijavacompiler.lexical_parser.Token;

import java.io.FileNotFoundException;
import java.util.Stack;

public class LexicalModule {
    private final Stack<LexicalException> stackException;
    private LexicalParser lexicalParser;
    private boolean errorsDetected = false;


    public LexicalModule(String filename) {
        stackException = new Stack<>();
        try {
            FileManager fileManager = new FileManagerImp(filename);
            fileManager.validate();
            lexicalParser = new LexicalParser(fileManager);
            lexicalParser.validate();

            parse();

            fileManager.invalidate();

        } catch (FileNotFoundException error) {
            System.out.println("File path not found");
        }

    }

    private void parse() {
        Token tokenAvailable;
        try {
            while (!lexicalParser.isEOF()) {
                tokenAvailable = lexicalParser.nextToken();
                System.out.println(tokenAvailable);
            }
        } catch (LexicalException exception) {
            stackException.push(exception);
        }

        generateReportAndRestore();
    }

    private void generateReportAndRestore() {
        if (stackException.isEmpty() && !errorsDetected) {
            System.out.println("[SinErrores]");
        } else if (!stackException.isEmpty()) {
            errorsDetected = true;
            while (!stackException.isEmpty()) {
                LexicalException exception = stackException.pop();
                System.out.println(exception.generateCodeError());
                System.out.println(exception.generateElegantError());
            }
            parse();
        }
    }

}
