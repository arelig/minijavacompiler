package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.ast_data_structures.expression_nodes.AccessNode;
import minijavacompiler.ast_data_structures.expression_nodes.ExpressionNode;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class LocalVarNode extends SentenceNode {
    AccessNode leftNode;
    ExpressionNode rightNode;

    public LocalVarNode(Token token) {
        super(token);
    }

    public void validate() throws SemanticException {

    }

    @Override
    public void generateCode() {

    }
}
