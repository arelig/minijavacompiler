package minijavacompiler.symbol_table.ast.sentence_nodes;

import minijavacompiler.lexical_parser.TokenType;
import minijavacompiler.symbol_table.ast.expression_nodes.ExpressionNode;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.symbol_table.types.TypePrimitive;
import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.code_generator.TagManager;

public class ForNode extends SentenceNode {
    private SentenceNode localVarCond;
    private ExpressionNode varCond;
    private SentenceNode assignmentNode;
    private SentenceNode bodyNode;

    public ForNode(Token token) {
        super(token);
    }

    public void checkSentences() throws SemanticException {
        checkLocalVar();
        checkCondition();
        checkAssignment();
        checkBody();
    }

    private void checkBody() throws SemanticException {
        if(bodyNode != null){
            bodyNode.checkSentences();
        }else{
            throw new SemanticException(getId(), getLine(),
                    "Falta el cuerpo del bucle For");
        }
    }

    private void checkAssignment() throws SemanticException {
        if(assignmentNode != null){
            assignmentNode.checkSentences();
        }else {
            throw new SemanticException(getId(), getLine(),
                    "Falta la asignación del bucle For");
        }
    }

    private void checkCondition() throws SemanticException {
        if(varCond != null) {
            Type varCondType = varCond.validate();
            if (!varCondType.conform(new TypePrimitive(new Token(TokenType.PR_BOOLEAN, "boolean", 0)))) {
                throw new SemanticException(getId(), getLine(),
                        "La condición del For debería ser de tipo boolean pero se obtuvo " + varCondType);
            } else {
                throw new SemanticException(getId(), getLine(), "Falta la condición del bucle For");
            }
        }
    }

    private void checkLocalVar() throws SemanticException {
        if(localVarCond != null){
            localVarCond.checkSentences();
        }else {
            throw new SemanticException(getId(), getLine(),
                    "Falta la declaración de la variable local en el For.");
        }
    }

    @Override
    public void generateCode() {
        // Generar código para la inicialización de la variable de control
        localVarCond.generateCode();

        String startForTag = TagManager.getTag("startFor");
        String endForTag = TagManager.getTag("endFor");

        // Etiqueta de inicio del bucle for
        CodeGenerator.setNextInstructionTag(startForTag);

        // Generar código para evaluar la condición
        varCond.generateCode();
        CodeGenerator.setComment("forCondition");
        CodeGenerator.generateCode("BF " + endForTag + " ; jump to the end of the for if the condition is false");

        // Cuerpo del bucle
        bodyNode.generateCode();

        // Generar código para la actualización
        assignmentNode.generateCode();
        CodeGenerator.generateCode("JUMP " + startForTag + " ; go back to the start of the for");

        // Etiqueta de fin del bucle for
        CodeGenerator.generateCode(endForTag + ": NOP ; end of the for");
    }

    public void setLocalVar(SentenceNode localVarCond) {
        this.localVarCond = localVarCond;
    }

    public void setVarCond(ExpressionNode varCond) {
        this.varCond = varCond;
    }

    public void setAssignmentNode(SentenceNode assignmentNode) {
        this.assignmentNode = assignmentNode;
    }

    public void setBodyNode(SentenceNode bodyNode) {
        this.bodyNode = bodyNode;
    }
}
