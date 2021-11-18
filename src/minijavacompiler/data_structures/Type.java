package minijavacompiler.data_structures;

import minijavacompiler.exceptions.SemanticException;

public abstract class Type extends Entity {

    public abstract boolean conform(Type type) throws SemanticException;

    //public abstract Token getTkn() ;
    public abstract boolean equals(Type a);
}
