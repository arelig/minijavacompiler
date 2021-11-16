package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class IfNode extends SentenceNode {
    public IfNode(Token token) {
        super(token);
    }


    public void validate() throws SemanticException {
        //si (tipo) de condicion.chequear() no es boolean
        //entonces error
        //sino sentenciaThen.chequear();
    }

    @Override
    public void generateCode() {

    }
}
