import Compiler.Lexer.RawScanner;
import Compiler.SymTable.Token;

import java.util.Scanner;

public class Main {
    public static  void main(String[] args) {


        RawScanner s = new RawScanner("/home/mrlotfi/IdeaProjects/MJCompiler/src/chert.txt");
        try {
            s.initialize();
        }
        catch(Exception o){System.out.println("No file exists");}
        Token n;
        while((n = s.getNextToken()) != null);
            //System.out.println(n.getName());

    }
}
