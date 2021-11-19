package minijavacompiler.data_structures;

import minijavacompiler.ast_data_structures.sentence_nodes.BlockNode;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

import java.util.HashMap;

public class SymbolTable {
    private static SymbolTable instance;
    private final HashMap<String, Class> classMap;
    private Class currentClass;
    private Unit currentUnit;
    private BlockNode currentBlock;


    private SymbolTable() {
        classMap = new HashMap<>();
        addObject();
        addSystem();
    }

    public static SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
        }
        return instance;
    }

    public void flush() {
        instance = null;
    }

    public void addClass(Class aClass) throws SemanticException {
        if (classMap.containsKey(aClass.getId())) {
            throw new SemanticException(aClass.getId(),
                    aClass.getLine(),
                    "La clase " +
                            aClass.getId() +
                            " ya esta declarada");
        }
        classMap.put(aClass.getId(), aClass);
    }

    public void checkDeclaration() throws SemanticException {
        checkClassDeclaration();
        consolidate();
        if (!checkMainDeclaration()) {
            throw new SemanticException("main", 0, "No se encontro declaracion de metodo main");
        }
    }

    public void checkSentences() throws SemanticException {
        for (Class aClass : classMap.values()) {
            setCurrentClass(aClass);
            aClass.checkSentences();
        }
    }

    private boolean checkMainDeclaration() throws SemanticException {
        boolean exist = false;
        for (Class aClass : classMap.values()) {
            if (aClass.getMethsMap().containsKey("main")) {
                Method main = aClass.getMethsMap().get("main");
                if (!(main.getBinding().equals("static"))) {
                    throw new SemanticException("main", 0, "Acceso incorrecto: deberia ser estatico");
                } else if (!(main.getReturnType().equals(new TypeVoid(new Token(TokenType.VOID, "void", 0))))) {
                    throw new SemanticException("main", 0, "Tipo de retorno incorrecto: deberia ser void");
                } else {
                    exist = true;
                }
            }
        }
        return exist;
    }

    public void checkClassDeclaration() throws SemanticException {
        for (Class aClass : classMap.values()) {
            aClass.check();
        }
    }

    private void consolidate() throws SemanticException {
        for (Class aClass : classMap.values()) {
            aClass.consolidate();
        }
    }

    public Class getClassById(String id) {
        return classMap.get(id);
    }

    public Class getClass(Token token) throws SemanticException {
        if (!(isClassDeclared(token.getLexeme()))) {
            throw new SemanticException(token.getLexeme(),
                    token.getLine(),
                    "La clase " +
                            token.getLexeme() +
                            " no fue declarada.");
        }
        return classMap.get(token.getLexeme());
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

        try {
            miniObject.addMet(getDebugMethod());
        } catch (SemanticException e) {
            e.printStackTrace();
        }

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

    public boolean classConform(String conforma, String conformante) throws SemanticException {
        boolean conforms = false;

        if (conforma.equals(conformante)) {
            conforms = true;
        } else {
            //todo refactor
            Class claseAConformar = getClassById(conforma);
            Class claseConformante = getClassById(conformante);

            conforms = searchForInheritance(claseAConformar, claseConformante);
        }

        return conforms;
    }

    private boolean searchForInheritance(Class claseAConformar, Class claseConformante) throws SemanticException {
        boolean conform = false;

        while (!claseAConformar.getId().equals("Object") && !conform) {
            if (!claseAConformar.getAncestor().getId().equals(claseConformante.getId())) {
                claseAConformar = claseAConformar.getAncestor();
            } else {
                conform = true;
            }
        }

        return conform;
    }
}
