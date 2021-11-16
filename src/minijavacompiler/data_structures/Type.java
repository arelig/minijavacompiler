package minijavacompiler.data_structures;

public abstract class Type extends Entity {

    public abstract boolean conform(Type type);

    public abstract boolean equals(Type a);
}
