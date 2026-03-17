/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.stackmap;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Opcode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.stackmap.TypeData;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.stackmap.TypeTag;

public abstract class Tracer
implements TypeTag {
    protected ClassPool classPool;
    protected ConstPool cpool;
    protected String returnType;
    protected int stackTop;
    protected TypeData[] stackTypes;
    protected TypeData[] localsTypes;

    public Tracer(ClassPool classPool, ConstPool constPool, int n, int n2, String string) {
        this.classPool = classPool;
        this.cpool = constPool;
        this.returnType = string;
        this.stackTop = 0;
        this.stackTypes = TypeData.make(n);
        this.localsTypes = TypeData.make(n2);
    }

    public Tracer(Tracer tracer) {
        this.classPool = tracer.classPool;
        this.cpool = tracer.cpool;
        this.returnType = tracer.returnType;
        this.stackTop = tracer.stackTop;
        this.stackTypes = TypeData.make(tracer.stackTypes.length);
        this.localsTypes = TypeData.make(tracer.localsTypes.length);
    }

    protected int doOpcode(int n, byte[] byArray) {
        try {
            int n2 = byArray[n] & 0xFF;
            if (n2 < 54) {
                return this.doOpcode0_53(n, byArray, n2);
            }
            if (n2 < 96) {
                return this.doOpcode54_95(n, byArray, n2);
            }
            if (n2 < 148) {
                return this.doOpcode96_147(n, byArray, n2);
            }
            return this.doOpcode148_201(n, byArray, n2);
        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new BadBytecode("inconsistent stack height " + arrayIndexOutOfBoundsException.getMessage(), (Throwable)arrayIndexOutOfBoundsException);
        }
    }

    protected void visitBranch(int n, byte[] byArray, int n2) {
    }

    protected void visitGoto(int n, byte[] byArray, int n2) {
    }

    protected void visitReturn(int n, byte[] byArray) {
    }

    protected void visitThrow(int n, byte[] byArray) {
    }

    protected void visitTableSwitch(int n, byte[] byArray, int n2, int n3, int n4) {
    }

    protected void visitLookupSwitch(int n, byte[] byArray, int n2, int n3, int n4) {
    }

    protected void visitJSR(int n, byte[] byArray) {
    }

    protected void visitRET(int n, byte[] byArray) {
    }

    private int doOpcode0_53(int n, byte[] byArray, int n2) {
        TypeData[] typeDataArray = this.stackTypes;
        switch (n2) {
            case 0: {
                break;
            }
            case 1: {
                typeDataArray[this.stackTop++] = new TypeData.NullType();
                break;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: {
                typeDataArray[this.stackTop++] = INTEGER;
                break;
            }
            case 9: 
            case 10: {
                typeDataArray[this.stackTop++] = LONG;
                typeDataArray[this.stackTop++] = TOP;
                break;
            }
            case 11: 
            case 12: 
            case 13: {
                typeDataArray[this.stackTop++] = FLOAT;
                break;
            }
            case 14: 
            case 15: {
                typeDataArray[this.stackTop++] = DOUBLE;
                typeDataArray[this.stackTop++] = TOP;
                break;
            }
            case 16: 
            case 17: {
                typeDataArray[this.stackTop++] = INTEGER;
                return n2 == 17 ? 3 : 2;
            }
            case 18: {
                this.doLDC(byArray[n + 1] & 0xFF);
                return 2;
            }
            case 19: 
            case 20: {
                this.doLDC(ByteArray.readU16bit(byArray, n + 1));
                return 3;
            }
            case 21: {
                return this.doXLOAD(INTEGER, byArray, n);
            }
            case 22: {
                return this.doXLOAD(LONG, byArray, n);
            }
            case 23: {
                return this.doXLOAD(FLOAT, byArray, n);
            }
            case 24: {
                return this.doXLOAD(DOUBLE, byArray, n);
            }
            case 25: {
                return this.doALOAD(byArray[n + 1] & 0xFF);
            }
            case 26: 
            case 27: 
            case 28: 
            case 29: {
                typeDataArray[this.stackTop++] = INTEGER;
                break;
            }
            case 30: 
            case 31: 
            case 32: 
            case 33: {
                typeDataArray[this.stackTop++] = LONG;
                typeDataArray[this.stackTop++] = TOP;
                break;
            }
            case 34: 
            case 35: 
            case 36: 
            case 37: {
                typeDataArray[this.stackTop++] = FLOAT;
                break;
            }
            case 38: 
            case 39: 
            case 40: 
            case 41: {
                typeDataArray[this.stackTop++] = DOUBLE;
                typeDataArray[this.stackTop++] = TOP;
                break;
            }
            case 42: 
            case 43: 
            case 44: 
            case 45: {
                int n3 = n2 - 42;
                typeDataArray[this.stackTop++] = this.localsTypes[n3];
                break;
            }
            case 46: {
                typeDataArray[--this.stackTop - 1] = INTEGER;
                break;
            }
            case 47: {
                typeDataArray[this.stackTop - 2] = LONG;
                typeDataArray[this.stackTop - 1] = TOP;
                break;
            }
            case 48: {
                typeDataArray[--this.stackTop - 1] = FLOAT;
                break;
            }
            case 49: {
                typeDataArray[this.stackTop - 2] = DOUBLE;
                typeDataArray[this.stackTop - 1] = TOP;
                break;
            }
            case 50: {
                int n4 = --this.stackTop - 1;
                TypeData typeData = typeDataArray[n4];
                typeDataArray[n4] = TypeData.ArrayElement.make(typeData);
                break;
            }
            case 51: 
            case 52: 
            case 53: {
                typeDataArray[--this.stackTop - 1] = INTEGER;
                break;
            }
            default: {
                throw new RuntimeException("fatal");
            }
        }
        return 1;
    }

    private void doLDC(int n) {
        TypeData[] typeDataArray = this.stackTypes;
        int n2 = this.cpool.getTag(n);
        if (n2 == 8) {
            typeDataArray[this.stackTop++] = new TypeData.ClassName("java.lang.String");
        } else if (n2 == 3) {
            typeDataArray[this.stackTop++] = INTEGER;
        } else if (n2 == 4) {
            typeDataArray[this.stackTop++] = FLOAT;
        } else if (n2 == 5) {
            typeDataArray[this.stackTop++] = LONG;
            typeDataArray[this.stackTop++] = TOP;
        } else if (n2 == 6) {
            typeDataArray[this.stackTop++] = DOUBLE;
            typeDataArray[this.stackTop++] = TOP;
        } else if (n2 == 7) {
            typeDataArray[this.stackTop++] = new TypeData.ClassName("java.lang.Class");
        } else if (n2 == 17) {
            String string = this.cpool.getDynamicType(n);
            this.pushMemberType(string);
        } else {
            throw new RuntimeException("bad LDC: " + n2);
        }
    }

    private int doXLOAD(TypeData typeData, byte[] byArray, int n) {
        int n2 = byArray[n + 1] & 0xFF;
        return this.doXLOAD(n2, typeData);
    }

    private int doXLOAD(int n, TypeData typeData) {
        this.stackTypes[this.stackTop++] = typeData;
        if (typeData.is2WordType()) {
            this.stackTypes[this.stackTop++] = TOP;
        }
        return 2;
    }

    private int doALOAD(int n) {
        this.stackTypes[this.stackTop++] = this.localsTypes[n];
        return 2;
    }

    private int doOpcode54_95(int n, byte[] byArray, int n2) {
        switch (n2) {
            case 54: {
                return this.doXSTORE(n, byArray, INTEGER);
            }
            case 55: {
                return this.doXSTORE(n, byArray, LONG);
            }
            case 56: {
                return this.doXSTORE(n, byArray, FLOAT);
            }
            case 57: {
                return this.doXSTORE(n, byArray, DOUBLE);
            }
            case 58: {
                return this.doASTORE(byArray[n + 1] & 0xFF);
            }
            case 59: 
            case 60: 
            case 61: 
            case 62: {
                int n3 = n2 - 59;
                this.localsTypes[n3] = INTEGER;
                --this.stackTop;
                break;
            }
            case 63: 
            case 64: 
            case 65: 
            case 66: {
                int n4 = n2 - 63;
                this.localsTypes[n4] = LONG;
                this.localsTypes[n4 + 1] = TOP;
                this.stackTop -= 2;
                break;
            }
            case 67: 
            case 68: 
            case 69: 
            case 70: {
                int n5 = n2 - 67;
                this.localsTypes[n5] = FLOAT;
                --this.stackTop;
                break;
            }
            case 71: 
            case 72: 
            case 73: 
            case 74: {
                int n6 = n2 - 71;
                this.localsTypes[n6] = DOUBLE;
                this.localsTypes[n6 + 1] = TOP;
                this.stackTop -= 2;
                break;
            }
            case 75: 
            case 76: 
            case 77: 
            case 78: {
                int n7 = n2 - 75;
                this.doASTORE(n7);
                break;
            }
            case 79: 
            case 80: 
            case 81: 
            case 82: {
                this.stackTop -= n2 == 80 || n2 == 82 ? 4 : 3;
                break;
            }
            case 83: {
                TypeData.ArrayElement.aastore(this.stackTypes[this.stackTop - 3], this.stackTypes[this.stackTop - 1], this.classPool);
                this.stackTop -= 3;
                break;
            }
            case 84: 
            case 85: 
            case 86: {
                this.stackTop -= 3;
                break;
            }
            case 87: {
                --this.stackTop;
                break;
            }
            case 88: {
                this.stackTop -= 2;
                break;
            }
            case 89: {
                int n8 = this.stackTop;
                this.stackTypes[n8] = this.stackTypes[n8 - 1];
                this.stackTop = n8 + 1;
                break;
            }
            case 90: 
            case 91: {
                int n9 = n2 - 90 + 2;
                this.doDUP_XX(1, n9);
                int n10 = this.stackTop;
                this.stackTypes[n10 - n9] = this.stackTypes[n10];
                this.stackTop = n10 + 1;
                break;
            }
            case 92: {
                this.doDUP_XX(2, 2);
                this.stackTop += 2;
                break;
            }
            case 93: 
            case 94: {
                int n11 = n2 - 93 + 3;
                this.doDUP_XX(2, n11);
                int n12 = this.stackTop;
                this.stackTypes[n12 - n11] = this.stackTypes[n12];
                this.stackTypes[n12 - n11 + 1] = this.stackTypes[n12 + 1];
                this.stackTop = n12 + 2;
                break;
            }
            case 95: {
                int n13 = this.stackTop - 1;
                TypeData typeData = this.stackTypes[n13];
                this.stackTypes[n13] = this.stackTypes[n13 - 1];
                this.stackTypes[n13 - 1] = typeData;
                break;
            }
            default: {
                throw new RuntimeException("fatal");
            }
        }
        return 1;
    }

    private int doXSTORE(int n, byte[] byArray, TypeData typeData) {
        int n2 = byArray[n + 1] & 0xFF;
        return this.doXSTORE(n2, typeData);
    }

    private int doXSTORE(int n, TypeData typeData) {
        --this.stackTop;
        this.localsTypes[n] = typeData;
        if (typeData.is2WordType()) {
            --this.stackTop;
            this.localsTypes[n + 1] = TOP;
        }
        return 2;
    }

    private int doASTORE(int n) {
        --this.stackTop;
        this.localsTypes[n] = this.stackTypes[this.stackTop];
        return 2;
    }

    private void doDUP_XX(int n, int n2) {
        int n3;
        TypeData[] typeDataArray = this.stackTypes;
        int n4 = n3 - n2;
        for (n3 = this.stackTop - 1; n3 > n4; --n3) {
            typeDataArray[n3 + n] = typeDataArray[n3];
        }
    }

    private int doOpcode96_147(int n, byte[] byArray, int n2) {
        if (n2 <= 131) {
            this.stackTop += Opcode.STACK_GROW[n2];
            return 1;
        }
        switch (n2) {
            case 132: {
                return 3;
            }
            case 133: {
                this.stackTypes[this.stackTop - 1] = LONG;
                this.stackTypes[this.stackTop] = TOP;
                ++this.stackTop;
                break;
            }
            case 134: {
                this.stackTypes[this.stackTop - 1] = FLOAT;
                break;
            }
            case 135: {
                this.stackTypes[this.stackTop - 1] = DOUBLE;
                this.stackTypes[this.stackTop] = TOP;
                ++this.stackTop;
                break;
            }
            case 136: {
                this.stackTypes[--this.stackTop - 1] = INTEGER;
                break;
            }
            case 137: {
                this.stackTypes[--this.stackTop - 1] = FLOAT;
                break;
            }
            case 138: {
                this.stackTypes[this.stackTop - 2] = DOUBLE;
                break;
            }
            case 139: {
                this.stackTypes[this.stackTop - 1] = INTEGER;
                break;
            }
            case 140: {
                this.stackTypes[this.stackTop - 1] = LONG;
                this.stackTypes[this.stackTop] = TOP;
                ++this.stackTop;
                break;
            }
            case 141: {
                this.stackTypes[this.stackTop - 1] = DOUBLE;
                this.stackTypes[this.stackTop] = TOP;
                ++this.stackTop;
                break;
            }
            case 142: {
                this.stackTypes[--this.stackTop - 1] = INTEGER;
                break;
            }
            case 143: {
                this.stackTypes[this.stackTop - 2] = LONG;
                break;
            }
            case 144: {
                this.stackTypes[--this.stackTop - 1] = FLOAT;
                break;
            }
            case 145: 
            case 146: 
            case 147: {
                break;
            }
            default: {
                throw new RuntimeException("fatal");
            }
        }
        return 1;
    }

    private int doOpcode148_201(int n, byte[] byArray, int n2) {
        switch (n2) {
            case 148: {
                this.stackTypes[this.stackTop - 4] = INTEGER;
                this.stackTop -= 3;
                break;
            }
            case 149: 
            case 150: {
                this.stackTypes[--this.stackTop - 1] = INTEGER;
                break;
            }
            case 151: 
            case 152: {
                this.stackTypes[this.stackTop - 4] = INTEGER;
                this.stackTop -= 3;
                break;
            }
            case 153: 
            case 154: 
            case 155: 
            case 156: 
            case 157: 
            case 158: {
                --this.stackTop;
                this.visitBranch(n, byArray, ByteArray.readS16bit(byArray, n + 1));
                return 3;
            }
            case 159: 
            case 160: 
            case 161: 
            case 162: 
            case 163: 
            case 164: 
            case 165: 
            case 166: {
                this.stackTop -= 2;
                this.visitBranch(n, byArray, ByteArray.readS16bit(byArray, n + 1));
                return 3;
            }
            case 167: {
                this.visitGoto(n, byArray, ByteArray.readS16bit(byArray, n + 1));
                return 3;
            }
            case 168: {
                this.visitJSR(n, byArray);
                return 3;
            }
            case 169: {
                this.visitRET(n, byArray);
                return 2;
            }
            case 170: {
                --this.stackTop;
                int n3 = (n & 0xFFFFFFFC) + 8;
                int n4 = ByteArray.read32bit(byArray, n3);
                int n5 = ByteArray.read32bit(byArray, n3 + 4);
                int n6 = n5 - n4 + 1;
                this.visitTableSwitch(n, byArray, n6, n3 + 8, ByteArray.read32bit(byArray, n3 - 4));
                return n6 * 4 + 16 - (n & 3);
            }
            case 171: {
                --this.stackTop;
                int n7 = (n & 0xFFFFFFFC) + 8;
                int n8 = ByteArray.read32bit(byArray, n7);
                this.visitLookupSwitch(n, byArray, n8, n7 + 4, ByteArray.read32bit(byArray, n7 - 4));
                return n8 * 8 + 12 - (n & 3);
            }
            case 172: {
                --this.stackTop;
                this.visitReturn(n, byArray);
                break;
            }
            case 173: {
                this.stackTop -= 2;
                this.visitReturn(n, byArray);
                break;
            }
            case 174: {
                --this.stackTop;
                this.visitReturn(n, byArray);
                break;
            }
            case 175: {
                this.stackTop -= 2;
                this.visitReturn(n, byArray);
                break;
            }
            case 176: {
                this.stackTypes[--this.stackTop].setType(this.returnType, this.classPool);
                this.visitReturn(n, byArray);
                break;
            }
            case 177: {
                this.visitReturn(n, byArray);
                break;
            }
            case 178: {
                return this.doGetField(n, byArray, false);
            }
            case 179: {
                return this.doPutField(n, byArray, false);
            }
            case 180: {
                return this.doGetField(n, byArray, true);
            }
            case 181: {
                return this.doPutField(n, byArray, true);
            }
            case 182: 
            case 183: {
                return this.doInvokeMethod(n, byArray, true);
            }
            case 184: {
                return this.doInvokeMethod(n, byArray, false);
            }
            case 185: {
                return this.doInvokeIntfMethod(n, byArray);
            }
            case 186: {
                return this.doInvokeDynamic(n, byArray);
            }
            case 187: {
                int n9 = ByteArray.readU16bit(byArray, n + 1);
                this.stackTypes[this.stackTop++] = new TypeData.UninitData(n, this.cpool.getClassInfo(n9));
                return 3;
            }
            case 188: {
                return this.doNEWARRAY(n, byArray);
            }
            case 189: {
                int n10 = ByteArray.readU16bit(byArray, n + 1);
                String string = this.cpool.getClassInfo(n10).replace('.', '/');
                string = string.charAt(0) == '[' ? "[" + string : "[L" + string + ";";
                this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(string);
                return 3;
            }
            case 190: {
                this.stackTypes[this.stackTop - 1].setType("[Ljava.lang.Object;", this.classPool);
                this.stackTypes[this.stackTop - 1] = INTEGER;
                break;
            }
            case 191: {
                this.stackTypes[--this.stackTop].setType("java.lang.Throwable", this.classPool);
                this.visitThrow(n, byArray);
                break;
            }
            case 192: {
                int n11 = ByteArray.readU16bit(byArray, n + 1);
                String string = this.cpool.getClassInfo(n11);
                if (string.charAt(0) == '[') {
                    string = string.replace('.', '/');
                }
                this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(string);
                return 3;
            }
            case 193: {
                this.stackTypes[this.stackTop - 1] = INTEGER;
                return 3;
            }
            case 194: 
            case 195: {
                --this.stackTop;
                break;
            }
            case 196: {
                return this.doWIDE(n, byArray);
            }
            case 197: {
                return this.doMultiANewArray(n, byArray);
            }
            case 198: 
            case 199: {
                --this.stackTop;
                this.visitBranch(n, byArray, ByteArray.readS16bit(byArray, n + 1));
                return 3;
            }
            case 200: {
                this.visitGoto(n, byArray, ByteArray.read32bit(byArray, n + 1));
                return 5;
            }
            case 201: {
                this.visitJSR(n, byArray);
                return 5;
            }
        }
        return 1;
    }

    private int doWIDE(int n, byte[] byArray) {
        int n2 = byArray[n + 1] & 0xFF;
        switch (n2) {
            case 21: {
                this.doWIDE_XLOAD(n, byArray, INTEGER);
                break;
            }
            case 22: {
                this.doWIDE_XLOAD(n, byArray, LONG);
                break;
            }
            case 23: {
                this.doWIDE_XLOAD(n, byArray, FLOAT);
                break;
            }
            case 24: {
                this.doWIDE_XLOAD(n, byArray, DOUBLE);
                break;
            }
            case 25: {
                int n3 = ByteArray.readU16bit(byArray, n + 2);
                this.doALOAD(n3);
                break;
            }
            case 54: {
                this.doWIDE_STORE(n, byArray, INTEGER);
                break;
            }
            case 55: {
                this.doWIDE_STORE(n, byArray, LONG);
                break;
            }
            case 56: {
                this.doWIDE_STORE(n, byArray, FLOAT);
                break;
            }
            case 57: {
                this.doWIDE_STORE(n, byArray, DOUBLE);
                break;
            }
            case 58: {
                int n4 = ByteArray.readU16bit(byArray, n + 2);
                this.doASTORE(n4);
                break;
            }
            case 132: {
                return 6;
            }
            case 169: {
                this.visitRET(n, byArray);
                break;
            }
            default: {
                throw new RuntimeException("bad WIDE instruction: " + n2);
            }
        }
        return 4;
    }

    private void doWIDE_XLOAD(int n, byte[] byArray, TypeData typeData) {
        int n2 = ByteArray.readU16bit(byArray, n + 2);
        this.doXLOAD(n2, typeData);
    }

    private void doWIDE_STORE(int n, byte[] byArray, TypeData typeData) {
        int n2 = ByteArray.readU16bit(byArray, n + 2);
        this.doXSTORE(n2, typeData);
    }

    private int doPutField(int n, byte[] byArray, boolean bl) {
        int n2 = ByteArray.readU16bit(byArray, n + 1);
        String string = this.cpool.getFieldrefType(n2);
        this.stackTop -= Descriptor.dataSize(string);
        char c = string.charAt(0);
        if (c == 'L') {
            this.stackTypes[this.stackTop].setType(Tracer.getFieldClassName(string, 0), this.classPool);
        } else if (c == '[') {
            this.stackTypes[this.stackTop].setType(string, this.classPool);
        }
        this.setFieldTarget(bl, n2);
        return 3;
    }

    private int doGetField(int n, byte[] byArray, boolean bl) {
        int n2 = ByteArray.readU16bit(byArray, n + 1);
        this.setFieldTarget(bl, n2);
        String string = this.cpool.getFieldrefType(n2);
        this.pushMemberType(string);
        return 3;
    }

    private void setFieldTarget(boolean bl, int n) {
        if (bl) {
            String string = this.cpool.getFieldrefClassName(n);
            this.stackTypes[--this.stackTop].setType(string, this.classPool);
        }
    }

    private int doNEWARRAY(int n, byte[] byArray) {
        String string;
        int n2 = this.stackTop - 1;
        switch (byArray[n + 1] & 0xFF) {
            case 4: {
                string = "[Z";
                break;
            }
            case 5: {
                string = "[C";
                break;
            }
            case 6: {
                string = "[F";
                break;
            }
            case 7: {
                string = "[D";
                break;
            }
            case 8: {
                string = "[B";
                break;
            }
            case 9: {
                string = "[S";
                break;
            }
            case 10: {
                string = "[I";
                break;
            }
            case 11: {
                string = "[J";
                break;
            }
            default: {
                throw new RuntimeException("bad newarray");
            }
        }
        this.stackTypes[n2] = new TypeData.ClassName(string);
        return 2;
    }

    private int doMultiANewArray(int n, byte[] byArray) {
        int n2 = ByteArray.readU16bit(byArray, n + 1);
        int n3 = byArray[n + 3] & 0xFF;
        this.stackTop -= n3 - 1;
        String string = this.cpool.getClassInfo(n2).replace('.', '/');
        this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(string);
        return 4;
    }

    private int doInvokeMethod(int n, byte[] byArray, boolean bl) {
        int n2 = ByteArray.readU16bit(byArray, n + 1);
        String string = this.cpool.getMethodrefType(n2);
        this.checkParamTypes(string, 1);
        if (bl) {
            TypeData typeData;
            String string2 = this.cpool.getMethodrefClassName(n2);
            if ((typeData = this.stackTypes[--this.stackTop]) instanceof TypeData.UninitTypeVar && typeData.isUninit()) {
                this.constructorCalled(typeData, ((TypeData.UninitTypeVar)typeData).offset());
            } else if (typeData instanceof TypeData.UninitData) {
                this.constructorCalled(typeData, ((TypeData.UninitData)typeData).offset());
            }
            typeData.setType(string2, this.classPool);
        }
        this.pushMemberType(string);
        return 3;
    }

    private void constructorCalled(TypeData typeData, int n) {
        int n2;
        typeData.constructorCalled(n);
        for (n2 = 0; n2 < this.stackTop; ++n2) {
            this.stackTypes[n2].constructorCalled(n);
        }
        for (n2 = 0; n2 < this.localsTypes.length; ++n2) {
            this.localsTypes[n2].constructorCalled(n);
        }
    }

    private int doInvokeIntfMethod(int n, byte[] byArray) {
        int n2 = ByteArray.readU16bit(byArray, n + 1);
        String string = this.cpool.getInterfaceMethodrefType(n2);
        this.checkParamTypes(string, 1);
        String string2 = this.cpool.getInterfaceMethodrefClassName(n2);
        this.stackTypes[--this.stackTop].setType(string2, this.classPool);
        this.pushMemberType(string);
        return 5;
    }

    private int doInvokeDynamic(int n, byte[] byArray) {
        int n2 = ByteArray.readU16bit(byArray, n + 1);
        String string = this.cpool.getInvokeDynamicType(n2);
        this.checkParamTypes(string, 1);
        this.pushMemberType(string);
        return 5;
    }

    private void pushMemberType(String string) {
        int n = 0;
        if (string.charAt(0) == '(' && (n = string.indexOf(41) + 1) < 1) {
            throw new IndexOutOfBoundsException("bad descriptor: " + string);
        }
        TypeData[] typeDataArray = this.stackTypes;
        int n2 = this.stackTop;
        switch (string.charAt(n)) {
            case '[': {
                typeDataArray[n2] = new TypeData.ClassName(string.substring(n));
                break;
            }
            case 'L': {
                typeDataArray[n2] = new TypeData.ClassName(Tracer.getFieldClassName(string, n));
                break;
            }
            case 'J': {
                typeDataArray[n2] = LONG;
                typeDataArray[n2 + 1] = TOP;
                this.stackTop += 2;
                return;
            }
            case 'F': {
                typeDataArray[n2] = FLOAT;
                break;
            }
            case 'D': {
                typeDataArray[n2] = DOUBLE;
                typeDataArray[n2 + 1] = TOP;
                this.stackTop += 2;
                return;
            }
            case 'V': {
                return;
            }
            default: {
                typeDataArray[n2] = INTEGER;
            }
        }
        ++this.stackTop;
    }

    private static String getFieldClassName(String string, int n) {
        return string.substring(n + 1, string.length() - 1).replace('/', '.');
    }

    private void checkParamTypes(String string, int n) {
        char c = string.charAt(n);
        if (c == ')') {
            return;
        }
        int n2 = n;
        boolean bl = false;
        while (c == '[') {
            bl = true;
            c = string.charAt(++n2);
        }
        if (c == 'L') {
            if ((n2 = string.indexOf(59, n2) + 1) <= 0) {
                throw new IndexOutOfBoundsException("bad descriptor");
            }
        } else {
            ++n2;
        }
        this.checkParamTypes(string, n2);
        this.stackTop = !(bl || c != 'J' && c != 'D') ? (this.stackTop -= 2) : --this.stackTop;
        if (bl) {
            this.stackTypes[this.stackTop].setType(string.substring(n, n2), this.classPool);
        } else if (c == 'L') {
            this.stackTypes[this.stackTop].setType(string.substring(n + 1, n2 - 1).replace('/', '.'), this.classPool);
        }
    }
}

