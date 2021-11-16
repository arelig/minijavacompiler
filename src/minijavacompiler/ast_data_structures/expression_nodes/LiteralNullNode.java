package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Type;
import minijavacompiler.data_structures.TypeNull;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class LiteralNullNode extends OperandNode {
    public LiteralNullNode(Token tkn) {
        super(tkn);
    }


    @Override
    public Type validate() throws SemanticException {
        return new TypeNull(getTkn());
    }

    @Override
    public void generateCode() {

    }
}
