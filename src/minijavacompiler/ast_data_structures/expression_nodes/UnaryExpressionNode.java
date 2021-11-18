package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Type;
import minijavacompiler.data_structures.TypePrimitive;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

public abstract class UnaryExpressionNode extends ExpressionNode {
    private ExpressionNode rightExpression;

    public UnaryExpressionNode(Token tkn) {
        super(tkn);
    }

    public Type validateInt() throws SemanticException {
        Type rightType = rightExpression.validate();

        if (!rightType.getId().equals("int")) {
            throw new SemanticException(getId(), getLine(), "Operando de tipo incompatible");
        }
        return new TypePrimitive(new Token(TokenType.PR_INT, "int", getLine()));
    }

    public ExpressionNode getRightExpression() {
        return rightExpression;
    }

    public void setRightExpression(ExpressionNode rightExpression) {
        this.rightExpression = rightExpression;
    }
}
