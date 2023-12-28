package minijavacompiler.symbol_table.entities;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.code_generator.TagManager;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.symbol_table.SymbolTable;

import java.util.*;


public class Class extends Entity {
    private static final SymbolTable ST = SymbolTable.getInstance();
    private final Token token;
    private Token ancestor;
    private final HashSet<String> ancestorsSet;
    private final LinkedList<Class> descendantList;
    private final HashMap<String, Method> methsMap;
    private final HashMap<String, Attribute> attrsMap;
    private final LinkedList<Constructor> constrList;
    private boolean consolidated;
    private final HashMap<Integer, Method> offsetMethods;
    private int maxMethodOffset;
    private String vTableTag;
    private boolean offsetGenerated;
    private boolean circularInheritance;
    private boolean errorDetected;

    public Class(Token tokenClass) {
        setId(tokenClass.getLexeme());
        setLine(tokenClass.getLine());

        token = tokenClass;
        constrList = new LinkedList<>();
        attrsMap = new HashMap<>();
        methsMap = new HashMap<>();
        ancestorsSet = new HashSet<>();
        descendantList = new LinkedList<>();
        offsetMethods = new HashMap<>();

        errorDetected = false;
        circularInheritance = false;
        consolidated = false;
        offsetGenerated = false;
    }

    public void generateOffsets() {
        if (!offsetGenerated) {
            generateOffsetsForAncestor();
            assignOffsetsToAttributes();

            int methodOffset = calculateMinMethodOffset();
            int offsetIndex = 0;

            assignOffsetsToNonStaticMethods(methodOffset, offsetIndex);
            offsetGenerated = true;
        }
    }

    private void generateOffsetsForAncestor() {
        if (ancestor != null) {
            ST.getClass(ancestor.getLexeme()).generateOffsets();
        }
    }

    private void assignOffsetsToAttributes() {
        int attributeIndex = 0;
        for (Attribute attr : attrsMap.values()) {
            attr.setOffset(attributeIndex++);
        }
    }

    private int calculateMinMethodOffset() {
        return getMinMethodOffset();
    }
    private int getMinMethodOffset() {
        int minOffset = 0;
        if (ancestor != null) {
            Class ancestorClass = ST.getClass(ancestor.getLexeme());
            if (ancestorClass != null) {
                int maxParentMethodOffset = findMaxNonStaticMethodOffset(ancestorClass);
                minOffset = maxParentMethodOffset + 1;
            }
        }
        return minOffset;
    }

    private int findMaxNonStaticMethodOffset(Class ancestorClass) {
        int maxParentMethodOffset = -1;

        for (Method method : ancestorClass.getMethods().values()) {
            if (!method.getBinding().equals("static")) {
                int methodMaxOffset = findMaxOffset(method);
                maxParentMethodOffset = Math.max(maxParentMethodOffset, methodMaxOffset);
            }
        }

        return maxParentMethodOffset;
    }

    private int findMaxOffset(Method method) {
        int maxOffset = -1;
        for (int offset : method.getOffsets()) {
            maxOffset = Math.max(maxOffset, offset);
        }
        return maxOffset;
    }

    private void assignOffsetsToNonStaticMethods(int methodOffset, int offsetIndex) {
        for (Method method : methsMap.values()) {
            if (!method.getBinding().equals("static")) {
                assignOffsetToNonStaticMethod(method, methodOffset, offsetIndex++);
            }
        }
    }
    private void assignOffsetToNonStaticMethod(Method stMethod, int methodOffset, int offsetIndex) {
        if (stMethod.getOffsets().isEmpty()) {
            stMethod.addOffset(methodOffset + offsetIndex);
        }
        offsetMethods.put(stMethod.getOffsets().getLast(), stMethod);
    }

    public void generateCode(){
        ST.setCurrentClass(this);
        loadVTable();
        for (Method method : methsMap.values()) {
            method.generateCode();
        }
    }

    public void loadVTable() {
        int maxMethodOffset = calculateMaxMethodOffset();
        if (maxMethodOffset > -1) {
            String methodsTags = generateMethodsTags(maxMethodOffset);

            generateVTableCode(methodsTags);
        }
    }

    private int calculateMaxMethodOffset() {
        int maxMethodOffset = getMaxMethodOffset();
        calculateOffsetsMethods();
        return maxMethodOffset;
    }

    private String generateMethodsTags(int maxMethodOffset) {
        StringBuilder methodsTags = new StringBuilder();
        for (int i = 1; i <= maxMethodOffset; i++) {
            Method method = offsetMethods.get(i);
            if (method == null) {
                methodsTags.append(0).append(", ");
            } else {
                methodsTags.append(method.getTag()).append(",");
            }
        }
        return methodsTags.substring(0, methodsTags.length() - 1);
    }

