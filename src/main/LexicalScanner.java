package main;

public class LexicalScanner {
    static final char ENTER = '\n';
    static final int EOF = -1;
    static final int TAB = 9;
    static final int EXCLAMATION_MARK = 33;
    static final int QUOTATION_MARK = 34;
    static final int PERCENT = 37;
    static final int AMPERSAND = 38;
    static final int APOSTROPHE = 39;
    static final int PARENTHESES_OPEN = 40;
    static final int PARENTHESES_CLOSE = 41;
    static final int ASTERISK = 42;
    static final int ADD = 43;
    static final int COMMA = 44;
    static final int SUB = 45;
    static final int DOT = 46;
    static final int SLASH = 47;
    static final int SEMICOLON = 59;
    static final int LESS = 60;
    static final int EQUALS = 61;
    static final int GREATER = 62;
    static final int BACKSLASH_REVERSE = 92;
    static final int UNDERSCORE = 95;
    static final int BRACE_OPEN = 123;
    static final int VERTICAL_SLASH = 124;
    static final int BRACE_CLOSE = 125;

    private final FileManager fileManager;
    private final Keywords keywords;

    private String lexeme;
    private int currentChar;
    private boolean flagEOF;

    public LexicalScanner(FileManager fileManager) {
        this.fileManager = fileManager;
        keywords = new Keywords();
        flagEOF = false;
    }

    public void validate() {
        updateCurrentChar();
        if (currentChar == EOF) {
            flagEOF = true;
        }
    }


    public boolean isEOF() {
        return flagEOF;
    }

    public Token nextToken() throws LexicalException {
        return s0();
    }

    private void updateLexeme() {
        lexeme = lexeme + ((char) currentChar);
    }

    private void updateCurrentChar() {
        currentChar = fileManager.nextCharacter();
    }

    private Token createTokenFor(TokenType tokenType) {
        if (keywords.isKeyword(lexeme)) {
            tokenType = keywords.getTokenType(lexeme);
        }
        return new Token(tokenType, lexeme, fileManager.line());
    }

    private LexicalException createLexicalException(String description) {
        return new LexicalException(description,
                fileManager.currentLine(),
                lexeme,
                fileManager.line(),
                fileManager.column());
    }

    private Token sEOF() {
        flagEOF = true;
        return createTokenFor(TokenType.EOF);
    }

