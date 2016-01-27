package Compiler.Lexer;

import Compiler.SymTable.Token;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RawScanner {
    public static String[] KEYWORDS_RAW = {"public", "class", "static", "void", "main", "extends",
            "return", "int", "boolean", "if", "else","while", "switch", "case", "break",
            "true", "false", "System.out.println"};

    public static String[] OPERATORS = { "=", "==", "<", "&&", "*", "+", "-"};

    private static String IDENTIFIER_PATTERN = "[a-zA-Z][a-zA-Z0-9]*";
    private static String INTEGER_PATTERN = "[+|-]?\\d+";

    private String filename;
    private FileInputStream file_descriptor;
    private Scanner reader;

    public RawScanner(String filename) {
        this.filename = filename;
    }

    public void initialize() throws IOException {
        file_descriptor = new FileInputStream(filename);
        FileOutputStream temp_file = new FileOutputStream("temp.txt");



        reader = new Scanner(file_descriptor);
    }

    boolean isMember(String arg, String[] list) {
        for(String c:list)
            if(arg.equals(c))
                return true;
        return false;
    }

    public Token getNextToken() throws Exception {
        String token = reader.next();

        if (isMember(token, KEYWORDS_RAW)) {
            return new Token(token,0);
        }

        if (token.matches(IDENTIFIER_PATTERN)) {
            return new Token(token,1);
        }

        if (token.matches(INTEGER_PATTERN)) {
            return new Token(token,2);
        }

        if(token.equals(",")) {
            return new Token(token,3);
        }

        if(token.equals(";")) {
            return new Token(token,3);
        }

        if(token.equals(".")) {
            return new Token(token,3);
        }

        if(isMember(token,OPERATORS)) {
            return new Token(token,4);
        }

        return new Token(token,-1); // TODO report error here
    }
}
