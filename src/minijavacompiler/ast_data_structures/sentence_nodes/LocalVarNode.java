package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.ast_data_structures.expression_nodes.ExpressionNode;
import minijavacompiler.data_structures.SymbolTable;
import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class LocalVarNode extends SentenceNode {
    private Type type;
    private Token tknAssignInit;
    private ExpressionNode init;

    public LocalVarNode(Token token) {
        super(token);
    }

    public void validate() throws SemanticException {
        validateInCurrentBlock();
        validateInit();
    }

    private void validateInit() throws SemanticException {
        if (init != null) {
            Type initType = init.validate();
            if (!initType.conform(type)) {
                throw new SemanticException(tknAssignInit.getLexeme(), tknAssignInit.getLine(), "Conformacion de tipos incompatible");
            }
        }
    }

    private void validateInCurrentBlock() throws SemanticException {
        boolean isDeclaredOuterBlock = false;
        boolean isDeclaredInnerBlock = isDeclaredInnerBlock();

        if (SymbolTable.getInstance().getCurrentBlock().getOuterBlock() != null) {
            isDeclaredOuterBlock = isDeclaredOuterBlock();
        }

        if (isDeclaredInnerBlock || isDeclaredOuterBlock) {
            throw new SemanticException(getId(), getLine(), "Variable duplicada en el alcance");
        }

        SymbolTable.getInstance().getCurrentBlock().addLocalVar(this);
    }

    private boolean isDeclaredOuterBlock() {
        boolean isDeclaredParams = SymbolTable.getInstance().getCurrentBlock().getOuterBlock().containsUnitParam(getId());
        boolean isDeclaredLocalVar = SymbolTable.getInstance().getCurrentBlock().getOuterBlock().containsLocalVar(getId());

        return isDeclaredParams || isDeclaredLocalVar;
    }

    private boolean isDeclaredInnerBlock() {
        boolean isDeclaredInParam = SymbolTable.getInstance().getCurrentBlock().containsUnitParam(getId());
        boolean isDeclaredLocal = SymbolTable.getInstance().getCurrentBlock().containsLocalVar(getId());

        return isDeclaredInParam || isDeclaredLocal;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ExpressionNode getInit() {
        return init;
    }

    public void setInit(ExpressionNode init) {
        this.init = init;
    }

    @Override
    public void generateCode() {
        //offset cuando termina un bloque la saco y la vuelvo a meter con metodo actual.
        //proceso por bloque. cuando salgo de un bloque saco las variables deberia acomodar cual es el ultimo offset libre
    }

    public void setTknAssignInit(Token tknAssignInit) {
        this.tknAssignInit = tknAssignInit;
    }
}
