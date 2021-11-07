package minijavacompiler.data_structures;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class Attribute extends Entity {
    private final String visibility;
    private final Type type;

    public Attribute(String visibility, Type type, Token token) {
        setId(token.getLexeme());
        setLine(token.getLine());
        this.visibility = visibility;
        this.type = type;
    }

    public String getVisibility() {
        return visibility;
    }

    public Type getType() {
        return type;
    }

    public void check() throws SemanticException {
        type.check();
    }

    public boolean equals(Attribute attr) {
        return visibility.equals(attr.getVisibility()) &&
                type.equals(attr.getType());
    }

}
