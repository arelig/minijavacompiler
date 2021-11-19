package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.ast_data_structures.Node;
import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public abstract class ChainedNode extends Node {
    private Token accessId;
    private ChainedNode rightChainedNode;

    public ChainedNode(Token tkn) {
        super(tkn);
    }

    public abstract Type validate(Type toCheck) throws SemanticException;

    @Override
    public void generateCode() {

    }

    public Token getAccessId() {
        return accessId;
    }

    public void setAccessId(Token accessId) {
        this.accessId = accessId;
    }

    public ChainedNode getRightChainedNode() {
        return rightChainedNode;
    }

    public void setRightChainedNode(ChainedNode rightChainedNode) {
        this.rightChainedNode = rightChainedNode;
    }
}
