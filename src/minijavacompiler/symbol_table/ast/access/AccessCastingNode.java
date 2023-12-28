package minijavacompiler.symbol_table.ast.access;

import minijavacompiler.symbol_table.ast.expression_nodes.ExpressionNode;
import minijavacompiler.symbol_table.entities.Class;
import minijavacompiler.symbol_table.SymbolTable;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.symbol_table.types.TypeReference;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
public class AccessCastingNode extends AccessNode {
    private ExpressionNode rightExpression;

    public AccessCastingNode(Token token) {
        super(token);
    }

    @Override
    public void generateCode() {
        //todo: no sé si esto esta funcionando, debería generar código para el acceso?
        rightExpression.generateCode();
    }

    @Override
    public Type validate() throws SemanticException {
        //la expresion derecha debe conformar con el tipo del acceso
        //the rightExpr must conform with acces type
        Type expressionType = rightExpression.validate();
        Type accessType = getAccessType();

        if(!expressionType.conform(accessType)){
            throw new SemanticException(rightExpression.getId(), rightExpression.getLine(),
                    "El tipo " + expressionType.getId() + " no conforma con el tipo de acceso " +
                            accessType.getId());
        }

        return accessType;
    }
    private Type getAccessType() throws SemanticException {
        Class accessClass = SymbolTable.getInstance().getClass(getId());
        TypeReference referenceAccess = new TypeReference(accessClass.getToken());

        return checkChaining(referenceAccess);
    }

    public void setRightExpression(ExpressionNode rightExpression) {
        this.rightExpression = rightExpression;
    }
}
