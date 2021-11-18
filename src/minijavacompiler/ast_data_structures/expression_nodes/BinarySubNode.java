package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class BinarySubNode extends BinaryExpressionNode {
    public BinarySubNode(Token token) {
        super(token);
    }

    @Override
    public Type validate() throws SemanticException {
        return this.validateInt();
    }

    @Override
    public void generateCode() {

    }
}
