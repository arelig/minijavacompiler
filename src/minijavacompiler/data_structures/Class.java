package minijavacompiler.data_structures;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class Class extends Entity {
    private final Token token;
    private final List<Constructor> constrList;
    private final HashMap<String, Attribute> attrsMap;
    private final HashMap<String, Method> methsMap;
    private Token ancestor;
    private boolean defaultConstr;
    private boolean consolidated = false;

    public Class(Token tokenClass) {
        setId(tokenClass.getLexeme());
        setLine(tokenClass.getLine());
        token = tokenClass;

        constrList = new ArrayList<>();
        attrsMap = new HashMap<>();
        methsMap = new HashMap<>();
    }


    public void addAttr(Attribute attr) throws SemanticException {
        if (attrsMap.get(attr.getId()) != null) {
            throw new SemanticException(attr.getId(),
                    attr.getLine(),
                    "El atributo " +
                            attr.getId() +
                            " ya fue declarado.");
        }
        attrsMap.put(attr.getId(), attr);
    }

    public void addConstructor(Constructor constructor) throws SemanticException {
        for (Constructor constr : constrList) {
            if (constructor.checkParameters(constr.getParamsAsList())) {
                throw new SemanticException(constructor.getId(),
                        constructor.getLine(),
                        "El constructor ya fue declarado");
            }
        }

        constrList.add(constructor);

        this.defaultConstr = false;
    }

    public void addMet(Method method) throws SemanticException {
        for (Method myMethod : methsMap.values()) {
            if (methsMap.containsKey(method.getId())) {
                if (method.checkParameters(myMethod.getParamsAsList())) {
                    throw new SemanticException(method.getId(),
                            method.getLine(),
                            "El metodo " +
                                    method.getId() +
                                    " ya fue declarado.");
                }
            }
        }

        methsMap.put(method.getId(), method);
    }

    public void addAncestor(Token ancestor) {
        this.ancestor = ancestor;
    }

    public Class getAncestor() throws SemanticException {
        return SymbolTable.getInstance().getClass(ancestor);
    }

    public Token getTkn() {
        return token;
    }

    private Token getTknAncestor() {
        return ancestor;
    }

    public void check() throws SemanticException {
        checkAncestor();
        checkForCircularInheritance();
        checkAttrs();
        checkConstr();
        checkMeths();
    }

    public HashMap<String, Method> getMethsMap() {
        return methsMap;
    }

    public HashMap<String, Attribute> getAttrsMap() {
        return attrsMap;
    }

    public void consolidate() throws SemanticException {
        if (!consolidated) {
            if (!(getAncestor().isConsolidated())) {
                getAncestor().consolidate();
            }
            consolidateAttrs();
            consolidateMethods();
            consolidated = true;
        }
    }

    private void consolidateMethods() throws SemanticException {
        HashMap<String, Method> ancestorMethods = getAncestor().getMethsMap();

        for (Method inheritedMethod : ancestorMethods.values()) {
            if (methsMap.containsKey(inheritedMethod.getId())) {
                if (!(checkOverwritten(inheritedMethod))) {
                    if (checkOverload(inheritedMethod)) {
                        methsMap.put(inheritedMethod.getId(), inheritedMethod);
                    }
                }
            } else {
                methsMap.put(inheritedMethod.getId(), inheritedMethod);
            }
        }
    }

    private boolean checkOverwritten(Method method) throws SemanticException {
        if (!checkBinding(methsMap.get(method.getId()), method)) {
            throw new SemanticException(methsMap.get(method.getId()).getId(),
                    methsMap.get(method.getId()).getLine(),
                    "El acceso para el metodo " +
                            methsMap.get(method.getId()).getId() +
                            " no coincide para redefinicion");
        } else if (!checkReturnType(methsMap.get(method.getId()), method)) {
            throw new SemanticException(methsMap.get(method.getId()).getId(),
                    methsMap.get(method.getId()).getLine(),
                    "El retorno para el metodo " +
                            methsMap.get(method.getId()).getId() +
                            " no coincide para redefinicion");
        } else if (!checkParameters(methsMap.get(method.getId()), method)) {
            throw new SemanticException(methsMap.get(method.getId()).getId(),
                    methsMap.get(method.getId()).getLine(),
                    "Los parametros de + " +
                            methsMap.get(method.getId()).getId() +
                            " no coincide para redefinicion");
        }

        return true;
    }

    private boolean checkOverload(Method inheritedMethod) throws SemanticException {
        boolean overload = false;

        if (checkBinding(methsMap.get(inheritedMethod.getId()), inheritedMethod) &&
                (checkReturnType(methsMap.get(inheritedMethod.getId()), inheritedMethod))) {
            if (!(checkParameters(methsMap.get(inheritedMethod.getId()), inheritedMethod))) {
                overload = true;
            }
        }

        return overload;
    }


    private boolean checkReturnType(Method myMethod, Method inheritedMethod) {
        return myMethod.getReturnType().equals(inheritedMethod.getReturnType());
    }

    private boolean checkParameters(Method myMethod, Method inheritedMethod) {
        return myMethod.checkParameters(inheritedMethod.getParamsAsList());
    }

    private void overloading(Method m) throws SemanticException {
        throw new SemanticException(m.getId(),
                m.getLine(),
                "Los parametros no se corresponden para sobrecarga");
    }

    private boolean checkBinding(Method myMethod, Method inheritedMethod) {
        return myMethod.getBinding().equals(inheritedMethod.getBinding());
    }

    private void consolidateAttrs() throws SemanticException {
        Collection<Attribute> inheritedAttrs = getAncestor().getAttrsMap().values();

        for (Attribute inheritedAttr : inheritedAttrs) {
            if (inheritedAttr.getVisibility().equals("public")) {
                if (!(attrsMap.containsKey(inheritedAttr.getId()))) {
                    attrsMap.put(inheritedAttr.getId(), inheritedAttr);
                }
            }
        }
    }

    public void setConsolidated() {
        consolidated = true;
    }

    private boolean isConsolidated() {
        return consolidated;
    }

    private void checkAncestor() throws SemanticException {
        checkImplicitInheritance();
        checkExplicitInheritance();
    }

    private void checkImplicitInheritance() {
        if (ancestor == null) {
            ancestor = new Token(TokenType.ID_CLASS, "Object", 0);
        }
    }

    private void checkExplicitInheritance() throws SemanticException {
        if (!(SymbolTable.getInstance().isClassDeclared(ancestor.getLexeme()))) {
            throw new SemanticException(ancestor.getLexeme(),
                    ancestor.getLine(),
                    "La clase " +
                            ancestor.getLexeme() +
                            " no fue declarada");
        }
    }

    private void checkForCircularInheritance() throws SemanticException {
        Token inheritance = ancestor;

        while (!(inheritance.getLexeme().equals("Object"))) {
            if (this.getId().equals(inheritance.getLexeme())) {
                throw new SemanticException(inheritance.getLexeme(),
                        inheritance.getLine(),
                        "La clase " +
                                inheritance.getLexeme() +
                                " y " +
                                this.getId() +
                                "forman una herencia circular");
            } else {
                inheritance = SymbolTable.getInstance().
                        getClass(inheritance).
                        getTknAncestor();
            }
        }
    }

    private void checkConstr() throws SemanticException {
        checkImplicitConstructor();
        checkSameIdClass();

        for (Constructor constr : constrList) {
            constr.check();
        }
    }

    private void checkImplicitConstructor() {
        if (defaultConstr == true) {
            Token token = new Token(TokenType.ID_CLASS, this.getId(), 0);
            constrList.add(new Constructor(token));
        }
    }

    private void checkSameIdClass() throws SemanticException {
        for (Constructor constr : constrList) {
            if (!(constr.getId().equals(this.getId()))) {
                throw new SemanticException(constr.getId(),
                        constr.getLine(),
                        "IdConstructor " +
                                constr.getId() +
                                " no coincide con IdClase " +
                                this.getId());
            }
        }
    }

    private void checkAttrs() throws SemanticException {
        for (Attribute attr : attrsMap.values()) {
            attr.check();
        }
    }

    private void checkMeths() throws SemanticException {
        for (Method met : methsMap.values()) {
            met.check();
        }
    }

    public boolean isAttrDeclared(String id) {
        return attrsMap.containsKey(id);
    }


    public void checkSentences() throws SemanticException {
        for (Method method : methsMap.values()) {
            SymbolTable.getInstance().setCurrentUnit(method);
            method.checkSentences();
        }

        for (Constructor constructor : constrList) {
            SymbolTable.getInstance().setCurrentUnit(constructor);
            constructor.checkSentences();
        }
    }
}
