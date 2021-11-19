package minijavacompiler.data_structures;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

import java.util.ArrayList;
import java.util.HashMap;

public class Constructor extends Unit {
    private final HashMap<String, Parameter> paramsMap;

    private final HashMap<String, Attribute> localAttrMap;

    public Constructor(Token tknConstr) {
        setId(tknConstr.getLexeme());
        setLine(tknConstr.getLine());

        paramsMap = new HashMap<>();
        paramsAsList = new ArrayList<>();

        localAttrMap = new HashMap<>();
    }

    @Override
    public void check() throws SemanticException {
        for (Parameter p : getParamsAsList()) {
            p.check();
        }
    }

    public void addParam(Parameter param) throws SemanticException {
        if (paramsMap.containsKey(param.getId())) {
            throw new SemanticException(param.getId(),
                    param.getLine(), "Parametro "
                    + param.getId()
                    + " ya declarado");
        }
        paramsMap.put(param.getId(), param);
        getParamsAsList().add(param);
    }

    public boolean isParamDeclared(String id) {
        return paramsMap.containsKey(id);
    }

    @Override
    public Type getReturnType() {
        return null;
    }

    @Override
    public boolean isLocalAttrDeclared(String id) {
        return localAttrMap.containsKey(id);
    }

    @Override
    public HashMap<String, Parameter> getParamsMap() {
        return paramsMap;
    }

    @Override
    public HashMap<String, Attribute> getLocalAttrMap() {
        return localAttrMap;
    }

}
