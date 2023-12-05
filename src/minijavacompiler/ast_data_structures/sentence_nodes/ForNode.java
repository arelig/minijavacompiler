package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.ast_data_structures.expression_nodes.ExpressionNode;
import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class ForNode extends SentenceNode {
    private SentenceNode localVarCond;
    private ExpressionNode varCond;
    private SentenceNode assignCond;
    private SentenceNode bodySentence;

    public ForNode(Token token) {
        super(token);
    }


    public void validate() throws SemanticException {
        localVarCond.validate();

        Type type = varCond.validate();
        if (type.getId().equals("boolean")) {
            //mismo que if, como evaluo el valor boolean de varCond
            //como modelo las repeticiones del for?!?!?!?!!?!?!??!?!?!?!!??!?!?! ah
            //creo que se modela en ejecucion cuando haces el codigo, tiene mas sentido
        }

        assignCond.validate();
        bodySentence.validate();
    }


    @Override
    public void generateCode() {

    }

    public void setLocalVar(SentenceNode localVarCond) {
        this.localVarCond = localVarCond;
    }

    public void setVarCond(ExpressionNode varCond) {
        this.varCond = varCond;
    }

    public void setAssignCond(SentenceNode assignCond) {
        this.assignCond = assignCond;
    }

    public void setBodySentence(SentenceNode bodySentence) {
        this.bodySentence = bodySentence;
    }
}
