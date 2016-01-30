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

    Stack semanticStack;

    public CodeGenerator(Parser parser, RawScanner scanner) {
        this.parser = parser;
        this.scanner = scanner;
        semanticStack = new Stack();
        memoryOrganizer = new MemoryOrganizer();
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





}

