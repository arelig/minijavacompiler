package minijavacompiler;

import com.sun.tools.attach.VirtualMachine;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.exceptions.SyntaxException;
import minijavacompiler.lexical_parser.source_file_manager.SourceFileManager;
import minijavacompiler.semantic_analysis.SemanticParser;
import minijavacompiler.exceptions.CompilerException;
import minijavacompiler.lexical_parser.LexicalParser;
import minijavacompiler.symbol_table.SymbolTable;
import minijavacompiler.syntax_analysis.SyntaxParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Stack;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.virtual_machine.VMModule;

public class MainModule {

    public MainModule(String[] args) {
        String sourceFileName = "/home/arelig/Documents/UNS/Compiladores/Compilador/minijavacompiler/resources/inout/input.txt";
        String outputFileName = "/home/arelig/Documents/UNS/Compiladores/Compilador/minijavacompiler/resources/inout/output.txt";
        //String sourceFileName = args[0];

        String code = generateCode(sourceFileName);
        System.out.println("Codigo generado: " + code);

        if(!code.isEmpty()){
            Path path;
            if(args.length != 1){
                //String outputFileName = args[1];
                path = Path.of(outputFileName);
            }else{
                path = Path.of("default.txt");
            }
            try{
                if(path.getParent() != null){
                    Files.createDirectories(path.getParent());
                }
                Files.writeString(path, code);
                VMModule.main(new String[]{path.toString()});
            }catch(IOException exception){
                System.out.println("IO Error: " + exception.getMessage());
                exception.printStackTrace();
            }
        }
    }
    private String generateCode(String sourceFileName){
        try {
            SourceFileManager sourceFileManager = new SourceFileManager(sourceFileName);
            sourceFileManager.validate();

            LexicalParser lexicalParser = new LexicalParser(sourceFileManager);
            lexicalParser.validate();

            SyntaxParser syntaxParser = new SyntaxParser(lexicalParser, sourceFileManager);
            syntaxParser.validate();

            SemanticParser semanticParser = new SemanticParser();
            semanticParser.validate();

            sourceFileManager.invalidate();
            System.out.println("[SinErrores]");
            return new CodeGenerator().generateCode();

        }catch(IOException exception){
            System.out.println("IO Error: " + exception.getMessage());
            exception.printStackTrace();
        }catch(CompilerException exception){
            System.out.println(exception.getMessage());
        }
        return "";
    }
}
