package Compiler.CodeGenerator;

public class Instruction {
    public String type;
    public int[] op;
    public int[] op_type;

    public Instruction(String type,int... optype) {
        this.type = type;
        op = new int[3];
        op_type = new int[3];
        for(int i=0;i<3;i++)
            op[i] = optype[i];
        for(int i=3;i<6;i++)
            op_type[i-3] = optype[i];
    }
}
