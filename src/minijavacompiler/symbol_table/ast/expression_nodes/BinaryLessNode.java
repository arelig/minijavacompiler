package minijavacompiler.symbol_table.ast.expression_nodes;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class BinaryLessNode extends BinaryExpressionNode {
    public BinaryLessNode(Token token) {
        super(token);
    }

    @Override
    public Type validate() throws SemanticException {
        return validateCompatibility();
    }

    @Override
    public void generateCode() {
        CodeGenerator.generateCode("LT");
    }
}
