package minijavacompiler.symbol_table.ast.sentence_nodes;

import minijavacompiler.lexical_parser.TokenType;
import minijavacompiler.symbol_table.ast.expression_nodes.ExpressionNode;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.symbol_table.entities.Unit;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.symbol_table.types.TypeVoid;

public class ReturnNode extends SentenceNode {
    private ExpressionNode returnExpression;
    private Unit currentUnit;

    public ReturnNode(Token token) {
        super(token);
    }

    @Override
    public void checkSentences() throws SemanticException {
        if (returnExpression != null) {
            Type actualType = returnExpression.validate();
            if (!actualType.conform(currentUnit.getReturnType())) {
                throw new SemanticException(getId(), getLine(),
                        "El tipo de retorno " + actualType + " no conforma con el tipo de la unidad (" +
                                currentUnit.getReturnType().getId() + ").");
            }
        } else {
            if (!currentUnit.getReturnType().equals(new TypeVoid(new Token(TokenType.VOID, "void", 0)))) {
                throw new SemanticException(getId(), getLine(),
                        "Falta la expresión de retorno en un método no void.");
            }
        }
    }

    @Override
    public void generateCode() {
        if(returnExpression != null) {
            returnExpression.generateCode();
            //@todo: falta sumar si la unidad es estatica (3) o dinamica (4)
            CodeGenerator.generateCode("STORE " + (currentUnit.getParamsAsList().size()) + " ;store return value");
        }

        CodeGenerator.generateCode("FMEM " + currentUnit.getBlock().getNextVariableOffset() + " ;release memory for method variables");
        CodeGenerator.generateCode("STOREFP ;update activation record (unstack)");
        CodeGenerator.generateCode("RET " + currentUnit.getParamsAsList().size()  + " ;method return");

    }

    public void setCurrentUnit(Unit currentUnit) {
        this.currentUnit = currentUnit;
    }


    public void setExpression(ExpressionNode expressionNode) {
        this.returnExpression = expressionNode;
    }
}
