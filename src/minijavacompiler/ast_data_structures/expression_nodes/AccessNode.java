package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.lexical_parser.Token;


public abstract class AccessNode extends OperandNode {
    private boolean isCallable;
    //agregar encadenado

    public AccessNode(Token token) {
        super(token);
    }


    //chequear una variable implica ver si esta en la tabla de parametros
    //o variables del metodo actual, o sino
    //si es una variable de instancia de la clase actual y
    //en caso de estar, devolver su tipo.


    @Override
    public void generateCode() {

    }

    public boolean isCallable() {
        return isCallable;
    }

    public void setCallable(boolean callable) {
        isCallable = callable;
    }
}
