package minijavacompiler.symbol_table.ast.sentence_nodes;

import minijavacompiler.lexical_parser.TokenType;
import minijavacompiler.symbol_table.ast.access.AccessNode;
import minijavacompiler.symbol_table.ast.expression_nodes.ExpressionNode;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.symbol_table.types.TypePrimitive;

public class AssignmentNode extends SentenceNode {
    private AccessNode access;
    private ExpressionNode value;
    public AssignmentNode(Token token) {
        super(token);
    }

    public void checkSentences() throws SemanticException {
        checkType();
    }

    private void checkType() throws SemanticException {
        Type accessType = access.validate();
        Type valueType = value.validate();

        if(getId().equals("=")){
            if (!valueType.conform(accessType)) {
                throw new SemanticException(getId(), getLine(), "Conformación de tipos incompatible.");
            }
        }else{
            if(!accessType.conform(new TypePrimitive(new Token(TokenType.PR_INT, "int", 0)))){
              throw new SemanticException(getId(),getLine(), "Se esperaba que el acceso de la asignación fuera de tipo Int");
            }
            if(!valueType.conform(new TypePrimitive(new Token(TokenType.PR_INT, "int", 0)))){
                throw new SemanticException(getId(),getLine(), "Se esperaba que el valor de la asignación fuera de tipo Int");
            }
        }


    }

    @Override
    public void generateCode() {
        if(getId().equals("=")){
            value.generateCode();
        }else if(getId().equals("+=")){
            access.generateCode();
            value.generateCode();
            CodeGenerator.generateCode("ADD");
        }
        else{
            access.generateCode();
            value.generateCode();
            CodeGenerator.generateCode("SUB");
        }
        access.generateCode();
    }

    public void setAccess(AccessNode accessNode) {
        this.access = accessNode;
    }

    public void setValue(ExpressionNode expressionNode) {
        this.value = expressionNode;
    }
}
