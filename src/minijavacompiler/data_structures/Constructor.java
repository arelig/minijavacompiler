package minijavacompiler.data_structures;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

import java.util.ArrayList;
import java.util.HashMap;

public class Constructor extends Unit {
    private final HashMap<String, Parameter> params;

    public Constructor(Token tknConstr) {
        setId(tknConstr.getLexeme());
        setLine(tknConstr.getLine());

        params = new HashMap<>();
        paramsAsList = new ArrayList<>();
    }

    @Override
    public void check() throws SemanticException {
        for (Parameter p : getParamsAsList()) {
            p.check();
        }
    }

    public void addParam(Parameter param) throws SemanticException {
        if (params.containsKey(param.getId())) {
            throw new SemanticException(param.getId(),
                    param.getLine(), "Parametro "
                    + param.getId()
                    + " ya declarado");
        }
        params.put(param.getId(), param);
        getParamsAsList().add(param);
    }

}
