package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Class;
import minijavacompiler.data_structures.*;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

import java.util.List;

public class AccessMetNode extends AccessUnitNode {
    private Method refMethod;

    public AccessMetNode(Token token) {
        super(token);
    }

    @Override
    public Type validate() throws SemanticException {
        getRefMethod();
        checkParameterPattern();
        return getType();
    }

    private Type getType() throws SemanticException {
        Type toReturn;
        if (getChainedNode() != null) {
            toReturn = getChainedNode().validate(refMethod.getReturnType());
        } else {
            toReturn = refMethod.getReturnType();
        }

        return toReturn;
    }

    public void checkParameterPattern() throws SemanticException {
        List<Parameter> formalParams = refMethod.getParamsAsList();

        this.checkParameters(formalParams);
    }

    private void getRefMethod() throws SemanticException {
        for (Class currentClass : SymbolTable.getInstance().getClassMap().values()) {
            if (currentClass.getMethsMap().containsKey(getId())) {
                refMethod = currentClass.getMethsMap().get(getId());
            }
        }
        if (refMethod == null) {
            throw new SemanticException(getId(), getLine(), "Acceso metodo sin declarar");
        }
    }
}
