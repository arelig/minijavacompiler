package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.ast_data_structures.expression_nodes.ExpressionNode;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class ReturnNode extends SentenceNode {
    //si es nulo no hay expresion de retorno...
    private ExpressionNode returnExpression;

    public ReturnNode(Token token) {
        super(token);
    }

    @Override
    public void validate() throws SemanticException {
        if (returnExpression != null) {

        }
    }


    @Override
    public void generateCode() {

    }

    public void setExpression(ExpressionNode expressionNode) {
        this.returnExpression = expressionNode;
    }
}
