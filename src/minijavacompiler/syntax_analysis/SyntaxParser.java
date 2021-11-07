package minijavacompiler.syntax_analysis;

import minijavacompiler.catbag.FileManager;
import minijavacompiler.data_structures.Class;
import minijavacompiler.data_structures.*;
import minijavacompiler.exceptions.CompilerException;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.exceptions.SyntaxException;
import minijavacompiler.lexical_parser.LexicalParser;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

import java.util.Arrays;
import java.util.List;

public class SyntaxParser {
    private final LexicalParser lexicalParser;
    private final FileManager fileManager;
    private final SymbolTable TS = SymbolTable.getInstance();
    private Token currentToken;

    public SyntaxParser(LexicalParser lexicalParser, FileManager fileManager) {
        this.lexicalParser = lexicalParser;
        this.fileManager = fileManager;
    }

    private void match(TokenType typeToken) throws CompilerException {
        if (typeToken == currentToken.getTokenType()) {
            currentToken = lexicalParser.nextToken();
        } else {
            throw createSyntaxException(currentToken, typeToken.name());
        }
    }

    private SyntaxException createSyntaxException(Token token, String expected) {
        return new SyntaxException(token, expected, fileManager.line());
    }

    private List<TokenType> firstsMember() {
        return Arrays.asList(TokenType.PUBLIC,
                TokenType.PRIVATE,
                TokenType.ID_CLASS,
                TokenType.STATIC,
                TokenType.DYNAMIC);
    }

    private List<TokenType> firstsTypePrimitive() {
        return Arrays.asList(TokenType.PR_BOOLEAN,
                TokenType.PR_CHAR,
                TokenType.PR_INT,
                TokenType.PR_STRING);
    }

    private List<TokenType> firstsType() {
        return Arrays.asList(
                TokenType.PR_BOOLEAN,
                TokenType.PR_CHAR,
                TokenType.PR_INT,
                TokenType.PR_STRING,
                TokenType.ID_CLASS
        );
    }

    private List<TokenType> firstsSentence() {
        return Arrays.asList(
                TokenType.SEMICOLON,
                TokenType.PARENTHESES_OPEN,
                TokenType.THIS,
                TokenType.ID_MET_VAR,
                TokenType.NEW,
                TokenType.PR_BOOLEAN,
                TokenType.PR_CHAR,
                TokenType.PR_INT,
                TokenType.PR_STRING,
                TokenType.ID_CLASS,
                TokenType.RETURN,
                TokenType.IF,
                TokenType.FOR,
                TokenType.BRACES_OPEN);
    }

    private List<TokenType> firstsOperands() {
        return Arrays.asList(TokenType.NULL,
                TokenType.TRUE,
                TokenType.FALSE,
                TokenType.LIT_INT,
                TokenType.LIT_CHAR,
                TokenType.LIT_STRING,
                TokenType.PARENTHESES_OPEN,
                TokenType.THIS,
                TokenType.ID_MET_VAR,
                TokenType.NEW);
    }

    private List<TokenType> firstsUnaryOperands() {
        return Arrays.asList(
                TokenType.ADD,
                TokenType.SUB,
                TokenType.OP_NOT
        );
    }

    private List<TokenType> firstsBinaryOperands() {
        return Arrays.asList(TokenType.OP_AND,
                TokenType.OP_OR,
                TokenType.EQUALS,
                TokenType.NOT_EQUALS,
                TokenType.GREATER_EQUALS,
                TokenType.LESS_EQUALS,
                TokenType.GREATER,
                TokenType.LESS,
                TokenType.ADD,
                TokenType.SUB,
                TokenType.MULTIPLY,
                TokenType.DIVIDE,
                TokenType.MOD);
    }

    private List<TokenType> firstsExpression() {
        return Arrays.asList(TokenType.ADD,
                TokenType.SUB,
                TokenType.OP_NOT,
                TokenType.NULL,
                TokenType.TRUE,
                TokenType.FALSE,
                TokenType.LIT_INT,
                TokenType.LIT_CHAR,
                TokenType.LIT_STRING,
                TokenType.PARENTHESES_OPEN,
                TokenType.THIS,
                TokenType.ID_MET_VAR,
                TokenType.NEW);
    }

