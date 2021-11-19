package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class AccessParenthesesExpr extends AccessNode {
    private ExpressionNode expressionNode;

    public AccessParenthesesExpr(Token token) {
        super(token);
    }

    @Override
    public Type validate() throws SemanticException {
        return null;
    }

    public void setExpression(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }
}
