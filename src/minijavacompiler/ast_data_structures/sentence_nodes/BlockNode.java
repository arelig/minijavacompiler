package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.data_structures.Parameter;
import minijavacompiler.data_structures.SymbolTable;
import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockNode extends SentenceNode {
    private final HashMap<String, LocalVarNode> localVarMap;
    private final List<SentenceNode> sentenceNodeList;
    private List<Parameter> unitParams;
    private BlockNode outerBlock;

    public BlockNode(Token token) {
        super(token);
        unitParams = new ArrayList<>();
        localVarMap = new HashMap<>();
        sentenceNodeList = new ArrayList<>();
    }

    public BlockNode getOuterBlock() {
        return outerBlock;
    }

    public void validate() throws SemanticException {
        outerBlock = SymbolTable.getInstance().getCurrentBlock();

        SymbolTable.getInstance().setCurrentBlock(this);
        for (SentenceNode sentence : sentenceNodeList) {
            sentence.validate();
        }
        SymbolTable.getInstance().setCurrentBlock(outerBlock);
    }


    @Override
    public void generateCode() {

    }

    public boolean containsUnitParam(String id) {
        boolean contains = false;
        for (Parameter parameter : unitParams) {
            if (parameter.getId().equals(id)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    public boolean containsOuterLocalVar(String id) {
        boolean contains = false;
        if (outerBlock != null) {
            contains = outerBlock.containsLocalVar(id);
        }
        return contains;
    }

    public boolean containsLocalVar(String id) {
        return localVarMap.containsKey(id);
    }

    public boolean isReachable(String id) {
        return containsLocalVar(id) || containsOuterLocalVar(id);
    }

    public Type getLocalVarType(String id) {
        Type type = null;
        if (containsLocalVar(id)) {
            type = localVarMap.get(id).getType();
        } else if (containsOuterLocalVar(id)) {
            type = outerBlock.getLocalVar(id).getType();
        }
        return type;
    }

    public void setUnitParams(List<Parameter> parameterList) {
        unitParams = parameterList;
    }

    public void addSentence(SentenceNode sentenceNode) {
        sentenceNodeList.add(sentenceNode);
    }

    public void addLocalVar(LocalVarNode localVarNode) {
        localVarMap.put(localVarNode.getId(), localVarNode);
    }

    public LocalVarNode getLocalVar(String id) {
        return localVarMap.get(id);
    }
}