    private List<TokenType> firstsUnaryExpression() {
        return Arrays.asList(
                TokenType.ADD,
                TokenType.SUB,
                TokenType.OP_NOT,
                TokenType.NULL,
                TokenType.TRUE,
                TokenType.FALSE,
                TokenType.LIT_INT,
                TokenType.LIT_CHAR,
                TokenType.LIT_STRING,
                TokenType.PARENTHESES_OPEN,
                TokenType.NEW,
                TokenType.ID_MET_VAR,
                TokenType.THIS
        );
    }

    private List<TokenType> firstsLiteral() {
        return Arrays.asList(TokenType.NULL,
                TokenType.TRUE,
                TokenType.FALSE,
                TokenType.LIT_INT,
                TokenType.LIT_CHAR,
                TokenType.LIT_STRING);
    }

    private List<TokenType> firstsAccess() {
        return Arrays.asList(
                TokenType.PARENTHESES_OPEN,
                TokenType.THIS,
                TokenType.ID_MET_VAR,
                TokenType.NEW);
    }

    private List<TokenType> firstsPrimaryWithoutParentheses() {
        return Arrays.asList(
                TokenType.THIS,
                TokenType.ID_MET_VAR,
                TokenType.NEW);
    }

    public void start() throws CompilerException {
        currentToken = lexicalParser.nextToken();
        startGrammarAnalysis();
    }

