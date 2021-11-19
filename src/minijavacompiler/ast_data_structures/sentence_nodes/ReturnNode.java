package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.ast_data_structures.expression_nodes.ExpressionNode;
import minijavacompiler.data_structures.Type;
import minijavacompiler.data_structures.Unit;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class ReturnNode extends SentenceNode {
    private ExpressionNode returnExpression;
    private Unit unit;

    public ReturnNode(Token token) {
        super(token);
    }

    @Override
    public void validate() throws SemanticException {
        if (returnExpression != null) {
            Type actualType = returnExpression.validate();
            if (!actualType.conform(unit.getReturnType())) {
                throw new SemanticException(getId(), getLine(), "La expresion de retorno no coincide con el tipo de la unidad");
            }
        }
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }


    @Override
    public void generateCode() {

    }

    public void setExpression(ExpressionNode expressionNode) {
        this.returnExpression = expressionNode;
    }
}
