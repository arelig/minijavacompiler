package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.ast_data_structures.expression_nodes.ExpressionNode;
import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class LocalVarNode extends SentenceNode {
    private Type type;
    private ExpressionNode init;

    public LocalVarNode(Token token) {
        super(token);
    }

    public void validate() throws SemanticException {
        //si esta inicializado, chequear tipos
        if (init != null) {
            Type initType = init.validate();
            if (!initType.conform(type)) {
                throw new SemanticException(getId(), getLine(), "Conformacion de tipos incompatible");
            }
        }
    }

    @Override
    public void generateCode() {

    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ExpressionNode getInit() {
        return init;
    }

    public void setInit(ExpressionNode init) {
        this.init = init;
    }
}
