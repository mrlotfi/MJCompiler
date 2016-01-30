package Compiler.SymTable;

import java.util.ArrayList;

/*
    Symbol Table is just held for id's
 */
public class SymbolTableEntry {
    public final static int CLASS_ID = 0;
    public final static int VARFIELD_ID = 1;
    public final static int METHOD_ID = 2;


    public final static int TYPE_INT = 0;
    public final static  int TYPE_BOOL = 1;
    public String name;
    public int id_type;

    public ArrayList<Integer> arguments;
    public int arguments_number;
    public int func_return_type;
    public int var_type;

    public SymbolTable next_level;

    public int variable_abosolute_address;
    public int method_aboslute_address;
    public int method_abosolute_return_addr;


    public SymbolTableEntry(String name , int id_type) {
        this.id_type = id_type;
        this.name = name;
        next_level = null;
        if(id_type == METHOD_ID)
            arguments = new ArrayList<>();
        method_aboslute_address = variable_abosolute_address = -1;
        func_return_type = var_type = -1;
    }

}
