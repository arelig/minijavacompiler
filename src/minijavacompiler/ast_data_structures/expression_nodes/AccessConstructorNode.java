package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.*;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

import java.util.ArrayList;
import java.util.List;

public class AccessConstructorNode extends AccessUnitNode {
    private Token tknRef;

    public AccessConstructorNode(Token token) {
        super(token);
    }

    @Override
    public Type validate() throws SemanticException {
        if (SymbolTable.getInstance().isClassDeclared(tknRef.getLexeme())) {
            checkParameterPattern();
        }
        return new TypeReference(tknRef);
    }

    public void checkParameterPattern() throws SemanticException {
        List<Constructor> constrList = SymbolTable.getInstance().getClass(tknRef).getConstrList();
        List<Parameter> formalParams = new ArrayList<>();

        for (Constructor constructor : constrList) {
            if (constructor.getParamsAsList().size() == getActualParams().size()) {
                formalParams = constructor.getParamsAsList();
            }
        }

        this.checkParameters(formalParams);
    }

    public void setTknRef(Token tknRef) {
        this.tknRef = tknRef;
    }
}
