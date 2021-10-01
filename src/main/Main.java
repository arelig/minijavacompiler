package main;

import main.SyntaxParser.SyntaxModule;

public class Main {
    public static void main(String[] args) {
        //Testing para personas que no tienen ganas de testear
        //LexicalModule m = new LexicalModule("D:\\UNS\\En curso\\4-Compiladores\\Etapa 1\\etapa1_iglesias\\src\\main\\test.txt");
        //LexicalModule m = new LexicalModule(args[0]);
        //SyntaxModule m = new SyntaxModule("D:\\UNS\\En curso\\4-Compiladores\\Etapa 1\\etapa1_iglesias\\src\\main\\test.txt");

        SyntaxModule m = new SyntaxModule(args[0]);
    }
}
