package minijavacompiler.data_structures;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

import java.util.ArrayList;
import java.util.HashMap;

public class Method extends Unit {
    private final String binding;
    private final Type returnType;
    private final HashMap<String, Parameter> params;

    private final HashMap<String, Attribute> localAttrMap;

    public Method(String binding, Type returnType, Token token) {
        setId(token.getLexeme());
        setLine(token.getLine());
        this.binding = binding;
        this.returnType = returnType;
        this.params = new HashMap<>();
        paramsAsList = new ArrayList<>();

        localAttrMap = new HashMap<>();
    }

    public Type getReturnType() {
        return returnType;
    }

    public String getBinding() {
        return binding;
    }

    @Override
    public void addParam(Parameter param) throws SemanticException {
        if (params.containsKey(param.getId())) {
            throw new SemanticException(param.getId(),
                    param.getLine(), "Parametro "
                    + param.getId()
                    + " ya declarado");
        }
        params.put(param.getId(), param);
        paramsAsList.add(param);
    }

    public void check() throws SemanticException {
        returnType.check();
        checkParams();
    }

    private void checkParams() throws SemanticException {
        for (Parameter p : getParamsAsList()) {
            p.check();
        }
    }

    public boolean isParamDeclared(String id) {
        return params.containsKey(id);
    }

    @Override
    public boolean isLocalAttrDeclared(String id) {
        return localAttrMap.containsKey(id);
    }

    @Override
    public HashMap<String, Parameter> getParamsMap() {
        return params;
    }

    @Override
    public HashMap<String, Attribute> getLocalAttrMap() {
        return localAttrMap;
    }
}
