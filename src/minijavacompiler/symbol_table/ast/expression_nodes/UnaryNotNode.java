package minijavacompiler.symbol_table.ast.expression_nodes;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.symbol_table.types.TypePrimitive;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

public class UnaryNotNode extends UnaryExpressionNode {
    public UnaryNotNode(Token token) {
        super(token);
    }

    @Override
    public void generateCode() {
        CodeGenerator.generateCode("NOT");
    }

    @Override
    public Type validate() throws SemanticException {
       return validateBoolean();
    }
}
