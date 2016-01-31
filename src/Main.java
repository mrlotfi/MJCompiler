import Compiler.CodeGenerator.CodeGenerator;
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
        int i;
        while((i=parser.doPars_oneStep())!=0 && (i=parser.doPars_oneStep())!=-1) {
            if(i <0)
                parser.error_accured = true;
            System.out.println(scanner.currentToken());
            parser.printStack();
            System.out.println('\n');
        }
        System.out.println("Parse error? :"+(parser.error_accured));
        if(parser.error_accured)
            System.out.println(parser.errors);
        System.out.println();
        if(!parser.error_accured)
            parser.printGeneratedCode();
        //scanner.mainSymbolTable.printElements(0);

    }
}

