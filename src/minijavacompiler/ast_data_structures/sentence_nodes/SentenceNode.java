package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.ast_data_structures.Node;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public abstract class SentenceNode extends Node {
    public SentenceNode(Token token) {
        super(token);
    }

    public abstract void validate() throws SemanticException;
}
