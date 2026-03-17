/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtPrimitiveType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteVector;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Opcode;

public class Bytecode
extends ByteVector
implements Cloneable,
Opcode {
    public static final CtClass THIS = ConstPool.THIS;
    ConstPool constPool;
    int maxStack;
    int maxLocals;
    ExceptionTable tryblocks;
    private int stackDepth;

    public Bytecode(ConstPool constPool, int n, int n2) {
        this.constPool = constPool;
        this.maxStack = n;
        this.maxLocals = n2;
        this.tryblocks = new ExceptionTable(constPool);
        this.stackDepth = 0;
    }

    public Bytecode(ConstPool constPool) {
        this(constPool, 0, 0);
    }

    @Override
    public Object clone() {
        try {
            Bytecode bytecode = (Bytecode)super.clone();
            bytecode.tryblocks = (ExceptionTable)this.tryblocks.clone();
            return bytecode;
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new RuntimeException(cloneNotSupportedException);
        }
    }

    public ConstPool getConstPool() {
        return this.constPool;
    }

    public ExceptionTable getExceptionTable() {
        return this.tryblocks;
    }

    public CodeAttribute toCodeAttribute() {
        return new CodeAttribute(this.constPool, this.maxStack, this.maxLocals, this.get(), this.tryblocks);
    }

    public int length() {
        return this.getSize();
    }

    public byte[] get() {
        return this.copy();
    }

    public int getMaxStack() {
        return this.maxStack;
    }

    public void setMaxStack(int n) {
        this.maxStack = n;
    }

    public int getMaxLocals() {
        return this.maxLocals;
    }

    public void setMaxLocals(int n) {
        this.maxLocals = n;
    }

    public void setMaxLocals(boolean bl, CtClass[] ctClassArray, int n) {
        if (!bl) {
            ++n;
        }
        if (ctClassArray != null) {
            CtClass ctClass = CtClass.doubleType;
            CtClass ctClass2 = CtClass.longType;
            for (CtClass ctClass3 : ctClassArray) {
                if (ctClass3 == ctClass || ctClass3 == ctClass2) {
                    n += 2;
                    continue;
                }
                ++n;
            }
        }
        this.maxLocals = n;
    }

    public void incMaxLocals(int n) {
        this.maxLocals += n;
    }

    public void addExceptionHandler(int n, int n2, int n3, CtClass ctClass) {
        this.addExceptionHandler(n, n2, n3, this.constPool.addClassInfo(ctClass));
    }

    public void addExceptionHandler(int n, int n2, int n3, String string) {
        this.addExceptionHandler(n, n2, n3, this.constPool.addClassInfo(string));
    }

    public void addExceptionHandler(int n, int n2, int n3, int n4) {
        this.tryblocks.add(n, n2, n3, n4);
    }

    public int currentPc() {
        return this.getSize();
    }

    @Override
    public int read(int n) {
        return super.read(n);
    }

    public int read16bit(int n) {
        int n2 = this.read(n);
        int n3 = this.read(n + 1);
        return (n2 << 8) + (n3 & 0xFF);
    }

    public int read32bit(int n) {
        int n2 = this.read16bit(n);
        int n3 = this.read16bit(n + 2);
        return (n2 << 16) + (n3 & 0xFFFF);
    }

    @Override
    public void write(int n, int n2) {
        super.write(n, n2);
    }

    public void write16bit(int n, int n2) {
        this.write(n, n2 >> 8);
        this.write(n + 1, n2);
    }

    public void write32bit(int n, int n2) {
        this.write16bit(n, n2 >> 16);
        this.write16bit(n + 2, n2);
    }

    @Override
    public void add(int n) {
        super.add(n);
    }

    public void add32bit(int n) {
        this.add(n >> 24, n >> 16, n >> 8, n);
    }

    @Override
    public void addGap(int n) {
        super.addGap(n);
    }

    public void addOpcode(int n) {
        this.add(n);
        this.growStack(STACK_GROW[n]);
    }

    public void growStack(int n) {
        this.setStackDepth(this.stackDepth + n);
    }

    public int getStackDepth() {
        return this.stackDepth;
    }

    public void setStackDepth(int n) {
        this.stackDepth = n;
        if (this.stackDepth > this.maxStack) {
            this.maxStack = this.stackDepth;
        }
    }

    public void addIndex(int n) {
        this.add(n >> 8, n);
    }

    public void addAload(int n) {
        if (n < 4) {
            this.addOpcode(42 + n);
        } else if (n < 256) {
            this.addOpcode(25);
            this.add(n);
        } else {
            this.addOpcode(196);
            this.addOpcode(25);
            this.addIndex(n);
        }
    }

    public void addAstore(int n) {
        if (n < 4) {
            this.addOpcode(75 + n);
        } else if (n < 256) {
            this.addOpcode(58);
            this.add(n);
        } else {
            this.addOpcode(196);
            this.addOpcode(58);
            this.addIndex(n);
        }
    }

    public void addIconst(int n) {
        if (n < 6 && -2 < n) {
            this.addOpcode(3 + n);
        } else if (n <= 127 && -128 <= n) {
            this.addOpcode(16);
            this.add(n);
        } else if (n <= Short.MAX_VALUE && Short.MIN_VALUE <= n) {
            this.addOpcode(17);
            this.add(n >> 8);
            this.add(n);
        } else {
            this.addLdc(this.constPool.addIntegerInfo(n));
        }
    }

    public void addConstZero(CtClass ctClass) {
        if (ctClass.isPrimitive()) {
            if (ctClass == CtClass.longType) {
                this.addOpcode(9);
            } else if (ctClass == CtClass.floatType) {
                this.addOpcode(11);
            } else if (ctClass == CtClass.doubleType) {
                this.addOpcode(14);
            } else {
                if (ctClass == CtClass.voidType) {
                    throw new RuntimeException("void type?");
                }
                this.addOpcode(3);
            }
        } else {
            this.addOpcode(1);
        }
    }

    public void addIload(int n) {
        if (n < 4) {
            this.addOpcode(26 + n);
        } else if (n < 256) {
            this.addOpcode(21);
            this.add(n);
        } else {
            this.addOpcode(196);
            this.addOpcode(21);
            this.addIndex(n);
        }
    }

    public void addIstore(int n) {
        if (n < 4) {
            this.addOpcode(59 + n);
        } else if (n < 256) {
            this.addOpcode(54);
            this.add(n);
        } else {
            this.addOpcode(196);
            this.addOpcode(54);
            this.addIndex(n);
        }
    }

    public void addLconst(long l) {
        if (l == 0L || l == 1L) {
            this.addOpcode(9 + (int)l);
        } else {
            this.addLdc2w(l);
        }
    }

    public void addLload(int n) {
        if (n < 4) {
            this.addOpcode(30 + n);
        } else if (n < 256) {
            this.addOpcode(22);
            this.add(n);
        } else {
            this.addOpcode(196);
            this.addOpcode(22);
            this.addIndex(n);
        }
    }

    public void addLstore(int n) {
        if (n < 4) {
            this.addOpcode(63 + n);
        } else if (n < 256) {
            this.addOpcode(55);
            this.add(n);
        } else {
            this.addOpcode(196);
            this.addOpcode(55);
            this.addIndex(n);
        }
    }

    public void addDconst(double d) {
        if (d == 0.0 || d == 1.0) {
            this.addOpcode(14 + (int)d);
        } else {
            this.addLdc2w(d);
        }
    }

    public void addDload(int n) {
        if (n < 4) {
            this.addOpcode(38 + n);
        } else if (n < 256) {
            this.addOpcode(24);
            this.add(n);
        } else {
            this.addOpcode(196);
            this.addOpcode(24);
            this.addIndex(n);
        }
    }

    public void addDstore(int n) {
        if (n < 4) {
            this.addOpcode(71 + n);
        } else if (n < 256) {
            this.addOpcode(57);
            this.add(n);
        } else {
            this.addOpcode(196);
            this.addOpcode(57);
            this.addIndex(n);
        }
    }

    public void addFconst(float f) {
        if (f == 0.0f || f == 1.0f || f == 2.0f) {
            this.addOpcode(11 + (int)f);
        } else {
            this.addLdc(this.constPool.addFloatInfo(f));
        }
    }

    public void addFload(int n) {
        if (n < 4) {
            this.addOpcode(34 + n);
        } else if (n < 256) {
            this.addOpcode(23);
            this.add(n);
        } else {
            this.addOpcode(196);
            this.addOpcode(23);
            this.addIndex(n);
        }
    }

    public void addFstore(int n) {
        if (n < 4) {
            this.addOpcode(67 + n);
        } else if (n < 256) {
            this.addOpcode(56);
            this.add(n);
        } else {
            this.addOpcode(196);
            this.addOpcode(56);
            this.addIndex(n);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public int addLoad(int n, CtClass ctClass) {
        if (!ctClass.isPrimitive()) {
            this.addAload(n);
            return 1;
        }
        if (ctClass == CtClass.booleanType || ctClass == CtClass.charType || ctClass == CtClass.byteType || ctClass == CtClass.shortType || ctClass == CtClass.intType) {
            this.addIload(n);
            return 1;
        }
        if (ctClass == CtClass.longType) {
            this.addLload(n);
            return 2;
        }
        if (ctClass == CtClass.floatType) {
            this.addFload(n);
            return 1;
        }
        if (ctClass != CtClass.doubleType) throw new RuntimeException("void type?");
        this.addDload(n);
        return 2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int addStore(int n, CtClass ctClass) {
        if (!ctClass.isPrimitive()) {
            this.addAstore(n);
            return 1;
        }
        if (ctClass == CtClass.booleanType || ctClass == CtClass.charType || ctClass == CtClass.byteType || ctClass == CtClass.shortType || ctClass == CtClass.intType) {
            this.addIstore(n);
            return 1;
        }
        if (ctClass == CtClass.longType) {
            this.addLstore(n);
            return 2;
        }
        if (ctClass == CtClass.floatType) {
            this.addFstore(n);
            return 1;
        }
        if (ctClass != CtClass.doubleType) throw new RuntimeException("void type?");
        this.addDstore(n);
        return 2;
    }

    public int addLoadParameters(CtClass[] ctClassArray, int n) {
        int n2 = 0;
        if (ctClassArray != null) {
            int n3 = ctClassArray.length;
            for (int i = 0; i < n3; ++i) {
                n2 += this.addLoad(n2 + n, ctClassArray[i]);
            }
        }
        return n2;
    }

    public void addCheckcast(CtClass ctClass) {
        this.addOpcode(192);
        this.addIndex(this.constPool.addClassInfo(ctClass));
    }

    public void addCheckcast(String string) {
        this.addOpcode(192);
        this.addIndex(this.constPool.addClassInfo(string));
    }

    public void addInstanceof(String string) {
        this.addOpcode(193);
        this.addIndex(this.constPool.addClassInfo(string));
    }

    public void addGetfield(CtClass ctClass, String string, String string2) {
        this.add(180);
        int n = this.constPool.addClassInfo(ctClass);
        this.addIndex(this.constPool.addFieldrefInfo(n, string, string2));
        this.growStack(Descriptor.dataSize(string2) - 1);
    }

    public void addGetfield(String string, String string2, String string3) {
        this.add(180);
        int n = this.constPool.addClassInfo(string);
        this.addIndex(this.constPool.addFieldrefInfo(n, string2, string3));
        this.growStack(Descriptor.dataSize(string3) - 1);
    }

    public void addGetstatic(CtClass ctClass, String string, String string2) {
        this.add(178);
        int n = this.constPool.addClassInfo(ctClass);
        this.addIndex(this.constPool.addFieldrefInfo(n, string, string2));
        this.growStack(Descriptor.dataSize(string2));
    }

    public void addGetstatic(String string, String string2, String string3) {
        this.add(178);
        int n = this.constPool.addClassInfo(string);
        this.addIndex(this.constPool.addFieldrefInfo(n, string2, string3));
        this.growStack(Descriptor.dataSize(string3));
    }

    public void addInvokespecial(CtClass ctClass, String string, CtClass ctClass2, CtClass[] ctClassArray) {
        String string2 = Descriptor.ofMethod(ctClass2, ctClassArray);
        this.addInvokespecial(ctClass, string, string2);
    }

    public void addInvokespecial(CtClass ctClass, String string, String string2) {
        boolean bl = ctClass == null ? false : ctClass.isInterface();
        this.addInvokespecial(bl, this.constPool.addClassInfo(ctClass), string, string2);
    }

    public void addInvokespecial(String string, String string2, String string3) {
        this.addInvokespecial(false, this.constPool.addClassInfo(string), string2, string3);
    }

    public void addInvokespecial(int n, String string, String string2) {
        this.addInvokespecial(false, n, string, string2);
    }

    public void addInvokespecial(boolean bl, int n, String string, String string2) {
        int n2 = bl ? this.constPool.addInterfaceMethodrefInfo(n, string, string2) : this.constPool.addMethodrefInfo(n, string, string2);
        this.addInvokespecial(n2, string2);
    }

    public void addInvokespecial(int n, String string) {
        this.add(183);
        this.addIndex(n);
        this.growStack(Descriptor.dataSize(string) - 1);
    }

    public void addInvokestatic(CtClass ctClass, String string, CtClass ctClass2, CtClass[] ctClassArray) {
        String string2 = Descriptor.ofMethod(ctClass2, ctClassArray);
        this.addInvokestatic(ctClass, string, string2);
    }

    public void addInvokestatic(CtClass ctClass, String string, String string2) {
        boolean bl = ctClass == THIS ? false : ctClass.isInterface();
        this.addInvokestatic(this.constPool.addClassInfo(ctClass), string, string2, bl);
    }

    public void addInvokestatic(String string, String string2, String string3) {
        this.addInvokestatic(this.constPool.addClassInfo(string), string2, string3);
    }

    public void addInvokestatic(int n, String string, String string2) {
        this.addInvokestatic(n, string, string2, false);
    }

    private void addInvokestatic(int n, String string, String string2, boolean bl) {
        this.add(184);
        int n2 = bl ? this.constPool.addInterfaceMethodrefInfo(n, string, string2) : this.constPool.addMethodrefInfo(n, string, string2);
        this.addIndex(n2);
        this.growStack(Descriptor.dataSize(string2));
    }

    public void addInvokevirtual(CtClass ctClass, String string, CtClass ctClass2, CtClass[] ctClassArray) {
        String string2 = Descriptor.ofMethod(ctClass2, ctClassArray);
        this.addInvokevirtual(ctClass, string, string2);
    }

    public void addInvokevirtual(CtClass ctClass, String string, String string2) {
        this.addInvokevirtual(this.constPool.addClassInfo(ctClass), string, string2);
    }

    public void addInvokevirtual(String string, String string2, String string3) {
        this.addInvokevirtual(this.constPool.addClassInfo(string), string2, string3);
    }

    public void addInvokevirtual(int n, String string, String string2) {
        this.add(182);
        this.addIndex(this.constPool.addMethodrefInfo(n, string, string2));
        this.growStack(Descriptor.dataSize(string2) - 1);
    }

    public void addInvokeinterface(CtClass ctClass, String string, CtClass ctClass2, CtClass[] ctClassArray, int n) {
        String string2 = Descriptor.ofMethod(ctClass2, ctClassArray);
        this.addInvokeinterface(ctClass, string, string2, n);
    }

    public void addInvokeinterface(CtClass ctClass, String string, String string2, int n) {
        this.addInvokeinterface(this.constPool.addClassInfo(ctClass), string, string2, n);
    }

    public void addInvokeinterface(String string, String string2, String string3, int n) {
        this.addInvokeinterface(this.constPool.addClassInfo(string), string2, string3, n);
    }

    public void addInvokeinterface(int n, String string, String string2, int n2) {
        this.add(185);
        this.addIndex(this.constPool.addInterfaceMethodrefInfo(n, string, string2));
        this.add(n2);
        this.add(0);
        this.growStack(Descriptor.dataSize(string2) - 1);
    }

    public void addInvokedynamic(int n, String string, String string2) {
        int n2 = this.constPool.addNameAndTypeInfo(string, string2);
        int n3 = this.constPool.addInvokeDynamicInfo(n, n2);
        this.add(186);
        this.addIndex(n3);
        this.add(0, 0);
        this.growStack(Descriptor.dataSize(string2));
    }

    public void addLdc(String string) {
        this.addLdc(this.constPool.addStringInfo(string));
    }

    public void addLdc(int n) {
        if (n > 255) {
            this.addOpcode(19);
            this.addIndex(n);
        } else {
            this.addOpcode(18);
            this.add(n);
        }
    }

    public void addLdc2w(long l) {
        this.addOpcode(20);
        this.addIndex(this.constPool.addLongInfo(l));
    }

    public void addLdc2w(double d) {
        this.addOpcode(20);
        this.addIndex(this.constPool.addDoubleInfo(d));
    }

    public void addNew(CtClass ctClass) {
        this.addOpcode(187);
        this.addIndex(this.constPool.addClassInfo(ctClass));
    }

    public void addNew(String string) {
        this.addOpcode(187);
        this.addIndex(this.constPool.addClassInfo(string));
    }

    public void addAnewarray(String string) {
        this.addOpcode(189);
        this.addIndex(this.constPool.addClassInfo(string));
    }

    public void addAnewarray(CtClass ctClass, int n) {
        this.addIconst(n);
        this.addOpcode(189);
        this.addIndex(this.constPool.addClassInfo(ctClass));
    }

    public void addNewarray(int n, int n2) {
        this.addIconst(n2);
        this.addOpcode(188);
        this.add(n);
    }

    public int addMultiNewarray(CtClass ctClass, int[] nArray) {
        int n = nArray.length;
        for (int i = 0; i < n; ++i) {
            this.addIconst(nArray[i]);
        }
        this.growStack(n);
        return this.addMultiNewarray(ctClass, n);
    }

    public int addMultiNewarray(CtClass ctClass, int n) {
        this.add(197);
        this.addIndex(this.constPool.addClassInfo(ctClass));
        this.add(n);
        this.growStack(1 - n);
        return n;
    }

    public int addMultiNewarray(String string, int n) {
        this.add(197);
        this.addIndex(this.constPool.addClassInfo(string));
        this.add(n);
        this.growStack(1 - n);
        return n;
    }

    public void addPutfield(CtClass ctClass, String string, String string2) {
        this.addPutfield0(ctClass, null, string, string2);
    }

    public void addPutfield(String string, String string2, String string3) {
        this.addPutfield0(null, string, string2, string3);
    }

    private void addPutfield0(CtClass ctClass, String string, String string2, String string3) {
        this.add(181);
        int n = string == null ? this.constPool.addClassInfo(ctClass) : this.constPool.addClassInfo(string);
        this.addIndex(this.constPool.addFieldrefInfo(n, string2, string3));
        this.growStack(-1 - Descriptor.dataSize(string3));
    }

    public void addPutstatic(CtClass ctClass, String string, String string2) {
        this.addPutstatic0(ctClass, null, string, string2);
    }

    public void addPutstatic(String string, String string2, String string3) {
        this.addPutstatic0(null, string, string2, string3);
    }

    private void addPutstatic0(CtClass ctClass, String string, String string2, String string3) {
        this.add(179);
        int n = string == null ? this.constPool.addClassInfo(ctClass) : this.constPool.addClassInfo(string);
        this.addIndex(this.constPool.addFieldrefInfo(n, string2, string3));
        this.growStack(-Descriptor.dataSize(string3));
    }

    public void addReturn(CtClass ctClass) {
        if (ctClass == null) {
            this.addOpcode(177);
        } else if (ctClass.isPrimitive()) {
            CtPrimitiveType ctPrimitiveType = (CtPrimitiveType)ctClass;
            this.addOpcode(ctPrimitiveType.getReturnOp());
        } else {
            this.addOpcode(176);
        }
    }

    public void addRet(int n) {
        if (n < 256) {
            this.addOpcode(169);
            this.add(n);
        } else {
            this.addOpcode(196);
            this.addOpcode(169);
            this.addIndex(n);
        }
    }

    public void addPrintln(String string) {
        this.addGetstatic("java.lang.System", "err", "Ljava/io/PrintStream;");
        this.addLdc(string);
        this.addInvokevirtual("java.io.PrintStream", "println", "(Ljava/lang/String;)V");
    }
}

