package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class AccessThisNode extends AccessNode {
    private Token refClassType;

    public AccessThisNode(Token token) {
        super(token);
    }

    @Override
    public Type validate() throws SemanticException {
        return null;
    }

    public void setRefClassType(Token refClassType) {
        this.refClassType = refClassType;
    }
}
