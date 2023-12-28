package minijavacompiler.symbol_table.ast.expression_nodes;

import minijavacompiler.code_generator.CodeGenerator;
import minijavacompiler.symbol_table.types.Type;
import minijavacompiler.symbol_table.types.TypePrimitive;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.lexical_parser.Token;
import minijavacompiler.lexical_parser.TokenType;

public class LiteralBooleanNode extends OperandNode {
    private final Type type;

    public LiteralBooleanNode(Token token) {
        super(token);
        type = new TypePrimitive(new Token(TokenType.PR_BOOLEAN, "boolean", token.getLine()));
    }


    @Override
    public Type validate() throws SemanticException {
        return type;
    }

    @Override
    public void generateCode() {
        if(getId().equals("true")){
           CodeGenerator.generateCode("PUSH 1 ; stack true value");
        }else{
            CodeGenerator.generateCode("PUSH 0 ; stack false value");
        }
    }
}
