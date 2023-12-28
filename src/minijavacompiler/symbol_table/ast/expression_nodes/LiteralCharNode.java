package minijavacompiler.symbol_table.ast.expression_nodes;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.symbol_table.types.TypePrimitive;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

public class LiteralCharNode extends OperandNode {
    private final Type type;

    public LiteralCharNode(Token token) {
        super(token);
        type = new TypePrimitive(new Token(TokenType.PR_CHAR, "char", token.getLine()));
    }

    @Override
    public Type validate() throws SemanticException {
        return type;
    }

    @Override
    public void generateCode() {
        CodeGenerator.generateCode("PUSH " + getId() + " ; stack char value");
    }
}
