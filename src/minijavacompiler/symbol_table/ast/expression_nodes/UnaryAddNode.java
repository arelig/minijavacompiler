package minijavacompiler.symbol_table.ast.expression_nodes;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class UnaryAddNode extends UnaryExpressionNode {
    public UnaryAddNode(Token token) {
        super(token);
    }

    @Override
    public Type validate() throws SemanticException {
        return validateInt();
    }

    @Override
    public void generateCode() {
        CodeGenerator.generateCode("ADD");
    }
}