    private Token s0() throws LexicalException {
        lexeme = "";

        if (currentChar == EOF) {
            return sEOF();
        } else if (Character.isWhitespace(currentChar)) {
            updateCurrentChar();
            return s0();
        } else if (currentChar == TAB) {
            updateCurrentChar();
            return s0();
        } else if (Character.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return s1();
        } else if (Character.isUpperCase(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return s2();
        } else if (Character.isLowerCase(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return s3();
        } else if (currentChar == APOSTROPHE) {
            updateLexeme();
            updateCurrentChar();
            return s4();
        } else if (currentChar == QUOTATION_MARK) {
            updateLexeme();
            updateCurrentChar();
            return s8();
        } else if (currentChar == BRACE_OPEN) {
            updateLexeme();
            updateCurrentChar();
            return s11();
        } else if (currentChar == BRACE_CLOSE) {
            updateLexeme();
            updateCurrentChar();
            return s12();
        } else if (currentChar == PARENTHESES_OPEN) {
            updateLexeme();
            updateCurrentChar();
            return s14();
        } else if (currentChar == PARENTHESES_CLOSE) {
            updateLexeme();
            updateCurrentChar();
            return s15();
        } else if (currentChar == COMMA) {
            updateLexeme();
            updateCurrentChar();
            return s16();
        } else if (currentChar == SEMICOLON) {
            updateLexeme();
            updateCurrentChar();
            return s17();
        } else if (currentChar == DOT) {
            updateLexeme();
            updateCurrentChar();
            return s18();
        } else if (currentChar == GREATER) {
            updateLexeme();
            updateCurrentChar();
            return s19();
        } else if (currentChar == LESS) {
            updateLexeme();
            updateCurrentChar();
            return s21();
        } else if (currentChar == EXCLAMATION_MARK) {
            updateLexeme();
            updateCurrentChar();
            return s23();
        } else if (currentChar == EQUALS) {
            updateLexeme();
            updateCurrentChar();
            return s25();
        } else if (currentChar == ADD) {
            updateLexeme();
            updateCurrentChar();
            return s27();
        } else if (currentChar == SUB) {
            updateLexeme();
            updateCurrentChar();
            return s29();
        } else if (currentChar == ASTERISK) {
            updateLexeme();
            updateCurrentChar();
            return s31();
        } else if (currentChar == SLASH) {
            updateLexeme();
            updateCurrentChar();
            return s32();
        } else if (currentChar == AMPERSAND) {
            updateLexeme();
            updateCurrentChar();
            return s37();
        } else if (currentChar == VERTICAL_SLASH) {
            updateLexeme();
            updateCurrentChar();
            return s39();
        } else if (currentChar == PERCENT) {
            updateLexeme();
            updateCurrentChar();
            return s41();
        }

        updateLexeme();
        updateCurrentChar();
        throw createLexicalException("Invalid symbol");
    }

    private Token s1() throws LexicalException {
        if (Character.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return s1();
        }
        if (lexeme.length() > 9) {
            throw createLexicalException("Integer too long");
        }
        return createTokenFor(TokenType.PR_INT);
    }

    private Token s2() {
        if (Character.isUpperCase(currentChar)
                || Character.isLowerCase(currentChar)
                || Character.isDigit(currentChar)
                || currentChar == UNDERSCORE) {
            updateLexeme();
            updateCurrentChar();
            return s2();
        } else {
            return createTokenFor(TokenType.ID_CLASS);
        }
    }

    private Token s3() {
        if (Character.isUpperCase(currentChar)
                || Character.isLowerCase(currentChar)
                || Character.isDigit(currentChar)
                || currentChar == UNDERSCORE) {
            updateLexeme();
            updateCurrentChar();
            return s3();
        } else {
            return createTokenFor(TokenType.ID_MET_VAR);
        }
    }

    /*
        @todo revisar algoritmo
     */

    private Token s4() throws LexicalException {
        if (currentChar == BACKSLASH_REVERSE) {
            updateLexeme();
            updateCurrentChar();
            return s6();
        } else if (Character.isLetter(currentChar) ||
                Character.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return s5();
        } else if (currentChar == QUOTATION_MARK) {
            updateCurrentChar();
            updateLexeme();
            return s13();
        }
        throw createLexicalException("Invalid character literal");
    }

    private Token s13() {
        return createTokenFor(TokenType.PR_CHAR);
    }


    private Token s5() throws LexicalException {
        if (currentChar == EOF ||
                currentChar == ENTER ||
                currentChar != APOSTROPHE) {
            throw createLexicalException("Unclosed literal char");
        }
        updateLexeme();
        updateCurrentChar();
        return s7();
    }

    private Token s6() throws LexicalException {
        if (currentChar == ENTER ||
                currentChar == EOF) {
            throw createLexicalException("Unclosed character literal");
        }

        updateLexeme();
        updateCurrentChar();
        return s5();
    }

    private Token s7() {
        //@todo limpiar el caracter de las comillas?
        return createTokenFor(TokenType.PR_CHAR);
    }

    private Token s8() throws LexicalException {
        if (currentChar == BACKSLASH_REVERSE) {
            updateLexeme();
            updateCurrentChar();
            return s9();
        } else if (currentChar == QUOTATION_MARK) {
            updateLexeme();
            updateCurrentChar();
            return s10();
        } else if (currentChar == ENTER ||
                currentChar == EOF) {
            throw createLexicalException("Unclosed string literal ");
        }

        updateLexeme();
        updateCurrentChar();
        return s8();
    }

    private Token s9() throws LexicalException {
        if (currentChar == ENTER ||
                currentChar == EOF) {
            throw createLexicalException("Unclosed string literal");
        }

        updateLexeme();
        updateCurrentChar();
        return s8();
    }

    private Token s10() {
        return createTokenFor(TokenType.PR_STRING);
    }

    private Token s11() {
        return createTokenFor(TokenType.BRACES_OPEN);
    }

    private Token s12() {
        return createTokenFor(TokenType.BRACES_CLOSE);
    }

    private Token s14() {
        return createTokenFor(TokenType.PARENTHESES_OPEN);
    }

    private Token s15() {
        return createTokenFor(TokenType.PARENTHESES_CLOSE);
    }

    private Token s16() {
        return createTokenFor(TokenType.COMMA);
    }

    private Token s17() {
        return createTokenFor(TokenType.SEMICOLON);
    }

    private Token s18() {
        return createTokenFor(TokenType.DOT);
    }

    private Token s19() {
        if (currentChar == EQUALS) {
            updateLexeme();
            updateCurrentChar();
            return s20();
        }

        return createTokenFor(TokenType.GREATER);
    }

    private Token s20() {
        return createTokenFor(TokenType.GREATER_EQUALS);
    }

    private Token s21() {
        if (currentChar == EQUALS) {
            return s22();
        }

        return createTokenFor(TokenType.LESS);
    }

    private Token s22() {
        return createTokenFor(TokenType.LESS_EQUALS);
    }

    private Token s23() {
        if (currentChar == EQUALS) {
            return s24();
        }

        return createTokenFor(TokenType.OP_NOT);
    }

    private Token s24() {
        return createTokenFor(TokenType.NOT_EQUALS);
    }

    private Token s25() {
        if (currentChar == EQUALS) {
            return s26();
        }

        return createTokenFor(TokenType.ASSIGN);
    }

    private Token s26() {
        return createTokenFor(TokenType.EQUALS);
    }

    private Token s27() {
        if (currentChar == ADD) {
            return s28();
        }

        return createTokenFor(TokenType.ADD);
    }

    private Token s28() {
        return createTokenFor(TokenType.ASSIGN_ADD);
    }

    private Token s29() {
        if (currentChar == SUB) {
            return s30();
        }

        return createTokenFor(TokenType.SUB);
    }

    private Token s30() {
        return createTokenFor(TokenType.ASSIGN_SUB);
    }

    private Token s31() {
        return createTokenFor(TokenType.MULTIPLY);
    }

    private Token s32() throws LexicalException {
        if (currentChar == SLASH) {
            return s33();
        } else if (currentChar == ASTERISK) {
            return s34();
        }

        return createTokenFor(TokenType.DIVIDE);
    }

    private Token s33() throws LexicalException {
        if (currentChar == ENTER || currentChar == EOF) {
            updateCurrentChar();
            return s0();
        }

        updateCurrentChar();
        return s33();
    }

    private Token s34() throws LexicalException {
        if (currentChar == EOF) {
            throw createLexicalException("comentario sin cerrar");
        } else if (currentChar == ASTERISK) {
            updateCurrentChar();
            return s35();
        }
        updateCurrentChar();
        return s34();
    }

    private Token s35() throws LexicalException {
        if (currentChar == EOF) {
            throw createLexicalException("comentario sin cerrar");
        } else if (currentChar == SLASH) {
            updateCurrentChar();
            return s36();
        }

        updateCurrentChar();
        return s34();
    }

    private Token s36() throws LexicalException {
        updateCurrentChar();
        return s0();
    }

    private Token s37() throws LexicalException {
        if (currentChar == AMPERSAND) {
            updateLexeme();
            updateCurrentChar();
            return s38();
        }

        throw createLexicalException("Missing symbol");
    }

    private Token s38() {
        return createTokenFor(TokenType.OP_AND);
    }

    private Token s39() throws LexicalException {
        if (currentChar == VERTICAL_SLASH) {
            updateLexeme();
            updateCurrentChar();
            return s40();
        }

        throw createLexicalException("Missing symbol");
    }

    private Token s40() {
        return createTokenFor(TokenType.OP_OR);
    }

    private Token s41() {
        return createTokenFor(TokenType.MOD);
    }

}






