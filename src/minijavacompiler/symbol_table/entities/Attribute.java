package minijavacompiler.symbol_table.entities;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.symbol_table.types.Type;

public class Attribute extends Entity {
    private final String visibility;
    private final Type type;
    private boolean errorDetected;
    private int offset;

    public Attribute(String visibility, Type type, Token token) {
        setId(token.getLexeme());
        setLine(token.getLine());

        this.visibility = visibility;
        this.type = type;

        errorDetected = false;
    }

    public void setErrorDetected(){
        errorDetected = true;
    }
    public void checkDeclaration() {
        if(!errorDetected){
            if(!type.checkDeclaration()){
                setErrorDetected();
            }
        }
    }

    public boolean errorDetected(){
        return errorDetected;
    }

    public int getOffset() {
        return offset + 1;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getVisibility() {
        return visibility;
    }

    public Type getType() {
        return type;
    }

    public boolean equals(Attribute attr) {
        return visibility.equals(attr.getVisibility()) &&
                type.equals(attr.getType());
    }

}
