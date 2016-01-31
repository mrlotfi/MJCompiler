package Compiler.CodeGenerator;

import java.util.ArrayList;

public class MemoryOrganizer {
    private int currentDataPointer;
    private int currentTempDataPointer;
    private int codeblocksPointer;

    public Instruction[] code;


    public MemoryOrganizer() {
        currentDataPointer = codeblocksPointer = 0;
        currentTempDataPointer = 100000;
        code = new Instruction[10000];
    }

    public int reserveVar() {
        currentDataPointer += 4;
        return currentDataPointer - 4;
    }

    public int reserveTemp() {
        currentTempDataPointer -= 4;
        return currentTempDataPointer + 4;
    }

    public int reserveProgramBlock() {
        codeblocksPointer += 1;
        return  codeblocksPointer - 1;
    }


    public int getCodeblocksPointer() {
        return codeblocksPointer;
    }
}
