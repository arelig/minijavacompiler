package minijavacompiler.data_structures;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class TypeVoid extends Type {

    public TypeVoid(Token token) {
        setId(token.getLexeme());
        setLine(token.getLine());
    }

    @Override
    public void check() throws SemanticException {

    }

    //@todo con que conforma tipo void?
    //void no termina de ser un tipo, denota un metodo que no tiene tipo de retorno
    //o sea, false
    @Override
    public boolean conform(Type type) {
        return false;
    }

    @Override
    public boolean equals(Type type) {
        return getId().equals(type.getId());
    }
}
