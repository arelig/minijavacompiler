package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.lexical_parser.Token;

public abstract class OperandNode extends ExpressionNode {
    public OperandNode(Token tkn) {
        super(tkn);
    }

    @Override
    public void generateCode() {

    }

}
