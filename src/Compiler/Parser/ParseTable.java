package Compiler.Parser;


import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ParseTable {
    private Map<String , Map<String,String[]>> MainTable;
    public ParseTable() {
        MainTable = new HashMap<>();
    }

    public void initialize() {
        Map<String , String[]> temp_column;

        //Goal
        temp_column = new HashMap<>();
        temp_column.put("class",new String[] {"Source"});
        temp_column.put("public",new String[] {"Source"});
        temp_column.put("$", new String[] {"sync"});
        MainTable.put("Goal",temp_column);

        //Source
        temp_column = new HashMap<>();
        temp_column.put("class",new String[] {"ClassDeclarations","MainClass"});
        temp_column.put("public",new String[] {"ClassDeclarations","MainClass"});
        temp_column.put("$", new String[] {"sync"});
        MainTable.put("Source",temp_column);

        //MainClass
        //MainClass -> public class Identifier { public static void main () { Vardeclarations Statements } }
        temp_column = new HashMap<>();
        temp_column.put("public",new String[] {
            "public", "class", "identifier", "{", "public",  "static", "void", "main", "()", "{",  "Vardeclarations", "Statements", "}", "}"
        });
        temp_column.put("$", new String[] {"sync"});
        MainTable.put("MainClass",temp_column);

        // ClassDeclarations
        // ClassDeclarations -> ClassDeclaration ClassDeclarations | e
        temp_column = new HashMap<>();
        temp_column.put("class", new String[] {"ClassDeclaration", "ClassDeclarations"});
        temp_column.put("public", new String[] {});
        MainTable.put("ClassDeclarations",temp_column);

        //ClassDeclaration
        //ClassDeclaration -> class Identifier Extension { FieldDeclarations MethodDeclarations }
        temp_column = new HashMap<>();
        temp_column.put("class", new String[] {
            "class", "Identifier", "Extension" ,"{", "FieldDeclarations", "MethodDeclarations", "}"
        });
        temp_column.put("public",new String[] {"sync"});
        MainTable.put("ClassDeclaration" , temp_column);

        //Extension
        //Extension -> extends Identifier | e
        temp_column = new HashMap<>();
        temp_column.put("extends",new String[] {"extends", "Identifier"});
        temp_column.put("{", new String[]{});
        MainTable.put("Extension",temp_column);

        //FieldDeclarations
        //FieldDeclarations -> FieldDeclaration FieldDeclarations | e
        temp_column = new HashMap<>();
        temp_column.put("static", new String[]{"FieldDeclaration",  "FieldDeclarations"});
        temp_column.put("public", new String[]{});
        temp_column.put("}", new String[]{});
        MainTable.put("FieldDeclarations",temp_column);

        //FieldDeclaration
        //FieldDeclaration -> static Type Identifier ;
        temp_column = new HashMap<>();
        temp_column.put("static", new String[] {
             "static", "Type", "Identifier" , ";"
        });
        temp_column.put("public", new String[] {"sync"});
        temp_column.put("}", new String[] {"sync"});
        MainTable.put("FieldDeclaration",temp_column);

        //VarDeclarations
        //VarDeclarations -> VarDeclaration VarDeclarations | e
        temp_column = new HashMap<>();
        temp_column.put("int", new String[] {"VarDeclaration", "VarDeclarations"});
        temp_column.put("boolean", new String[] {"VarDeclaration", "VarDeclarations"});
        String[] follows = {"boolean", "int", "{" , "if" , "while" , "switch" , "System.out.println" , "<IDENTIFIER_LITERAL>" , "return" , "}" , "break"};
        for(String s:follows)
            temp_column.put(s,new String[]{});
        MainTable.put("VarDeclarations", temp_column);

        //VarDeclaration -> Type Identifier ;
        temp_column = new HashMap<>();
        temp_column.put("int", new String[] {"Type", "Identifier", ";"});
        temp_column.put("boolean", new String[] {"Type", "Identifier", ";"});
        for(String s:follows)
            temp_column.put(s,new String[]{"sync"});
        MainTable.put("VarDeclaration", temp_column);

        //MethodDeclarations -> MethodDeclaration MethodDeclarations |
        temp_column = new HashMap<>();
        temp_column.put("public", new String[] {"MethodDeclaration", "MethodDeclarations"});
        temp_column.put("}", new String[]{});
        MainTable.put("MethodDeclarations",temp_column);

        //MethodDeclaration -> public static Type Identifier ( Parameters ) { VarDeclarations Statements return GenExpression ; }
        temp_column = new HashMap<>();
        temp_column.put("public",new String[] {
            "public", "static", "Type", "Identifier", "(", "Parameters", ")", "{" ,"VarDeclarations", "Statements", "return", "GenExpression", ";",  "}"
        });
        temp_column.put("}", new String[] {"sync"});
        MainTable.put("MethodDeclaration",temp_column);

        //Parameters -> Type Identifier Parameter |
        temp_column = new HashMap<>();
        temp_column.put("boolean", new String[]{
                "Type", "Identifier", "Parameter"
        });
        temp_column.put("int", new String[]{
                "Type", "Identifier", "Parameter"
        });
        temp_column.put(")", new String[]{});
        MainTable.put("Parameters",temp_column);

        //Parameter -> , Type Identifier Parameter |
        temp_column = new HashMap<>();
        temp_column.put(",", new String[] {",", "Type", "Identifier", "Parameter"});
        temp_column.put(")", new String[]{});
        MainTable.put("Parameter",temp_column);

        //Type -> boolean | int
        temp_column = new HashMap<>();
        temp_column.put("int", new String[]{"int"});
        temp_column.put("boolean", new String[]{"boolean"});
        MainTable.put("Type",temp_column);

        //Statements -> Statement Statements |
        temp_column = new HashMap<>();
        String[] statements_follows = {
                "{", "if", "while", "switch", "System.out.println", "<IDENTIFIER_LITERAL>"
        };
        for(String s:statements_follows)
            temp_column.put(s,new String[] {"Statement", "Statements"});
        temp_column.put("break", new String[]{});
        temp_column.put("}", new String[]{});
        temp_column.put("return", new String[]{});
        MainTable.put("Statements",temp_column);

        //Statement -> { Statements } | if ( GenExpression ) Statement else Statement | while ( GenExpression ) | switch ( GenExpression) { CaseStatements } | System.out.println ( GenExpression ) ; | Identifier = GenExpression ;
        temp_column = new HashMap<>();
        temp_column.put("{",new String[] {"{","Statements","}"});
        temp_column.put("if", new String[] {
            "if", "(", "GenExpression", ")", "Statement", "else", "Statement"
        });
        temp_column.put("while", new String[] {
             "while", "(", "GenExpression" ,")", "Statement"
        });
        temp_column.put("switch", new String[] {
            "switch", "(" ,"GenExpression", ")", "{", "CaseStatements", "}"
        });
        temp_column.put("System.out.println", new String[] {
                "System.out.println", "(", "GenExpression", ")", ";"
        });
        temp_column.put("<IDENTIFIER_LITERAL>", new String[] {
                "Identifier", "=", "GenExpression", ";"
        });
        String[] statement_follows = {
             "else", "break", "}", "return"
        };
        for(String s:statement_follows)
            temp_column.put(s,new String[] {"sync"});
        MainTable.put("Statement",temp_column);

        //CaseStatements -> CaseStatement CaseStatementsRest
        temp_column = new HashMap<>();
        temp_column.put("case",new String[] {
                "CaseStatement", "CaseStatementsRest"
        });
        temp_column.put("}", new String[]{"sync"});
        MainTable.put("CaseStatements", temp_column);

        //CaseStatementsRest -> CaseStatement CaseStatementRest |
        temp_column = new HashMap<>();
        temp_column.put("case", new String[] {
                "CaseStatement", "CaseStatementRest"
        });
        temp_column.put("}", new String[]{});
        MainTable.put("CaseStatementsRest",temp_column);

        //CaseStatement -> case GenExpression : Statements break ;
        temp_column = new HashMap<>();
        temp_column.put("case",new String[] {
             "case", "GenExpression", ":", "Statements", "break", ";"
        });
        temp_column.put("}", new String[] {"sync"});
        MainTable.put("CaseStatement", temp_column);

        //GenExpression -> Expression | RelExpression
        //TODO

        //Expression -> Term Expressions
        temp_column  = new HashMap<>();
        String[] expression_firsts = {
                "(", "true", "false", "<IDENTIFIER_LITERAL>", "<INTEGER_LITERAL>"
        };
        for(String s:expression_firsts)
            temp_column.put(s,new String[] {"Term", "Expressions"});
        String[] expression_follows = {
             ")" , ":" , "," , "==" , "<" , "&&"
        };
        for(String s:expression_follows)
            temp_column.put(s,new String[]{"sync"});
        MainTable.put("Expression",temp_column);

        //Expressions -> - Term Expressions | + Term Expressions |
        temp_column = new HashMap<>();
        temp_column.put("+",new String[] {
                "+", "Term", "Expressions"
        });
        temp_column.put("-",new String[] {
                "-", "Term", "Expressions"
        });
        String[] expressions_follow = {
                "==", "<", ")", "&&", ",", ":", ";"
        };
        for(String s:expressions_follow)
            temp_column.put(s,new String[]{});
        MainTable.put("Expressions",temp_column);

        //Term -> Factor Terms
        temp_column = new HashMap<>();
        for(String s :expression_firsts)
            temp_column.put(s,new String[]{"Factor" ,"Terms"});
        String[] term_follow = new String[] {
            ")" , ":" , "," ,"==" , "<" , "&&" , "-" , "+"
        };
        for(String s:term_follow)
            temp_column.put(s,new String[]{"sync"});
        MainTable.put("Term",temp_column);

        //Terms -> * Factor Terms |
        temp_column = new HashMap<>();
        temp_column.put("*", new String[] {
                "*","Factor", "Terms"
        });
        for(String s:term_follow)
            temp_column.put(s,new String[]{});
        MainTable.put("Terms",temp_column);

        //Factor -> ( Expression ) | Identifier FactorRest | true | false | Integer
        temp_column = new HashMap<>();
        temp_column.put(")", new String[] {"(","Expression",")"});
        temp_column.put("<IDENTIFIER_LITERAL>",new String[] {"Identifier", "FactorRest"});
        temp_column.put("<INTEGER_LITERAL>", new String[]{"Integer"});
        temp_column.put("true",new String[]{"true"});
        temp_column.put("false", new String[]{"false"});
        String[] factor_follow = {
                "*" , ")" , ":" , "," , "==" , "<" , "&&"
        };
        for(String s:factor_follow)
            temp_column.put(s,new String[]{"sync"});
        MainTable.put("Factor",temp_column);

        //FactorRest ->  | . Identifier FactorRest2
        temp_column = new HashMap<>();
        temp_column.put(".", new String[] {".", "Identifier", "FactorRest2"});
        for(String s:factor_follow)
            temp_column.put(s,new String[] {});
        MainTable.put("FactorRest",temp_column);

        //FactorRest2 ->  | ( Arguments )
        temp_column = new HashMap<>();
        temp_column.put(")" , new String[] {"(", "Arguments" ,")"});
        for(String s:factor_follow)
            temp_column.put(s,new String[]{});
        MainTable.put("FactorRest2",temp_column);

        //RelExpression -> RelTerm RelExpressions
        temp_column = new HashMap<>();
        String[] rel_firsts = {
                "(" , "<IDENTIFIER_LITERAL>" , "true" , "false" , "<INTEGER_LITERAL>"
        };
        for(String s:rel_firsts)
            temp_column.put(s, new String[] {"RelTerm", "RelExpressions"});
        temp_column.put(")",new String[]{"sync"});
        temp_column.put(":",new String[]{"sync"});
        temp_column.put(",",new String[]{"sync"});
        temp_column.put(";",new String[]{"sync"});
        MainTable.put("RelExpression",temp_column);

        //RelExpressions -> && RelTerm RelExpressions | EPSILON
        temp_column = new HashMap<>();
        temp_column.put("&&",new String[] {
                "&&", "RelTerm", "RelExpressions"
        });
        temp_column.put(")",new String[]{});
        temp_column.put(":",new String[]{});
        temp_column.put(",",new String[]{});
        temp_column.put(";",new String[]{"sync"});
        MainTable.put("RelExpressions",temp_column);

        //Relterm -> Expression RelTermRest
        temp_column = new HashMap<>();
        for(String s:expression_firsts)
            temp_column.put(s,new String[] {
                    "Expression", "RelTermRest"
            });
        temp_column.put("&&", new String[]{"sync"});
        MainTable.put("Relterm",temp_column);

        //RelTermRest -> == Expression | < Expression
        temp_column = new HashMap<>();
        temp_column.put("==", new String[] {"==", "Expression"});
        temp_column.put("<", new String[] {"<" ,"Expression"});
        String[] reltermrest_follow = {
                "(" , "<IDENTIFIER_LITERAL>" , "true" , "false" , "<INTEGER_LITERAL>" , "&&"
        };
        for(String s:reltermrest_follow)
            temp_column.put(s,new String[]{"sync"});
        MainTable.put("RelTermRest",temp_column);


        //Arguments -> GenExpression Argument | EPSILON
        temp_column = new HashMap<>();
        for(String s:expression_firsts)
            temp_column.put(s,new String[] {"GenExpression", "Argument"});
        temp_column.put(")", new String[]{});
        MainTable.put("Arguments", temp_column);

        //Argument -> , GenExpression Argument | EPSILON
        temp_column = new HashMap<>();
        temp_column.put(",", new String[] {",","GenExpression", "Argument"});
        temp_column.put(")", new String[]{});

        //Identifier -> <IDENTIFIER_LITERAL>
        //Integer -> <INTEGER_LITERAL>
        String[] integer_follows = {
                "*", "-", "+", "==", "<", ")", ",", ":", ";"
        };
        String[] identifier_follows = {
                "(", ".", "=", ",", ";", "extends", "{", "*", "-", "+", "==", "<", ")", ":"
        };
        
        temp_column = new HashMap<>();
        temp_column.put("<IDENTIFIER_LITERAL>", new String[]{"<IDENTIFIER_LITERAL>"});
        for(String s:identifier_follows)
            temp_column.put(s,new String[]{"sync"});
        MainTable.put("Identifier",temp_column);

        temp_column= new HashMap<>();
        temp_column.put("<INTEGER_LITERAL>", new String[]{"<INTEGER_LITERAL>"});
        for(String s:integer_follows)
            temp_column.put(s,new String[]{"sync"});
        MainTable.put("Integer",temp_column);
    }
}
