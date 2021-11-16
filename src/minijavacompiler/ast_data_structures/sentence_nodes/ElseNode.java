package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class ElseNode extends SentenceNode {
    public ElseNode(Token token) {
        super(token);
    }

    public void validate() throws SemanticException {

    }

    @Override
    public void generateCode() {

    }
}
