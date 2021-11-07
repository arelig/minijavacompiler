package minijavacompiler.data_structures;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class Parameter extends Entity {
    private final Type type;

    public Parameter(Type type, Token token) {
        setId(token.getLexeme());
        setLine(token.getLine());
        this.type = type;
    }

    @Override
    public void check() throws SemanticException {
        type.check();
    }

    public Type getType() {
        return type;
    }

    public boolean equals(Parameter p) {
        return type.equals(p.getType());
    }

}
