package minijavacompiler.symbol_table.entities;

import minijavacompiler.lexical_parser.Token;

import java.io.IOException;

public class MiniSystem extends Class {

    public MiniSystem(Token token) {
        super(token);
        setConsolidated();
    }

    static int read() throws IOException {
        return System.in.read();
    }

    static void printB(boolean b) {
        System.out.print(b);
    }

    static void printC(char c) {
        System.out.print(c);
    }

    static void printS(String s) {
        System.out.print(s);
    }

    static void printI(int i) {
        System.out.println(i);
    }

    static void println() {
        System.out.print("\n");
    }

    static void printBln(boolean b) {
        System.out.println(b);
    }

    static void printCln(char c) {
        System.out.println(c);
    }

    public static void printSln(String s) {
        System.out.println(s);
    }

    public static void printIln(int i) {
        System.out.println(i);
    }


}
