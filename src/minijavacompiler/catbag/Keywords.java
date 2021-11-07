package minijavacompiler.catbag;

import minijavacompiler.lexical_parser.TokenType;

import java.util.HashMap;
import java.util.Map;

public class Keywords {
    private final Map<String, TokenType> map;

    public Keywords() {
        map = new HashMap<>();

        fillMapWithKeywords();
    }

    private void fillMapWithKeywords() {
        map.put("class", TokenType.CLASS);
        map.put("extends", TokenType.EXTENDS);
        map.put("static", TokenType.STATIC);
        map.put("dynamic", TokenType.DYNAMIC);
        map.put("void", TokenType.VOID);
        map.put("boolean", TokenType.PR_BOOLEAN);
        map.put("char", TokenType.PR_CHAR);
        map.put("int", TokenType.PR_INT);
        map.put("String", TokenType.PR_STRING);
        map.put("public", TokenType.PUBLIC);
        map.put("private", TokenType.PRIVATE);
        map.put("if", TokenType.IF);
        map.put("else", TokenType.ELSE);
        map.put("for", TokenType.FOR);
        map.put("return", TokenType.RETURN);
        map.put("this", TokenType.THIS);
        map.put("new", TokenType.NEW);
        map.put("null", TokenType.NULL);
        map.put("true", TokenType.TRUE);
        map.put("false", TokenType.FALSE);
    }

    public boolean isKeyword(String lexeme) {
        return map.get(lexeme) != null;
    }

    public TokenType getTokenType(String lexeme) {
        return map.get(lexeme);
    }

}
