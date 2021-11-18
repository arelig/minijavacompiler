package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class BinaryAddNode extends BinaryExpressionNode {

    public BinaryAddNode(Token tkn) {
        super(tkn);
    }

    @Override
    public Type validate() throws SemanticException {
        return this.validateInt();
    }

    @Override
    public void generateCode() {

    }
}
