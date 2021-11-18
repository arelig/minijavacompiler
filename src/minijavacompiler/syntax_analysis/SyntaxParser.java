package minijavacompiler.syntax_analysis;

import minijavacompiler.ast_data_structures.expression_nodes.*;
import minijavacompiler.ast_data_structures.sentence_nodes.*;
import minijavacompiler.catbag.FileManager;
import minijavacompiler.data_structures.Class;
import minijavacompiler.data_structures.*;
import minijavacompiler.exceptions.CompilerException;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.exceptions.SyntaxException;
import minijavacompiler.lexical_parser.LexicalParser;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

import java.util.ArrayList;
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
            if (TS.isClassDeclared(currentToken.getLexeme())) {
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
            aMethod.setBlock(block());
            TS.getCurrentClass().addMet(aMethod);
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
            aConstr.setBlock(block());
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

    private BlockNode block() throws CompilerException {
        BlockNode quickSave = SymbolTable.getInstance().getCurrentBlock();
        BlockNode blockNode;

        if (currentToken.getTokenType() == TokenType.BRACES_OPEN) {
            blockNode = new BlockNode(currentToken);
            match(TokenType.BRACES_OPEN);
            TS.setCurrentBlock(blockNode);
            ListaSentencias();
            match(TokenType.BRACES_CLOSE);
        } else {
            throw createSyntaxException(currentToken, "bloque");
        }

        SymbolTable.getInstance().setCurrentBlock(quickSave);

        return blockNode;
    }


    private void ListaSentencias() throws CompilerException {
        if (firstsSentence().contains(currentToken.getTokenType())) {
            SentenceNode sentenceNode = Sentencia();
            TS.getCurrentBlock().addSentence(sentenceNode);
            ListaSentencias();
        } else {
        }
    }

    private SentenceNode Sentencia() throws CompilerException {
        SentenceNode sentenceNode = null;
        if (currentToken.getTokenType() == TokenType.SEMICOLON) {
            sentenceNode = new EmptyNode(currentToken);
            match(TokenType.SEMICOLON);
        } else if (firstsAccess().contains(currentToken.getTokenType())) {
            sentenceNode = Asignacion();
            match(TokenType.SEMICOLON);
        } else if (firstsType().contains(currentToken.getTokenType())) {
            sentenceNode = VarLocal();
            match(TokenType.SEMICOLON);
        } else if (currentToken.getTokenType() == TokenType.RETURN) {
            sentenceNode = Return();
            match(TokenType.SEMICOLON);
        } else if (currentToken.getTokenType() == TokenType.IF) {
            sentenceNode = If();
        } else if (currentToken.getTokenType() == TokenType.FOR) {
            sentenceNode = For();
        } else if (currentToken.getTokenType() == TokenType.BRACES_OPEN) {
            sentenceNode = block();
        } else {
            throw createSyntaxException(currentToken, "sentencia");
        }

        return sentenceNode;
    }

//metele a acceso y encadenado

    private SentenceNode Asignacion() throws CompilerException {
        AssignmentNode assignmentNode = null;

        if (firstsAccess().contains(currentToken.getTokenType())) {
            AccessNode accessNode = Acceso();
            assignmentNode = new AssignmentNode(currentToken);
            ExpressionNode expressionNode = TipoDeAsignacion();
            assignmentNode.setLeftNode(accessNode);
            assignmentNode.setRightNode(expressionNode);
        } else {
            throw createSyntaxException(currentToken, "(, this, new, idMet o idVar");
        }
        return assignmentNode;
    }

    private ExpressionNode TipoDeAsignacion() throws CompilerException {
        ExpressionNode expressionNode = null;
        if (currentToken.getTokenType() == TokenType.ASSIGN) {
            match(TokenType.ASSIGN);
            expressionNode = Expresion();
        } else if (currentToken.getTokenType() == TokenType.ASSIGN_SUB) {
            expressionNode = new LiteralIntNode(new Token(TokenType.LIT_INT, "1", currentToken.getLine()));
            match(TokenType.ASSIGN_SUB);
        } else if (currentToken.getTokenType() == TokenType.ASSIGN_ADD) {
            expressionNode = new LiteralIntNode(new Token(TokenType.LIT_INT, "1", currentToken.getLine()));
            match(TokenType.ASSIGN_ADD);
        } else {
            throw createSyntaxException(currentToken, "=, ++, --");
        }
        return expressionNode;
    }

    private SentenceNode VarLocal() throws CompilerException {
        LocalVarNode localVarNode = null;
        if (firstsType().contains(currentToken.getTokenType())) {
            Type type = type();
            localVarNode = new LocalVarNode(currentToken);
            match(TokenType.ID_MET_VAR);
            ExpressionNode expression = VarLocalIni(localVarNode);
            localVarNode.setType(type);
            localVarNode.setInit(expression);
        } else {
            throw createSyntaxException(currentToken, "int, boolean, char, String, idClass");
        }
        return localVarNode;
    }

    private ExpressionNode VarLocalIni(LocalVarNode localVarNode) throws CompilerException {
        ExpressionNode expressionNode = null;
        if (currentToken.getTokenType() == TokenType.ASSIGN) {
            localVarNode.setTknAssignInit(currentToken);
            match(TokenType.ASSIGN);
            expressionNode = Expresion();
        } else {
        }
        return expressionNode;
    }

    private SentenceNode Return() throws CompilerException {
        ReturnNode returnNode = null;

        if (currentToken.getTokenType() == TokenType.RETURN) {
            returnNode = new ReturnNode(currentToken);
            match(TokenType.RETURN);
            ExpressionNode expressionNode = ExpresionOVacio();
            returnNode.setExpression(expressionNode);
        } else {
            throw createSyntaxException(currentToken, "return");
        }

        return returnNode;
    }

    private SentenceNode If() throws CompilerException {
        IfNode sentenceIf = null;

        if (currentToken.getTokenType() == TokenType.IF) {
            sentenceIf = new IfNode(currentToken);
            match(TokenType.IF);
            match(TokenType.PARENTHESES_OPEN);
            ExpressionNode condExpression = Expresion();
            match(TokenType.PARENTHESES_CLOSE);
            SentenceNode bodySentence = Sentencia();
            SentenceNode elseSentence = ElseOVacio();

            sentenceIf.setCondExpression(condExpression);
            sentenceIf.setBodySentence(bodySentence);
            if (elseSentence != null) {
                sentenceIf.setElseNode(elseSentence);
            }
        } else {
            throw createSyntaxException(currentToken, "if, (, )");
        }
        return sentenceIf;
    }

    private SentenceNode ElseOVacio() throws CompilerException {
        ElseNode sentenceElse = null;
        if (currentToken.getTokenType() == TokenType.ELSE) {
            sentenceElse = new ElseNode(currentToken);
            match(TokenType.ELSE);
            SentenceNode bodySentence = Sentencia();
            sentenceElse.setBodySentence(bodySentence);
        } else {
        }
        return sentenceElse;
    }

    private SentenceNode For() throws CompilerException {
        ForNode forNode = null;

        if (currentToken.getTokenType() == TokenType.FOR) {
            forNode = new ForNode(currentToken);
            match(TokenType.FOR);
            match(TokenType.PARENTHESES_OPEN);
            SentenceNode localVarCond = VarLocal();
            match(TokenType.SEMICOLON);
            ExpressionNode varCond = Expresion();
            match(TokenType.SEMICOLON);
            SentenceNode assignCond = Asignacion();
            match(TokenType.PARENTHESES_CLOSE);
            SentenceNode bodySentence = Sentencia();

            //todo testear creacion
            forNode.setLocalVar(localVarCond);
            forNode.setVarCond(varCond);
            forNode.setAssignCond(assignCond);
            forNode.setBodySentence(bodySentence);
        } else {
            throw createSyntaxException(currentToken, "for, (, ;, )");
        }

        return forNode;
    }

    private ExpressionNode ExpresionOVacio() throws CompilerException {
        ExpressionNode expressionNode = null;

        if (firstsExpression().contains(currentToken.getTokenType())) {
            expressionNode = Expresion();
        } else {
        }

        return expressionNode;
    }

    private ExpressionNode Expresion() throws CompilerException {
        ExpressionNode finalExpression = null;

        if (firstsUnaryExpression().contains(currentToken.getTokenType())) {
            ExpressionNode leftExpression = ExpresionUnaria();
            finalExpression = ExpresionPrima(leftExpression);
        } else {
            throw createSyntaxException(currentToken, "expresion unaria");
        }
        return finalExpression;
    }

    private ExpressionNode ExpresionPrima(ExpressionNode leftExpression) throws CompilerException {
        ExpressionNode finalExpression = null;

        if (firstsBinaryOperands().contains(currentToken.getTokenType())) {
            BinaryExpressionNode binaryExpressionNode = OperadorBinario();
            ExpressionNode rightExpression = ExpresionUnaria(); //lado derecho expr bin
            binaryExpressionNode.setRightExpression(rightExpression);
            binaryExpressionNode.setLeftExpression(leftExpression);
            finalExpression = ExpresionPrima(binaryExpressionNode);
        } else {
            finalExpression = leftExpression;
        }

        return finalExpression;
    }

    private BinaryExpressionNode OperadorBinario() throws CompilerException {
        BinaryExpressionNode binaryExpressionNode = null;

        switch (currentToken.getTokenType()) {
            case OP_OR:
                binaryExpressionNode = new BinaryOrNode(currentToken);
                match(TokenType.OP_OR);
                break;
            case OP_AND:
                binaryExpressionNode = new BinaryAndNode(currentToken);
                match(TokenType.OP_AND);
                break;
            case EQUALS:
                binaryExpressionNode = new BinaryEqualsNode(currentToken);
                match(TokenType.EQUALS);
                break;
            case NOT_EQUALS:
                binaryExpressionNode = new BinaryNotEqualsNode(currentToken);
                match(TokenType.NOT_EQUALS);
                break;
            case GREATER_EQUALS:
                binaryExpressionNode = new BinaryGreaterEqualsNode(currentToken);
                match(TokenType.GREATER_EQUALS);
                break;
            case LESS_EQUALS:
                binaryExpressionNode = new BinaryLessEqualsNode(currentToken);
                match(TokenType.LESS_EQUALS);
                break;
            case GREATER:
                binaryExpressionNode = new BinaryGreaterNode(currentToken);
                match(TokenType.GREATER);
                break;
            case LESS:
                binaryExpressionNode = new BinaryLessNode(currentToken);
                match(TokenType.LESS);
                break;
            case ADD:
                binaryExpressionNode = new BinaryAddNode(currentToken);
                match(TokenType.ADD);
                break;
            case SUB:
                binaryExpressionNode = new BinarySubNode(currentToken);
                match(TokenType.SUB);
                break;
            case MULTIPLY:
                binaryExpressionNode = new BinaryMpyNode(currentToken);
                match(TokenType.MULTIPLY);
                break;
            case DIVIDE:
                binaryExpressionNode = new BinaryDivideNode(currentToken);
                match(TokenType.DIVIDE);
                break;
            case MOD:
                binaryExpressionNode = new BinaryModNode(currentToken);
                match(TokenType.MOD);
                break;
            default:
                throw createSyntaxException(currentToken, "operador binario");
        }
        return binaryExpressionNode;
    }

    private ExpressionNode ExpresionUnaria() throws CompilerException {
        ExpressionNode expressionNode = null;
        if (firstsUnaryOperands().contains(currentToken.getTokenType())) {
            OperadorUnario();
            Operando();
        } else if (firstsOperands().contains(currentToken.getTokenType())) {
            expressionNode = Operando();
        } else {
            throw createSyntaxException(currentToken, "operador unario, operando");
        }

        return expressionNode;
    }

    private UnaryExpressionNode OperadorUnario() throws CompilerException {
        UnaryExpressionNode unaryExpressionNode = null;

        switch (currentToken.getTokenType()) {
            case ADD:
                unaryExpressionNode = new UnaryAddNode(currentToken);
                match(TokenType.ADD);
                break;
            case SUB:
                unaryExpressionNode = new UnarySubNode(currentToken);
                match(TokenType.SUB);
                break;
            case OP_NOT:
                unaryExpressionNode = new UnaryNotNode(currentToken);
                match(TokenType.OP_NOT);
                break;
            default:
                throw createSyntaxException(currentToken, "operador unario");
        }
        return unaryExpressionNode;
    }

    private OperandNode Operando() throws CompilerException {
        OperandNode operandNode = null;
        if (firstsLiteral().contains(currentToken.getTokenType())) {
            operandNode = Literal();
        } else if (firstsAccess().contains(currentToken.getTokenType())) {
            operandNode = Acceso();
        } else {
            throw createSyntaxException(currentToken, "literal, acceso");
        }

        return operandNode;
    }

    private OperandNode Literal() throws CompilerException {
        OperandNode operandNode = null;
        switch (currentToken.getTokenType()) {
            case NULL:
                operandNode = new LiteralNullNode(currentToken);
                match(TokenType.NULL);
                break;
            case TRUE:
                operandNode = new LiteralBooleanNode(currentToken);
                match(TokenType.TRUE);
                break;
            case FALSE:
                operandNode = new LiteralBooleanNode(currentToken);
                match(TokenType.FALSE);
                break;
            case LIT_INT:
                operandNode = new LiteralIntNode(currentToken);
                match(TokenType.LIT_INT);
                break;
            case LIT_CHAR:
                operandNode = new LiteralCharNode(currentToken);
                match(TokenType.LIT_CHAR);
                break;
            case LIT_STRING:
                operandNode = new LiteralStringNode(currentToken);
                match(TokenType.LIT_STRING);
                break;
            default:
                throw createSyntaxException(currentToken, "literal");
        }
        return operandNode;
    }

    private AccessNode Acceso() throws CompilerException {
        AccessNode accessNode = null;

        if (currentToken.getTokenType() == TokenType.PARENTHESES_OPEN) {
            match(TokenType.PARENTHESES_OPEN);
            PrimarioParentesisAbierta();
        } else if (firstsPrimaryWithoutParentheses().contains(currentToken.getTokenType())) {
            accessNode = PrimarioSinParentesis();
            Encadenado();
        } else {
            throw createSyntaxException(currentToken, "(, this, new, idMet o idVar");
        }
        return accessNode;
    }


    private AccessNode PrimarioSinParentesis() throws CompilerException {
        AccessNode accessNode = null;
        switch (currentToken.getTokenType()) {
            case THIS:
                accessNode = AccesoThis();
                break;
            case ID_MET_VAR:
                accessNode = AccesoVarOMet();
                break;
            case NEW:
                accessNode = AccesoConstructor();
                break;
            default:
                throw createSyntaxException(currentToken, "this, new, idMet o idVar");
        }

        return accessNode;
    }

    private void PrimarioParentesisAbierta() throws CompilerException {
        //casting
        if (currentToken.getTokenType() == TokenType.ID_CLASS) {
            match(TokenType.ID_CLASS);
            match(TokenType.PARENTHESES_CLOSE);
            //aca tengo un casting (a)
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

    private AccessNode AccesoThis() throws CompilerException {
        AccessThisNode accessThisNode = null;
        if (currentToken.getTokenType() == TokenType.THIS) {
            accessThisNode = new AccessThisNode(currentToken);
            match(TokenType.THIS);
        } else {
            throw createSyntaxException(currentToken, "this");
        }

        //todo almacenar el id de la clase en la que fue declarada?
        accessThisNode.setRefClassType(TS.getCurrentClass().getTkn());

        return accessThisNode;
    }

    private AccessNode AccesoVarOMet() throws CompilerException {
        AccessNode accessNode = null;
        Token id;
        if (currentToken.getTokenType() == TokenType.ID_MET_VAR) {
            id = currentToken;
            match(TokenType.ID_MET_VAR);
            //si devuelve nulo es porque es una variable, por ahora.


            List<ExpressionNode> actualParams = ArgOVacio();
            if (actualParams == null) {
                accessNode = new AccessVarNode(id);
            } else {
                accessNode = new AccessVarNode(id);

            }
        } else {
            throw createSyntaxException(currentToken, "idMet o idVar");
        }

        return accessNode;
    }

    private AccessNode AccesoConstructor() throws CompilerException {
        AccessConstructorNode accessConstructorNode = null;
        if (currentToken.getTokenType() == TokenType.NEW) {
            accessConstructorNode = new AccessConstructorNode(currentToken);
            match(TokenType.NEW);
            Token tknRef = currentToken;
            match(TokenType.ID_CLASS);

            List<ExpressionNode> actualParams = ArgsActuales();
            accessConstructorNode.setActualParams(actualParams);
            accessConstructorNode.setTknRef(tknRef);
        } else {
            throw createSyntaxException(currentToken, "new idClass");
        }

        return accessConstructorNode;
    }

    //si me devuelve nulo quiere decir que no hay argumentos ni parentesis, o sea, es un acceso var
    private List<ExpressionNode> ArgOVacio() throws CompilerException {
        List<ExpressionNode> actualParams = null;

        if (currentToken.getTokenType() == TokenType.PARENTHESES_OPEN) {
            actualParams = ArgsActuales();
        } else {
        }

        return actualParams;
    }

    private List<ExpressionNode> ArgsActuales() throws CompilerException {
        List<ExpressionNode> actualParams = new ArrayList<>();

        if (currentToken.getTokenType() == TokenType.PARENTHESES_OPEN) {
            match(TokenType.PARENTHESES_OPEN);
            ListaExpsOVacio(actualParams);
            match(TokenType.PARENTHESES_CLOSE);
        } else {
            throw createSyntaxException(currentToken, "args actuales");
        }

        return actualParams;
    }

    private void ListaExpsOVacio(List<ExpressionNode> actualParams) throws CompilerException {
        if (firstsExpression().contains(currentToken.getTokenType())) {
            ListaExps(actualParams);
        } else {
        }

    }

    private void ListaExps(List<ExpressionNode> actualParams) throws CompilerException {
        ExpressionNode expressionNode;

        if (firstsExpression().contains(currentToken.getTokenType())) {
            expressionNode = Expresion();
            actualParams.add(expressionNode);
            ExpresionEncadenada(actualParams);
        } else {
            throw createSyntaxException(currentToken, "expresion");
        }
    }

    private void ExpresionEncadenada(List<ExpressionNode> actualParams) throws CompilerException {
        if (currentToken.getTokenType() == TokenType.COMMA) {
            match(TokenType.COMMA);
            ListaExps(actualParams);
        } else {
        }
    }

    private void Encadenado() throws CompilerException {
        if (currentToken.getTokenType() == TokenType.DOT) {
            //nodo variable puede tener un encadenado
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