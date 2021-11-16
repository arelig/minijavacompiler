package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.ast_data_structures.Node;
import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public abstract class ExpressionNode extends Node {
    public ExpressionNode(Token tkn) {
        super(tkn);
    }

    public abstract Type validate() throws SemanticException;
}
