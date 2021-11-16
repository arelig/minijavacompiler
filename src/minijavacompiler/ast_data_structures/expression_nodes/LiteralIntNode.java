package minijavacompiler.ast_data_structures.expression_nodes;


import minijavacompiler.data_structures.Type;
import minijavacompiler.data_structures.TypePrimitive;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

public class LiteralIntNode extends OperandNode {
    private final Type type;

    public LiteralIntNode(Token tkn) {
        super(tkn);
        type = new TypePrimitive(new Token(TokenType.PR_INT, "int", tkn.getLine()));
    }

    @Override
    public void generateCode() {

    }

    public Type validate() throws SemanticException {
        return type;
    }
}
