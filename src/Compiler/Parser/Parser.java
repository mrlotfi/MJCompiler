package Compiler.Parser;


import Compiler.CodeGenerator.CodeGenerator;
import Compiler.Lexer.RawScanner;
import Compiler.SymTable.Token;

import java.util.Stack;

public class Parser {
    private RawScanner scanner;
    private Stack<String> parseStack;
    private ParseTable parseTable;
    private CodeGenerator codegenerator;
    public boolean error_accured;
    public Parser(RawScanner scanner) {
        codegenerator  = new CodeGenerator(this,scanner);
        this.scanner =  scanner;
        parseStack = new Stack<>();
        parseStack.push("$");
        parseStack.push("Goal");
        parseTable = new ParseTable();
        parseTable.initialize();
        error_accured = false;
        errors = new String("");
    }

    public String errors;


    private void checkAndSetScannerBITS(String rule) {
        if(rule.equals("FieldDeclaration") || rule.equals("VarDeclaration"))
            scanner.declaring_var = true;
        if(rule.equals("MethodDeclaration")) {
            scanner.declaring_method = true;
            scanner.returnToClassTable();
        }
        if(rule.equals("ClassDeclaration") || rule.equals("MainClass"))
            scanner.delcaring_class = true;
        if(rule.equals("FactorRest")) {
            scanner.after_dot = true;
        }
    }


    public int doPars_oneStep() {
        Token currentToken = scanner.getCurrentToken();
        //Accept Action
        if(currentToken == null)
            if(parseStack.peek().equals("$"))
                return 0;
            else
                return -1;

        String nonterminal;
        if(currentToken.getTokenType() == Token.ID_TYPE)
            nonterminal = "<IDENTIFIER_LITERAL>";
        else if(currentToken.getTokenType() == Token.INT_TYPE)
            nonterminal = "<INTEGER_LITERAL>";
        else
            nonterminal = currentToken.getName();

        String stackTop = parseStack.peek();
        if(Character.isUpperCase(stackTop.charAt(0)) &&
                Character.isAlphabetic(stackTop.charAt(0)) &&
                !stackTop.equals("System.out.println")) {

            String[] lookup_value = parseTable.lookup(parseStack.pop(),nonterminal);
            if(lookup_value == null) {
                errors = errors + "Error at line "+scanner.getCurrentLine()+" didn't expect this input here.\n";
                System.out.print("Error at line "+scanner.getCurrentLine()+" didn't expect this input here. ");
                error_accured = true;
                while(true) {
                    scanner.roll();
                    currentToken = scanner.getCurrentToken();
                    if(currentToken == null)
                        return -2;
                    if(currentToken.getTokenType() == Token.ID_TYPE)
                        nonterminal = "<IDENTIFIER_LITERAL>";
                    else if(currentToken.getTokenType() == Token.INT_TYPE)
                        nonterminal = "<INTEGER_LITERAL>";
                    else
                        nonterminal = currentToken.getName();

                    if(parseTable.lookup(stackTop,nonterminal) == null)
                        continue ;
                    if(parseTable.lookup(stackTop,nonterminal).length == 0)
                        break;
                    else if(parseTable.lookup(stackTop,nonterminal).length == 1)
                        if (parseTable.lookup(stackTop, nonterminal)[0].equals("sync"))
                            break;
                }
                System.out.print("Now considering "+stackTop+" was seen.\n");
                return -2;
            }
            if(lookup_value.length == 0) {
                //System.out.println("Used epsilon rule for "+stackTop);
                return 1;
            }
            else {
                //System.out.println("popped "+stackTop);
                if(lookup_value[0].equals("sync")) {
                    System.out.println("Error at line " + scanner.getCurrentLine()+" input was missed there");
                    errors = errors + "Error at line " + scanner.getCurrentLine()+" input was missed there\n";
                    error_accured = true;
                    return -3;
                }
                for(int i=lookup_value.length-1;i>=0;i--)
                {
                    parseStack.push(lookup_value[i]);
                    //System.out.println("poped. now pushing "+lookup_value[i]);
                }
                checkAndSetScannerBITS(stackTop);
                return 1;
            }
        }
        else if(stackTop.charAt(0) == '#') {
            if(error_accured) {
                parseStack.pop();
                return -10;
            }

            String actiontype = stackTop.substring(1);
            java.lang.reflect.Method method = null;
            try {
                method = codegenerator.getClass().getMethod(actiontype);
            } catch (Exception e) {
                System.out.println("Verybad2");
            }
            try {
                method.invoke(codegenerator);
             } catch (Exception e) {
                System.out.println("Verybad");
            }
            parseStack.pop();
            return 1;
        }
        else {
            if(stackTop.equals(nonterminal))
            {
                parseStack.pop();
                scanner.roll();
                if(scanner.after_dot && currentToken.getTokenType() == Token.ID_TYPE) {
                    scanner.after_dot = false;
                }
                return 1;
            }
            else {
                parseStack.pop();
                System.out.println("Error at line " + scanner.getCurrentLine()+ " expected "+stackTop+ " terminal here but saw "+nonterminal);
                errors = errors + "Error at line " + scanner.getCurrentLine()+ " expected "+stackTop+ " terminal here but saw "+nonterminal +"\n";
                error_accured = true;
                return -4;
            }
        }
    }

    public void printStack() {
        for(String s:parseStack)
            System.out.print(s+" ");
    }

    public void printGeneratedCode() {
        codegenerator.printCode();
    }
}
