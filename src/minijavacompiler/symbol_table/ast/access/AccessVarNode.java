package minijavacompiler.symbol_table.ast.access;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.symbol_table.SymbolTable;
import minijavacompiler.symbol_table.ast.sentence_nodes.LocalVarNode;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.symbol_table.entities.Attribute;
import minijavacompiler.symbol_table.entities.Parameter;

public class AccessVarNode extends AccessNode {
    private LocalVarNode localVar;
    private Parameter parameter;
    private Attribute attribute;

    public AccessVarNode(Token token) {
        super(token);
    }

    @Override
    public Type validate() throws SemanticException {
        return checkChaining(getVarType());
    }

    private Type getVarType() throws SemanticException {
        Type type;

        localVar = SymbolTable.getInstance().getCurrentBlock().getLocalVar(getId());

        parameter = SymbolTable.getInstance().getCurrentUnit().getParamsMap().get(getId());

        attribute = SymbolTable.getInstance().getCurrentClass().getAttributes().get(getId());

        if(localVar != null){
            type = localVar.getType();
        } else if(parameter != null){
            type = parameter.getType();
        } else if(attribute != null){
            type = attribute.getType();
            checkInstanceAttribute();
        } else {
            throw new SemanticException(getId(), getLine(),
                    "La variable " + getId() + " no fue declarada en el contexto o es inalcanzable.");
        }

        return type;
    }

    private void checkInstanceAttribute() throws SemanticException {
//        if(localVar != null && SymbolTable.getInstance().getCurrentClass().getBinding().equals("static")){
//            throw new SemanticException(getId(),getLine(),
//                    "Acceso: atributo de instancia en metodo estático.");
//        }
    }

    public void generateCode(){
        if(attribute != null){
            CodeGenerator.generateCode("LOAD 3 ;load this");
            if(chainedNode != null){
                CodeGenerator.generateCode("LOADREF " + attribute.getOffset() + " ;stores the top of the stack in the attribute");
            } else {
                CodeGenerator.generateCode("SWAP ;swap the value of the assignment with this");
                CodeGenerator.generateCode("STOREREF " + attribute.getOffset() + " ;stores the top of the stack in the attribute");
            }
        } else if(parameter != null){
            if(chainedNode != null) {
                CodeGenerator.generateCode("LOAD " + parameter.getOffset() + " ;load parameter value onto stack");
            }else {
                CodeGenerator.generateCode("STORE " + parameter.getOffset() + " ;stores the top of the stack in the parameter");
            }
        }else if(localVar != null){
            if(chainedNode != null){
                CodeGenerator.generateCode("LOAD " + localVar.getOffset() + " ;load local variable value onto stack");
            }else {
                CodeGenerator.generateCode("STORE " + localVar.getOffset() + " ;stores the top of the stack in the local variable");
            }
        }
        if(chainedNode != null){
            chainedNode.generateCode();
        }
    }

}
