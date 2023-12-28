package minijavacompiler.symbol_table.types;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class TypeVoid extends Type {

    public TypeVoid(Token token) {
        setId(token.getLexeme());
        setLine(token.getLine());
    }

    @Override
    public boolean checkDeclaration()  {
        return true;
    }
    @Override
    public boolean conform(Type type) {
        return false;
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
    public boolean equals(Type type) {
        return getId().equals(type.getId());
    }
}
