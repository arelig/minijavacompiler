package minijavacompiler.symbol_table.types;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.symbol_table.SymbolTable;
import minijavacompiler.symbol_table.entities.Class;

import java.util.Set;

public class TypeReference extends Type {
    private final Token reference;

    public TypeReference(Token token) {
        setId(token.getLexeme());
        setLine(token.getLine());
        reference = token;
    }

    public boolean checkDeclaration() {
        boolean isReferenceDeclared = SymbolTable.getInstance().isClassDeclared(reference.getLexeme());

        if (!isReferenceDeclared) {
            SymbolTable.getInstance().addError(new SemanticException(
                    reference.getLexeme(),
                    reference.getLine(),
                    "El tipo " +
                            reference.getLexeme() +
                            " no puede ser referenciado."));
        }

        return isReferenceDeclared;
    }

    @Override
    public boolean conform(Type type) {
        boolean conforms = false;
        if (type.isReference()) {
            conforms = classConform(reference.getId(), type.getId());
        } else {
            SymbolTable.getInstance().addError(new SemanticException(
                    getId(),
                    getLine(),
                    "Conformacion de tipos incompatible"));
        }
        return conforms;
    }

    @Override
    public boolean isReference() {
        return true;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    private boolean classConform(String conforms, String conformant){
        if (conforms.equals(conformant)) {
            return true;
        }

        Class classToConform = SymbolTable.getInstance().getClass(conforms);
        Class conformantClass = SymbolTable.getInstance().getClass(conformant);

        return searchForInheritance(classToConform, conformantClass);
    }

        private boolean searchForInheritance(Class classToConform, Class conformantClass) {
            Set<String> ancestors = classToConform.getAncestors();

            // Comprueba si claseConformante está entre los ancestros de claseAConformar
            return ancestors.contains(conformantClass.getId());
        }

    @Override
    public boolean equals(Type type) {
        return getId().equals(type.getId());
    }

}
