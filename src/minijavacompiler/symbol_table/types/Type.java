package minijavacompiler.symbol_table.types;

import minijavacompiler.symbol_table.entities.Entity;

public abstract class Type extends Entity {

    public abstract boolean checkDeclaration();
    public abstract boolean conform(Type type);
    public abstract boolean isReference();
    public abstract boolean isPrimitive();
    public abstract boolean equals(Type a);
}
