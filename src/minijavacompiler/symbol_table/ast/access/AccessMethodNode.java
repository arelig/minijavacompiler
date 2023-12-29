package minijavacompiler.symbol_table.ast.access;

import minijavacompiler.ST;
import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.lexical_parser.TokenType;
import minijavacompiler.symbol_table.ast.expression_nodes.ExpressionNode;
import minijavacompiler.symbol_table.entities.*;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.symbol_table.entities.Class;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.symbol_table.types.TypeVoid;

import java.util.List;

public class AccessMethodNode extends AccessUnitNode {
    private Method method;
    public AccessMethodNode(Token token) {
        super(token);
    }

    @Override
    public Type validate() throws SemanticException {
        checkMethodReference();
        checkParameterPattern();
        return checkChaining(method.getReturnType());
    }

    private void checkMethodReference() throws SemanticException {
        Class currentClass = ST.symbolTable.getCurrentClass();
        if(currentClass != null) {
            method = currentClass.getMethod(getId());
        }

        if(method == null){
            throw new SemanticException(getId(), getLine(),
                    "El método " + getId() + " no se encontró en la clase " +
                            currentClass.getId());
        }
    }

    public void checkParameterPattern() throws SemanticException {
        List<Parameter> formalParams = method.getParamsAsList();

        this.validateParamCompatibility(formalParams);
    }

    public void generateCode() {
        loadThisIfNotStatic();
        handleVoidReturnType();
        generateActualParams();
        loadMethodTag();
        generateChainedNode();
    }

    private void loadThisIfNotStatic() {
        if (!method.getBinding().equals("static")) {
            CodeGenerator.generateCode("LOAD 3 ;load this");
        }
    }

    private void handleVoidReturnType() {
        if (method.getReturnType().equals(new TypeVoid(new Token(TokenType.VOID, "void", 0)))) {
            CodeGenerator.generateCode("RMEM 1 ;return");
            if (!method.getBinding().equals("static")) {
                CodeGenerator.generateCode("SWAP ;swap this and return");
            }
        }
    }

    private void generateActualParams() {
        for (int i = getActualParams().size() - 1; i >= 0; i--) {
            ExpressionNode actualParam = getActualParams().get(i);
            actualParam.generateCode();
            CodeGenerator.setComment("Parameter " + i);
            if (!method.getBinding().equals("static")) {
                CodeGenerator.generateCode("SWAP ;swap this and argument");
            }
        }
    }

    private void loadMethodTag() {
        if (!method.getBinding().equals("static")) {
            CodeGenerator.generateCode("DUP ;load tag method " + method.getId() + " from VTable (1)");
            CodeGenerator.generateCode("LOADREF 0 ;load tag method " + method.getId() + " from VTable (2)");
            CodeGenerator.generateCode("LOADREF " + method.getOffset() + " ; load tag method " + method.getId() + " from VTable(3)");
        } else {
            CodeGenerator.generateCode("PUSH " + method.getTag() + " ; load tag method " + method.getId());
        }
    }

    private void generateChainedNode() {
        if (chainedNode != null) {
            chainedNode.generateCode();
        }
    }

}
