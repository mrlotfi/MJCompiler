package Compiler.Parser;


import Compiler.Lexer.RawScanner;
import Compiler.SymTable.Token;

public class Parser {
    private RawScanner scanner;


    public Token getToken_Generate() {
        Token t = scanner.getNextToken();
        if(t.getTokenType() == Token.KEYWORD_TYPE && t.getName() == "class") {
       }
        return t;
    }
}
