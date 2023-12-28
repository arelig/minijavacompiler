package minijavacompiler.symbol_table.entities;


import minijavacompiler.lexical_parser.Token;

public class MiniObject extends Class {

    public MiniObject(Token token) {
        super(token);
        setConsolidated();
    }


    public static void debugPrint(int i) {
        System.out.println(i);
    }


}
