package minijavacompiler;

import com.sun.tools.attach.VirtualMachine;
import minijavacompiler.lexical_parser.source_file_manager.SourceFileManager;
import minijavacompiler.semantic_analysis.SemanticParser;
import minijavacompiler.exceptions.CompilerException;
import minijavacompiler.lexical_parser.LexicalParser;
import minijavacompiler.syntax_analysis.SyntaxParser;
import java.io.IOException;
import java.util.Stack;

import minijavacompiler.code_generator.CodeGenerator;

public class MainModule {
    public static final boolean GENERATE_CODE = true;
    public static final boolean FORMAT_CODE = true;
    private Stack<CompilerException> compilerErrors;
    private boolean errorsDetected;
    private SourceFileManager sourceFileManager;
    private LexicalParser lexicalParser;
    private SyntaxParser syntaxParser;
    private SemanticParser semanticParser;

    public MainModule(String fileName) {
        compilerErrors = new Stack<>();
        generateCode(fileName);
    }
    private String generateCode(String sourceFileName){
        try {
            sourceFileManager = new SourceFileManager(sourceFileName);
            sourceFileManager.validate();

            lexicalParser = new LexicalParser(sourceFileManager);
            lexicalParser.validate();

            syntaxParser = new SyntaxParser(lexicalParser, sourceFileManager);
            syntaxParser.validate();

            generateReportAndRestore();

            semanticParser = new SemanticParser();


            if(GENERATE_CODE){
                return new CodeGenerator().generateCode(FORMAT_CODE);
            }else{
                return "";
            }

            sourceFileManager.invalidate();

        }catch(IOException e){
            System.out.println("IO Error");
        }catch(CompilerException e){
            compilerErrors.push(e);
        }


    }

    private void generateReportAndRestore() {
        if (compilerErrors.isEmpty() && !errorsDetected) {
            System.out.println("Compilacion Exitosa");
            System.out.println("[SinErrores]");
        } else if (!compilerErrors.isEmpty()) {
            errorsDetected = true;
            while (!compilerErrors.isEmpty()) {
                CompilerException exception = compilerErrors.pop();
                System.out.println(exception.generateCodeError());
                System.out.println(exception.generateElegantError());
            }
        }
    }
}
