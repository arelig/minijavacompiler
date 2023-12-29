package minijavacompiler.semantic_analysis;

import minijavacompiler.ST;
import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.symbol_table.SymbolTable;
import minijavacompiler.syntax_analysis.SyntaxParser;

public class SemanticParser {

	public void validate()throws SemanticException {
		ST.symbolTable.checkDeclaration();
		ST.symbolTable.consolidate();
		ST.symbolTable.throwErrorsAfterConsolidate();
		ST.symbolTable.checkSentences();
	}
}