    private void generateVTableCode(String methodsTags) {
        String vTableHeader = ".DATA ; VTable for " + getId();
        String vTableTag = getVTableTag();
        String vTableData = "DWORD " + methodsTags;
        String codeHeader = ".CODE ; code for defined methods in " + getToken().getLexeme();

        CodeGenerator.generateCode(vTableHeader);
        CodeGenerator.setNextInstructionTag(vTableTag);
        CodeGenerator.generateCode(vTableData);
        CodeGenerator.generateCode(codeHeader);
    }
    private void calculateOffsetsMethods() {
        for(Method method : methsMap.values()){
            for(int offset : method.getOffsets()){
                offsetMethods.put(offset, method);
            };
        }
    }
    public int getMaxMethodOffset(){
        if(maxMethodOffset == -1){
            for (int offset : offsetMethods.keySet()) {
                maxMethodOffset = Math.max(maxMethodOffset, offset);
            }
        }
        return maxMethodOffset;
    }

    public void setMaxMethodOffset(int offset){
        maxMethodOffset = Math.max(maxMethodOffset, offset);
        for (Class descendant : descendantList) {
            descendant.setMaxMethodOffset(offset);
        }
    }

    public String getVTableTag(){
        if(vTableTag == null){
            vTableTag = TagManager.getTag("VT_" + getToken().getLexeme());
        }
        return vTableTag;
    }

    public void setErrorDetected(){
        errorDetected = true;
    }

    public boolean errorDetected(){
        return errorDetected;
    }

    public void checkDeclaration() {
        if(!errorDetected) {
            checkInheritance();
            checkImplicitConstructor();
            checkAttrs();
            checkMeths();
            checkConstr();
        }
    }

    private void checkInheritance(){
        if(ancestor != null){
            if(ST.isClassDeclared(ancestor.getLexeme())){
                ancestorsSet.add(ancestor.getLexeme());
                if(checkForCircularInheritance(ancestor)){
                    circularInheritance = true;
                    errorDetected = true;
                    ST.addError(new SemanticException(ancestor.getLexeme(),
                            ancestor.getLine(),
                            "La clase "
                                    + ancestor.getLexeme()
                                    + " forman una herencia circular."));
                } else {
                    errorDetected = true;
                    ST.addError(new SemanticException(ancestor.getLexeme(),
                            ancestor.getLine(),
                            "La clase " +  ancestor.getLexeme() + " no fue declarada."));
                }
            }
        }
    }
    private void checkImplicitConstructor() {
        if(constrList.isEmpty()){
            constrList.add(new Constructor(this.token));
        }
    }
    private void checkMeths()  {
        for (Method met : methsMap.values()) {
            if(!met.errorDetected()) {
                met.checkDeclaration();
            }
        }
    }
    private void checkAttrs()  {
        for (Attribute attr : attrsMap.values()) {
            if(!attr.errorDetected()) {
                attr.checkDeclaration();
            }
        }
    }
    private void checkConstr() {
        checkSameIdClass();

        for (Constructor constr : constrList) {
            if(!constr.errorDetected()) {
                constr.checkDeclaration();
            }
        }
    }
    private boolean checkForCircularInheritance(Token inheritance) {
        boolean circular;

        if(inheritance.getLexeme().equals("Object")){
            circular = false;
        }else if(ancestorsSet.contains(ancestor.getLexeme())){
            circular = true;
        }else{
            ancestorsSet.add(inheritance.getLexeme());
            circular = checkForCircularInheritance(ST.getClass(inheritance.getLexeme()).getAncestorToken());
        }

        return circular;
    }

    private boolean checkOverwritten(Method method) {
        Method myMethod = methsMap.get(method.getId());

        if (!checkBinding(myMethod, method)) {
            ST.addError(new SemanticException(myMethod.getId(), myMethod.getLine(),
                    "El acceso para el método " + myMethod.getId() + " no coincide para redefinición"));
            return false;
        } else if (!checkReturnType(myMethod, method)) {
            ST.addError(new SemanticException(myMethod.getId(), myMethod.getLine(),
                    "El retorno para el método " + myMethod.getId() + " no coincide para redefinición"));
            return false;
        } else if (!checkParameters(myMethod, method)) {
            ST.addError(new SemanticException(myMethod.getId(), myMethod.getLine(),
                    "Los parámetros de " + myMethod.getId() + " no coinciden para redefinición"));
            return false;
        }
        method.addRedefinition(myMethod);

        return true;
    }

    private boolean checkBinding(Method myMethod, Method inheritedMethod) {
        return myMethod.getBinding().equals(inheritedMethod.getBinding());
    }

    private boolean checkOverload(Method inheritedMethod) {
        boolean bindingCheck = checkBinding(methsMap.get(inheritedMethod.getId()), inheritedMethod);
        boolean returnTypeCheck = checkReturnType(methsMap.get(inheritedMethod.getId()), inheritedMethod);
        boolean parametersCheck = checkParameters(methsMap.get(inheritedMethod.getId()), inheritedMethod);

        return bindingCheck && returnTypeCheck && !parametersCheck;
    }

