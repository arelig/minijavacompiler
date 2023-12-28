package minijavacompiler.symbol_table.types;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class TypeNull extends Type {

    public TypeNull(Token tkn) {
        setId(tkn.getLexeme());
        setLine(tkn.getLine());
    }

    @Override
    public boolean checkDeclaration() {
        return true;
    }

    @Override
    public boolean conform(Type type) {
        return type instanceof TypeReference;
    }

    @Override
    public boolean isReference() {
        return false;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean equals(Type a) {
        return getId().equals(a.getId());
    }
}
