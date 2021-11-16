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
    public boolean conform(Type type) {
        //si el this conforma con el tipo que te estan pasando
        //o es el mismo tipo o hereda de ese tipo.
        if (type instanceof TypeReference) {
            Class claseAConformar = SymbolTable.getInstance().getClassById(type.getId());
            Class claseConformante = SymbolTable.getInstance().getClassById(getId());

            //primero, ver si clase a conformar es la misma clase que clase conformante
            //sino, si la clase conformante es una decendiente a conformar.

            return claseAConformar.equals(claseConformante);
        }
        return false;
    }

    @Override
    public boolean equals(Type type) {
        return getId().equals(type.getId());
    }
}
