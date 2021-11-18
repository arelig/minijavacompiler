package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

import java.util.ArrayList;
import java.util.List;

public class AccessConstructorNode extends AccessUnitNode {
    private Token tknRef;
    private List<ExpressionNode> actualParams;

    public AccessConstructorNode(Token token) {
        super(token);
        actualParams = new ArrayList<>();
    }

    @Override
    public Type validate() throws SemanticException {
        //el tipo de una llamada a un metodo constructor es determinado a partir de la TS

        return null;
    }

    public void setActualParams(List<ExpressionNode> actualParams) {
        this.actualParams = actualParams;
    }

    public void setTknRef(Token tknRef) {
        this.tknRef = tknRef;
    }
}
