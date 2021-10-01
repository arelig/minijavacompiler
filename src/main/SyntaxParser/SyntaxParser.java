package main.SyntaxParser;

import main.Exceptions.CompilerException;
import main.Exceptions.SyntaxException;
import main.LexicalParser.LexicalParser;
import main.Utilities.FileManager;
import main.Utilities.Token;
import main.Utilities.TypeToken;

import java.util.Arrays;
import java.util.List;

public class SyntaxParser {
    private final LexicalParser lexicalParser;
    private final FileManager fileManager;
    private Token currentToken;

    public SyntaxParser(LexicalParser lexicalParser, FileManager fileManager) {
        this.lexicalParser = lexicalParser;
        this.fileManager = fileManager;
    }

    private void match(TypeToken typeToken) throws CompilerException {
        if (typeToken == currentToken.getTokenType()) {
            currentToken = lexicalParser.nextToken();
        } else {
            throw createSyntaxException(currentToken, typeToken.name());
        }
    }

    private SyntaxException createSyntaxException(Token token, String expected) {
        return new SyntaxException(token, expected, fileManager.line());
    }

    private String listToString(List<TypeToken> list) {
        StringBuilder builder = new StringBuilder();

        for (TypeToken expected : list) {
            builder.append(expected);
            builder.append(" ");
        }
        return builder.toString();
    }

    private List<TypeToken> primerosMiembro() {
        return Arrays.asList(TypeToken.PUBLIC,
                TypeToken.PRIVATE,
                TypeToken.ID_CLASS,
                TypeToken.STATIC,
                TypeToken.DYNAMIC);
    }

    private List<TypeToken> primerosTipoPrimitivo() {
        return Arrays.asList(TypeToken.PR_BOOLEAN,
                TypeToken.PR_CHAR,
                TypeToken.PR_INT,
                TypeToken.PR_STRING);
    }

    private List<TypeToken> primerosTipo() {
        return Arrays.asList(
                TypeToken.PR_BOOLEAN,
                TypeToken.PR_CHAR,
                TypeToken.PR_INT,
                TypeToken.PR_STRING,
                TypeToken.ID_CLASS
        );
    }

    private List<TypeToken> primerosSentencia() {
        return Arrays.asList(
                TypeToken.SEMICOLON,
                TypeToken.PARENTHESES_OPEN,
                TypeToken.THIS,
                TypeToken.ID_MET_VAR,
                TypeToken.NEW,
                TypeToken.PR_BOOLEAN,
                TypeToken.PR_CHAR,
                TypeToken.PR_INT,
                TypeToken.PR_STRING,
                TypeToken.ID_CLASS,
                TypeToken.RETURN,
                TypeToken.IF,
                TypeToken.FOR,
                TypeToken.BRACES_OPEN);
    }

    private List<TypeToken> primerosOperando() {
        return Arrays.asList(TypeToken.NULL,
                TypeToken.TRUE,
                TypeToken.FALSE,
                TypeToken.LIT_INT,
                TypeToken.LIT_CHAR,
                TypeToken.LIT_STRING,
                TypeToken.PARENTHESES_OPEN,
                TypeToken.THIS,
                TypeToken.ID_MET_VAR,
                TypeToken.NEW);
    }

    private List<TypeToken> primerosOperadorUnario() {
        return Arrays.asList(
                TypeToken.ADD,
                TypeToken.SUB,
                TypeToken.OP_NOT
        );
    }

    private List<TypeToken> primerosOperadorBinario() {
        return Arrays.asList(TypeToken.OP_AND,
                TypeToken.OP_OR,
                TypeToken.EQUALS,
                TypeToken.NOT_EQUALS,
                TypeToken.GREATER_EQUALS,
                TypeToken.LESS_EQUALS,
                TypeToken.GREATER,
                TypeToken.LESS,
                TypeToken.ADD,
                TypeToken.SUB,
                TypeToken.MULTIPLY,
                TypeToken.DIVIDE,
                TypeToken.MOD);
    }

