package minijavacompiler.symbol_table.ast.access;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.symbol_table.ast.expression_nodes.ChainedNode;
import minijavacompiler.symbol_table.ast.expression_nodes.OperandNode;
import minijavacompiler.symbol_table.types.Type;


public abstract class AccessNode extends OperandNode {
    protected ChainedNode chainedNode;

    protected Token accessToken;

    public AccessNode(Token token) {
        super(token);
        this.accessToken = token;
    }

    public Token getAccessToken(){
        return accessToken;
    }
    public Type checkChaining(Type accessType) throws SemanticException {
        if(chainedNode == null){
            return accessType;
        }else{
            return chainedNode.validate(accessType);
        }
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    @Override
    public abstract void generateCode();
}
