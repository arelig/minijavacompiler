package minijavacompiler.symbol_table;

import minijavacompiler.exceptions.CompilerException;
import minijavacompiler.symbol_table.ast.sentence_nodes.BlockNode;
import minijavacompiler.symbol_table.entities.*;
import minijavacompiler.symbol_table.entities.Class;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.symbol_table.types.TypePrimitive;
import minijavacompiler.symbol_table.types.TypeVoid;

import java.util.HashMap;
import java.util.Stack;

public class SymbolTable {
    private static SymbolTable instance;
    private final HashMap<String, Class> classMap;
    private final Stack<CompilerException> compilerErrors;
    private Method mainMethod;
    private Class currentClass;
    private Unit currentUnit;
    private BlockNode currentBlock;

    public static SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
        }
        return instance;
    }

    private SymbolTable() {
        classMap = new HashMap<>();
        compilerErrors = new Stack<>();
        loadPredefinedClasses();

        mainMethod = null;
        currentBlock = null;
        currentClass = null;
        currentUnit = null;
    }

    private void loadPredefinedClasses() {
        addObject();
        addSystem();
    }

    public void generateCode(){
        for(Class aClass : classMap.values())
            aClass.generateOffsets();
        for(Class aClass : classMap.values())
            aClass.generateCode();
    }

    public void checkDeclaration() {
        checkClassDeclaration();
        checkMainDeclaration();
        if (mainMethod == null) {
            addError(new SemanticException("main", 0, "No se encontró un método static void main()"));
        }
    }

    public void flush() {
        instance = null;
    }

    public void consolidate()  {
        for (Class aClass : classMap.values()) {
            aClass.consolidate();
        }
    }
    private void checkMainDeclaration() {
        for (Class aClass : classMap.values()) {
            if (aClass.getMethods().containsKey("main")) {
                Method main = aClass.getMethods().get("main");
                if (!(main.getBinding().equals("static"))) {
                    addError(new SemanticException("main", 0, "Acceso incorrecto: deberia ser estático."));
                } else if (!(main.getReturnType().equals(new TypeVoid(new Token(TokenType.VOID, "void", 0))))) {
                    addError(new SemanticException("main", 0, "Tipo de retorno incorrecto: deberia ser void"));
                } else {
                    mainMethod = main;
                }
            }
        }
    }

    public void checkSentences() throws SemanticException {
        for (Class aClass : classMap.values()) {
            setCurrentClass(aClass);
            aClass.checkSentences();
        }
    }

    public void addError(CompilerException exception) {
        compilerErrors.push(exception);
    }

    public void addClass(Class newClass) {
        Class existingClass = classMap.get(newClass.getId());

        if (existingClass == null) {
            classMap.put(newClass.getId(), newClass);
        } else {
            existingClass.setErrorDetected();
            String errorMessage = "La clase " + newClass.getId() + " ya fue declarada";
            addError(new SemanticException(newClass.getId(), newClass.getLine(), errorMessage));
        }
    }

    public Class getClass(String id) {
        return classMap.get(id);
    }
    public void checkClassDeclaration() {
        for (Class aClass : classMap.values()) {
            aClass.checkDeclaration();
        }
    }

    public Class getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(Class aClass) {
        this.currentClass = aClass;
    }

    public Unit getCurrentUnit() {
        return currentUnit;
    }

    public void setCurrentUnit(Unit unit) {
        this.currentUnit = unit;
    }

    public boolean isClassDeclared(String idClass) {
        return classMap.containsKey(idClass);
    }

    private void addObject() {
        Class miniObject = new MiniObject(new Token(TokenType.ID_CLASS, "Object", 0));

        miniObject.addMet(getDebugMethod());

        classMap.put("Object", miniObject);
    }

    private Method getDebugMethod() {
        Token tknDebugPrint = new Token(TokenType.ID_MET_VAR,
                "debugPrint",
                0);

        Token tknVoid = new Token(TokenType.VOID,
                "void",
                0);

        Type typeVoid = new TypeVoid(tknVoid);


        Method met = new Method("static",
                typeVoid,
                tknDebugPrint);
        met.setBlock(new BlockNode(tknDebugPrint));
        return met;
    }

    private void addSystem() {
        Class miniSystem = new MiniSystem(new Token(TokenType.ID_CLASS,
                "System",
                0));

        try {
            miniSystem.addMet(getReadMethod());
            miniSystem.addMet(getPrintBMethod());
            miniSystem.addMet(getPrintBlnMethod());
            miniSystem.addMet(getPrintIMethod());
            miniSystem.addMet(getPrintIlnMethod());
            miniSystem.addMet(getPrintSMethod());
            miniSystem.addMet(getPrintSlnMethod());
            miniSystem.addMet(getPrintCMethod());
            miniSystem.addMet(getPrintClnMethod());
            miniSystem.addMet(getPrintlnMethod());
        } catch (SemanticException e) {
            e.printStackTrace();
        }

        classMap.put("System", miniSystem);
    }

    private Method getPrintBMethod() throws SemanticException {
        Parameter paramBoolean = new Parameter(getBooleanType(),
                new Token(TokenType.ID_MET_VAR,
                        "b",
                        0));

        Token tknPrintB = new Token(TokenType.ID_MET_VAR,
                "printB",
                0);

        Method m = new Method("static",
                getVoidType(),
                tknPrintB);

        m.addParam(paramBoolean);
        m.setBlock(new BlockNode(tknPrintB));

        return m;
    }

    private Method getPrintBlnMethod() throws SemanticException {
        Parameter paramBoolean = new Parameter(getBooleanType(),
                new Token(TokenType.ID_MET_VAR,
                        "b",
                        0));

        Token tknPrintBln = new Token(TokenType.ID_MET_VAR,
                "printBln",
                0);

        Method m = new Method("static",
                getVoidType(),
                tknPrintBln);

        m.addParam(paramBoolean);
        m.setBlock(new BlockNode(tknPrintBln));

        return m;
    }

    private Method getPrintSMethod() throws SemanticException {
        Parameter paramString = new Parameter(getStringType(),
                new Token(TokenType.ID_MET_VAR,
                        "s",
                        0));

        Token tknPrintS = new Token(TokenType.ID_MET_VAR,
                "printS",
                0);

        Method m = new Method("static",
                getVoidType(),
                tknPrintS);
        m.addParam(paramString);
        m.setBlock(new BlockNode(tknPrintS));
        return m;
    }

    private Method getPrintSlnMethod() throws SemanticException {
        Parameter paramString = new Parameter(getStringType(),
                new Token(TokenType.ID_MET_VAR,
                        "s",
                        0));

        Token tknPrintSln = new Token(TokenType.ID_MET_VAR,
                "printSln",
                0);

        Method m = new Method("static",
                getVoidType(),
                tknPrintSln);

        m.addParam(paramString);
        m.setBlock(new BlockNode(tknPrintSln));

        return m;
    }

    private Method getPrintCMethod() throws SemanticException {
        Parameter paramChar = new Parameter(getCharType(),
                new Token(TokenType.ID_MET_VAR,
                        "c",
                        0));

        Token tknPrintC = new Token(TokenType.ID_MET_VAR,
                "printC",
                0);

        Method m = new Method("static",
                getVoidType(),
                tknPrintC);
        m.addParam(paramChar);
        m.setBlock(new BlockNode(tknPrintC));

        return m;
    }

    private Method getPrintClnMethod() throws SemanticException {
        Parameter paramChar = new Parameter(getCharType(),
                new Token(TokenType.ID_MET_VAR,
                        "c",
                        0));

        Token tknPrintCln = new Token(TokenType.ID_MET_VAR,
                "printCln",
                0);

        Method m = new Method("static",
                getVoidType(),
                tknPrintCln);

        m.addParam(paramChar);
        m.setBlock(new BlockNode(tknPrintCln));

        return m;
    }

    private Method getPrintIMethod() throws SemanticException {
        Parameter paramInt = new Parameter(getIntType(),
                new Token(TokenType.ID_MET_VAR,
                        "i",
                        0));

        Token tknPrintI = new Token(TokenType.ID_MET_VAR,
                "printI",
                0);


        Method m = new Method("static",
                getVoidType(),
                tknPrintI);

        m.addParam(paramInt);
        m.setBlock(new BlockNode(tknPrintI));

        return m;
    }

    private Method getPrintIlnMethod() throws SemanticException {
        Parameter paramInt = new Parameter(getIntType(),
                new Token(TokenType.ID_MET_VAR,
                        "i",
                        0));

        Token tknPrintIln = new Token(TokenType.ID_MET_VAR,
                "printIln",
                0);


        Method m = new Method("static",
                getVoidType(),
                tknPrintIln);

        m.addParam(paramInt);
        m.setBlock(new BlockNode(tknPrintIln));

        return m;
    }

    private Method getPrintlnMethod() throws SemanticException {
        Token tknPrintln = new Token(TokenType.ID_MET_VAR,
                "println",
                0);


        Method m = new Method("static",
                getVoidType(),
                tknPrintln);

        m.setBlock(new BlockNode(tknPrintln));

        return m;
    }

    private Type getStringType() {
        Token token = new Token(TokenType.PR_STRING,
                "String",
                0);

        return new TypePrimitive(token);
    }

    private Type getVoidType() {
        Token token = new Token(TokenType.VOID,
                "void",
                0);

        return new TypeVoid(token);
    }

    private Type getBooleanType() {
        Token token = new Token(TokenType.PR_BOOLEAN,
                "boolean",
                0);

        return new TypePrimitive(token);
    }

    private Type getIntType() {
        Token token = new Token(TokenType.PR_INT,
                "int",
                0);

        return new TypePrimitive(token);
    }

    private Type getCharType() {
        Token token = new Token(TokenType.PR_CHAR,
                "char",
                0);

        return new TypePrimitive(token);
    }

    private Method getReadMethod() {
        Token tknMet = new Token(TokenType.ID_MET_VAR,
                "read",
                0);

        Type typePrimitive = getIntType();

        Method m = new Method("static",
                typePrimitive,
                tknMet);
        m.setBlock(new BlockNode(tknMet));
        return m;
    }

    public BlockNode getCurrentBlock() {
        return currentBlock;
    }

    public void setCurrentBlock(BlockNode currentBlock) {
        this.currentBlock = currentBlock;
    }

    public HashMap<String, Class> getClassMap() {
        return classMap;
    }

//    public void throwErrorsAfterConsolidate(){
//        throw new SemanticException(compilerErrors);
//    }





}
