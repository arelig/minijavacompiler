package minijavacompiler.symbol_table.ast.access;

import minijavacompiler.symbol_table.ast.expression_nodes.ExpressionNode;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class AccessParenthesesExpr extends AccessNode {
    private ExpressionNode expressionNode;

    public AccessParenthesesExpr(Token token) {
        super(token);
    }

    @Override
    public Type validate() throws SemanticException {
        Type expressionType = expressionNode.validate();
        return checkChaining(expressionType);
    }

    public void setExpression(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    public void generateCode(){
        expressionNode.generateCode();
        if(chainedNode != null){
            chainedNode.generateCode();
        }
    }
}
