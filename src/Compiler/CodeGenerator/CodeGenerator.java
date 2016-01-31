package Compiler.CodeGenerator;


import Compiler.Lexer.RawScanner;
import Compiler.Parser.Parser;
import Compiler.SymTable.SymbolTable;
import Compiler.SymTable.SymbolTableEntry;
import Compiler.SymTable.Token;


import java.util.Stack;

public class CodeGenerator {

    MemoryOrganizer memoryOrganizer;
    RawScanner scanner;
    Parser parser;

    Stack<Object> semanticStack;

    public CodeGenerator(Parser parser, RawScanner scanner) {
        this.parser = parser;
        this.scanner = scanner;
        semanticStack = new Stack<>();
        memoryOrganizer = new MemoryOrganizer();
    }

    public void printCode() {
        System.out.println("Stack is empty? "+semanticStack.empty());
        for(int i=0;i<memoryOrganizer.getCodeblocksPointer();i++) {
            System.out.println(i+". "+memoryOrganizer.code[i].toString());
        }
    }

    public void patchmain() {
        Integer t = (Integer) semanticStack.pop();
        //TODO jp beriz to pb[t]
        memoryOrganizer.code[t.intValue()] =
                new Instruction("jp",memoryOrganizer.getCodeblocksPointer(),-1,-1,2,0,0);
    }

    public void classpid() {
        semanticStack.push(scanner.getCurrentToken());
    }
    //There is nothing to worry. simply copy the father table. address,.. are pre created and correct
    public void extend() {
        Token s1 = (Token) semanticStack.peek();
        Token s2 = scanner.getCurrentToken();
        SymbolTable parentTable = s2.related_address.related_symbolTable.list.get(s2.related_address.rowNumber).next_level;
        SymbolTable childTable = s1.related_address.related_symbolTable.list.get(s1.related_address.rowNumber).next_level;
        for(SymbolTableEntry entry:parentTable.list) {
            childTable.addEntry(entry);
        }
    }
    public void extend2() {
        semanticStack.pop();
    }



    public void cachetype() {
        Token s = scanner.getCurrentToken();
        semanticStack.push(s.getName());
    }


    public void declarevar() {
        String s = (String) semanticStack.pop();
        Token t = scanner.getCurrentToken();
        SymbolTableEntry entry = t.related_address.related_symbolTable.list.get(t.related_address.rowNumber);
        if(s.equals("boolean"))
            entry.var_type = SymbolTableEntry.TYPE_BOOL;
        else
            entry.var_type = SymbolTableEntry.TYPE_INT;
        entry.variable_abosolute_address = memoryOrganizer.reserveVar();
    }


    public void completefunctioninfo() {
        String s = (String) semanticStack.pop();
        Token t = scanner.getCurrentToken();
        SymbolTableEntry entry = t.related_address.related_symbolTable.list.get(t.related_address.rowNumber);
        if(s.equals("boolean"))
            entry.method_return_type = SymbolTableEntry.TYPE_BOOL;
        else
            entry.method_return_type = SymbolTableEntry.TYPE_INT;
        entry.method_aboslute_address = memoryOrganizer.getCodeblocksPointer();
        entry.method_abosolute_return_addr = memoryOrganizer.reserveVar();
        entry.method_caller_address = memoryOrganizer.reserveVar();
        semanticStack.push(new Integer(entry.method_caller_address));
        semanticStack.push(new Integer(entry.method_abosolute_return_addr));
        semanticStack.push(entry);
    }

    public void completeparameterinfo() {
        String s = (String) semanticStack.pop();
        SymbolTableEntry entryfunc = (SymbolTableEntry) semanticStack.peek();

        Token t = scanner.getCurrentToken();
        SymbolTableEntry entry = t.related_address.related_symbolTable.list.get(t.related_address.rowNumber);
        if(s.equals("boolean"))
            entry.var_type = SymbolTableEntry.TYPE_BOOL;
        else
            entry.var_type = SymbolTableEntry.TYPE_INT;
        entry.variable_abosolute_address = memoryOrganizer.reserveVar();
        entryfunc.arguments.add(new Integer(entry.variable_abosolute_address));
    }

    public void retaddress() {
        semanticStack.pop();
    }
    public void returner() {
        Integer retrunto  = (Integer) semanticStack.pop();
        int i = memoryOrganizer.reserveProgramBlock();
        memoryOrganizer.code[i] = new Instruction(
            "jp",retrunto.intValue(),-1,-1,0,0,0
        );
    }

    public void assignment() {
        Integer rightSideAddr = (Integer) semanticStack.pop();
        Integer leftSideAddr = (Integer) semanticStack.pop();
        int i = memoryOrganizer.reserveProgramBlock();
        //TODO
        memoryOrganizer.code[i] = new Instruction(
                "assign",rightSideAddr.intValue(),leftSideAddr.intValue(),-1,0,0,0
        );
    }

