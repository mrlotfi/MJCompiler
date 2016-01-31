package Compiler.CodeGenerator;

public class Instruction {
    public String type;
    public int[] op;
    public int[] op_type;

    public Instruction(String type,int op1,int op2,int op3,int op1type,int op2type,int op3type) {
        this.type = type;
        op = new int[3];
        op_type = new int[3];
        op[0] = op1;
        op[1] = op2;
        op[2] = op3;
        op_type[0] = op1type;
        op_type[1] = op2type;
        op_type[2] = op3type;
    }

    public String toString() {
        String t = "( "+type+", ";
        for(int i=0;i<3;i++) {
            if(op[i] == -1)
                break;
            switch (op_type[i]) {
                case 0:
                    t = t + op[i] + ", ";
                    break;
                case 1:
                    t = t + "@" + op[i] + ", ";
                    break;
                case 2:
                    t = t + "#" + op[i] + ", ";
                    break;
            }
        }
        t = t+" )";
        return t;
    }
}
