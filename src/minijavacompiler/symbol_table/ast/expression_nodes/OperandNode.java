package minijavacompiler.symbol_table.ast.expression_nodes;

import minijavacompiler.lexical_parser.Token;

public abstract class OperandNode extends ExpressionNode {
    public OperandNode(Token token) {
        super(token);
    }

}
