package minijavacompiler.ast_data_structures.expression_nodes;

import minijavacompiler.data_structures.Class;
import minijavacompiler.data_structures.SymbolTable;
import minijavacompiler.data_structures.Type;
import minijavacompiler.data_structures.TypeReference;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class AccessCastingNode extends AccessNode {
    private ExpressionNode rightExpression;

    public AccessCastingNode(Token token) {
        super(token);
    }

    @Override
    public Type validate() throws SemanticException {
        //rightExpression  tiene que conformar con clase getId
        Type castType = checkRefClass();
        Type rightExpr = rightExpression.validate();
        if (!rightExpr.conform(castType)) {
            throw new SemanticException(rightExpression.getId(), rightExpression.getLine(), "Casteo de tipos no conformantes");
        }

        return castType;
    }

    private Type checkRefClass() throws SemanticException {
        Class refClass = SymbolTable.getInstance().getClassById(getId());
        TypeReference typeRefClass = new TypeReference(refClass.getTkn());

        Type toReturn;
        if (getChainedNode() != null) {
            toReturn = getChainedNode().validate(typeRefClass);
        } else {
            toReturn = typeRefClass;
        }
        return toReturn;
    }

    public void setRightExpression(ExpressionNode rightExpression) {
        this.rightExpression = rightExpression;
    }
}
