package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.ast_data_structures.Node;
import minijavacompiler.data_structures.Type;
import minijavacompiler.lexical_parser.Token;

public abstract class ChainedNode extends Node {

    public ChainedNode(Token tkn) {
        super(tkn);
    }

    public abstract Type validate(Type toCheck);

    @Override
    public void generateCode() {

    }
}
