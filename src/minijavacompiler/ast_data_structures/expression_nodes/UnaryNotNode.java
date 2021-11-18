package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Type;
import minijavacompiler.data_structures.TypePrimitive;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

public class UnaryNotNode extends UnaryExpressionNode {
    public UnaryNotNode(Token token) {
        super(token);
    }

    @Override
    public void generateCode() {

    }

    @Override
    public Type validate() throws SemanticException {
        Type rightType = this.getRightExpression().validate();

        if (!rightType.getId().equals("boolean")) {
            throw new SemanticException(getId(), getLine(), "Operando de tipo incompatible");
        }
        return new TypePrimitive(new Token(TokenType.PR_BOOLEAN, "boolean", getLine()));
    }
}
