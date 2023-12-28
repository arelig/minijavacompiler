package minijavacompiler.symbol_table.ast.expression_nodes;


import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.symbol_table.types.TypePrimitive;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

public class LiteralIntNode extends OperandNode {
    private final Type type;

    public LiteralIntNode(Token token) {
        super(token);
        type = new TypePrimitive(new Token(TokenType.PR_INT, "int", token.getLine()));
    }

    @Override
    public void generateCode() {
        CodeGenerator.generateCode("PUSH " + getId() + " ; load int value at the top of the stack");
    }

    public Type validate() throws SemanticException {
        return type;
    }
}
