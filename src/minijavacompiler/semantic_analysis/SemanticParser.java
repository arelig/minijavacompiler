package minijavacompiler.semantic_analysis;

import minijavacompiler.exceptions.SemanticException;
import minijavacompiler.symbol_table.SymbolTable;
import minijavacompiler.syntax_analysis.SyntaxParser;

public class SemanticParser {


	public SemanticParser() throws SemanticException {
		SymbolTable.getInstance().checkDeclaration();
		SymbolTable.getInstance().consolidate();
		//SymbolTable.getInstance().throwExceptionIfErrorsWereFound();
		SymbolTable.getInstance().checkSentences();
	}
}
