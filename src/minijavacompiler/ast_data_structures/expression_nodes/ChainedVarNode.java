package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Type;
import minijavacompiler.lexical_parser.Token;

public class ChainedVarNode extends ChainedNode {
    private final boolean isAssignable = false;
    private Type type;

    public ChainedVarNode(Token tkn) {
        super(tkn);
    }

    @Override
    public Type validate(Type toCheck) {
        return null;
    }


}
