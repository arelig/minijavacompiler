package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class ElseNode extends SentenceNode {
    private SentenceNode bodySentence;

    public ElseNode(Token token) {
        super(token);
    }

    public void setBodySentence(SentenceNode bodySentence) {
        this.bodySentence = bodySentence;
    }

    public void validate() throws SemanticException {
        bodySentence.validate();
    }

    @Override
    public void generateCode() {

    }
}
