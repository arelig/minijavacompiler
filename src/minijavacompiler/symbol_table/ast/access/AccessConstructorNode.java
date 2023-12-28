package minijavacompiler.symbol_table.ast.access;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.symbol_table.entities.*;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.symbol_table.SymbolTable;
import minijavacompiler.symbol_table.entities.Class;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.symbol_table.types.TypeReference;

import java.util.LinkedList;
import java.util.List;

public class AccessConstructorNode extends AccessUnitNode {
    private Token reference;
    private Class referenceClass;
    public AccessConstructorNode(Token token) {
        super(token);
    }

    @Override
    public Type validate() throws SemanticException {
        referenceClass = SymbolTable.getInstance().getClass(reference.getLexeme());
        if (referenceClass != null) {
            checkParameterPattern();
        }else{
            throw new SemanticException(reference.getLexeme(), reference.getLine(),
                    "Acceso: constructor de clase " + reference.getId() + " sin declarar.");
        }
        return checkChaining(new TypeReference(reference));
    }

    public void checkParameterPattern() throws SemanticException {
        List<Constructor> constructorList = SymbolTable.getInstance().getClass(reference.getLexeme()).getConstructors();
        List<Parameter> formalParams = new LinkedList<>();

        for (Constructor constructor : constructorList) {
            if (constructor.getParamsAsList().size() == getActualParams().size()) {
                formalParams = constructor.getParamsAsList();
                break;
            }
        }

        this.validateParamCompatibility(formalParams);
    }

    public void setReference(Token reference) {
        this.reference = reference;
    }

    public void generateCode(){
        int attrSize = SymbolTable.getInstance().getClass(reference.getLexeme()).getAttributes().size();
        CodeGenerator.generateCode("RMEM 1 ;return ");
        CodeGenerator.generateCode("PUSH " + (attrSize + 1) + " ;memory to reserve");
        CodeGenerator.generateCode("PUSH " + CodeGenerator.getMallocTag() + " ;load malloc tag routine");
        CodeGenerator.generateCode("CALL ;call malloc");

        if(referenceClass.hasDynamicMethods()){
            CodeGenerator.generateCode("DUP ;set VTable (1)");
            CodeGenerator.generateCode("PUSH " + referenceClass.getVTableTag() + " ;set VTable (2)");
            CodeGenerator.generateCode("STOREREF 0 ;set VTable (3)");
        }

        if(chainedNode != null){
            chainedNode.generateCode();
        }

    }
}
