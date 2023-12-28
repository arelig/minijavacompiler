package minijavacompiler.symbol_table.ast.access;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.symbol_table.SymbolTable;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.symbol_table.types.TypeReference;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class AccessThisNode extends AccessNode {
    private Token reference;

    public AccessThisNode(Token token) {
        super(token);
    }

    @Override
    public Type validate() throws SemanticException {
        return checkChaining(getCurrentType());
    }

    public Type getCurrentType() {
        return new TypeReference(reference);
    }

    public void setReference(Token reference) {
        this.reference = reference;
    }

    public void generateCode(){
        CodeGenerator.generateCode("LOAD 3 ;load this");
        if(chainedNode!= null){
            chainedNode.generateCode();
        }

    }
}
