package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Class;
import minijavacompiler.data_structures.*;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

import java.util.List;

public class ChainedCallNode extends ChainedNode {
    private Method refMethod;
    private List<ExpressionNode> actualParams;

    public ChainedCallNode(Token tkn) {
        super(tkn);
    }

    public void setActualParams(List<ExpressionNode> actualParams) {
        this.actualParams = actualParams;
    }

    @Override
    public Type validate(Type toCheck) throws SemanticException {
        getRefMethod();
        checkParameterPattern();

        Type toReturn;
        Type metType = refMethod.getReturnType();

        if (getRightChainedNode() != null) {
            toReturn = getRightChainedNode().validate(metType);
        } else {
            toReturn = metType;
        }

        return toReturn;
    }

    public void checkParameterPattern() throws SemanticException {
        List<Parameter> formalParams = refMethod.getParamsAsList();
        checkParameters(formalParams);
    }

    private void getRefMethod() throws SemanticException {
        for (Class currentClass : SymbolTable.getInstance().getClassMap().values()) {
            if (currentClass.getMethsMap().containsKey(getAccessId().getLexeme())) {
                refMethod = currentClass.getMethsMap().get(getAccessId().getLexeme());
            }
        }
        if (refMethod == null) {
            throw new SemanticException(getId(), getLine(), "Encadenado metodo sin declarar");
        }
    }

    public void checkParameters(List<Parameter> formalParams) throws SemanticException {
        for (int i = 0; i < formalParams.size(); i++) {
            Type formalType = formalParams.get(i).getType();
            Type actualType = actualParams.get(i).validate();

            if (!actualType.conform(formalType)) {
                throw new SemanticException(actualParams.get(i).getId(), actualParams.get(i).getLine(),
                        "El parametro actual no conforma con el parametro formal");
            }
        }
    }
}