    private void startGrammarAnalysis() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.CLASS) {
            classList();
            match(TokenType.EOF);
        } else {
            throw createSyntaxException(currentToken, "class or EOF");
        }
    }

    private void classList() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.CLASS) {
            classes();
            classListOrEmpty();
        } else {
            throw createSyntaxException(currentToken, "id class");
        }
    }

    private void classListOrEmpty() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.CLASS) {
            classList();
        } else {
        }
    }

    private void classes() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.CLASS) {
            match(TokenType.CLASS);
            if (SymbolTable.getInstance().isClassDeclared(currentToken.getLexeme())) {
                throw new SemanticException(currentToken.getLexeme(), currentToken.getLine(), "Clase ya definida");
            }
            Class aClass = new Class(currentToken);
            match(TokenType.ID_CLASS);
            TS.setCurrentClass(aClass);
            Token ancestor = inheritance();
            TS.getCurrentClass().addAncestor(ancestor);
            match(TokenType.BRACES_OPEN);
            membersList();
            match(TokenType.BRACES_CLOSE);
            TS.addClass(aClass);
        } else {
            throw createSyntaxException(currentToken, "class or id class");
        }
    }

    private Token inheritance() throws CompilerException {
        Token ancestor = null;

        if (currentToken.getTokenType() == TokenType.EXTENDS) {
            match(TokenType.EXTENDS);
            ancestor = currentToken;
            match(TokenType.ID_CLASS);
        }

        return ancestor;
    }

    private void member() throws CompilerException {
        if (Arrays.asList(TokenType.PUBLIC, TokenType.PRIVATE).
                contains(currentToken.getTokenType())) {
            attribute();
        } else if (currentToken.getTokenType() == TokenType.ID_CLASS) {
            constructor();
        } else if (Arrays.asList(TokenType.STATIC, TokenType.DYNAMIC).
                contains(currentToken.getTokenType())) {
            method();
        } else {
            throw createSyntaxException(currentToken, "public, private, id class, static, dynamic");
        }
    }

    private void membersList() throws CompilerException {
        if (firstsMember().contains(currentToken.getTokenType())) {
            member();
            membersList();
        } else {
        }
    }

    private void attribute() throws CompilerException {
        if (Arrays.asList(TokenType.PUBLIC, TokenType.PRIVATE).
                contains(currentToken.getTokenType())) {
            String visib = visibility();
            Type type = type();
            //atributo heredado
            attrDeclList(visib, type);
            match(TokenType.SEMICOLON);
        } else {
            throw createSyntaxException(currentToken, "attribute visibility: public or private");
        }
    }

    private void method() throws CompilerException {
        if (Arrays.asList(TokenType.STATIC, TokenType.DYNAMIC).
                contains(currentToken.getTokenType())) {
            String bindingForm = bindingForm();
            Type returnType = methodReturnType();
            Token tknMet = currentToken;
            match(TokenType.ID_MET_VAR);
            Method aMethod = new Method(bindingForm, returnType, tknMet);
            TS.setCurrentUnit(aMethod);
            parameters();
            TS.getCurrentClass().addMet(aMethod);
            block();
        } else {
            throw createSyntaxException(currentToken, "method binding: static or dynamic");
        }
    }

    private void constructor() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.ID_CLASS) {
            Token tknConstr = currentToken;
            match(TokenType.ID_CLASS);
            Constructor aConstr = new Constructor(tknConstr);
            TS.setCurrentUnit(aConstr);
            parameters();
            TS.getCurrentClass().addConstructor(aConstr);
            block();
        } else {
            throw createSyntaxException(currentToken, "id class");
        }
    }

    private String visibility() throws CompilerException {
        String visib;
        if (currentToken.getTokenType() == TokenType.PUBLIC) {
            match(TokenType.PUBLIC);
            visib = "public";
        } else if (currentToken.getTokenType() == TokenType.PRIVATE) {
            match(TokenType.PRIVATE);
            visib = "private";
        } else {
            throw createSyntaxException(currentToken, "public, private");
        }
        return visib;
    }

    private Type type() throws CompilerException {
        if (firstsTypePrimitive().contains(currentToken.getTokenType())) {
            return typePrimitive();
        } else if (currentToken.getTokenType() == TokenType.ID_CLASS) {
            Token tknTypeRef = currentToken;
            match(TokenType.ID_CLASS);
            return new TypeReference(tknTypeRef);
        } else {
            throw createSyntaxException(currentToken, "tipo");
        }
    }

    private Type typePrimitive() throws CompilerException {
        Token tknTipo;
        if (currentToken.getTokenType() == TokenType.PR_BOOLEAN) {
            tknTipo = currentToken;
            match(TokenType.PR_BOOLEAN);
            return new TypePrimitive(tknTipo);
        } else if (currentToken.getTokenType() == TokenType.PR_INT) {
            tknTipo = currentToken;
            match(TokenType.PR_INT);
            return new TypePrimitive(tknTipo);
        } else if (currentToken.getTokenType() == TokenType.PR_STRING) {
            tknTipo = currentToken;
            match(TokenType.PR_STRING);
            return new TypePrimitive(tknTipo);
        } else if (currentToken.getTokenType() == TokenType.PR_CHAR) {
            tknTipo = currentToken;
            match(TokenType.PR_CHAR);
            return new TypePrimitive(tknTipo);
        } else {
            throw createSyntaxException(currentToken, "tipo primitivo");
        }
    }

    //atributo heredado
    private void attrDeclList(String visib, Type type) throws CompilerException {
        if (currentToken.getTokenType() == TokenType.ID_MET_VAR) {
            Token tknAttr = currentToken;
            match(TokenType.ID_MET_VAR);
            Attribute a = new Attribute(visib, type, tknAttr);
            TS.getCurrentClass().addAttr(a);
            attrDeclListOrEmpty(visib, type);
        } else {
            throw createSyntaxException(currentToken, "id method or id var");
        }

    }

    private void attrDeclListOrEmpty(String visib, Type type) throws CompilerException {
        if (currentToken.getTokenType() == TokenType.COMMA) {
            match(TokenType.COMMA);
            attrDeclList(visib, type);
        } else {
        }
    }

    private String bindingForm() throws CompilerException {
        String visibility;

        if (currentToken.getTokenType() == TokenType.STATIC) {
            match(TokenType.STATIC);
            visibility = "static";
        } else if (currentToken.getTokenType() == TokenType.DYNAMIC) {
            match(TokenType.DYNAMIC);
            visibility = "dynamic";
        } else {
            throw createSyntaxException(currentToken, "static, dynamic");
        }
        return visibility;
    }

    private Type methodReturnType() throws CompilerException {
        Type returnType;

        if (firstsType().contains(currentToken.getTokenType())) {
            returnType = type();
        } else if (currentToken.getTokenType() == TokenType.VOID) {
            Token tknVoid = currentToken;
            match(TokenType.VOID);
            returnType = new TypeVoid(tknVoid);
        } else {
            throw createSyntaxException(currentToken, "tipos primitivos y void");
        }
        return returnType;
    }

    private void parameters() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.PARENTHESES_OPEN) {
            match(TokenType.PARENTHESES_OPEN);
            parameterListOrEmpty();
            match(TokenType.PARENTHESES_CLOSE);
        } else {
            throw createSyntaxException(currentToken, "parametros");
        }
    }

    private void parameterListOrEmpty() throws CompilerException {
        if (firstsType().contains(currentToken.getTokenType())) {
            parameterList();
        } else {
        }
    }

    private void parameterList() throws CompilerException {
        if (firstsType().contains(currentToken.getTokenType())) {
            parameter();
            chainedParameterList();
        } else {
            throw createSyntaxException(currentToken, "lista de parametros: tipo");
        }
    }

    private void chainedParameterList() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.COMMA) {
            match(TokenType.COMMA);
            parameterList();
        } else {
        }
    }

    private void parameter() throws CompilerException {
        if (firstsType().contains(currentToken.getTokenType())) {
            Type type = type();
            Token tkn = currentToken;
            match(TokenType.ID_MET_VAR);
            TS.getCurrentUnit().addParam(new Parameter(type, tkn));
        } else {
            throw createSyntaxException(currentToken, "parametro: tipo de parametro o nombre");
        }
    }

    private void block() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.BRACES_OPEN) {
            match(TokenType.BRACES_OPEN);
            ListaSentencias();
            match(TokenType.BRACES_CLOSE);
        } else {
            throw createSyntaxException(currentToken, "bloque");
        }
    }

    //CONSTRUCCION DE AST
    //---------------------------------------------------------------------------------------------
    private void ListaSentencias() throws CompilerException {
        if (firstsSentence().contains(currentToken.getTokenType())) {
            Sentencia();
            ListaSentencias();
        } else {
        }
    }

    private void Sentencia() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.SEMICOLON) {
            match(TokenType.SEMICOLON);
        } else if (firstsAccess().contains(currentToken.getTokenType())) {
            Acceso();
            SentenciaAsignacion();
            match(TokenType.SEMICOLON);
        } else if (firstsType().contains(currentToken.getTokenType())) {
            VarLocal();
            match(TokenType.SEMICOLON);
        } else if (currentToken.getTokenType() == TokenType.RETURN) {
            Return();
            match(TokenType.SEMICOLON);
        } else if (currentToken.getTokenType() == TokenType.IF) {
            If();
        } else if (currentToken.getTokenType() == TokenType.FOR) {
            For();
        } else if (currentToken.getTokenType() == TokenType.BRACES_OPEN) {
            block();
        } else {
            throw createSyntaxException(currentToken, "sentencia");
        }
    }

    private void SentenciaAsignacion() throws CompilerException {
        if (Arrays.asList(TokenType.ASSIGN_ADD,
                TokenType.ASSIGN,
                TokenType.ASSIGN_SUB).contains(currentToken.getTokenType())) {
            TipoDeAsignacion();
        } else {
        }
    }

    private void TipoDeAsignacion() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.ASSIGN) {
            match(TokenType.ASSIGN);
            Expresion();
        } else if (currentToken.getTokenType() == TokenType.ASSIGN_SUB) {
            match(TokenType.ASSIGN_SUB);
        } else if (currentToken.getTokenType() == TokenType.ASSIGN_ADD) {
            match(TokenType.ASSIGN_ADD);
        } else {
            throw createSyntaxException(currentToken, "=, ++, --");
        }
    }

    private void VarLocal() throws CompilerException {
        if (firstsType().contains(currentToken.getTokenType())) {
            type();
            match(TokenType.ID_MET_VAR);
            VarLocalIni();
        } else {
            throw createSyntaxException(currentToken, "int, boolean, char, String, idClass");
        }
    }

    private void VarLocalIni() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.ASSIGN) {
            match(TokenType.ASSIGN);
            Expresion();
        } else {
        }
    }

    private void Return() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.RETURN) {
            match(TokenType.RETURN);
            ExpresionOVacio();
        } else {
            throw createSyntaxException(currentToken, "return");
        }
    }

    private void ExpresionOVacio() throws CompilerException {
        if (firstsExpression().contains(currentToken.getTokenType())) {
            Expresion();
        } else {
        }
    }

    private void If() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.IF) {
            match(TokenType.IF);
            match(TokenType.PARENTHESES_OPEN);
            Expresion();
            match(TokenType.PARENTHESES_CLOSE);
            Sentencia();
            ElseOVacio();
        } else {
            throw createSyntaxException(currentToken, "if, (, )");
        }
    }

    private void ElseOVacio() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.ELSE) {
            match(TokenType.ELSE);
            Sentencia();
        } else {
        }
    }

    private void For() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.FOR) {
            match(TokenType.FOR);
            match(TokenType.PARENTHESES_OPEN);
            VarLocal();
            match(TokenType.SEMICOLON);
            Expresion();
            match(TokenType.SEMICOLON);
            Asignacion();
            match(TokenType.PARENTHESES_CLOSE);
            Sentencia();
        } else {
            throw createSyntaxException(currentToken, "for, (, ;, )");
        }
    }

    private void Asignacion() throws CompilerException {
        if (firstsAccess().contains(currentToken.getTokenType())) {
            Acceso();
            TipoDeAsignacion();
        } else {
            throw createSyntaxException(currentToken, "(, this, new, idMet o idVar");
        }
    }

    private void Expresion() throws CompilerException {
        if (firstsUnaryExpression().contains(currentToken.getTokenType())) {
            ExpresionUnaria();
            ExpresionPrima();
        } else {
            throw createSyntaxException(currentToken, "expresion unaria");
        }
    }

    private void ExpresionPrima() throws CompilerException {
        if (firstsBinaryOperands().contains(currentToken.getTokenType())) {
            OperadorBinario();
            ExpresionUnaria();
            ExpresionPrima();
        } else {
        }
    }

    private void OperadorBinario() throws CompilerException {
        switch (currentToken.getTokenType()) {
            case OP_OR:
                match(TokenType.OP_OR);
                break;
            case OP_AND:
                match(TokenType.OP_AND);
                break;
            case EQUALS:
                match(TokenType.EQUALS);
                break;
            case NOT_EQUALS:
                match(TokenType.NOT_EQUALS);
                break;
            case GREATER_EQUALS:
                match(TokenType.GREATER_EQUALS);
                break;
            case LESS_EQUALS:
                match(TokenType.LESS_EQUALS);
                break;
            case GREATER:
                match(TokenType.GREATER);
                break;
            case LESS:
                match(TokenType.LESS);
                break;
            case ADD:
                match(TokenType.ADD);
                break;
            case SUB:
                match(TokenType.SUB);
                break;
            case MULTIPLY:
                match(TokenType.MULTIPLY);
                break;
            case DIVIDE:
                match(TokenType.DIVIDE);
                break;
            case MOD:
                match(TokenType.MOD);
                break;
            default:
                throw createSyntaxException(currentToken, "operador binario");
        }
    }

    private void ExpresionUnaria() throws CompilerException {
        if (firstsUnaryOperands().contains(currentToken.getTokenType())) {
            OperadorUnario();
            Operando();
        } else if (firstsOperands().contains(currentToken.getTokenType())) {
            Operando();
        } else {
            throw createSyntaxException(currentToken, "operador unario, operando");
        }
    }

    private void OperadorUnario() throws CompilerException {
        switch (currentToken.getTokenType()) {
            case ADD:
                match(TokenType.ADD);
                break;
            case SUB:
                match(TokenType.SUB);
                break;
            case OP_NOT:
                match(TokenType.OP_NOT);
                break;
            default:
                throw createSyntaxException(currentToken, "operador unario");
        }
    }

    private void Operando() throws CompilerException {
        if (firstsLiteral().contains(currentToken.getTokenType())) {
            Literal();
        } else if (firstsAccess().contains(currentToken.getTokenType())) {
            Acceso();
        } else {
            throw createSyntaxException(currentToken, "literal, acceso");
        }
    }

    private void Literal() throws CompilerException {
        switch (currentToken.getTokenType()) {
            case NULL:
                match(TokenType.NULL);
                break;
            case TRUE:
                match(TokenType.TRUE);
                break;
            case FALSE:
                match(TokenType.FALSE);
                break;
            case LIT_INT:
                match(TokenType.LIT_INT);
                break;
            case LIT_CHAR:
                match(TokenType.LIT_CHAR);
                break;
            case LIT_STRING:
                match(TokenType.LIT_STRING);
                break;
            default:
                throw createSyntaxException(currentToken, "literal");
        }
    }

    private void Acceso() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.PARENTHESES_OPEN) {
            match(TokenType.PARENTHESES_OPEN);
            PrimarioParentesisAbierta();
        } else if (firstsPrimaryWithoutParentheses().contains(currentToken.getTokenType())) {
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
        //casting
        if (currentToken.getTokenType() == TokenType.ID_CLASS) {
            match(TokenType.ID_CLASS);
            match(TokenType.PARENTHESES_CLOSE);
            PrimarioEntero();
            Encadenado();
        } else if (firstsExpression().contains(currentToken.getTokenType())) {
            PrimarioEnParentesis();
            Encadenado();
        } else {
            throw createSyntaxException(currentToken, "idClase, expresion");
        }
    }

    private void PrimarioEntero() throws CompilerException {
        if (firstsPrimaryWithoutParentheses().contains(currentToken.getTokenType())) {
            PrimarioSinParentesis();
        } else if (currentToken.getTokenType() == TokenType.PARENTHESES_OPEN) {
            match(TokenType.PARENTHESES_OPEN);
            PrimarioParentesisAbierta();
        } else {
            throw createSyntaxException(currentToken, "(, acceso");
        }
    }

    private void PrimarioEnParentesis() throws CompilerException {
        if (firstsExpression().contains(currentToken.getTokenType())) {
            Expresion();
            match(TokenType.PARENTHESES_CLOSE);
        } else {
            throw createSyntaxException(currentToken, "expresion");
        }
    }

    private void AccesoThis() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.THIS) {
            match(TokenType.THIS);
        } else {
            throw createSyntaxException(currentToken, "this");
        }
    }

    private void AccesoVarOMet() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.ID_MET_VAR) {
            match(TokenType.ID_MET_VAR);
            ArgOVacio();
        } else {
            throw createSyntaxException(currentToken, "idMet o idVar");
        }
    }

    private void ArgOVacio() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.PARENTHESES_OPEN) {
            ArgsActuales();
        } else {
        }
    }

    private void AccesoConstructor() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.NEW) {
            match(TokenType.NEW);
            match(TokenType.ID_CLASS);
            ArgsActuales();
        } else {
            throw createSyntaxException(currentToken, "new idClass");
        }
    }

    private void ArgsActuales() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.PARENTHESES_OPEN) {
            match(TokenType.PARENTHESES_OPEN);
            ListaExpsOVacio();
            match(TokenType.PARENTHESES_CLOSE);
        } else {
            throw createSyntaxException(currentToken, "args actuales");
        }
    }

    private void ListaExpsOVacio() throws CompilerException {
        if (firstsExpression().contains(currentToken.getTokenType())) {
            ListaExps();
        } else {
        }
    }

    private void ListaExps() throws CompilerException {
        if (firstsExpression().contains(currentToken.getTokenType())) {
            Expresion();
            ExpresionEncadenada();
        } else {
            throw createSyntaxException(currentToken, "expresion");
        }
    }

    private void ExpresionEncadenada() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.COMMA) {
            match(TokenType.COMMA);
            ListaExps();
        } else {
        }
    }

    private void Encadenado() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.DOT) {
            VarOMetodoEncadenado();
            Encadenado();
        } else {
        }
    }

    private void VarOMetodoEncadenado() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.DOT) {
            match(TokenType.DOT);
            match(TokenType.ID_MET_VAR);
            ArgOVacio();
        } else {
            throw createSyntaxException(currentToken, ". , idMet, idVar");
        }
    }
}