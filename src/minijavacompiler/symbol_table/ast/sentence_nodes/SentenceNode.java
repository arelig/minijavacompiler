package minijavacompiler.symbol_table.ast.sentence_nodes;

import minijavacompiler.symbol_table.ast.Node;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public abstract class SentenceNode extends Node {
    public SentenceNode(Token token) {
        super(token);
    }

    public abstract void checkSentences() throws SemanticException;
}
