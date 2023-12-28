package minijavacompiler.symbol_table.ast.expression_nodes;

import minijavacompiler.symbol_table.ast.Node;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public abstract class ExpressionNode extends Node {
    public ExpressionNode(Token token) {
        super(token);
    }

    public abstract Type validate() throws SemanticException;
}
