package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.lexical_parser.Token;

public class EmptyNode extends SentenceNode {
    public EmptyNode(Token token) {
        super(token);
    }

    @Override
    public void validate() {

    }


    @Override
    public void generateCode() {

    }
}
