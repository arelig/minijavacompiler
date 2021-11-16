package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Type;
import minijavacompiler.data_structures.TypePrimitive;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

public class LiteralStringNode extends OperandNode {
    private final Type type;

    public LiteralStringNode(Token tkn) {
        super(tkn);
        type = new TypePrimitive(new Token(TokenType.PR_STRING, "String", tkn.getLine()));
    }

    @Override
    public Type validate() throws SemanticException {
        return type;
    }

    @Override
    public void generateCode() {

    }
}
