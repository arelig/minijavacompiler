package minijavacompiler.symbol_table.ast.expression_nodes;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.symbol_table.SymbolTable;
import minijavacompiler.symbol_table.entities.Attribute;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.symbol_table.entities.Class;

public class ChainedVarNode extends ChainedNode {
    private Attribute attribute;
    public ChainedVarNode(Token token) {
        super(token);
    }
    @Override
    public Type validate(Type prevType) throws SemanticException {
        validateTypeReference(prevType);
        validateAttribute(prevType);
        return getType();
    }

    private Type getType() throws SemanticException {
        Type type;

        if (rightChainedNode != null) {
            type = rightChainedNode.validate(attribute.getType());
        } else {
            type = attribute.getType();
        }

        return type;
    }

    private void validateAttribute(Type prevType) throws SemanticException{
        Class prevClass = SymbolTable.getInstance().getClass(prevType.getId());

        if (prevClass != null) {
            attribute = prevClass.getAttribute(accessToken.getId());
            if (attribute == null) {
                throw new SemanticException(accessToken.getId(), accessToken.getLine(),
                        "No se encontró un atributo " + accessToken.getId() + " definido en la clase " + prevType.getId());
            }
            validateVisibility(prevClass);
        }
    }

    private void validateVisibility(Class prevClass) throws SemanticException {
        Class currentClass = SymbolTable.getInstance().getCurrentClass();
        boolean isPublic = attribute.getVisibility().equals("public");

        if (!isPublic && prevClass != currentClass) {
            throw new SemanticException(accessToken.getId(), accessToken.getLine(),
                    "El atributo " + accessToken.getId() + "es privado.");
        }
    }

    private void validateTypeReference(Type prevType) throws SemanticException {
        if (!prevType.isReference()) {
            throw new SemanticException(accessToken.getId(), accessToken.getLine(),
                    "Encadenado: no se puede aplicar a un tipo void o primitivo.");
        }
    }
    @Override
    public void generateCode() {
        if(rightChainedNode == null){
            CodeGenerator.generateCode("SWAP");
            CodeGenerator.generateCode("STOREREF " + attribute.getOffset() + " ;saves the top of the stack in the attribute");
        }else{
            CodeGenerator.generateCode("LOADREF " + attribute.getOffset() + "stacks the value of the attribute");
            rightChainedNode.generateCode();
        }

    }
}
