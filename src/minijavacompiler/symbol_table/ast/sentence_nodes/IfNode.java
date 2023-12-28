package minijavacompiler.symbol_table.ast.sentence_nodes;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.code_generator.TagManager;
import minijavacompiler.lexical_parser.TokenType;
import minijavacompiler.symbol_table.ast.expression_nodes.ExpressionNode;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.symbol_table.types.TypePrimitive;

public class IfNode extends SentenceNode {
    private ExpressionNode condition;
    private SentenceNode thenNode;
    private SentenceNode elseNode;

    public IfNode(Token token) {
        super(token);
    }

    public void checkSentences() throws SemanticException {
        checkConditionType();
        thenNode.checkSentences();
        if(elseNode != null){
            elseNode.checkSentences();
        }
    }

    private void checkConditionType() throws SemanticException {
        Type conditionType = condition.validate();

        if(!conditionType.conform(new TypePrimitive(new Token(TokenType.PR_BOOLEAN,"boolean",0)))){
            throw new SemanticException(getId(),getLine(),"La condición del If debería ser de tipo boolean pero se obtuvo " + conditionType);
        }
    }

    @Override
    public void generateCode() {
        String thenTag = TagManager.getTag("thenIf");
        String endIfTag = TagManager.getTag("endIf");

        String elseTag;
        if(elseNode != null) {
            elseTag = TagManager.getTag("elseIf");
        }else{
            elseTag = endIfTag;
        }

        condition.generateCode();
        CodeGenerator.setComment("If condition");
        CodeGenerator.generateCode("BF "+ elseTag + " ;else jump");
        CodeGenerator.setNextInstructionTag(thenTag);
        thenNode.generateCode();

        if(elseNode != null) {
            CodeGenerator.generateCode("JUMP " + endIfTag + " ;jump to end if");
            CodeGenerator.setNextInstructionTag(elseTag);
            elseNode.generateCode();
        }
        CodeGenerator.generateCode(endIfTag + ": " + "NOP ;end if");
    }

    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    public void setThenNode(SentenceNode thenNode) {
        this.thenNode = thenNode;
    }

    public void setElseNode(SentenceNode elseNode) {
        this.elseNode = elseNode;
    }
}
