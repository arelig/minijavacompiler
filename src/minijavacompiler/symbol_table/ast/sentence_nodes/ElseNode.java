package minijavacompiler.symbol_table.ast.sentence_nodes;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class ElseNode extends SentenceNode {
    private SentenceNode bodyNode;

    public ElseNode(Token token) {
        super(token);
    }

    public void checkSentences() throws SemanticException {
        bodyNode.checkSentences();
    }

    public void setBodyNode(SentenceNode bodyNode) {
        this.bodyNode = bodyNode;
    }

    @Override
    public void generateCode() {
        bodyNode.generateCode();
    }
}
