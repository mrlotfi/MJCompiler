package Compiler;


import java.io.FileInputStream;
import java.io.IOException;

public class Scanner {



    private static String IDENTIFIER_PATTERN = "[a-zA-Z][a-zA-Z0-9]*";
    private static String INTEGER_PATTERN = "[+|-]?\\d+";


    public static void main(String[] args) throws IOException {
        java.util.Scanner r = new java.util.Scanner(new FileInputStream("/home/mrlotfi/IdeaProjects/MJCompiler/src/chert.txt"));
        while(r.hasNext())
            System.out.println(r.next());
    }
    public Scanner(String fileName) {

    }
}
