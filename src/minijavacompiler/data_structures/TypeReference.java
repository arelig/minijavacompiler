package minijavacompiler.data_structures;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class TypeReference extends Type {
    private final Token tknRef;

    public TypeReference(Token token) {
        setId(token.getLexeme());
        setLine(token.getLine());
        tknRef = token;
    }

    @Override
    public void check() throws SemanticException {
        if (!(SymbolTable.getInstance().isClassDeclared(tknRef.getLexeme()))) {
            throw new SemanticException(tknRef.getLexeme(),
                    tknRef.getLine(),
                    "El tipo " +
                            tknRef.getLexeme() +
                            "no puede ser referenciado");
        }
    }

    @Override
    public boolean conform(Type type) throws SemanticException {
        boolean conforma = false;

        if (type instanceof TypeReference) {
            conforma = SymbolTable.getInstance().classConform(getId(), type.getId());
        } else {
            throw new SemanticException(getId(), getLine(), "Conformacion de tipos incompatible");
        }
        return conforma;
    }

    @Override
    public boolean equals(Type type) {
        return getId().equals(type.getId());
    }
}
