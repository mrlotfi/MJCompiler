package Compiler.SymTable;


public class Token {
    private String name;
    private int type;
    public SymbolTableAddress related_address;

    public final static int KEYWORD_TYPE = 0;
    public final static int ID_TYPE = 1;
    public final static int INT_TYPE = 2;
    public final static int SEPERATOR_TYPE = 3;
    public final static int OPERATOR_TYPE = 4;

    public Token(String name , int tag) {
        this.name = name;
        this.type = tag;
        related_address = null;
    }

    public int getTokenType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
