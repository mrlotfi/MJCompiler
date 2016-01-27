package Compiler.SymTable;


public class Token {
    private String name;
    private int type;

    public Token(String name , int tag) {
        this.name = name;
        this.type = tag;
    }

    public int getTokenType() {
        return type;
    }
}
