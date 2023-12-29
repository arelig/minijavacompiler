package minijavacompiler.symbol_table.entities;

import minijavacompiler.ST;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.symbol_table.SymbolTable;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.symbol_table.types.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;

public class Constructor extends Unit {
    private final HashMap<String, Parameter> paramsMap;
    private boolean errorDetected;

    public Constructor(Token token) {
        setId(token.getLexeme());
        setLine(token.getLine());

        paramsMap = new HashMap<>();
        paramsAsList = new ArrayList<>();

        errorDetected = false;
    }

    public boolean errorDetected(){
        return errorDetected;
    }

    public void setErrorDetected(){
        errorDetected = true;
    }
    public void checkDeclaration() {
        if(!errorDetected) {
            for (Parameter parameter : getParamsAsList()) {
                if(!parameter.checkDeclaration()){
                    setErrorDetected();
                }
            }
        }
    }

    public void addParam(Parameter param) {
        if (paramsMap.containsKey(param.getId())) {
            ST.symbolTable.addError(new SemanticException(param.getId(), param.getLine(),
                    "Parametro " + param.getId() + " ya declarado."));
        }
        paramsMap.put(param.getId(), param);
        getParamsAsList().add(param);
    }

    @Override
    public Type getReturnType() {
        //Los constructores, a diferencia de los métodos, tienen como tipo de retorno el tipo de la clase del
        //objeto construido
        Class referencedClass = ST.symbolTable.getClass(getId());
        return new TypeReference(referencedClass.getToken());
    }
    @Override
    public HashMap<String, Parameter> getParamsMap() {
        return paramsMap;
    }

}
