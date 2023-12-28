package minijavacompiler.symbol_table.ast.expression_nodes;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.symbol_table.types.TypeNull;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class LiteralNullNode extends OperandNode {
    private final Type type;
    public LiteralNullNode(Token token) {
        super(token);
        type = new TypeNull(token);
    }


    @Override
    public Type validate() throws SemanticException {
        return type;
    }

    @Override
    public void generateCode() {
        CodeGenerator.generateCode("PUSH 0 ; stack null literal value");
    }
}