    private List<TypeToken> primerosExpresion() {
        return Arrays.asList(TypeToken.ADD,
                TypeToken.SUB,
                TypeToken.OP_NOT,
                TypeToken.NULL,
                TypeToken.TRUE,
                TypeToken.FALSE,
                TypeToken.LIT_INT,
                TypeToken.LIT_CHAR,
                TypeToken.LIT_STRING,
                TypeToken.PARENTHESES_OPEN,
                TypeToken.THIS,
                TypeToken.ID_MET_VAR,
                TypeToken.NEW);
    }

    private List<TypeToken> primerosExpresionUnaria() {
        return Arrays.asList(
                TypeToken.ADD,
                TypeToken.SUB,
                TypeToken.OP_NOT,
                TypeToken.NULL,
                TypeToken.TRUE,
                TypeToken.FALSE,
                TypeToken.LIT_INT,
                TypeToken.LIT_CHAR,
                TypeToken.LIT_STRING,
                TypeToken.PARENTHESES_OPEN,
                TypeToken.NEW,
                TypeToken.ID_MET_VAR,
                TypeToken.THIS
        );
    }

    private List<TypeToken> primerosLiteral() {
        return Arrays.asList(TypeToken.NULL,
                TypeToken.TRUE,
                TypeToken.FALSE,
                TypeToken.LIT_INT,
                TypeToken.LIT_CHAR,
                TypeToken.LIT_STRING);
    }

    private List<TypeToken> primerosAcceso() {
        return Arrays.asList(
                TypeToken.PARENTHESES_OPEN,
                TypeToken.THIS,
                TypeToken.ID_CLASS,
                TypeToken.ID_MET_VAR,
                TypeToken.NEW);
    }

    public void start() throws CompilerException {
        currentToken = lexicalParser.nextToken();
        inicial();
    }

