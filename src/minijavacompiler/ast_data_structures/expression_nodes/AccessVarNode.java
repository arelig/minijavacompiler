package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.SymbolTable;
import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class AccessVarNode extends AccessNode {


    public AccessVarNode(Token token) {
        super(token);
    }

    private Type getType(Type toCheck) throws SemanticException {
        Type returnType;

        if (getChainedNode() != null) {
            returnType = getChainedNode().validate(toCheck);
        } else {
            returnType = toCheck;
        }

        return returnType;
    }

    @Override
    public Type validate() throws SemanticException {
        return getType(getVarType());

    }

    private Type getVarType() throws SemanticException {
        Type varType;
        boolean isDeclaredInBlock = false;
        boolean isDeclaredInParam = false;
        boolean isDeclaredInClass = false;

        isDeclaredInBlock = SymbolTable.getInstance().getCurrentBlock().isReachable(getId());
        isDeclaredInParam = SymbolTable.getInstance().getCurrentUnit().isParamDeclared(getId());
        isDeclaredInClass = SymbolTable.getInstance().getCurrentClass().isAttrDeclared(getId());

        if (isDeclaredInBlock) {
            varType = SymbolTable.getInstance().getCurrentBlock().getLocalVarType(getId());
        } else if (isDeclaredInParam) {
            varType = SymbolTable.getInstance().getCurrentUnit().getParamsMap().get(getId()).getType();
        } else if (isDeclaredInClass) {
            varType = SymbolTable.getInstance().getCurrentClass().getAttrsMap().get(getId()).getType();
        } else {
            throw new SemanticException(getId(), getLine(), "la variable no fue declarada en el contexto o es inalcanzable");
        }
        return varType;
    }

}
