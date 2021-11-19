package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.ast_data_structures.expression_nodes.ExpressionNode;
import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class IfNode extends SentenceNode {
    private ExpressionNode condExpression;
    private SentenceNode bodySentence;
    private SentenceNode elseNode;

    public IfNode(Token token) {
        super(token);
    }

    public void validate() throws SemanticException {
        Type type = condExpression.validate();

        if (type.getId().equals("boolean")) {
            bodySentence.validate();
            if (elseNode != null) {
                elseNode.validate();
            }
        } else {
            throw new SemanticException(condExpression.getId(), condExpression.getLine(), "La expresion no es booleana");
        }
    }

    public void setCondExpression(ExpressionNode condExpression) {
        this.condExpression = condExpression;
    }

    public void setBodySentence(SentenceNode bodySentence) {
        this.bodySentence = bodySentence;
    }

    public void setElseNode(SentenceNode elseNode) {
        this.elseNode = elseNode;
    }

    @Override
    public void generateCode() {

    }
}
