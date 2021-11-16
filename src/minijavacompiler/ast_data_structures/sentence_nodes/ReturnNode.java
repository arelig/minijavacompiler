package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class ReturnNode extends SentenceNode {
    public ReturnNode(Token token) {
        super(token);
    }

    @Override
    public void validate() throws SemanticException {

    }


    @Override
    public void generateCode() {

    }
}
