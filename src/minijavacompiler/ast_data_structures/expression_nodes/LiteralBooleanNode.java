package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Type;
import minijavacompiler.data_structures.TypePrimitive;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

public class LiteralBooleanNode extends OperandNode {
    private final Type type;

    public LiteralBooleanNode(Token tkn) {
        super(tkn);
        type = new TypePrimitive(new Token(TokenType.PR_BOOLEAN, "boolean", tkn.getLine()));
    }


    @Override
    public Type validate() throws SemanticException {
        return type;
    }

    @Override
    public void generateCode() {

    }
}
