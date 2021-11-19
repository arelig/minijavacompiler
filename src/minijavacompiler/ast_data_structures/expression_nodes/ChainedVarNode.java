package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.SymbolTable;
import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class ChainedVarNode extends ChainedNode {
    public ChainedVarNode(Token tkn) {
        super(tkn);
    }

    @Override
    public Type validate(Type toCheck) throws SemanticException {
        Type toReturn;
        Type varType = getVarType();
        if (getRightChainedNode() != null) {
            toReturn = getRightChainedNode().validate(varType);
        } else {
            toReturn = varType;
        }

        return toReturn;
    }

    private Type getVarType() throws SemanticException {
        Type varType;
        boolean isDeclaredInBlock = false;
        boolean isDeclaredInParam = false;
        boolean isDeclaredInClass = false;

        isDeclaredInBlock = SymbolTable.getInstance().getCurrentBlock().containsLocalVar(getAccessId().getLexeme());
        isDeclaredInParam = SymbolTable.getInstance().getCurrentUnit().isParamDeclared(getAccessId().getLexeme());
        isDeclaredInClass = SymbolTable.getInstance().getCurrentClass().isAttrDeclared(getAccessId().getLexeme());

        if (isDeclaredInBlock) {
            varType = SymbolTable.getInstance().getCurrentBlock().getLocalVar(getAccessId().getLexeme()).getType();
        } else if (isDeclaredInParam) {
            varType = SymbolTable.getInstance().getCurrentUnit().getParamsMap().get(getAccessId().getLexeme()).getType();
        } else if (isDeclaredInClass) {
            varType = SymbolTable.getInstance().getCurrentClass().getAttrsMap().get(getAccessId().getLexeme()).getType();
        } else {
            throw new SemanticException(getId(), getLine(), "la variable no fue declarada en el contexto o es inalcanzable");
        }
        return varType;
    }
}
