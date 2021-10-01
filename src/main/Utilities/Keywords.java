package main.Utilities;

import java.util.HashMap;
import java.util.Map;

public class Keywords {
    private final Map<String, TypeToken> map;

    public Keywords(){
        map = new HashMap<>();

        fillMapWithKeywords();
    }

    private void fillMapWithKeywords() {
        map.put("class", TypeToken.CLASS);
        map.put("extends", TypeToken.EXTENDS);
        map.put("static", TypeToken.STATIC);
        map.put("dynamic", TypeToken.DYNAMIC);
        map.put("void", TypeToken.VOID);
        map.put("boolean", TypeToken.PR_BOOLEAN);
        map.put("char", TypeToken.PR_CHAR);
        map.put("int", TypeToken.PR_INT);
        map.put("String", TypeToken.PR_STRING);
        map.put("public", TypeToken.PUBLIC);
        map.put("private", TypeToken.PRIVATE);
        map.put("if", TypeToken.IF);
        map.put("else", TypeToken.ELSE);
        map.put("for", TypeToken.FOR);
        map.put("return", TypeToken.RETURN);
        map.put("this", TypeToken.THIS);
        map.put("new", TypeToken.NEW);
        map.put("null", TypeToken.NULL);
        map.put("true", TypeToken.TRUE);
        map.put("false", TypeToken.FALSE);
    }

    public boolean isKeyword(String lexeme){
        return map.get(lexeme) != null;
    }

    public TypeToken getTokenType(String lexeme) {
        return map.get(lexeme);
    }

}
