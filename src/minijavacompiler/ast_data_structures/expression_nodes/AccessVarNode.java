package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.SymbolTable;
import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class AccessVarNode extends AccessNode {
    //todos los accesos deberia tener un posible encadenado
    //si tengo encadenado le pido que se chequee

    // a mi acceso aregar un metodo es asignable o es llamable, cada no do lo define si es el ultimo del encadenado no
    //var es asignable si no tiene encadenado
    //si tiene ecadenado, le pregunta si es asignable
    //o sea triggerea la validacion y la asignacion


    public AccessVarNode(Token token) {
        super(token);
    }

    @Override
    public Type validate() throws SemanticException {
        Type varType = null;

        boolean isDeclaredInBlock = SymbolTable.getInstance().getCurrentBlock().containsLocalVar(getId());
        boolean isDeclaredInParams = SymbolTable.getInstance().getCurrentUnit().isParamDeclared(getId());

        //resolver nombre.
        //1. ver si es var local, preguntar a la unidad si existe dicha variable
        //2. ver si es param

        if (isDeclaredInParams) {
            varType = SymbolTable.getInstance().getCurrentUnit().getParamsMap().get(getId()).getType();
        } else if (SymbolTable.getInstance().getCurrentClass().isAttrDeclared(getId())) {
            varType = SymbolTable.getInstance().getCurrentClass().getAttrsMap().get(getId()).getType();
        } else {
            throw new SemanticException(getId(), getLine(), "la variable no fue declarada en el contexto o es inalcanzable");
        }
        return varType;

    }


}
