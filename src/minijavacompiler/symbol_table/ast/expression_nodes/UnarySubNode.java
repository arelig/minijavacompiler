package minijavacompiler.symbol_table.ast.expression_nodes;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class UnarySubNode extends UnaryExpressionNode {
    public UnarySubNode(Token token) {
        super(token);
    }

    @Override
    public void generateCode() {
        CodeGenerator.generateCode("SUB");
    }

    @Override
    public Type validate() throws SemanticException {
        return validateInt();
    }
}
