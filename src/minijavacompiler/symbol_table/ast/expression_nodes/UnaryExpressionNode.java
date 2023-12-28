package minijavacompiler.symbol_table.ast.expression_nodes;

import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.symbol_table.types.TypePrimitive;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

public abstract class UnaryExpressionNode extends ExpressionNode {
    protected ExpressionNode rightExpression;

    public UnaryExpressionNode(Token token) {
        super(token);
    }

    public Type validateInt() throws SemanticException {
        Type rightType = rightExpression.validate();

        if (!rightType.equals(new TypePrimitive(new Token(TokenType.PR_INT, "int", 0)))) {
            throw new SemanticException(getId(), getLine(),
                    "No se puede aplicar el operador " + getId() + " al tipo " + rightType.getId());
        }
        return new TypePrimitive(new Token(TokenType.PR_INT, "int", getLine()));
    }

    public Type validateBoolean() throws SemanticException {
        Type rightType = rightExpression.validate();

        if (!rightType.equals(new TypePrimitive(new Token(TokenType.PR_BOOLEAN, "boolean", 0)))) {
            throw new SemanticException(getId(), getLine(),
                    "No se puede aplicar el operador " + getId() + " al tipo " + rightType.getId());
        }
        return new TypePrimitive(new Token(TokenType.PR_BOOLEAN, "boolean", getLine()));
    }

    public void setRightExpression(ExpressionNode rightExpression) {
        this.rightExpression = rightExpression;
    }
}
