package Compiler.Lexer;

import Compiler.SymTable.SymbolTable;
import Compiler.SymTable.SymbolTableAddress;
import Compiler.SymTable.SymbolTableEntry;
import Compiler.SymTable.Token;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;


public class RawScanner {
    public final  static String[] KEYWORDS_RAW = {"public", "class", "static", "void", "main", "extends",
            "return", "int", "boolean", "if", "else","while", "switch", "case", "break",
            "true", "false", "System.out.println"};

    public final  static String[] OPERATORS = { "=", "==", "<", "&&", "*", "+", "-","(",")","()"};

    private final static String IDENTIFIER_PATTERN = "[a-zA-Z][a-zA-Z0-9]*";
    private final static String INTEGER_PATTERN = "[+|-]?\\d+";


    public boolean delcaring_class;
    public boolean declaring_var;
    public boolean declaring_method;
    public boolean after_dot;
    private SymbolTable after_dot_symbolTable;

    private String filename;
    private FileInputStream file_descriptor;
    private Scanner reader;


    private int line_number;
    private Scanner line_reader;
    private String current_token;
    private String next_token;

    private SymbolTable mainSymbolTable;
    public SymbolTable currentSymbolTable;

    public RawScanner(String filename) {
        this.filename = filename;
        currentSymbolTable = mainSymbolTable = new SymbolTable(null,0);
        line_number = 0;
    }

    public void initialize() throws IOException {
        file_descriptor = new FileInputStream(filename);
        FileOutputStream temp_file = new FileOutputStream("temp.txt");
        int write1;
        char c;
        char d;
        int a;
        while(true) {
            write1 = a = file_descriptor.read();
            if(a == -1)
                break;
            c = (char) a;

            if (c == '/') {
                a = file_descriptor.read();
                if(a==-1)
                    break;
                d = (char) a;
                if(d == '/') {
                    while (true) {
                        a = file_descriptor.read();
                        d = (char) a;
                        if (a == -1 || d == '\n')
                            break;
                    }
                    temp_file.write((int) '\n');
                }
                else if(d == '*') {
                    while (true) {
                        a = file_descriptor.read();
                        d = (char) a;
                        if (a == -1)
                            break;
                        if(d == '*') {
                            c = (char) file_descriptor.read();
                            if (c == '/')
                                break;
                        }
                    }
                    temp_file.write((int) ' ');
                }
            }
            else
                temp_file.write(write1);
        }
        temp_file.close();
        file_descriptor = new FileInputStream("temp.txt");
        reader = new Scanner(file_descriptor);
        line_reader = new Scanner(reader.nextLine());
        current_token = getNextRawToken();
        next_token = getNextRawToken();
    }

    boolean isMember(String arg, String[] list) {
        for(String c:list)
            if(arg.equals(c))
                return true;
        return false;
    }

    public String getNextRawToken() {
        if(line_reader.hasNext())
            return line_reader.next();
        else {
            do {
                if (reader.hasNext()) {
                    line_reader = new Scanner(reader.nextLine());
                    line_number += 1;
                }
                 else
                    break;
            } while (!line_reader.hasNext());
        }
        if(line_reader.hasNext())
            return line_reader.next();
        return null;
    }

    public Token getNextToken()  {
        String token = current_token;
        current_token = next_token;
        next_token = getNextRawToken();
        System.out.println("Read this token " + token);
        if(token == null)
            return null;
        //TODO use line number
        if (isMember(token, KEYWORDS_RAW)) {
            if(token.equals("class"))
                delcaring_class = true;
            return new Token(token,Token.OPERATOR_TYPE);
        }

        if (token.matches(IDENTIFIER_PATTERN)) {
            Token t = new Token(token,Token.ID_TYPE);
            t.related_address = processIdentifier(token);
            return t;
        }

        if (token.matches(INTEGER_PATTERN)) {
            return new Token(token,Token.INT_TYPE);
        }

        if(token.equals(",")) {
            return new Token(token,Token.SEPERATOR_TYPE);
        }

        if(token.equals(";")) {
            return new Token(token,Token.SEPERATOR_TYPE);
        }

        if(token.equals(".")) {
            return new Token(token,Token.SEPERATOR_TYPE);
        }

        if(token.equals(":")) {
            return new Token(token,Token.SEPERATOR_TYPE);
        }

        if(isMember(token,OPERATORS)) {
            return new Token(token,Token.OPERATOR_TYPE);
        }

        return new Token(token,-1); // TODO report error here
    }

