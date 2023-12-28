package minijavacompiler.symbol_table.ast.access;

import minijavacompiler.symbol_table.ast.expression_nodes.ExpressionNode;
import minijavacompiler.symbol_table.entities.Parameter;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AccessUnitNode extends AccessNode {
    private List<ExpressionNode> actualParams;

    public AccessUnitNode(Token token) {
        super(token);
        actualParams = new LinkedList<>();
    }
    public void validateParamCompatibility(List<Parameter> formalParams) throws SemanticException {
        for (int i = 0; i < formalParams.size(); i++) {
            Type formalType = formalParams.get(i).getType();
            Type actualType = actualParams.get(i).validate();

            if (!actualType.conform(formalType)) {
                throw new SemanticException(getId(), getLine(),
                        "El parámetro actual " + (i + 1) + " no conforma con el parámetro formal " +
                                formalType.getId());
            }
        }
    }

    public void setActualParams(List<ExpressionNode> actualParams) {
        this.actualParams = actualParams;
    }

    public List<ExpressionNode> getActualParams() {
        return actualParams;
    }
}