    public void label() {
        semanticStack.push(new Integer(memoryOrganizer.getCodeblocksPointer()));
    }

    public void save() {
        semanticStack.push(new Integer(memoryOrganizer.reserveProgramBlock()));
    }

    public void whiler() {
        int i = memoryOrganizer.reserveProgramBlock();
        Integer saveaddr = (Integer) semanticStack.pop();
        Integer expressionaddr = (Integer) semanticStack.pop();
        Integer labeladdr = (Integer) semanticStack.pop();
        memoryOrganizer.code[i] = new Instruction(
                "jp", labeladdr,-1,-1,2,0,0
        );
        memoryOrganizer.code[saveaddr] = new Instruction(
                "jpf",expressionaddr , memoryOrganizer.getCodeblocksPointer(),-1,0,2,0
        );
    }

    public void pid() {
        Token t = scanner.getCurrentToken();
        semanticStack.push(new Integer(
                t.related_address.related_symbolTable.list.get(t.related_address.rowNumber).variable_abosolute_address
        ));
    }

    public void printer() {
        int i = memoryOrganizer.reserveProgramBlock();
        Integer addr = (Integer) semanticStack.pop();
        memoryOrganizer.code[i] = new Instruction(
                "print", addr.intValue(),-1,-1,0,0,0
        );
    }

    public void jpfsave() {
        Integer saveAddr = (Integer) semanticStack.pop();
        Integer expressionAddr = (Integer) semanticStack.pop();
        int i = memoryOrganizer.reserveProgramBlock();
        semanticStack.push(new Integer(i));
        //TODO fill saveAddr
        memoryOrganizer.code[saveAddr] = new Instruction(
                "jpf", expressionAddr,i+1,-1,0,2,0
        );
    }

    public void jp() {
        Integer saveAddr = (Integer) semanticStack.pop();
        memoryOrganizer.code[saveAddr] = new Instruction(
                "jp", memoryOrganizer.getCodeblocksPointer(),-1,-1,2,0,0
        );
    }

    public void add() {
        Integer op1 = (Integer) semanticStack.pop();
        Integer op2 = (Integer) semanticStack.pop();
        int temp = memoryOrganizer.reserveTemp();
        int i = memoryOrganizer.reserveProgramBlock();
        memoryOrganizer.code[i] = new Instruction(
                "add",op1.intValue(),op2.intValue(),temp,0,0,0
        );
        semanticStack.push(new Integer(temp));
    }

    public void sub() {
        Integer op1 = (Integer) semanticStack.pop();
        Integer op2 = (Integer) semanticStack.pop();
        int temp = memoryOrganizer.reserveTemp();
        int i = memoryOrganizer.reserveProgramBlock();
        memoryOrganizer.code[i] = new Instruction(
                "sub",op1.intValue(),op2.intValue(),temp,0,0,0
        );
        semanticStack.push(new Integer(temp));
    }

    public void mult() {
        Integer op1 = (Integer) semanticStack.pop();
        Integer op2 = (Integer) semanticStack.pop();
        int temp = memoryOrganizer.reserveTemp();
        int i = memoryOrganizer.reserveProgramBlock();
        memoryOrganizer.code[i] = new Instruction(
                "mult",op1.intValue(),op2.intValue(),temp,0,0,0
        );
        semanticStack.push(new Integer(temp));
    }

    public void pid1() {
        Token t = scanner.getCurrentToken();
        SymbolTableEntry entry = t.related_address.related_symbolTable.list.get(t.related_address.rowNumber);
        if(entry.id_type == SymbolTableEntry.VARFIELD_ID)
            semanticStack.push(new Integer(entry.variable_abosolute_address));
    }

    public void pid2() {
        Token t = scanner.getCurrentToken();
        SymbolTableEntry entry = t.related_address.related_symbolTable.list.get(t.related_address.rowNumber);
        if(entry.id_type == SymbolTableEntry.VARFIELD_ID)
            semanticStack.push(new Integer(entry.variable_abosolute_address));
        else if(entry.id_type == SymbolTableEntry.METHOD_ID)
            semanticStack.push(entry);
    }

    public void putarg() {
        Integer expressionaddress = (Integer) semanticStack.pop();
        SymbolTableEntry entry = (SymbolTableEntry) semanticStack.peek();
        int i = memoryOrganizer.reserveProgramBlock();
        int argaddress = entry.arguments.get(entry.put_arg_number);
        entry.putarg();
        memoryOrganizer.code[i] = new Instruction(
                "assign",expressionaddress,argaddress,-1,0,0,0
        );
    }

