package minijavacompiler.symbol_table.entities;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.code_generator.TagManager;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.symbol_table.SymbolTable;
import minijavacompiler.symbol_table.types.Type;

import java.util.HashMap;
import java.util.LinkedList;

public class Method extends Unit {
    private static final SymbolTable ST = SymbolTable.getInstance();
    private final String binding;
    private final Type returnType;
    private final HashMap<String, Parameter> params;
    private boolean errorDetected;
    private Class referencedClass;
    private final LinkedList<Method> redefinitions;
    private final LinkedList<Integer> offsets;
    private String methodTag;

    public Method(String binding, Type returnType, Token token) {
        setId(token.getLexeme());
        setLine(token.getLine());

        this.binding = binding;
        this.returnType = returnType;
        this.params = new HashMap<>();
        paramsAsList = new LinkedList<>();

        redefinitions = new LinkedList<>();
        offsets = new LinkedList<>();
        errorDetected = false;
    }

    public boolean errorDetected(){
        return errorDetected;
    }

    public void setErrorDetected(){
        errorDetected = true;
    }

    public void generateCode(){
        if(ST.getCurrentClass() == referencedClass){
            ST.setCurrentUnit(this);
            generateCodeForMethod();
            getBlock().generateCode();
            generateMethodForReturn();
        }
    }

    private void generateMethodForReturn() {
        CodeGenerator.generateCode("STOREFP ;unstack activation record");
        CodeGenerator.generateCode("RET " + params.size() + (binding.equals("static") ? 0 : 1) + " ; method return");
    }

    private void generateCodeForMethod() {
        CodeGenerator.setNextInstructionTag(this.methodTag);
        CodeGenerator.generateCode("LOADFP ;dynamic link");
        CodeGenerator.generateCode("LOADSP ;stack activation record (1)");
        CodeGenerator.generateCode("STOREFP ;stack activation record (2)");
    }

    public String getTag(){
        if(methodTag == null){
            methodTag = TagManager.getTag(getId() + "@" + referencedClass.getId());
        }
        return methodTag;
    }

    public void addOffset(int offset){
        offsets.add(offset);
        for(Method method : redefinitions){
            method.addOffset(offset);
        }
    }
    public int getOffset(){
        if(!offsets.isEmpty()){
            return offsets.getFirst();
        }else{
            return -1;
        }
    }

    public LinkedList<Integer> getOffsets(){
        return offsets;
    }
    public Type getReturnType() {
        return returnType;
    }

    public String getBinding() {
        return binding;
    }

    @Override
    public void addParam(Parameter param){
        if (params.containsKey(param.getId())) {
            setErrorDetected();
            ST.addError(new SemanticException(param.getId(), param.getLine(),
                    "Parametro " + param.getId() + " ya declarado"));
        }
        params.put(param.getId(), param);
        paramsAsList.add(param);
    }

    public void checkDeclaration() {
        if(!errorDetected){
            returnType.checkDeclaration();
            checkParams();
        }
    }

    private void checkParams() {
        for (Parameter param : getParamsAsList()) {
            param.checkDeclaration();
        }
    }

    @Override
    public HashMap<String, Parameter> getParamsMap() {
        return params;
    }

    public void addRedefinition(Method method) {
        redefinitions.add(method);
    }

    public void setReferencedClass(Class referencedClass) {
        this.referencedClass = referencedClass;
    }
}
