package minijavacompiler.data_structures;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class TypeNull extends Type {

    public TypeNull(Token tkn) {
        setId(tkn.getLexeme());
        setLine(tkn.getLine());
    }

    @Override
    public void check() throws SemanticException {

    }

    @Override
    public boolean conform(Type type) {
        return false;
    }

    @Override
    public boolean equals(Type a) {
        return getId().equals(a.getId());
    }
}
