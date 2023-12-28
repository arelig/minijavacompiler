package minijavacompiler.symbol_table.ast.expression_nodes;

import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.symbol_table.types.TypePrimitive;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

public abstract class BinaryExpressionNode extends ExpressionNode {
    protected ExpressionNode leftNode;
    protected ExpressionNode rightNode;

    public BinaryExpressionNode(Token token) {
        super(token);
    }

    public Type validateAndOr() throws SemanticException {
        Type leftType = leftNode.validate();
        Type rightType = rightNode.validate();

        if (!leftType.getId().equals("boolean")
                && !rightType.getId().equals("boolean")
                && !rightType.conform(leftType)) {
            throw new SemanticException(getId(), getLine(), "Operandos de tipo incompatible");
        }

        return new TypePrimitive(new Token(TokenType.PR_BOOLEAN, "boolean", getLine()));
    }

    public Type validateInt() throws SemanticException {
        Type leftType = leftNode.validate();
        Type rightType = rightNode.validate();

        if (!leftType.getId().equals("int")
                && !rightType.getId().equals("int")
                && !rightType.conform(leftType)) {
            throw new SemanticException(getId(), getLine(), "Operandos de tipo incompatible");
        }

        return new TypePrimitive(new Token(TokenType.PR_INT, "int", getLine()));
    }

    public Type validateEquals() throws SemanticException {
        Type leftType = leftNode.validate();
        Type rightType = rightNode.validate();

        if (!rightType.conform(leftType)) {
            throw new SemanticException(getId(), getLine(), "Operandos de tipo incompatible");
        }

        return new TypePrimitive(new Token(TokenType.PR_BOOLEAN, "boolean", getLine()));
    }

    public Type validateCompatibility() throws SemanticException {
        Type leftType = leftNode.validate();
        Type rightType = rightNode.validate();

        if (!leftType.getId().equals("int")
                && !rightType.getId().equals("int")
                && !rightType.conform(leftType)) {
            throw new SemanticException(getId(), getLine(), "Operandos de tipo incompatible");
        }

        return new TypePrimitive(new Token(TokenType.PR_BOOLEAN, "boolean", getLine()));
    }

    public void setLeftExpression(ExpressionNode leftExpression) {
        this.leftNode = leftExpression;
    }

    public void setRightExpression(ExpressionNode rightExpression) {
        this.rightNode = rightExpression;
    }
}
