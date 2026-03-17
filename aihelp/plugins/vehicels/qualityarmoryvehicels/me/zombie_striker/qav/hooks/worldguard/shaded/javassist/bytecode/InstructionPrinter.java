/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.PrintStream;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Mnemonic;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Opcode;

public class InstructionPrinter
implements Opcode {
    private static final String[] opcodes = Mnemonic.OPCODE;
    private final PrintStream stream;

    public InstructionPrinter(PrintStream printStream) {
        this.stream = printStream;
    }

    public static void print(CtMethod ctMethod, PrintStream printStream) {
        new InstructionPrinter(printStream).print(ctMethod);
    }

    public void print(CtMethod ctMethod) {
        MethodInfo methodInfo = ctMethod.getMethodInfo2();
        ConstPool constPool = methodInfo.getConstPool();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        if (codeAttribute == null) {
            return;
        }
        CodeIterator codeIterator = codeAttribute.iterator();
        while (codeIterator.hasNext()) {
            int n;
            try {
                n = codeIterator.next();
            } catch (BadBytecode badBytecode) {
                throw new RuntimeException(badBytecode);
            }
            this.stream.println(n + ": " + InstructionPrinter.instructionString(codeIterator, n, constPool));
        }
    }

    public static String instructionString(CodeIterator codeIterator, int n, ConstPool constPool) {
        int n2 = codeIterator.byteAt(n);
        if (n2 > opcodes.length || n2 < 0) {
            throw new IllegalArgumentException("Invalid opcode, opcode: " + n2 + " pos: " + n);
        }
        String string = opcodes[n2];
        switch (n2) {
            case 16: {
                return string + " " + codeIterator.byteAt(n + 1);
            }
            case 17: {
                return string + " " + codeIterator.s16bitAt(n + 1);
            }
            case 18: {
                return string + " " + InstructionPrinter.ldc(constPool, codeIterator.byteAt(n + 1));
            }
            case 19: 
            case 20: {
                return string + " " + InstructionPrinter.ldc(constPool, codeIterator.u16bitAt(n + 1));
            }
            case 21: 
            case 22: 
            case 23: 
            case 24: 
            case 25: 
            case 54: 
            case 55: 
            case 56: 
            case 57: 
            case 58: {
                return string + " " + codeIterator.byteAt(n + 1);
            }
            case 153: 
            case 154: 
            case 155: 
            case 156: 
            case 157: 
            case 158: 
            case 159: 
            case 160: 
            case 161: 
            case 162: 
            case 163: 
            case 164: 
            case 165: 
            case 166: 
            case 198: 
            case 199: {
                return string + " " + (codeIterator.s16bitAt(n + 1) + n);
            }
            case 132: {
                return string + " " + codeIterator.byteAt(n + 1) + ", " + codeIterator.signedByteAt(n + 2);
            }
            case 167: 
            case 168: {
                return string + " " + (codeIterator.s16bitAt(n + 1) + n);
            }
            case 169: {
                return string + " " + codeIterator.byteAt(n + 1);
            }
            case 170: {
                return InstructionPrinter.tableSwitch(codeIterator, n);
            }
            case 171: {
                return InstructionPrinter.lookupSwitch(codeIterator, n);
            }
            case 178: 
            case 179: 
            case 180: 
            case 181: {
                return string + " " + InstructionPrinter.fieldInfo(constPool, codeIterator.u16bitAt(n + 1));
            }
            case 182: 
            case 183: 
            case 184: {
                return string + " " + InstructionPrinter.methodInfo(constPool, codeIterator.u16bitAt(n + 1));
            }
            case 185: {
                return string + " " + InstructionPrinter.interfaceMethodInfo(constPool, codeIterator.u16bitAt(n + 1));
            }
            case 186: {
                return string + " " + codeIterator.u16bitAt(n + 1);
            }
            case 187: {
                return string + " " + InstructionPrinter.classInfo(constPool, codeIterator.u16bitAt(n + 1));
            }
            case 188: {
                return string + " " + InstructionPrinter.arrayInfo(codeIterator.byteAt(n + 1));
            }
            case 189: 
            case 192: {
                return string + " " + InstructionPrinter.classInfo(constPool, codeIterator.u16bitAt(n + 1));
            }
            case 196: {
                return InstructionPrinter.wide(codeIterator, n);
            }
            case 197: {
                return string + " " + InstructionPrinter.classInfo(constPool, codeIterator.u16bitAt(n + 1));
            }
            case 200: 
            case 201: {
                return string + " " + (codeIterator.s32bitAt(n + 1) + n);
            }
        }
        return string;
    }

    private static String wide(CodeIterator codeIterator, int n) {
        int n2 = codeIterator.byteAt(n + 1);
        int n3 = codeIterator.u16bitAt(n + 2);
        switch (n2) {
            case 21: 
            case 22: 
            case 23: 
            case 24: 
            case 25: 
            case 54: 
            case 55: 
            case 56: 
            case 57: 
            case 58: 
            case 132: 
            case 169: {
                return opcodes[n2] + " " + n3;
            }
        }
        throw new RuntimeException("Invalid WIDE operand");
    }

    private static String arrayInfo(int n) {
        switch (n) {
            case 4: {
                return "boolean";
            }
            case 5: {
                return "char";
            }
            case 8: {
                return "byte";
            }
            case 9: {
                return "short";
            }
            case 10: {
                return "int";
            }
            case 11: {
                return "long";
            }
            case 6: {
                return "float";
            }
            case 7: {
                return "double";
            }
        }
        throw new RuntimeException("Invalid array type");
    }

    private static String classInfo(ConstPool constPool, int n) {
        return "#" + n + " = Class " + constPool.getClassInfo(n);
    }

    private static String interfaceMethodInfo(ConstPool constPool, int n) {
        return "#" + n + " = Method " + constPool.getInterfaceMethodrefClassName(n) + "." + constPool.getInterfaceMethodrefName(n) + "(" + constPool.getInterfaceMethodrefType(n) + ")";
    }

    private static String methodInfo(ConstPool constPool, int n) {
        return "#" + n + " = Method " + constPool.getMethodrefClassName(n) + "." + constPool.getMethodrefName(n) + "(" + constPool.getMethodrefType(n) + ")";
    }

    private static String fieldInfo(ConstPool constPool, int n) {
        return "#" + n + " = Field " + constPool.getFieldrefClassName(n) + "." + constPool.getFieldrefName(n) + "(" + constPool.getFieldrefType(n) + ")";
    }

    private static String lookupSwitch(CodeIterator codeIterator, int n) {
        StringBuilder stringBuilder = new StringBuilder("lookupswitch {\n");
        int n2 = (n & 0xFFFFFFFC) + 4;
        stringBuilder.append("\t\tdefault: ").append(n + codeIterator.s32bitAt(n2)).append('\n');
        int n3 = codeIterator.s32bitAt(n2 += 4);
        int n4 = n3 * 8 + (n2 += 4);
        while (n2 < n4) {
            int n5 = codeIterator.s32bitAt(n2);
            int n6 = codeIterator.s32bitAt(n2 + 4) + n;
            stringBuilder.append("\t\t").append(n5).append(": ").append(n6).append('\n');
            n2 += 8;
        }
        stringBuilder.setCharAt(stringBuilder.length() - 1, '}');
        return stringBuilder.toString();
    }

    private static String tableSwitch(CodeIterator codeIterator, int n) {
        StringBuilder stringBuilder = new StringBuilder("tableswitch {\n");
        int n2 = (n & 0xFFFFFFFC) + 4;
        stringBuilder.append("\t\tdefault: ").append(n + codeIterator.s32bitAt(n2)).append('\n');
        int n3 = codeIterator.s32bitAt(n2 += 4);
        int n4 = codeIterator.s32bitAt(n2 += 4);
        int n5 = (n4 - n3 + 1) * 4 + (n2 += 4);
        int n6 = n3;
        while (n2 < n5) {
            int n7 = codeIterator.s32bitAt(n2) + n;
            stringBuilder.append("\t\t").append(n6).append(": ").append(n7).append('\n');
            n2 += 4;
            ++n6;
        }
        stringBuilder.setCharAt(stringBuilder.length() - 1, '}');
        return stringBuilder.toString();
    }

    private static String ldc(ConstPool constPool, int n) {
        int n2 = constPool.getTag(n);
        switch (n2) {
            case 8: {
                return "#" + n + " = \"" + constPool.getStringInfo(n) + "\"";
            }
            case 3: {
                return "#" + n + " = int " + constPool.getIntegerInfo(n);
            }
            case 4: {
                return "#" + n + " = float " + constPool.getFloatInfo(n);
            }
            case 5: {
                return "#" + n + " = long " + constPool.getLongInfo(n);
            }
            case 6: {
                return "#" + n + " = double " + constPool.getDoubleInfo(n);
            }
            case 7: {
                return InstructionPrinter.classInfo(constPool, n);
            }
        }
        throw new RuntimeException("bad LDC: " + n2);
    }
}

