package minijavacompiler.ast_data_structures.sentence_nodes;

import minijavacompiler.ast_data_structures.expression_nodes.AccessNode;
import minijavacompiler.ast_data_structures.expression_nodes.ExpressionNode;
import minijavacompiler.data_structures.Type;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;

public class AssignmentNode extends SentenceNode {
    private AccessNode leftNode;
    private ExpressionNode rightNode;

    public AssignmentNode(Token token) {
        super(token);
    }

    public void validate() throws SemanticException {
        Type leftType = leftNode.validate();
        Type rightType = rightNode.validate();

        if (!rightType.conform(leftType)) {
            throw new SemanticException(getId(), getLine(), "Asignacion: conformacion de tipos incompatible");
        }


        //sentencias tiene un chequear que no devuelve nada, no te devuelven un tipo, estan bien o mal
        //el chequear en las expresiones aparte del chequeo te devuelve un tipo
        //el tipo lo va a usar quien contenga esa expresion


        //si el tipo ladoDer.chequear() no conforma con el tipo de ladoIzq.chequear()
        //entonces Error
    }

    @Override
    public void generateCode() {

    }

    public void setLeftNode(AccessNode accessNode) {
        this.leftNode = accessNode;
    }

    public void setRightNode(ExpressionNode expressionNode) {
        this.rightNode = expressionNode;
    }
}
