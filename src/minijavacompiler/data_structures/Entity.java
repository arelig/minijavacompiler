package minijavacompiler.data_structures;

import minijavacompiler.exceptions.SemanticException;

public abstract class Entity {

    private String id;
    private int line;

    public Entity() {
    }

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

    public abstract void check() throws SemanticException;

}
