package minijavacompiler.code_generator;

import minijavacompiler.symbol_table.SymbolTable;

import java.util.Scanner;

public class CodeGenerator {
	private final SymbolTable ST = SymbolTable.getInstance();
	private static String code;
	private static boolean newLine;
	private static String tagMain;
	private static String tagMalloc;
	public CodeGenerator(){
		code = "";
		newLine = true;
	}
	public String generateCode(boolean formatCode){
		String tagHeapInit = TagManager.getTag("heapInit");
		tagMalloc = getMallocTag();
		tagMain = getMainTag();
		generateCode(".CODE ;program start");
		generateCode("PUSH " + tagHeapInit + " ;load heapInit routine tag");
		generateCode("CALL ;call to heapInit");
		generateCode("PUSH " + tagMain + " ;load main method tag");
		generateCode("CALL ;call to main");
		generateCode("HALT ;program completion");

		generateCode(tagHeapInit + ": RET 0 ;empty heapInit routine");

		generateCode(tagMalloc + ": LOADFP ;Unit initialization");
		generateCode("LOADSP");
		generateCode("STOREFP ;RA initialization completes");
		generateCode("LOADHL ;hl");
		generateCode("DUP ;hl");
		generateCode("PUSH 1 ;1");
		generateCode("ADD ;hl+1");
		generateCode("STORE 4 ;Save the result (a pointer to the first cell in the memory region)");
		generateCode("LOAD 3 ;Load the number of cells to accommodate (parameter that must be positive)");
		generateCode("ADD");
		generateCode("STOREHL ;Move the heap limit (hl). Expand the heap");
		generateCode("STOREFP");
		generateCode("RET 1 ;Return removing the parameter");

		ST.generateCode();
		if(formatCode)
			return format(code);
		else
			return code;
	}

	private static String getMainTag() {
		return TagManager.getTag("main");
	}

	public static String getMallocTag(){
		return TagManager.getTag("malloc");
	}

	public static void generateCode(String instruction) {
		String formattedInstruction = newLine ? instruction : "\n" + instruction;
		code += formattedInstruction;
		newLine = false;
	}
	public static void setNextInstructionTag(String tag) {
		String tagWithColon = tag + ": ";
		String newLineOrTagWithNewLine = newLine ? "" : "\n";
		code += newLineOrTagWithNewLine + tagWithColon;
		newLine = true;
	}
	private static String formatTags(String code) {
		StringBuilder formattedCode = new StringBuilder();
		int maxTagIndex = findMaxTagIndex(code);

		try (Scanner scanner = new Scanner(code)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String formattedLine = formatLineWithTag(line, maxTagIndex);
				formattedCode.append(formattedLine).append("\n");
			}
		}

		return formattedCode.toString();
	}
	private static int findMaxTagIndex(String code) {
		int maxTagIndex = 0;

		try (Scanner scanner = new Scanner(code)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				int tagIndex = line.indexOf(':');
				if (tagIndex != -1) {
					maxTagIndex = Math.max(tagIndex, maxTagIndex);
				}
			}
		}

		return maxTagIndex;
	}

	private static String formatLineWithTag(String line, int maxTagIndex) {
		int tagIndex = line.indexOf(':');
		if (tagIndex != -1) {
			String spaces = getSpaces(maxTagIndex - tagIndex);
			return line.substring(0, tagIndex + 1) + spaces + line.substring(tagIndex + 1);
		} else {
			String spaces = getSpaces(maxTagIndex + 2);
			return spaces + line;
		}
	}

	private static String getSpaces(int n) {
		return " ".repeat(Math.max(0, n));
	}

	public static void setComment(String comment){
		code += " ;" + comment;
		newLine = false;
	}
	private static String format(String code){
		return formatComments(formatTags(code));
	}

	private static String formatComments(String code) {
		StringBuilder formattedCode = new StringBuilder();
		int maxCommentIndex = getMaxCommentIndex(code);

		Scanner scanner = new Scanner(code);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String formattedLine = formatLineWithComment(line, maxCommentIndex);
			formattedCode.append(formattedLine).append("\n");
		}
		return formattedCode.toString();
	}
	private static int getMaxCommentIndex(String code) {
		int maxCommentIndex = 0;
		Scanner scanner = new Scanner(code);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			int commentIndex = getCharacterIndex(line, ';');
			if (commentIndex != -1) {
				maxCommentIndex = Math.max(commentIndex, maxCommentIndex);
			}
		}
		return maxCommentIndex;
	}
	private static String formatLineWithComment(String line, int maxCommentIndex) {
		int commentIndex = getCharacterIndex(line, ';');
		if (commentIndex != -1) {
			String spaces = getSpaces(maxCommentIndex - commentIndex + 10);
			return line.substring(0, commentIndex) + spaces + line.substring(commentIndex);
		} else {
			return line;
		}
	}

	private static int getCharacterIndex(String line, char character) {
		boolean isSpecialChar = false;
		for (int i = 0; i < line.length(); i++) {
			char currentChar = line.charAt(i);
			if (!isSpecialChar && currentChar == character) {
				return i;
			} else if (currentChar == '\'' || currentChar == '"') {
				isSpecialChar = !isSpecialChar;
			}
		}
		return -1;
	}
}
