package minijavacompiler.symbol_table.entities;

import minijavacompiler.exceptions.SemanticException;

public abstract class Entity {

    private String id;
    private int line;

    public String getId() {
        return id;
    }

    public void setId(String name) {
        this.id = name;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }


}
