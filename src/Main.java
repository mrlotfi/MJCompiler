import Compiler.Lexer.RawScanner;
import Compiler.Parser.ParseTable;
import Compiler.Parser.Parser;

public class Main {
    public static  void main(String[] args) {
        ParseTable table = new ParseTable();
        table.initialize();


        RawScanner scanner = new RawScanner("/home/mrlotfi/IdeaProjects/MJCompiler/src/chert.txt");
        try {
            scanner.initialize();
        }
        catch(Exception o){}



        Parser parser = new Parser(scanner);
        while(parser.doPars_oneStep()!=0) {
            System.out.println(scanner.currentToken());
            parser.printStack();
            System.out.println('\n');
        }

        //scanner.mainSymbolTable.printElements(0);

    }
}

