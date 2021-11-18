package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Type;
import minijavacompiler.data_structures.TypePrimitive;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

public abstract class BinaryExpressionNode extends ExpressionNode {
    private ExpressionNode leftNode;
    private ExpressionNode rightNode;

    public BinaryExpressionNode(Token tkn) {
        super(tkn);
    }

    public ExpressionNode getLeftNode() {
        return leftNode;
    }

    public ExpressionNode getRightNode() {
        return rightNode;
    }

    public Type validateAndOr() throws SemanticException {
        Type leftType = this.getLeftNode().validate();
        Type rightType = this.getRightNode().validate();

        if (!leftType.getId().equals("boolean")
                && !rightType.getId().equals("boolean")
                && !rightType.conform(leftType)) {
            throw new SemanticException(getId(), getLine(), "Operandos de tipo incompatible");
        }

        return new TypePrimitive(new Token(TokenType.PR_BOOLEAN, "boolean", getLine()));
    }

    public Type validateInt() throws SemanticException {
        Type leftType = this.getLeftNode().validate();
        Type rightType = this.getRightNode().validate();

        if (!leftType.getId().equals("int")
                && !rightType.getId().equals("int")
                && !rightType.conform(leftType)) {
            throw new SemanticException(getId(), getLine(), "Operandos de tipo incompatible");
        }

        return new TypePrimitive(new Token(TokenType.PR_INT, "int", getLine()));
    }

    public Type validateEquals() throws SemanticException {
        Type leftType = this.getLeftNode().validate();
        Type rightType = this.getRightNode().validate();

        if (!rightType.conform(leftType)) {
            throw new SemanticException(getId(), getLine(), "Operandos de tipo incompatible");
        }

        return new TypePrimitive(new Token(TokenType.PR_BOOLEAN, "boolean", getLine()));
    }

    public Type validateComp() throws SemanticException {
        Type leftType = this.getLeftNode().validate();
        Type rightType = this.getRightNode().validate();

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
