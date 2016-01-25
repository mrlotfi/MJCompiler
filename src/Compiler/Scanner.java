package Compiler;


public class Scanner {

    public static String[] KEYWORDS_RAW = { "public", "class", "static", "void", "main", "extends",
            "return", "int", "boolean", "if", "else","while", "switch", "case", "break",
            "true", "false"};

    private static String IDENTIFIER_PATTERN = "[a-zA-Z][a-zA-Z0-9]*";
    private static String INTEGER_PATTERN = "[+|-]?\\d+";

    public static void main(String[] args) {
        System.out.println("8".matches(IDENTIFIER_PATTERN));
        System.out.println("98".matches(INTEGER_PATTERN));
    }




    public Scanner(String fileName) {

    }
}
