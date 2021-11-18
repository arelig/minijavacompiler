package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.lexical_parser.Token;

import java.util.List;

public abstract class AccessUnitNode extends AccessNode {
    private List<ExpressionNode> actualParams;

    public AccessUnitNode(Token token) {
        super(token);
    }

    public List<ExpressionNode> getActualParams() {
        return actualParams;
    }

    public void setActualParams(List<ExpressionNode> actualParams) {
        this.actualParams = actualParams;
    }
}
