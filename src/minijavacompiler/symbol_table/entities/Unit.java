package minijavacompiler.symbol_table.entities;

import minijavacompiler.symbol_table.ast.sentence_nodes.BlockNode;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.symbol_table.types.Type;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class Unit extends Entity {
    protected List<Parameter> paramsAsList;
    protected BlockNode block;

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
        block.setUnitParams(paramsAsList);
    }

    public abstract Type getReturnType();

    public abstract HashMap<String, Parameter> getParamsMap();

    public void checkSentences() throws SemanticException {
        if(block != null){
            block.checkSentences();
        }
    }
}
