package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.lexical_parser.Token;


public abstract class AccessNode extends OperandNode {
    private ChainedNode chainedNode;


    public AccessNode(Token token) {
        super(token);
    }

    @Override
    public void generateCode() {

    }

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }
}
