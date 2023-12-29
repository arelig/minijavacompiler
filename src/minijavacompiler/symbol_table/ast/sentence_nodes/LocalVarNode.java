package minijavacompiler.symbol_table.ast.sentence_nodes;

import minijavacompiler.ST;
import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.lexical_parser.TokenType;
import minijavacompiler.symbol_table.ast.expression_nodes.ExpressionNode;
import minijavacompiler.symbol_table.SymbolTable;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.symbol_table.types.TypeNull;
import minijavacompiler.symbol_table.types.TypeVoid;

public class LocalVarNode extends SentenceNode {
    private Token tknAssignment;
    private Type type;
    private ExpressionNode value;
    private int offset;

    public LocalVarNode(Token token) {
        super(token);
    }

    @Override
    public void generateCode() {
        if(value != null) {
            value.generateCode();
            CodeGenerator.setComment("Local variable declaration");
        }
    }

    public void checkSentences() throws SemanticException {
        checkInScope();
        checkValue();
    }

    private void checkInScope() throws SemanticException {
        boolean isDeclaredInnerBlock = isDeclaredInnerBlock();

        boolean isDeclaredOuterBlock = false;
        if (ST.symbolTable.getCurrentBlock().getOuterBlock() != null) {
            isDeclaredOuterBlock = isDeclaredOuterBlock();
        }

        if (isDeclaredInnerBlock || isDeclaredOuterBlock) {
            throw new SemanticException(getId(), getLine(), "Variable duplicada en el alcance.");
        }

        ST.symbolTable.getCurrentBlock().addLocalVar(this);
    }

    private boolean isDeclaredInnerBlock() {
        boolean isDeclaredInParam = ST.symbolTable.getCurrentBlock().containsUnitParam(getId());
        boolean isDeclaredLocal = ST.symbolTable.getCurrentBlock().containsLocalVar(getId());

        return isDeclaredInParam || isDeclaredLocal;
    }

    private void checkValue() throws SemanticException {
        if (value != null) {
            Type valueType = value.validate();

            validateVoidOrNull();

            if (!valueType.conform(type)) {
                throw new SemanticException(tknAssignment.getLexeme(), tknAssignment.getLine(), "Conformacion de tipos incompatible");
            }
        }
    }

    private void validateVoidOrNull() throws SemanticException {
        if (type.equals(new TypeVoid(new Token(TokenType.VOID, "void", 0)))) {
            throw new SemanticException(tknAssignment.getLexeme(), tknAssignment.getLine(), "La variable no puede ser VOID");
        }
        if(type.equals(new TypeNull(new Token(TokenType.NULL, "null", 0)))) {
            throw new SemanticException(tknAssignment.getLexeme(), tknAssignment.getLine(), "La variable no puede ser NULL");
        }
    }

    private boolean isDeclaredOuterBlock() {
        boolean isDeclaredParams = ST.symbolTable.getCurrentBlock().getOuterBlock().containsUnitParam(getId());
        boolean isDeclaredLocalVar = ST.symbolTable.getCurrentBlock().getOuterBlock().containsLocalVar(getId());

        return isDeclaredParams || isDeclaredLocalVar;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ExpressionNode getValue() {
        return value;
    }

    public void setValue(ExpressionNode value) {
        this.value = value;
    }

    public void setTknAssignment(Token tknAssignment) {
        this.tknAssignment = tknAssignment;
    }

    //@todo: recordar que esto en realidad iba en una entidad Variable y no en una sentencia
    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return -offset;
    }
}
