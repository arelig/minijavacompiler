package minijavacompiler.symbol_table.ast.sentence_nodes;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.lexical_parser.Token;

public class EmptyNode extends SentenceNode {
    public EmptyNode(Token token) {
        super(token);
    }

    @Override
    public void checkSentences() {}


    @Override
    public void generateCode() {
        CodeGenerator.generateCode("NOP ;empty sentence");
    }
}
