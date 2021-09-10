package main;

import java.io.FileNotFoundException;

import java.util.Stack;

public class ModuloPrincipal {
    private LexicalScanner lexicalScanner;
    private Stack<LexicalException> stackException;
    private boolean errorsDetected = false;


    public ModuloPrincipal(String filename) {
        stackException = new Stack<>();
        try {
            FileManager fileManager = new FileManagerImp(filename);
            fileManager.validate();
            lexicalScanner = new LexicalScanner(fileManager);
            lexicalScanner.validate();

            completeScan();

            fileManager.invalidate();

        } catch (FileNotFoundException error) {
            System.out.println("File path not found");
        }

    }

    private void completeScan() {
        Token tokenAvailable;
        try {
            while (!lexicalScanner.isEOF()) {
                tokenAvailable = lexicalScanner.nextToken();
                if (!(tokenAvailable.getTokenType() == TokenType.EOF)) {
                    System.out.println(tokenAvailable);
                }
            }
        } catch (LexicalException exception) {
            stackException.push(exception);
        }

        generateReport();
    }

    private void generateReport() {
        if (stackException.isEmpty() && !errorsDetected) {
            System.out.println("[SinErrores]");
        } else {
            while (!stackException.isEmpty()) {
                LexicalException exception = stackException.pop();
                System.out.println(exception.generateCodeError());
                System.out.println(exception.generateElegantReport());
                recover();
            }
        }
    }

    private void recover() {
        errorsDetected = true;
        completeScan();
    }

}
