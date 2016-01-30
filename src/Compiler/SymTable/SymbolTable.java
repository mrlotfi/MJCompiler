package Compiler.SymTable;


import java.util.ArrayList;

public class SymbolTable {
    public SymbolTable previous_level;
    public ArrayList<SymbolTableEntry> list;
    private int level;


    public SymbolTable(SymbolTable previous, int level) {
        previous_level = previous;
        list = new ArrayList<>();
        this.level = level;
    }


    public int getSize() {
        return list.size();
    }

    public int getLevel() {
        return level;
    }

    public void printElements(int padding) {
        for(SymbolTableEntry s:list) {
            for(int i=0;i<padding;i++) System.out.print(" ");
            System.out.println(s.name);
            if(s.next_level != null) {
                for(int i=0;i<padding;i++) System.out.print(" ");
                System.out.println("|");
                s.next_level.printElements(padding+2);
            }
        }
    }

    public void addEntry(SymbolTableEntry t) {
        list.add(t);
    }

    public int localSearch(String name) {
        for(int i=0;i<list.size();i++)
            if(list.get(i).name.equals(name))
                return i;
        return -1;
    }

    public SymbolTableAddress globalSearch(String name) {
        int i;
        if((i=localSearch(name)) != -1) {
            SymbolTableAddress t = new SymbolTableAddress();
            t.rowNumber = i;
            t.related_symbolTable = this;
            return t;
        }
        if(previous_level != null)
            return previous_level.globalSearch(name);
        return null;
    }
}
