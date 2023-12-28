package minijavacompiler.symbol_table.ast.expression_nodes;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.symbol_table.entities.*;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.symbol_table.SymbolTable;
import minijavacompiler.symbol_table.entities.Class;
import minijavacompiler.symbol_table.types.Type;

import java.util.List;

public class ChainedMethodNode extends ChainedNode {
    private Method method;
    private boolean isStatic;
    private List<ExpressionNode> actualParams;

    public ChainedMethodNode(Token token) {
        super(token);
    }

    @Override
    public Type validate(Type prevType) throws SemanticException {
        validateTypeReference(prevType);
        validateMethod(prevType);
        return getType();
    }

    private Type getType() throws SemanticException {
        Type type;

        if (rightChainedNode != null) {
            type = rightChainedNode.validate(method.getReturnType());
        } else {
            type = method.getReturnType();
        }

        return type;
    }

    private void validateMethod(Type prevType) throws SemanticException {
        Class prevClass = SymbolTable.getInstance().getClass(prevType.getId());
        if(prevClass != null){
            method = prevClass.getMethod(accessToken.getId());

            if(method == null){
                throw new SemanticException(accessToken.getId(), accessToken.getLine(),
                        "No se encontró un método " + accessToken.getId() + " en la clase " + prevType);
            }

            isStatic = method.getBinding().equals("static");
            checkActualAndFormalParams();
        }
    }

    private void checkActualAndFormalParams() throws SemanticException {
        List<Parameter> formalParams = method.getParamsAsList();

        validateSizes(formalParams);
        validateTypes(formalParams);

    }

    private void validateSizes(List<Parameter> formalParams) throws SemanticException {
        if(actualParams.size() != formalParams.size()){
            throw new SemanticException(accessToken.getId(), accessToken.getLine(),
                    "Encadenado: se llamó al método " + accessToken.getId() +
                            " con " + actualParams.size() + " parametros actuales cuando se esperaban " +
                            formalParams.size() + " parametros formales.");
        }
    }

    public void validateTypes(List<Parameter> formalParams) throws SemanticException {
        for (int i = 0; i < formalParams.size(); i++) {
            Type formalType = formalParams.get(i).getType();
            Type actualType = actualParams.get(i).validate();

            if (!actualType.conform(formalType)) {
                throw new SemanticException(accessToken.getId(), accessToken.getLine(),
                        "El tipo del " + (i + 1) + "° parametro actual (" + actualType + ") del método " +
                                method.getId() + " no conforma con el tipo del parametro formal (" + formalType + ")");
            }
        }
    }

    private void validateTypeReference(Type toCheck) throws SemanticException {
        if (!toCheck.isReference()) {
            throw new SemanticException(accessToken.getId(), accessToken.getLine(), "Encadenado: no se puede aplicar encadenado a un tipo primitivo o void");
        }
    }

    public void setActualParams(List<ExpressionNode> actualParams) {
        this.actualParams = actualParams;
    }

    public void generateCode(){
        generateIfStatic();
        generateReturn();
        generateActualParams();
        generateCall();

        if(rightChainedNode != null){
            rightChainedNode.generateCode();
        }
    }

    private void generateIfStatic() {
        if(isStatic){
            CodeGenerator.generateCode("POP ;free space from this in static method call" );
        }
    }

    private void generateReturn() {
        boolean isVoid = method.getReturnType().getId().equals("void");
        if(!isVoid){
            CodeGenerator.generateCode("RMEM 1 ; save return value");
            if(!isStatic){
                CodeGenerator.generateCode("SWAP");
            }
        }
    }

    private void generateCall() {
        if(isStatic){
            CodeGenerator.generateCode("PUSH " + method.getTag());
        }else{
            CodeGenerator.generateCode("DUP");
            CodeGenerator.generateCode("LOADREF 0");
            CodeGenerator.generateCode("LOADREF " + method.getOffset());
        }

        CodeGenerator.generateCode("CALL ;call method" + method.getId());
    }

    private void generateActualParams() {
        for(int i = actualParams.size()-1 ; i>=0 ; i--){
            ExpressionNode param = actualParams.get(i);
            param.generateCode();
            CodeGenerator.setComment("Actual param " + i + "from method " + method.getId());
            if(!isStatic){
                CodeGenerator.generateCode("SWAP");
            }
        }
    }
}
