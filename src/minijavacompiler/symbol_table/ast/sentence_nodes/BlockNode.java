package minijavacompiler.symbol_table.ast.sentence_nodes;

import minijavacompiler.ST;
import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.symbol_table.entities.Parameter;
import minijavacompiler.symbol_table.SymbolTable;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BlockNode extends SentenceNode {
    private List<Parameter> unitParams;
    private final List<SentenceNode> sentenceList;
    private BlockNode outerBlock;
    private final HashMap<String, LocalVarNode> localVars;
    private int minVariableOffset;
    private int nextVariableOffset;

    public BlockNode(Token token) {
        super(token);

        outerBlock = null;
        sentenceList = new LinkedList<>();
        unitParams = new LinkedList<>();
        localVars = new HashMap<>();
    }

    public void checkSentences() throws SemanticException {
        checkMinOffset();
        checkInnerSentences();
    }

    private void checkInnerSentences() throws SemanticException {
        outerBlock = ST.symbolTable.getCurrentBlock();
        ST.symbolTable.setCurrentBlock(this);

        for (SentenceNode sentence : sentenceList) {
            sentence.checkSentences();
        }

        ST.symbolTable.setCurrentBlock(outerBlock);
    }

    private void checkMinOffset() {
        if(outerBlock == null){
            minVariableOffset = 0;
        }else{
            minVariableOffset = outerBlock.getNextVariableOffset();
        }
        nextVariableOffset = minVariableOffset;
    }

    @Override
    public void generateCode() {
        ST.symbolTable.setCurrentBlock(this);

        if(sentenceList.isEmpty()){
            CodeGenerator.generateCode("NOP ;empty block");
        }
        for(SentenceNode sentence : sentenceList){
            sentence.generateCode();
        }
        CodeGenerator.generateCode("FMEM " + (nextVariableOffset - minVariableOffset) + " ;free memory from local variables");

        ST.symbolTable.setCurrentBlock(outerBlock);
    }

    public int getNextVariableOffset(){
        return nextVariableOffset;
    }

    public void addLocalVar(LocalVarNode localVarNode) {
        localVarNode.setOffset(nextVariableOffset++);
        localVars.put(localVarNode.getId(), localVarNode);
    }

    public LocalVarNode getLocalVar(String id) {
        return localVars.get(id);
    }

    public boolean containsLocalVar(String id) {
        return localVars.containsKey(id);
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

    public BlockNode getOuterBlock() {
        return outerBlock;
    }

    public void setUnitParams(List<Parameter> parameterList) {
        unitParams = parameterList;
    }

    public void addSentence(SentenceNode sentenceNode) {
        sentenceList.add(sentenceNode);
    }

}
