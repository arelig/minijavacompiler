package minijavacompiler.symbol_table.ast.expression_nodes;

import minijavacompiler.symbol_table.ast.Node;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public abstract class ChainedNode extends Node {
    protected Token accessToken;
    protected ChainedNode rightChainedNode;

    public ChainedNode(Token token) {
        super(token);
    }

    public abstract Type validate(Type prevType) throws SemanticException;

    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }

    public void setRightChainedNode(ChainedNode rightChainedNode) {
        this.rightChainedNode = rightChainedNode;
    }
}
