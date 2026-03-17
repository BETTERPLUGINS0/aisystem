/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Opcode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Frame;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Subroutine;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Type;

public class Executor
implements Opcode {
    private final ConstPool constPool;
    private final ClassPool classPool;
    private final Type STRING_TYPE;
    private final Type CLASS_TYPE;
    private final Type THROWABLE_TYPE;
    private int lastPos;

    public Executor(ClassPool classPool, ConstPool constPool) {
        this.constPool = constPool;
        this.classPool = classPool;
        try {
            this.STRING_TYPE = this.getType("java.lang.String");
            this.CLASS_TYPE = this.getType("java.lang.Class");
            this.THROWABLE_TYPE = this.getType("java.lang.Throwable");
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public void execute(MethodInfo methodInfo, int n, CodeIterator codeIterator, Frame frame, Subroutine subroutine) {
        this.lastPos = n;
        int n2 = codeIterator.byteAt(n);
        switch (n2) {
            case 0: {
                break;
            }
            case 1: {
                frame.push(Type.UNINIT);
                break;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: {
                frame.push(Type.INTEGER);
                break;
            }
            case 9: 
            case 10: {
                frame.push(Type.LONG);
                frame.push(Type.TOP);
                break;
            }
            case 11: 
            case 12: 
            case 13: {
                frame.push(Type.FLOAT);
                break;
            }
            case 14: 
            case 15: {
                frame.push(Type.DOUBLE);
                frame.push(Type.TOP);
                break;
            }
            case 16: 
            case 17: {
                frame.push(Type.INTEGER);
                break;
            }
            case 18: {
                this.evalLDC(codeIterator.byteAt(n + 1), frame);
                break;
            }
            case 19: 
            case 20: {
                this.evalLDC(codeIterator.u16bitAt(n + 1), frame);
                break;
            }
            case 21: {
                this.evalLoad(Type.INTEGER, codeIterator.byteAt(n + 1), frame, subroutine);
                break;
            }
            case 22: {
                this.evalLoad(Type.LONG, codeIterator.byteAt(n + 1), frame, subroutine);
                break;
            }
            case 23: {
                this.evalLoad(Type.FLOAT, codeIterator.byteAt(n + 1), frame, subroutine);
                break;
            }
            case 24: {
                this.evalLoad(Type.DOUBLE, codeIterator.byteAt(n + 1), frame, subroutine);
                break;
            }
            case 25: {
                this.evalLoad(Type.OBJECT, codeIterator.byteAt(n + 1), frame, subroutine);
                break;
            }
            case 26: 
            case 27: 
            case 28: 
            case 29: {
                this.evalLoad(Type.INTEGER, n2 - 26, frame, subroutine);
                break;
            }
            case 30: 
            case 31: 
            case 32: 
            case 33: {
                this.evalLoad(Type.LONG, n2 - 30, frame, subroutine);
                break;
            }
            case 34: 
            case 35: 
            case 36: 
            case 37: {
                this.evalLoad(Type.FLOAT, n2 - 34, frame, subroutine);
                break;
            }
            case 38: 
            case 39: 
            case 40: 
            case 41: {
                this.evalLoad(Type.DOUBLE, n2 - 38, frame, subroutine);
                break;
            }
            case 42: 
            case 43: 
            case 44: 
            case 45: {
                this.evalLoad(Type.OBJECT, n2 - 42, frame, subroutine);
                break;
            }
            case 46: {
                this.evalArrayLoad(Type.INTEGER, frame);
                break;
            }
            case 47: {
                this.evalArrayLoad(Type.LONG, frame);
                break;
            }
            case 48: {
                this.evalArrayLoad(Type.FLOAT, frame);
                break;
            }
            case 49: {
                this.evalArrayLoad(Type.DOUBLE, frame);
                break;
            }
            case 50: {
                this.evalArrayLoad(Type.OBJECT, frame);
                break;
            }
            case 51: 
            case 52: 
            case 53: {
                this.evalArrayLoad(Type.INTEGER, frame);
                break;
            }
            case 54: {
                this.evalStore(Type.INTEGER, codeIterator.byteAt(n + 1), frame, subroutine);
                break;
            }
            case 55: {
                this.evalStore(Type.LONG, codeIterator.byteAt(n + 1), frame, subroutine);
                break;
            }
            case 56: {
                this.evalStore(Type.FLOAT, codeIterator.byteAt(n + 1), frame, subroutine);
                break;
            }
            case 57: {
                this.evalStore(Type.DOUBLE, codeIterator.byteAt(n + 1), frame, subroutine);
                break;
            }
            case 58: {
                this.evalStore(Type.OBJECT, codeIterator.byteAt(n + 1), frame, subroutine);
                break;
            }
            case 59: 
            case 60: 
            case 61: 
            case 62: {
                this.evalStore(Type.INTEGER, n2 - 59, frame, subroutine);
                break;
            }
            case 63: 
            case 64: 
            case 65: 
            case 66: {
                this.evalStore(Type.LONG, n2 - 63, frame, subroutine);
                break;
            }
            case 67: 
            case 68: 
            case 69: 
            case 70: {
                this.evalStore(Type.FLOAT, n2 - 67, frame, subroutine);
                break;
            }
            case 71: 
            case 72: 
            case 73: 
            case 74: {
                this.evalStore(Type.DOUBLE, n2 - 71, frame, subroutine);
                break;
            }
            case 75: 
            case 76: 
            case 77: 
            case 78: {
                this.evalStore(Type.OBJECT, n2 - 75, frame, subroutine);
                break;
            }
            case 79: {
                this.evalArrayStore(Type.INTEGER, frame);
                break;
            }
            case 80: {
                this.evalArrayStore(Type.LONG, frame);
                break;
            }
            case 81: {
                this.evalArrayStore(Type.FLOAT, frame);
                break;
            }
            case 82: {
                this.evalArrayStore(Type.DOUBLE, frame);
                break;
            }
            case 83: {
                this.evalArrayStore(Type.OBJECT, frame);
                break;
            }
            case 84: 
            case 85: 
            case 86: {
                this.evalArrayStore(Type.INTEGER, frame);
                break;
            }
            case 87: {
                if (frame.pop() != Type.TOP) break;
                throw new BadBytecode("POP can not be used with a category 2 value, pos = " + n);
            }
            case 88: {
                frame.pop();
                frame.pop();
                break;
            }
            case 89: {
                Type type = frame.peek();
                if (type == Type.TOP) {
                    throw new BadBytecode("DUP can not be used with a category 2 value, pos = " + n);
                }
                frame.push(frame.peek());
                break;
            }
            case 90: 
            case 91: {
                int n3;
                Type type = frame.peek();
                if (type == Type.TOP) {
                    throw new BadBytecode("DUP can not be used with a category 2 value, pos = " + n);
                }
                int n4 = n3 - (n2 - 90) - 1;
                frame.push(type);
                for (n3 = frame.getTopIndex(); n3 > n4; --n3) {
                    frame.setStack(n3, frame.getStack(n3 - 1));
                }
                frame.setStack(n4, type);
                break;
            }
            case 92: {
                frame.push(frame.getStack(frame.getTopIndex() - 1));
                frame.push(frame.getStack(frame.getTopIndex() - 1));
                break;
            }
            case 93: 
            case 94: {
                int n5;
                int n6 = n5 - (n2 - 93) - 1;
                Type type = frame.getStack(frame.getTopIndex() - 1);
                Type type2 = frame.peek();
                frame.push(type);
                frame.push(type2);
                for (n5 = frame.getTopIndex(); n5 > n6; --n5) {
                    frame.setStack(n5, frame.getStack(n5 - 2));
                }
                frame.setStack(n6, type2);
                frame.setStack(n6 - 1, type);
                break;
            }
            case 95: {
                Type type = frame.pop();
                Type type3 = frame.pop();
                if (type.getSize() == 2 || type3.getSize() == 2) {
                    throw new BadBytecode("Swap can not be used with category 2 values, pos = " + n);
                }
                frame.push(type);
                frame.push(type3);
                break;
            }
            case 96: {
                this.evalBinaryMath(Type.INTEGER, frame);
                break;
            }
            case 97: {
                this.evalBinaryMath(Type.LONG, frame);
                break;
            }
            case 98: {
                this.evalBinaryMath(Type.FLOAT, frame);
                break;
            }
            case 99: {
                this.evalBinaryMath(Type.DOUBLE, frame);
                break;
            }
            case 100: {
                this.evalBinaryMath(Type.INTEGER, frame);
                break;
            }
            case 101: {
                this.evalBinaryMath(Type.LONG, frame);
                break;
            }
            case 102: {
                this.evalBinaryMath(Type.FLOAT, frame);
                break;
            }
            case 103: {
                this.evalBinaryMath(Type.DOUBLE, frame);
                break;
            }
            case 104: {
                this.evalBinaryMath(Type.INTEGER, frame);
                break;
            }
            case 105: {
                this.evalBinaryMath(Type.LONG, frame);
                break;
            }
            case 106: {
                this.evalBinaryMath(Type.FLOAT, frame);
                break;
            }
            case 107: {
                this.evalBinaryMath(Type.DOUBLE, frame);
                break;
            }
            case 108: {
                this.evalBinaryMath(Type.INTEGER, frame);
                break;
            }
            case 109: {
                this.evalBinaryMath(Type.LONG, frame);
                break;
            }
            case 110: {
                this.evalBinaryMath(Type.FLOAT, frame);
                break;
            }
            case 111: {
                this.evalBinaryMath(Type.DOUBLE, frame);
                break;
            }
            case 112: {
                this.evalBinaryMath(Type.INTEGER, frame);
                break;
            }
            case 113: {
                this.evalBinaryMath(Type.LONG, frame);
                break;
            }
            case 114: {
                this.evalBinaryMath(Type.FLOAT, frame);
                break;
            }
            case 115: {
                this.evalBinaryMath(Type.DOUBLE, frame);
                break;
            }
            case 116: {
                this.verifyAssignable(Type.INTEGER, this.simplePeek(frame));
                break;
            }
            case 117: {
                this.verifyAssignable(Type.LONG, this.simplePeek(frame));
                break;
            }
            case 118: {
                this.verifyAssignable(Type.FLOAT, this.simplePeek(frame));
                break;
            }
            case 119: {
                this.verifyAssignable(Type.DOUBLE, this.simplePeek(frame));
                break;
            }
            case 120: {
                this.evalShift(Type.INTEGER, frame);
                break;
            }
            case 121: {
                this.evalShift(Type.LONG, frame);
                break;
            }
            case 122: {
                this.evalShift(Type.INTEGER, frame);
                break;
            }
            case 123: {
                this.evalShift(Type.LONG, frame);
                break;
            }
            case 124: {
                this.evalShift(Type.INTEGER, frame);
                break;
            }
            case 125: {
                this.evalShift(Type.LONG, frame);
                break;
            }
            case 126: {
                this.evalBinaryMath(Type.INTEGER, frame);
                break;
            }
            case 127: {
                this.evalBinaryMath(Type.LONG, frame);
                break;
            }
            case 128: {
                this.evalBinaryMath(Type.INTEGER, frame);
                break;
            }
            case 129: {
                this.evalBinaryMath(Type.LONG, frame);
                break;
            }
            case 130: {
                this.evalBinaryMath(Type.INTEGER, frame);
                break;
            }
            case 131: {
                this.evalBinaryMath(Type.LONG, frame);
                break;
            }
            case 132: {
                int n7 = codeIterator.byteAt(n + 1);
                this.verifyAssignable(Type.INTEGER, frame.getLocal(n7));
                this.access(n7, Type.INTEGER, subroutine);
                break;
            }
            case 133: {
                this.verifyAssignable(Type.INTEGER, this.simplePop(frame));
                this.simplePush(Type.LONG, frame);
                break;
            }
            case 134: {
                this.verifyAssignable(Type.INTEGER, this.simplePop(frame));
                this.simplePush(Type.FLOAT, frame);
                break;
            }
            case 135: {
                this.verifyAssignable(Type.INTEGER, this.simplePop(frame));
                this.simplePush(Type.DOUBLE, frame);
                break;
            }
            case 136: {
                this.verifyAssignable(Type.LONG, this.simplePop(frame));
                this.simplePush(Type.INTEGER, frame);
                break;
            }
            case 137: {
                this.verifyAssignable(Type.LONG, this.simplePop(frame));
                this.simplePush(Type.FLOAT, frame);
                break;
            }
            case 138: {
                this.verifyAssignable(Type.LONG, this.simplePop(frame));
                this.simplePush(Type.DOUBLE, frame);
                break;
            }
            case 139: {
                this.verifyAssignable(Type.FLOAT, this.simplePop(frame));
                this.simplePush(Type.INTEGER, frame);
                break;
            }
            case 140: {
                this.verifyAssignable(Type.FLOAT, this.simplePop(frame));
                this.simplePush(Type.LONG, frame);
                break;
            }
            case 141: {
                this.verifyAssignable(Type.FLOAT, this.simplePop(frame));
                this.simplePush(Type.DOUBLE, frame);
                break;
            }
            case 142: {
                this.verifyAssignable(Type.DOUBLE, this.simplePop(frame));
                this.simplePush(Type.INTEGER, frame);
                break;
            }
            case 143: {
                this.verifyAssignable(Type.DOUBLE, this.simplePop(frame));
                this.simplePush(Type.LONG, frame);
                break;
            }
            case 144: {
                this.verifyAssignable(Type.DOUBLE, this.simplePop(frame));
                this.simplePush(Type.FLOAT, frame);
                break;
            }
            case 145: 
            case 146: 
            case 147: {
                this.verifyAssignable(Type.INTEGER, frame.peek());
                break;
            }
            case 148: {
                this.verifyAssignable(Type.LONG, this.simplePop(frame));
                this.verifyAssignable(Type.LONG, this.simplePop(frame));
                frame.push(Type.INTEGER);
                break;
            }
            case 149: 
            case 150: {
                this.verifyAssignable(Type.FLOAT, this.simplePop(frame));
                this.verifyAssignable(Type.FLOAT, this.simplePop(frame));
                frame.push(Type.INTEGER);
                break;
            }
            case 151: 
            case 152: {
                this.verifyAssignable(Type.DOUBLE, this.simplePop(frame));
                this.verifyAssignable(Type.DOUBLE, this.simplePop(frame));
                frame.push(Type.INTEGER);
                break;
            }
            case 153: 
            case 154: 
            case 155: 
            case 156: 
            case 157: 
            case 158: {
                this.verifyAssignable(Type.INTEGER, this.simplePop(frame));
                break;
            }
            case 159: 
            case 160: 
            case 161: 
            case 162: 
            case 163: 
            case 164: {
                this.verifyAssignable(Type.INTEGER, this.simplePop(frame));
                this.verifyAssignable(Type.INTEGER, this.simplePop(frame));
                break;
            }
            case 165: 
            case 166: {
                this.verifyAssignable(Type.OBJECT, this.simplePop(frame));
                this.verifyAssignable(Type.OBJECT, this.simplePop(frame));
                break;
            }
            case 167: {
                break;
            }
            case 168: {
                frame.push(Type.RETURN_ADDRESS);
                break;
            }
            case 169: {
                this.verifyAssignable(Type.RETURN_ADDRESS, frame.getLocal(codeIterator.byteAt(n + 1)));
                break;
            }
            case 170: 
            case 171: 
            case 172: {
                this.verifyAssignable(Type.INTEGER, this.simplePop(frame));
                break;
            }
            case 173: {
                this.verifyAssignable(Type.LONG, this.simplePop(frame));
                break;
            }
            case 174: {
                this.verifyAssignable(Type.FLOAT, this.simplePop(frame));
                break;
            }
            case 175: {
                this.verifyAssignable(Type.DOUBLE, this.simplePop(frame));
                break;
            }
            case 176: {
                try {
                    CtClass ctClass = Descriptor.getReturnType(methodInfo.getDescriptor(), this.classPool);
                    this.verifyAssignable(Type.get(ctClass), this.simplePop(frame));
                    break;
                } catch (NotFoundException notFoundException) {
                    throw new RuntimeException(notFoundException);
                }
            }
            case 177: {
                break;
            }
            case 178: {
                this.evalGetField(n2, codeIterator.u16bitAt(n + 1), frame);
                break;
            }
            case 179: {
                this.evalPutField(n2, codeIterator.u16bitAt(n + 1), frame);
                break;
            }
            case 180: {
                this.evalGetField(n2, codeIterator.u16bitAt(n + 1), frame);
                break;
            }
            case 181: {
                this.evalPutField(n2, codeIterator.u16bitAt(n + 1), frame);
                break;
            }
            case 182: 
            case 183: 
            case 184: {
                this.evalInvokeMethod(n2, codeIterator.u16bitAt(n + 1), frame);
                break;
            }
            case 185: {
                this.evalInvokeIntfMethod(n2, codeIterator.u16bitAt(n + 1), frame);
                break;
            }
            case 186: {
                this.evalInvokeDynamic(n2, codeIterator.u16bitAt(n + 1), frame);
                break;
            }
            case 187: {
                frame.push(this.resolveClassInfo(this.constPool.getClassInfo(codeIterator.u16bitAt(n + 1))));
                break;
            }
            case 188: {
                this.evalNewArray(n, codeIterator, frame);
                break;
            }
            case 189: {
                this.evalNewObjectArray(n, codeIterator, frame);
                break;
            }
            case 190: {
                Type type = this.simplePop(frame);
                if (!type.isArray() && type != Type.UNINIT) {
                    throw new BadBytecode("Array length passed a non-array [pos = " + n + "]: " + type);
                }
                frame.push(Type.INTEGER);
                break;
            }
            case 191: {
                this.verifyAssignable(this.THROWABLE_TYPE, this.simplePop(frame));
                break;
            }
            case 192: {
                this.verifyAssignable(Type.OBJECT, this.simplePop(frame));
                frame.push(this.typeFromDesc(this.constPool.getClassInfoByDescriptor(codeIterator.u16bitAt(n + 1))));
                break;
            }
            case 193: {
                this.verifyAssignable(Type.OBJECT, this.simplePop(frame));
                frame.push(Type.INTEGER);
                break;
            }
            case 194: 
            case 195: {
                this.verifyAssignable(Type.OBJECT, this.simplePop(frame));
                break;
            }
            case 196: {
                this.evalWide(n, codeIterator, frame, subroutine);
                break;
            }
            case 197: {
                this.evalNewObjectArray(n, codeIterator, frame);
                break;
            }
            case 198: 
            case 199: {
                this.verifyAssignable(Type.OBJECT, this.simplePop(frame));
                break;
            }
            case 200: {
                break;
            }
            case 201: {
                frame.push(Type.RETURN_ADDRESS);
            }
        }
    }

    private Type zeroExtend(Type type) {
        if (type == Type.SHORT || type == Type.BYTE || type == Type.CHAR || type == Type.BOOLEAN) {
            return Type.INTEGER;
        }
        return type;
    }

    private void evalArrayLoad(Type type, Frame frame) {
        Type type2 = frame.pop();
        Type type3 = frame.pop();
        if (type3 == Type.UNINIT) {
            this.verifyAssignable(Type.INTEGER, type2);
            if (type == Type.OBJECT) {
                this.simplePush(Type.UNINIT, frame);
            } else {
                this.simplePush(type, frame);
            }
            return;
        }
        Type type4 = type3.getComponent();
        if (type4 == null) {
            throw new BadBytecode("Not an array! [pos = " + this.lastPos + "]: " + type4);
        }
        type4 = this.zeroExtend(type4);
        this.verifyAssignable(type, type4);
        this.verifyAssignable(Type.INTEGER, type2);
        this.simplePush(type4, frame);
    }

    private void evalArrayStore(Type type, Frame frame) {
        Type type2 = this.simplePop(frame);
        Type type3 = frame.pop();
        Type type4 = frame.pop();
        if (type4 == Type.UNINIT) {
            this.verifyAssignable(Type.INTEGER, type3);
            return;
        }
        Type type5 = type4.getComponent();
        if (type5 == null) {
            throw new BadBytecode("Not an array! [pos = " + this.lastPos + "]: " + type5);
        }
        type5 = this.zeroExtend(type5);
        this.verifyAssignable(type, type5);
        this.verifyAssignable(Type.INTEGER, type3);
        if (type == Type.OBJECT) {
            this.verifyAssignable(type, type2);
        } else {
            this.verifyAssignable(type5, type2);
        }
    }

    private void evalBinaryMath(Type type, Frame frame) {
        Type type2 = this.simplePop(frame);
        Type type3 = this.simplePop(frame);
        this.verifyAssignable(type, type2);
        this.verifyAssignable(type, type3);
        this.simplePush(type3, frame);
    }

    private void evalGetField(int n, int n2, Frame frame) {
        String string = this.constPool.getFieldrefType(n2);
        Type type = this.zeroExtend(this.typeFromDesc(string));
        if (n == 180) {
            Type type2 = this.resolveClassInfo(this.constPool.getFieldrefClassName(n2));
            this.verifyAssignable(type2, this.simplePop(frame));
        }
        this.simplePush(type, frame);
    }

    private void evalInvokeIntfMethod(int n, int n2, Frame frame) {
        String string = this.constPool.getInterfaceMethodrefType(n2);
        Type[] typeArray = this.paramTypesFromDesc(string);
        int n3 = typeArray.length;
        while (n3 > 0) {
            this.verifyAssignable(this.zeroExtend(typeArray[--n3]), this.simplePop(frame));
        }
        String string2 = this.constPool.getInterfaceMethodrefClassName(n2);
        Type type = this.resolveClassInfo(string2);
        this.verifyAssignable(type, this.simplePop(frame));
        Type type2 = this.returnTypeFromDesc(string);
        if (type2 != Type.VOID) {
            this.simplePush(this.zeroExtend(type2), frame);
        }
    }

    private void evalInvokeMethod(int n, int n2, Frame frame) {
        Type type;
        String string = this.constPool.getMethodrefType(n2);
        Type[] typeArray = this.paramTypesFromDesc(string);
        int n3 = typeArray.length;
        while (n3 > 0) {
            this.verifyAssignable(this.zeroExtend(typeArray[--n3]), this.simplePop(frame));
        }
        if (n != 184) {
            type = this.resolveClassInfo(this.constPool.getMethodrefClassName(n2));
            this.verifyAssignable(type, this.simplePop(frame));
        }
        if ((type = this.returnTypeFromDesc(string)) != Type.VOID) {
            this.simplePush(this.zeroExtend(type), frame);
        }
    }

    private void evalInvokeDynamic(int n, int n2, Frame frame) {
        String string = this.constPool.getInvokeDynamicType(n2);
        Type[] typeArray = this.paramTypesFromDesc(string);
        int n3 = typeArray.length;
        while (n3 > 0) {
            this.verifyAssignable(this.zeroExtend(typeArray[--n3]), this.simplePop(frame));
        }
        Type type = this.returnTypeFromDesc(string);
        if (type != Type.VOID) {
            this.simplePush(this.zeroExtend(type), frame);
        }
    }

    private void evalLDC(int n, Frame frame) {
        Type type;
        int n2 = this.constPool.getTag(n);
        switch (n2) {
            case 8: {
                type = this.STRING_TYPE;
                break;
            }
            case 3: {
                type = Type.INTEGER;
                break;
            }
            case 4: {
                type = Type.FLOAT;
                break;
            }
            case 5: {
                type = Type.LONG;
                break;
            }
            case 6: {
                type = Type.DOUBLE;
                break;
            }
            case 7: {
                type = this.CLASS_TYPE;
                break;
            }
            default: {
                throw new BadBytecode("bad LDC [pos = " + this.lastPos + "]: " + n2);
            }
        }
        this.simplePush(type, frame);
    }

    private void evalLoad(Type type, int n, Frame frame, Subroutine subroutine) {
        Type type2 = frame.getLocal(n);
        this.verifyAssignable(type, type2);
        this.simplePush(type2, frame);
        this.access(n, type2, subroutine);
    }

    private void evalNewArray(int n, CodeIterator codeIterator, Frame frame) {
        this.verifyAssignable(Type.INTEGER, this.simplePop(frame));
        Type type = null;
        int n2 = codeIterator.byteAt(n + 1);
        switch (n2) {
            case 4: {
                type = this.getType("boolean[]");
                break;
            }
            case 5: {
                type = this.getType("char[]");
                break;
            }
            case 8: {
                type = this.getType("byte[]");
                break;
            }
            case 9: {
                type = this.getType("short[]");
                break;
            }
            case 10: {
                type = this.getType("int[]");
                break;
            }
            case 11: {
                type = this.getType("long[]");
                break;
            }
            case 6: {
                type = this.getType("float[]");
                break;
            }
            case 7: {
                type = this.getType("double[]");
                break;
            }
            default: {
                throw new BadBytecode("Invalid array type [pos = " + n + "]: " + n2);
            }
        }
        frame.push(type);
    }

    private void evalNewObjectArray(int n, CodeIterator codeIterator, Frame frame) {
        int n2;
        Type type = this.resolveClassInfo(this.constPool.getClassInfo(codeIterator.u16bitAt(n + 1)));
        String string = type.getCtClass().getName();
        int n3 = codeIterator.byteAt(n);
        if (n3 == 197) {
            n2 = codeIterator.byteAt(n + 3);
        } else {
            string = string + "[]";
            n2 = 1;
        }
        while (n2-- > 0) {
            this.verifyAssignable(Type.INTEGER, this.simplePop(frame));
        }
        this.simplePush(this.getType(string), frame);
    }

    private void evalPutField(int n, int n2, Frame frame) {
        String string = this.constPool.getFieldrefType(n2);
        Type type = this.zeroExtend(this.typeFromDesc(string));
        this.verifyAssignable(type, this.simplePop(frame));
        if (n == 181) {
            Type type2 = this.resolveClassInfo(this.constPool.getFieldrefClassName(n2));
            this.verifyAssignable(type2, this.simplePop(frame));
        }
    }

    private void evalShift(Type type, Frame frame) {
        Type type2 = this.simplePop(frame);
        Type type3 = this.simplePop(frame);
        this.verifyAssignable(Type.INTEGER, type2);
        this.verifyAssignable(type, type3);
        this.simplePush(type3, frame);
    }

    private void evalStore(Type type, int n, Frame frame, Subroutine subroutine) {
        Type type2 = this.simplePop(frame);
        if (type != Type.OBJECT || type2 != Type.RETURN_ADDRESS) {
            this.verifyAssignable(type, type2);
        }
        this.simpleSetLocal(n, type2, frame);
        this.access(n, type2, subroutine);
    }

    private void evalWide(int n, CodeIterator codeIterator, Frame frame, Subroutine subroutine) {
        int n2 = codeIterator.byteAt(n + 1);
        int n3 = codeIterator.u16bitAt(n + 2);
        switch (n2) {
            case 21: {
                this.evalLoad(Type.INTEGER, n3, frame, subroutine);
                break;
            }
            case 22: {
                this.evalLoad(Type.LONG, n3, frame, subroutine);
                break;
            }
            case 23: {
                this.evalLoad(Type.FLOAT, n3, frame, subroutine);
                break;
            }
            case 24: {
                this.evalLoad(Type.DOUBLE, n3, frame, subroutine);
                break;
            }
            case 25: {
                this.evalLoad(Type.OBJECT, n3, frame, subroutine);
                break;
            }
            case 54: {
                this.evalStore(Type.INTEGER, n3, frame, subroutine);
                break;
            }
            case 55: {
                this.evalStore(Type.LONG, n3, frame, subroutine);
                break;
            }
            case 56: {
                this.evalStore(Type.FLOAT, n3, frame, subroutine);
                break;
            }
            case 57: {
                this.evalStore(Type.DOUBLE, n3, frame, subroutine);
                break;
            }
            case 58: {
                this.evalStore(Type.OBJECT, n3, frame, subroutine);
                break;
            }
            case 132: {
                this.verifyAssignable(Type.INTEGER, frame.getLocal(n3));
                break;
            }
            case 169: {
                this.verifyAssignable(Type.RETURN_ADDRESS, frame.getLocal(n3));
                break;
            }
            default: {
                throw new BadBytecode("Invalid WIDE operand [pos = " + n + "]: " + n2);
            }
        }
    }

    private Type getType(String string) {
        try {
            return Type.get(this.classPool.get(string));
        } catch (NotFoundException notFoundException) {
            throw new BadBytecode("Could not find class [pos = " + this.lastPos + "]: " + string);
        }
    }

    private Type[] paramTypesFromDesc(String string) {
        CtClass[] ctClassArray = null;
        try {
            ctClassArray = Descriptor.getParameterTypes(string, this.classPool);
        } catch (NotFoundException notFoundException) {
            throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + notFoundException.getMessage());
        }
        if (ctClassArray == null) {
            throw new BadBytecode("Could not obtain parameters for descriptor [pos = " + this.lastPos + "]: " + string);
        }
        Type[] typeArray = new Type[ctClassArray.length];
        for (int i = 0; i < typeArray.length; ++i) {
            typeArray[i] = Type.get(ctClassArray[i]);
        }
        return typeArray;
    }

    private Type returnTypeFromDesc(String string) {
        CtClass ctClass = null;
        try {
            ctClass = Descriptor.getReturnType(string, this.classPool);
        } catch (NotFoundException notFoundException) {
            throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + notFoundException.getMessage());
        }
        if (ctClass == null) {
            throw new BadBytecode("Could not obtain return type for descriptor [pos = " + this.lastPos + "]: " + string);
        }
        return Type.get(ctClass);
    }

    private Type simplePeek(Frame frame) {
        Type type = frame.peek();
        return type == Type.TOP ? frame.getStack(frame.getTopIndex() - 1) : type;
    }

    private Type simplePop(Frame frame) {
        Type type = frame.pop();
        return type == Type.TOP ? frame.pop() : type;
    }

    private void simplePush(Type type, Frame frame) {
        frame.push(type);
        if (type.getSize() == 2) {
            frame.push(Type.TOP);
        }
    }

    private void access(int n, Type type, Subroutine subroutine) {
        if (subroutine == null) {
            return;
        }
        subroutine.access(n);
        if (type.getSize() == 2) {
            subroutine.access(n + 1);
        }
    }

    private void simpleSetLocal(int n, Type type, Frame frame) {
        frame.setLocal(n, type);
        if (type.getSize() == 2) {
            frame.setLocal(n + 1, Type.TOP);
        }
    }

    private Type resolveClassInfo(String string) {
        CtClass ctClass = null;
        try {
            ctClass = string.charAt(0) == '[' ? Descriptor.toCtClass(string, this.classPool) : this.classPool.get(string);
        } catch (NotFoundException notFoundException) {
            throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + notFoundException.getMessage());
        }
        if (ctClass == null) {
            throw new BadBytecode("Could not obtain type for descriptor [pos = " + this.lastPos + "]: " + string);
        }
        return Type.get(ctClass);
    }

    private Type typeFromDesc(String string) {
        CtClass ctClass = null;
        try {
            ctClass = Descriptor.toCtClass(string, this.classPool);
        } catch (NotFoundException notFoundException) {
            throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + notFoundException.getMessage());
        }
        if (ctClass == null) {
            throw new BadBytecode("Could not obtain type for descriptor [pos = " + this.lastPos + "]: " + string);
        }
        return Type.get(ctClass);
    }

    private void verifyAssignable(Type type, Type type2) {
        if (!type.isAssignableFrom(type2)) {
            throw new BadBytecode("Expected type: " + type + " Got: " + type2 + " [pos = " + this.lastPos + "]");
        }
    }
}

