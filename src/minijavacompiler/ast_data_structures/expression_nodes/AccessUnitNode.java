package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Parameter;
import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

import java.util.ArrayList;
import java.util.List;

public abstract class AccessUnitNode extends AccessNode {
    private List<ExpressionNode> actualParams;

    public AccessUnitNode(Token token) {
        super(token);
        actualParams = new ArrayList<>();
    }

    public List<ExpressionNode> getActualParams() {
        return actualParams;
    }

    public void checkParameters(List<Parameter> formalParams) throws SemanticException {
        for (int i = 0; i < formalParams.size(); i++) {
            Type formalType = formalParams.get(i).getType();
            Type actualType = actualParams.get(i).validate();

            if (!actualType.conform(formalType)) {
                throw new SemanticException(getId(), getLine(),
                        "El parametro actual no conforma con el parametro formal");
            }
        }
    }

    public void setActualParams(List<ExpressionNode> actualParams) {
        this.actualParams = actualParams;
    }
}
