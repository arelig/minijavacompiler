package minijavacompiler.data_structures;

import minijavacompiler.exceptions.SemanticException;

import java.util.Iterator;
import java.util.List;

public abstract class Unit extends Entity {
    List<Parameter> paramsAsList;

    public abstract void addParam(Parameter param) throws SemanticException;

    public List<Parameter> getParamsAsList() {
        return paramsAsList;
    }

    public boolean checkParameters(List<Parameter> paramList) {
        boolean sameParams = paramsAsList.size() == paramList.size();

        Iterator<Parameter> myParams = paramsAsList.iterator();
        Iterator<Parameter> inheritedParams = paramList.iterator();

        while (myParams.hasNext() && inheritedParams.hasNext()) {
            Parameter myParam = myParams.next();
            Parameter inheritedParam = inheritedParams.next();
            if (!(myParam.equals(inheritedParam))) {
                sameParams = false;
            }
        }
        return sameParams;
    }
}
