package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Type;
import minijavacompiler.data_structures.TypeReference;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class AccessThisNode extends AccessNode {
    private Token refClass;

    public AccessThisNode(Token token) {
        super(token);
    }

    public Type getCurrentType() {
        return new TypeReference(refClass);
    }

    @Override
    public Type validate() throws SemanticException {
        Type toReturn;
        if (getChainedNode() != null) {
            toReturn = getChainedNode().validate(getCurrentType());
        } else {
            toReturn = getCurrentType();
        }
        return toReturn;
    }

    public void setRefClass(Token refClass) {
        this.refClass = refClass;
    }
}
