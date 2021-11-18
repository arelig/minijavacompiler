package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class UnaryAddNode extends UnaryExpressionNode {
    public UnaryAddNode(Token token) {
        super(token);
    }

    @Override
    public void generateCode() {

    }

    @Override
    public Type validate() throws SemanticException {
        return this.validateInt();
    }
}
