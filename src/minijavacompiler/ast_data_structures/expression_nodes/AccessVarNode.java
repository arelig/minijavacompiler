package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.SymbolTable;
import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class AccessVarNode extends AccessNode {


    public AccessVarNode(Token token) {
        super(token);
    }

    @Override
    public Type validate() throws SemanticException {
        Type varType = null;
        //resolver nombre.
        //1. ver si es var local, preguntar a la unidad si existe dicha variable
        //2. ver si es param

        if (SymbolTable.getInstance().getCurrentUnit().isParamDeclared(getId())) {
            varType = SymbolTable.getInstance().getCurrentUnit().getParamsMap().get(getId()).getType();
        } else if (SymbolTable.getInstance().getCurrentClass().isAttrDeclared(getId())) {
            varType = SymbolTable.getInstance().getCurrentClass().getAttrsMap().get(getId()).getType();
        } else {
            throw new SemanticException(getId(), getLine(), "la variable no fue declarada en el contexto o es inalcanzable");
        }
        return varType;

    }


}