    private boolean checkReturnType(Method myMethod, Method inheritedMethod) {
        return myMethod.getReturnType().equals(inheritedMethod.getReturnType());
    }

    private boolean checkParameters(Method myMethod, Method inheritedMethod) {
        return myMethod.checkParameters(inheritedMethod.getParamsAsList());
    }

    private void checkSameIdClass() {
        for (Constructor constr : constrList) {
            if (!(constr.getId().equals(this.getId()))) {
                ST.addError(new SemanticException(constr.getId(),
                        constr.getLine(),
                        "IdConstructor " +
                                constr.getId() +
                                " no coincide con IdClase " +
                                this.getId()));
            }
        }
    }
    public void consolidate()  {
        if(!consolidated){
            if(!circularInheritance) {
                if(ancestor != null) {
                    Class ancestorClass = ST.getClass(ancestor.getLexeme());
                    if(ancestorClass != null){
                        descendantList.add(this);
                        ancestorClass.consolidate();
                        if(!ancestorClass.errorDetected()){
                            consolidateMethods();
                            consolidateAttrs();
                        }
                    }
                }
            }
            consolidated = true;
        }
    }
    private void consolidateMethods() {
        HashMap<String, Method> ancestorMethods = getAncestorClass().getMethods();

        ancestorMethods.values().forEach(inheritedMethod -> {
            if (!inheritedMethod.errorDetected()) {
                if (methsMap.containsKey(inheritedMethod.getId())) {
                    if (!checkOverwritten(inheritedMethod)) {
                        if (checkOverload(inheritedMethod)) {
                            methsMap.put(inheritedMethod.getId(), inheritedMethod);
                        }
                    }
                } else {
                    methsMap.put(inheritedMethod.getId(), inheritedMethod);
                }
            }
        });
    }
    private void consolidateAttrs() {
        HashMap<String, Attribute> ancestorAttrs = getAncestorClass().getAttributes();

        ancestorAttrs.values().forEach(inheritedAttr -> {
            if (!inheritedAttr.errorDetected()) {
                if(inheritedAttr.getVisibility().equals("public")) {
                    if (!(attrsMap.containsKey(inheritedAttr.getId()))) {
                        attrsMap.put(inheritedAttr.getId(), inheritedAttr);
                    }else{
                        attrsMap.put(inheritedAttr.getId() + "\\" + inheritedAttr.getClass(), inheritedAttr);
                    }
                }
            }
        });

    }

    public void setConsolidated() {
        consolidated = true;
    }

    private boolean isConsolidated() {
        return consolidated;
    }

    public void addMet(Method method) {
        for (Method myMethod : methsMap.values()) {
            if (methsMap.containsKey(method.getId())) {
                if (method.checkParameters(myMethod.getParamsAsList())) {
                    ST.addError(new SemanticException(method.getId(),
                            method.getLine(),
                            "El metodo " +
                                    method.getId() +
                                    " ya fue declarado."));
                }
            }
        }
        method.setReferencedClass(this);
        methsMap.put(method.getId(), method);
    }

    public void addAttr(Attribute attr) {
        if (attrsMap.get(attr.getId()) != null) {
            ST.addError(new SemanticException(attr.getId(),
                    attr.getLine(),
                    "El atributo " +
                            attr.getId() +
                            " ya fue declarado."));
        }
        attrsMap.put(attr.getId(), attr);
    }

    public void addConstructor(Constructor constructor) {
        for (Constructor constr : constrList) {
            if (constructor.checkParameters(constr.getParamsAsList())) {
                ST.addError(new SemanticException(constructor.getId(),
                        constructor.getLine(),
                        "El constructor ya fue declarado"));
            }
        }

        constrList.add(constructor);

    }

    public void setDirectAncestor(Token ancestor) {
        this.ancestor = ancestor;
    }
    public Class getAncestorClass() {
        return ST.getClass(ancestor.getLexeme());
    }

    private Token getAncestorToken() {
        return ancestor;
    }

    public Set<String> getAncestors() {
        return ancestorsSet;
    }

    public HashMap<String, Method> getMethods() {
        return methsMap;
    }

    public Method getMethod(String id) {
        return methsMap.get(id);
    }

    public Attribute getAttribute(String id){
        return attrsMap.get(id);
    }

    public boolean hasDynamicMethods(){
        return !offsetMethods.isEmpty();
    }

    public HashMap<String, Attribute> getAttributes() {
        return attrsMap;
    }

    public List<Constructor> getConstructors() {
        return constrList;
    }

    public boolean isAttrDeclared(String id) {
        return attrsMap.containsKey(id);
    }

    public Token getToken() {
        return token;
    }

    public void checkSentences() throws SemanticException {
        for (Method method : methsMap.values()) {
            ST.setCurrentUnit(method);
            method.checkSentences();
        }

        for (Constructor constructor : constrList) {
            ST.setCurrentUnit(constructor);
            constructor.checkSentences();
        }
    }

}