    public void returnToClassTable() {
        if(currentSymbolTable.getLevel() == 2)
            currentSymbolTable = currentSymbolTable.previous_level;
    }

    private SymbolTableAddress processIdentifier(String name) {
        if(delcaring_class) {
            delcaring_class = false;
            SymbolTableEntry t= new SymbolTableEntry(name, SymbolTableEntry.CLASS_ID);
            t.next_level = new SymbolTable(mainSymbolTable,1);
            currentSymbolTable = t.next_level;
            mainSymbolTable.addEntry(t);
            SymbolTableAddress address = new SymbolTableAddress();
            address.related_symbolTable = mainSymbolTable;
            address.rowNumber = mainSymbolTable.getSize()-1;
            return address;
        }
        else if(declaring_method) {
            declaring_method = false;
            SymbolTableEntry t = new SymbolTableEntry(name,SymbolTableEntry.METHOD_ID);
            t.next_level = new SymbolTable(currentSymbolTable,2);
            t.arguments_number = 0;
            currentSymbolTable.addEntry(t);
            SymbolTableAddress address = new SymbolTableAddress();
            address.related_symbolTable = currentSymbolTable;
            address.rowNumber = currentSymbolTable.getSize()-1;
            currentSymbolTable = t.next_level;
            return address;
        }
        else {
            if(next_token.equals(".")) {
                after_dot = true;
                if(currentSymbolTable.getLevel() == 1)
                    after_dot_symbolTable = currentSymbolTable;
                else
                    after_dot_symbolTable = currentSymbolTable.previous_level;

                SymbolTableAddress t = mainSymbolTable.globalSearch(name);
                if( t != null)
                    return t;
                //TODO generate error
                SymbolTableEntry temp = new SymbolTableEntry(name,SymbolTableEntry.CLASS_ID);
                mainSymbolTable.addEntry(temp);
                SymbolTableAddress address = new SymbolTableAddress();
                address.related_symbolTable = mainSymbolTable;
                address.rowNumber = mainSymbolTable.getSize() - 1;
                return address;
            }
            else {
                if(after_dot) {
                    after_dot = false;
                    int t = after_dot_symbolTable.localSearch(name);
                    if( t != -1) {
                        SymbolTableAddress address = new SymbolTableAddress();
                        address.rowNumber = t;
                        address.related_symbolTable = after_dot_symbolTable;
                        return address;
                    }
                    SymbolTableEntry temp = new SymbolTableEntry(name,-1);
                    after_dot_symbolTable.addEntry(temp);
                    SymbolTableAddress address = new SymbolTableAddress();
                    address.related_symbolTable = after_dot_symbolTable;
                    address.rowNumber = after_dot_symbolTable.getSize() - 1;
                    return address;
                }
                else if(declaring_var) {
                    declaring_var = false;
                    SymbolTableEntry temp = new SymbolTableEntry(name,SymbolTableEntry.VARFIELD_ID);
                    currentSymbolTable.addEntry(temp);
                    SymbolTableAddress t = new SymbolTableAddress();
                    t.rowNumber = currentSymbolTable.getSize() -1 ;
                    t.related_symbolTable = currentSymbolTable;
                    return t;
                }
                else {
                    SymbolTableAddress t = currentSymbolTable.globalSearch(name);
                    if (t != null)
                        return t;
                    //TODO report some kind of error here. undeclared variable
                    SymbolTableEntry temp = new SymbolTableEntry(name, SymbolTableEntry.VARFIELD_ID);
                    SymbolTableAddress address = new SymbolTableAddress();
                    address.related_symbolTable = currentSymbolTable;
                    address.rowNumber = currentSymbolTable.getSize();
                    currentSymbolTable.addEntry(temp);
                    return address;
                }
            }
        }
    }

    public String nextToken() {
        return next_token;
    }

    public String currentToken() {
        return current_token;
    }
}
