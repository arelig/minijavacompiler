package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Type;
import minijavacompiler.lexical_parser.Token;

public class ChainedCallNode extends ChainedNode {
    public ChainedCallNode(Token tkn) {
        super(tkn);
    }

    @Override
    public Type validate(Type toCheck) {
        return null;
    }
}
