package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends SentenceNode {
    List<SentenceNode> sentenceNodeList;

    public BlockNode(Token token) {
        super(token);
        sentenceNodeList = new ArrayList<>();
    }

    public void validate() throws SemanticException {
        for (SentenceNode sentence : sentenceNodeList) {
            sentence.validate();
        }

        //para toda sentence node list, check
    }

    public void addSentence(SentenceNode sentenceNode) {
        sentenceNodeList.add(sentenceNode);
    }

    @Override
    public void generateCode() {

    }
}
