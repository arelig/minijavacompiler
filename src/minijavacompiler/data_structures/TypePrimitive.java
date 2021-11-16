package minijavacompiler.data_structures;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class TypePrimitive extends Type {

    public TypePrimitive(Token token) {
        setId(token.getLexeme());
        setLine(token.getLine());
    }

    @Override
    public void check() throws SemanticException {
    }

    @Override
    public boolean conform(Type type) {
        return equals(type);
    }

    @Override
    public boolean equals(Type type) {
        return getId().equals(type.getId());
    }
}
