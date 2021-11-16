package minijavacompiler.data_structures;

import minijavacompiler.ast_data_structures.sentence_nodes.BlockNode;
import minijavacompiler.exceptions.SemanticException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class Unit extends Entity {
    List<Parameter> paramsAsList;
    BlockNode block;

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

    public BlockNode getBlock() {
        return block;
    }

    public void setBlock(BlockNode block) {
        this.block = block;
    }

    public abstract boolean isParamDeclared(String id);

    public abstract boolean isLocalAttrDeclared(String id);

    public abstract HashMap<String, Parameter> getParamsMap();

    public abstract HashMap<String, Attribute> getLocalAttrMap();

    public void checkSentences() throws SemanticException {
        block.validate();
    }
}
