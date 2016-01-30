package Compiler.CodeGenerator;

public class MemoryOrganizer {
    private int currentDataPointer;
    private int currentTempDataPointer;
    private int codeblocksPointer;

    public MemoryOrganizer() {
        currentDataPointer = codeblocksPointer = 0;
        currentTempDataPointer = 100000;
    }

    public int reserveVar() {
        currentDataPointer += 4;
        return currentDataPointer - 4;
    }

    public int getTemp() {
        currentTempDataPointer -= 4;
        return currentTempDataPointer + 4;
    }

    public int getProgramBlock() {
        codeblocksPointer += 1;
        return  codeblocksPointer - 1;
    }
}
