package main.Utilities;

public enum TypeToken {
    //Palabras reservas/clave
    ID_MET_VAR,
    ID_CLASS,
    CLASS,
    EXTENDS,
    STATIC,
    DYNAMIC,
    VOID,
    PUBLIC,
    PRIVATE,
    IF,
    ELSE,
    FOR,
    RETURN,
    THIS,
    NEW,
    PR_INT,
    PR_CHAR,
    PR_BOOLEAN,
    PR_STRING,
    LIT_INT,
    LIT_CHAR,
    LIT_STRING,
    TRUE,
    FALSE,
    NULL,

    //Simbolos de puntuacion
    BRACES_OPEN,
    BRACES_CLOSE,
    PARENTHESES_OPEN,
    PARENTHESES_CLOSE,
    COMMA,
    SEMICOLON,
    DOT,

    //Operadores
    GREATER,
    LESS,
    OP_NOT,
    EQUALS,
    GREATER_EQUALS,
    LESS_EQUALS,
    NOT_EQUALS,
    ADD,
    SUB,
    MULTIPLY,
    DIVIDE,
    OP_AND,
    OP_OR,
    MOD,

    //Asignacion
    ASSIGN,
    ASSIGN_ADD,
    ASSIGN_SUB,

    EOF
}