    public void completefunctioncall() {
        SymbolTableEntry entry = (SymbolTableEntry) semanticStack.pop();
        int i = memoryOrganizer.reserveProgramBlock();
        int i2 = memoryOrganizer.reserveProgramBlock();
        memoryOrganizer.code[i] = new Instruction(
          "assign",i2+1,entry.method_abosolute_return_addr,-1,2,0,0
        );
        memoryOrganizer.code[i2] = new Instruction(
                "jp",entry.method_aboslute_address,-1,-1,2,0,0
        );
        //mese inke variable bude asan
        semanticStack.push(new Integer(entry.method_abosolute_return_addr));
    }

    public void truer() {
        int address = memoryOrganizer.reserveTemp();
        int i = memoryOrganizer.reserveProgramBlock();
        memoryOrganizer.code[i] = new Instruction(
                "assign",1,address,-1,2,0,0
        );
        semanticStack.push(new Integer(address));
    }

    public void falser() {
        int address = memoryOrganizer.reserveTemp();
        int i = memoryOrganizer.reserveProgramBlock();
        memoryOrganizer.code[i] = new Instruction(
                "assign",0,address,-1,2,0,0
        );
        semanticStack.push(new Integer(address));
    }

    public void number() {
        Token t = scanner.getCurrentToken();
        Integer value = new Integer(t.getName());
        int address = memoryOrganizer.reserveTemp();
        int i = memoryOrganizer.reserveProgramBlock();
        memoryOrganizer.code[i] = new Instruction(
                "assign",value,address,-1,2,0,0
        );
        semanticStack.push(new Integer(address));
    }

    public void cmpeq() {
        Integer op1 = (Integer) semanticStack.pop();
        Integer op2 = (Integer) semanticStack.pop();
        int temp = memoryOrganizer.reserveTemp();
        int i = memoryOrganizer.reserveProgramBlock();
        memoryOrganizer.code[i] = new Instruction(
                "eq",op1,op2,temp,0,0,0
        );
        semanticStack.push(new Integer(temp));
    }

    public void cmplower() {
        Integer op1 = (Integer) semanticStack.pop();
        Integer op2 = (Integer) semanticStack.pop();
        int temp = memoryOrganizer.reserveTemp();
        int i = memoryOrganizer.reserveProgramBlock();
        memoryOrganizer.code[i] = new Instruction(
                "lt",op1,op2,temp,0,0,0
        );
        semanticStack.push(new Integer(temp));
    }

    public void ander() {
        Integer op1 = (Integer) semanticStack.pop();
        Integer op2 = (Integer) semanticStack.pop();
        int temp = memoryOrganizer.reserveTemp();
        int i = memoryOrganizer.reserveProgramBlock();
        memoryOrganizer.code[i] = new Instruction(
                "and",op1,op2,temp,0,0,0
        );
        semanticStack.push(new Integer(temp));
    }

    public void switchsave() {
        int i = memoryOrganizer.reserveProgramBlock();
        int i2 = memoryOrganizer.reserveProgramBlock();
        memoryOrganizer.code[i] = new Instruction(
                "jp",i2+1,-1,-1,2,0,0
        );
        semanticStack.push(new Integer(i2));
    }

    public void savecase() {
        System.out.println(semanticStack.size());
        int i = memoryOrganizer.reserveProgramBlock();
        int i2 = memoryOrganizer.reserveProgramBlock();
        int addr = memoryOrganizer.reserveTemp();
        Integer op1 = (Integer) semanticStack.pop();
        Integer op2 = (Integer) semanticStack.peek();
        memoryOrganizer.code[i] = new Instruction(
            "eq",op1.intValue(),op2.intValue(),addr,0,0,0
        );
        semanticStack.push(new Integer(addr));
        semanticStack.push(new Integer(i2));
    }

    public void casejp() {
        Integer underjp = (Integer) semanticStack.get(0);
        int i = memoryOrganizer.reserveProgramBlock();
        memoryOrganizer.code[i] = new Instruction(
                "jp",underjp,-1,-1,2,0,0
        );
    }

    public void patchcase() {
        System.out.println(semanticStack.size());
        Integer save2 = (Integer) semanticStack.pop();
        Integer varaddr1 = (Integer) semanticStack.pop();
        memoryOrganizer.code[save2] = new Instruction(
            "jpf",varaddr1,memoryOrganizer.getCodeblocksPointer(),-1,0,2,0
        );

    }

    public void patchswitch() {
        semanticStack.pop();
        Integer save = (Integer) semanticStack.pop();
        memoryOrganizer.code[save] = new Instruction(
                "jp",memoryOrganizer.getCodeblocksPointer(),-1,-1,2,0,0
        );
    }

}

