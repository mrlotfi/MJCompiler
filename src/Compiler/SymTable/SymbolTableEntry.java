package Compiler.SymTable;

/*
    Symbol Table is just held for id's
 */
public class SymbolTableEntry {
    public final static int CLASS_ID = 0;
    public final static int VARFIELD_ID = 1;
    public final static int METHOD_ID = 2;
   // public final static int VAR_ID = 3;

    public final static int VAR_TYPE_INT = 0;
    public final static  int VAR_TYPE_BOOL = 1;
    public String name;
    private int id_type;
    public int arguments_number;
    public SymbolTable next_level;
    public int var_type;
    public SymbolTableEntry(String name , int id_type) {
        this.id_type = id_type;
        this.name = name;
        next_level = null;
        arguments_number = -1;
        var_type = -1;
    }

}