    private void inicial() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.CLASS) {
            ListaClases();
        } else if (currentToken.getTokenType() == TypeToken.EOF) {
            match(TypeToken.EOF);
        } else {
            throw createSyntaxException(currentToken, "class, EOF");
        }
    }

    private void ListaClases() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.CLASS) {
            Clase();
            ListaClasesOVacio();
        } else {
            throw createSyntaxException(currentToken, "idClass");
        }
    }

    private void ListaClasesOVacio() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.CLASS) {
            match(TypeToken.CLASS);
            ListaClases();
        } else {
        }
    }

    private void Clase() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.CLASS) {
            match(TypeToken.CLASS);
            match(TypeToken.ID_CLASS);
            Herencia();
            match(TypeToken.BRACES_OPEN);
            ListaMiembros();
            match(TypeToken.BRACES_CLOSE);
        } else {
            throw createSyntaxException(currentToken, "class, idClass");
        }
    }

    private void Herencia() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.EXTENDS) {
            match(TypeToken.EXTENDS);
            match(TypeToken.ID_CLASS);
        } else {
        }
    }

    private void ListaMiembros() throws CompilerException {
        if (primerosMiembro().contains(currentToken.getTokenType())) {
            Miembro();
            ListaMiembros();
        } else {
        }
    }

    private void Miembro() throws CompilerException {
        if (Arrays.asList(TypeToken.PUBLIC, TypeToken.PRIVATE).
                contains(currentToken.getTokenType())) {
            Atributo();
        } else if (currentToken.getTokenType() == TypeToken.ID_CLASS) {
            Constructor();
        } else if (Arrays.asList(TypeToken.STATIC, TypeToken.DYNAMIC).
                contains(currentToken.getTokenType())) {
            Metodo();
        } else {
            throw createSyntaxException(currentToken, "public, private, idClass, static, dynamic");
        }
    }

    private void Atributo() throws CompilerException {
        if (Arrays.asList(TypeToken.PUBLIC, TypeToken.PRIVATE).
                contains(currentToken.getTokenType())) {
            Visibilidad();
            Tipo();
            ListaDeclAtrs();
            match(TypeToken.SEMICOLON);
        } else {
            throw createSyntaxException(currentToken, "public, private");
        }
    }

    private void Metodo() throws CompilerException {
        if (Arrays.asList(TypeToken.STATIC, TypeToken.DYNAMIC).
                contains(currentToken.getTokenType())) {
            FormaMetodo();
            TipoMetodo();
            match(TypeToken.ID_MET_VAR);
            ArgsFormales();
            Bloque();
        } else {
            throw createSyntaxException(currentToken, "static, dynamic");
        }
    }

    private void Constructor() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.ID_CLASS) {
            match(TypeToken.ID_CLASS);
            ArgsFormales();
            Bloque();
        } else {
            throw createSyntaxException(currentToken, "idClase");
        }
    }

    private void Visibilidad() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.PUBLIC) {
            match(TypeToken.PUBLIC);
        } else if (currentToken.getTokenType() == TypeToken.PRIVATE) {
            match(TypeToken.PRIVATE);
        } else {
            throw createSyntaxException(currentToken, "public, private");
        }
    }

    private void Tipo() throws CompilerException {
        if (primerosTipoPrimitivo().contains(currentToken.getTokenType())) {
            TipoPrimitivo();
        } else if (currentToken.getTokenType() == TypeToken.ID_CLASS) {
            match(TypeToken.ID_CLASS);
        } else {
            throw createSyntaxException(currentToken, "tipo");
        }
    }

    private void TipoPrimitivo() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.PR_BOOLEAN) {
            match(TypeToken.PR_BOOLEAN);
        } else if (currentToken.getTokenType() == TypeToken.PR_INT) {
            match(TypeToken.PR_INT);
        } else if (currentToken.getTokenType() == TypeToken.PR_STRING) {
            match(TypeToken.PR_STRING);
        } else if (currentToken.getTokenType() == TypeToken.PR_CHAR) {
            match(TypeToken.PR_CHAR);
        } else {
            throw createSyntaxException(currentToken, "tipo primitivo");
        }
    }

    private void ListaDeclAtrs() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.ID_MET_VAR) {
            match(TypeToken.ID_MET_VAR);
            ListaDeclAtrsOVacio();
        } else {
            throw createSyntaxException(currentToken, "idMet o idVar");
        }
    }

    private void ListaDeclAtrsOVacio() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.COMMA) {
            match(TypeToken.COMMA);
            ListaDeclAtrs();
        } else {
        }
    }

    private void FormaMetodo() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.STATIC) {
            match(TypeToken.STATIC);
        } else if (currentToken.getTokenType() == TypeToken.DYNAMIC) {
            match(TypeToken.DYNAMIC);
        } else {
            throw createSyntaxException(currentToken, "static, dynamic");
        }
    }

    private void TipoMetodo() throws CompilerException {
        if (primerosTipoPrimitivo().contains(currentToken.getTokenType())) {
            Tipo();
        } else if (currentToken.getTokenType() == TypeToken.VOID) {
            match(TypeToken.VOID);
        } else {
            throw createSyntaxException(currentToken, "tipos primitivos y void");
        }
    }

    private void ArgsFormales() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.PARENTHESES_OPEN) {
            match(TypeToken.PARENTHESES_OPEN);
            ListaArgsFormalesOVacio();
            match(TypeToken.PARENTHESES_CLOSE);
        } else {
            throw createSyntaxException(currentToken, "args formales");
        }
    }

    private void ListaArgsFormalesOVacio() throws CompilerException {
        if (primerosTipo().contains(currentToken.getTokenType())) {
            ListaArgsFormales();
        } else {
        }
    }

    private void ListaArgsFormales() throws CompilerException {

        if (primerosTipo().contains(currentToken.getTokenType())) {
            ArgFormal();
            ListaArgFormalesEncadenada();
        } else {
            throw createSyntaxException(currentToken, "tipo");
        }
    }

    private void ArgFormal() throws CompilerException {
        if (primerosTipo().contains(currentToken.getTokenType())) {
            Tipo();
            match(TypeToken.ID_MET_VAR);
        } else {
            throw createSyntaxException(currentToken, "tipo");
        }
    }

    private void ListaArgFormalesEncadenada() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.COMMA) {
            match(TypeToken.COMMA);
            ListaArgsFormales();
        } else {
        }
    }

    private void Bloque() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.BRACES_OPEN) {
            match(TypeToken.BRACES_OPEN);
            ListaSentencias();
            match(TypeToken.BRACES_CLOSE);
        } else {
            throw createSyntaxException(currentToken, "bloque");
        }
    }

    private void ListaSentencias() throws CompilerException {
        if (primerosSentencia().contains(currentToken.getTokenType())) {
            Sentencia();
            ListaSentencias();
        } else {
        }
    }

    private void Sentencia() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.SEMICOLON) {
            match(TypeToken.SEMICOLON);
        } else if (primerosAcceso().contains(currentToken.getTokenType())) {
            Acceso();
            SentenciaAsignacion();
            match(TypeToken.SEMICOLON);
        } else if (primerosTipo().contains(currentToken.getTokenType())) {
            VarLocal();
            match(TypeToken.SEMICOLON);
        } else if (currentToken.getTokenType() == TypeToken.RETURN) {
            Return();
            match(TypeToken.SEMICOLON);
        } else if (currentToken.getTokenType() == TypeToken.IF) {
            If();
        } else if (currentToken.getTokenType() == TypeToken.FOR) {
            For();
        } else if (currentToken.getTokenType() == TypeToken.BRACES_OPEN) {
            Bloque();
        } else {
            throw createSyntaxException(currentToken, "sentencia");
        }
    }

    private void SentenciaAsignacion() throws CompilerException {
        if (Arrays.asList(TypeToken.ASSIGN_ADD,
                TypeToken.ASSIGN,
                TypeToken.ASSIGN_SUB).contains(currentToken.getTokenType())) {
            TipoDeAsignacion();
        } else {
        }
    }

    private void TipoDeAsignacion() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.ASSIGN) {
            match(TypeToken.ASSIGN);
            Expresion();
        } else if (currentToken.getTokenType() == TypeToken.ASSIGN_SUB) {
            match(TypeToken.ASSIGN_SUB);
        } else if (currentToken.getTokenType() == TypeToken.ASSIGN_ADD) {
            match(TypeToken.ASSIGN_ADD);
        } else {
            throw createSyntaxException(currentToken, "=, ++, --");
        }
    }

    private void VarLocal() throws CompilerException {
        if (primerosTipo().contains(currentToken.getTokenType())) {
            Tipo();
            match(TypeToken.ID_MET_VAR);
            VarLocalIni();
        } else {
            throw createSyntaxException(currentToken, "int, boolean, char, String, idClass");
        }
    }

    private void VarLocalIni() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.ASSIGN) {
            match(TypeToken.ASSIGN);
            Expresion();
        } else {
        }
    }

    private void Return() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.RETURN) {
            match(TypeToken.RETURN);
            ExpresionOVacio();
        } else {
            throw createSyntaxException(currentToken, "return");
        }
    }

    private void ExpresionOVacio() throws CompilerException {
        if (primerosExpresion().contains(currentToken.getTokenType())) {
            Expresion();
        } else {
        }
    }

    private void If() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.IF) {
            match(TypeToken.IF);
            match(TypeToken.PARENTHESES_OPEN);
            Expresion();
            match(TypeToken.PARENTHESES_CLOSE);
            Sentencia();
            ElseOVacio();
        } else {
            throw createSyntaxException(currentToken, "if, (, )");
        }
    }

    private void ElseOVacio() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.ELSE) {
            match(TypeToken.ELSE);
            Sentencia();
        } else {
        }
    }

    private void For() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.FOR) {
            match(TypeToken.FOR);
            match(TypeToken.PARENTHESES_OPEN);
            VarLocal();
            match(TypeToken.SEMICOLON);
            Expresion();
            match(TypeToken.SEMICOLON);
            Asignacion();
            match(TypeToken.PARENTHESES_CLOSE);
            Sentencia();
        } else {
            throw createSyntaxException(currentToken, "for, (, ;, )");
        }
    }

    private void Asignacion() throws CompilerException {
        if (primerosAcceso().contains(currentToken.getTokenType())) {
            Acceso();
            TipoDeAsignacion();
        } else {
            throw createSyntaxException(currentToken, "(, this, new, idMet o idVar");
        }
    }

    private void Expresion() throws CompilerException {
        if (primerosExpresionUnaria().contains(currentToken.getTokenType())) {
            ExpresionUnaria();
            ExpresionPrima();
        } else {
            throw createSyntaxException(currentToken, "expresion unaria");
        }
    }

    private void ExpresionPrima() throws CompilerException {
        if (primerosOperadorBinario().contains(currentToken.getTokenType())) {
            OperadorBinario();
            ExpresionUnaria();
            ExpresionPrima();
        } else {
        }
    }

    private void OperadorBinario() throws CompilerException {
        switch (currentToken.getTokenType()) {
            case OP_OR:
                match(TypeToken.OP_OR);
                break;
            case OP_AND:
                match(TypeToken.OP_AND);
                break;
            case EQUALS:
                match(TypeToken.EQUALS);
                break;
            case NOT_EQUALS:
                match(TypeToken.NOT_EQUALS);
                break;
            case GREATER_EQUALS:
                match(TypeToken.GREATER_EQUALS);
                break;
            case LESS_EQUALS:
                match(TypeToken.LESS_EQUALS);
                break;
            case GREATER:
                match(TypeToken.GREATER);
                break;
            case LESS:
                match(TypeToken.LESS);
                break;
            case ADD:
                match(TypeToken.ADD);
                break;
            case SUB:
                match(TypeToken.SUB);
                break;
            case MULTIPLY:
                match(TypeToken.MULTIPLY);
                break;
            case DIVIDE:
                match(TypeToken.DIVIDE);
                break;
            case MOD:
                match(TypeToken.MOD);
                break;
            default:
                throw createSyntaxException(currentToken, "operador binario");
        }
    }

    private void ExpresionUnaria() throws CompilerException {
        if (primerosOperadorUnario().contains(currentToken.getTokenType())) {
            OperadorUnario();
            Operando();
        } else if (primerosOperando().contains(currentToken.getTokenType())) {
            Operando();
        } else {
            throw createSyntaxException(currentToken, "operador unario, operando");
        }
    }

    private void OperadorUnario() throws CompilerException {
        switch (currentToken.getTokenType()) {
            case ADD:
                match(TypeToken.ADD);
                break;
            case SUB:
                match(TypeToken.SUB);
                break;
            case OP_NOT:
                match(TypeToken.OP_NOT);
                break;
            default:
                throw createSyntaxException(currentToken, "operador unario");
        }
    }

    private void Operando() throws CompilerException {
        if (primerosLiteral().contains(currentToken.getTokenType())) {
            Literal();
        } else if (primerosAcceso().contains(currentToken.getTokenType())) {
            Acceso();
        } else {
            throw createSyntaxException(currentToken, "literal, acceso");
        }
    }

    private void Literal() throws CompilerException {
        switch (currentToken.getTokenType()) {
            case NULL:
                match(TypeToken.NULL);
                break;
            case TRUE:
                match(TypeToken.TRUE);
                break;
            case FALSE:
                match(TypeToken.FALSE);
                break;
            case LIT_INT:
                match(TypeToken.LIT_INT);
                break;
            case LIT_CHAR:
                match(TypeToken.LIT_CHAR);
                break;
            case LIT_STRING:
                match(TypeToken.LIT_STRING);
                break;
            default:
                throw createSyntaxException(currentToken, "literal");
        }
    }

    private void Acceso() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.PARENTHESES_OPEN) {
            match(TypeToken.PARENTHESES_OPEN);
            PrimarioParentesisAbierta();
        } else if (primerosAcceso().contains(currentToken.getTokenType())) {
            PrimarioSinParentesis();
            Encadenado();
        } else {
            throw createSyntaxException(currentToken, "(, this, new, idMet o idVar");
        }
    }

    private void PrimarioSinParentesis() throws CompilerException {
        switch (currentToken.getTokenType()) {
            case THIS:
                AccesoThis();
                break;
            case ID_MET_VAR:
                AccesoVarOMet();
                break;
            case NEW:
                AccesoConstructor();
                break;
            default:
                throw createSyntaxException(currentToken, "this, new, idMet o idVar");
        }
    }

    private void PrimarioParentesisAbierta() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.ID_CLASS) {
            match(TypeToken.ID_CLASS);
            match(TypeToken.PARENTHESES_CLOSE);
            PrimarioEntero();
            Encadenado();
        } else if (primerosExpresion().contains(currentToken.getTokenType())) {
            PrimarioEnParentesis();
        } else {//casting situation
            throw createSyntaxException(currentToken, "idClase, expresion");
        }
    }

    private void PrimarioEntero() throws CompilerException {
        if (primerosAcceso().contains(currentToken.getTokenType())) {
            PrimarioSinParentesis();
        } else if (currentToken.getTokenType() == TypeToken.PARENTHESES_OPEN) {
            match(TypeToken.PARENTHESES_OPEN);
            PrimarioParentesisAbierta();
        } else {
            throw createSyntaxException(currentToken, "(, acceso");
        }
    }

    private void PrimarioEnParentesis() throws CompilerException {
        if (primerosExpresion().contains(currentToken.getTokenType())) {
            Expresion();
            match(TypeToken.PARENTHESES_CLOSE);
        } else {
            throw createSyntaxException(currentToken, "expresion");
        }
    }

    private void AccesoThis() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.THIS) {
            match(TypeToken.THIS);
        } else {
            throw createSyntaxException(currentToken, "this");
        }
    }

    private void AccesoVarOMet() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.ID_MET_VAR) {
            match(TypeToken.ID_MET_VAR);
            VarOMet();
        } else {
            throw createSyntaxException(currentToken, "idMet o idVar");
        }
    }

    private void VarOMet() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.PARENTHESES_OPEN) {
            ArgsActuales();
        } else {
        }
    }

    private void AccesoConstructor() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.NEW) {
            match(TypeToken.NEW);
            match(TypeToken.ID_CLASS);
            ArgsActuales();
        } else {
            throw createSyntaxException(currentToken, "new idClass");
        }
    }

    private void ArgsActuales() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.PARENTHESES_OPEN) {
            match(TypeToken.PARENTHESES_OPEN);
            ListaExpsOVacio();
            match(TypeToken.PARENTHESES_CLOSE);
        } else {
            throw createSyntaxException(currentToken, "args actuales");
        }
    }

    private void ListaExpsOVacio() throws CompilerException {
        if (primerosExpresion().contains(currentToken.getTokenType())) {
            ListaExps();
        } else {
        }
    }

    private void ListaExps() throws CompilerException {
        if (primerosExpresion().contains(currentToken.getTokenType())) {
            Expresion();
            ExpresionEncadenada();
        } else {
            throw createSyntaxException(currentToken, "expresion");
        }
    }

    private void ExpresionEncadenada() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.COMMA) {
            match(TypeToken.COMMA);
            ListaExps();
        } else {
        }
    }

    private void Encadenado() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.DOT) {
            VarOMetodoEncadenado();
            Encadenado();
        } else {
        }
    }

    private void VarOMetodoEncadenado() throws CompilerException {
        if (currentToken.getTokenType() == TypeToken.DOT) {
            match(TypeToken.DOT);
            match(TypeToken.ID_MET_VAR);
            VarOMet();
        } else {
            throw createSyntaxException(currentToken, ". , idMet, idVar");
        }
    }
}