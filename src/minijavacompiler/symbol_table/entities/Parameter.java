package minijavacompiler.symbol_table.entities;

import minijavacompiler.lexical_parser.Token;
import minijavacompiler.symbol_table.SymbolTable;
import minijavacompiler.symbol_table.types.Type;

public class Parameter extends Entity {
    private final Type type;
    private boolean errorDetected;
    private final int offset;


    public Parameter(Type type, Token token) {
        setId(token.getLexeme());
        setLine(token.getLine());
        this.type = type;
        this.offset = -1;
        errorDetected = false;
    }
    public boolean checkDeclaration() {
        if(type.checkDeclaration()){
            return true;
        }else{
            setErrorDetected();
            return false;
        }
    }

    public Type getType() {
        return type;
    }

    public boolean equals(Parameter p) {
        return type.equals(p.getType());
    }

    public boolean errorDetected() {
        return errorDetected;
    }

    public void setErrorDetected() {
        errorDetected = true;
	}

    public int getOffset() {
        return offset + 4;
//        return offset + (SymbolTable.getInstance().getCurrentUnit().getReturnType().equals("static") ? 3 : 4);
    }
}
