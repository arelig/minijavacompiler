package minijavacompiler.ast_data_structures;

import minijavacompiler.lexical_parser.Token;

public abstract class Node {

    private Token token;

    public Node(Token tkn) {
        this.token = tkn;
    }

    public String getId() {
        return token.getLexeme();
    }

    public int getLine() {
        return token.getLine();
    }

    public Token getTkn() {
        return token;
    }

    public void setTkn(Token token) {
        this.token = token;
    }

    public abstract void generateCode();
}
