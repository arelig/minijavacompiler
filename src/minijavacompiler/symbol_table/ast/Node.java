package minijavacompiler.symbol_table.ast;

import minijavacompiler.lexical_parser.Token;

public abstract class Node {
    private final Token token;
    public Node(Token tkn) {
        this.token = tkn;
    }

    public String getId() {
        return token.getLexeme();
    }

    public int getLine() {
        return token.getLine();
    }

    public abstract void generateCode();
}
