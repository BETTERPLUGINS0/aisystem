/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAnalyzer;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Opcode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMapTable;

public class CodeAttribute
extends AttributeInfo
implements Opcode {
    public static final String tag = "Code";
    private int maxStack;
    private int maxLocals;
    private ExceptionTable exceptions;
    private List<AttributeInfo> attributes;

    public CodeAttribute(ConstPool constPool, int n, int n2, byte[] byArray, ExceptionTable exceptionTable) {
        super(constPool, tag);
        this.maxStack = n;
        this.maxLocals = n2;
        this.info = byArray;
        this.exceptions = exceptionTable;
        this.attributes = new ArrayList<AttributeInfo>();
    }

    private CodeAttribute(ConstPool constPool, CodeAttribute codeAttribute, Map<String, String> map) {
        super(constPool, tag);
        this.maxStack = codeAttribute.getMaxStack();
        this.maxLocals = codeAttribute.getMaxLocals();
        this.exceptions = codeAttribute.getExceptionTable().copy(constPool, map);
        this.attributes = new ArrayList<AttributeInfo>();
        List<AttributeInfo> list = codeAttribute.getAttributes();
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            AttributeInfo attributeInfo = list.get(i);
            this.attributes.add(attributeInfo.copy(constPool, map));
        }
        this.info = codeAttribute.copyCode(constPool, map, this.exceptions, this);
    }

    CodeAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, (byte[])null);
        int n2 = dataInputStream.readInt();
        this.maxStack = dataInputStream.readUnsignedShort();
        this.maxLocals = dataInputStream.readUnsignedShort();
        int n3 = dataInputStream.readInt();
        this.info = new byte[n3];
        dataInputStream.readFully(this.info);
        this.exceptions = new ExceptionTable(constPool, dataInputStream);
        this.attributes = new ArrayList<AttributeInfo>();
        int n4 = dataInputStream.readUnsignedShort();
        for (int i = 0; i < n4; ++i) {
            this.attributes.add(AttributeInfo.read(constPool, dataInputStream));
        }
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        try {
            return new CodeAttribute(constPool, this, map);
        } catch (BadBytecode badBytecode) {
            throw new RuntimeCopyException("bad bytecode. fatal?");
        }
    }

    @Override
    public int length() {
        return 18 + this.info.length + this.exceptions.size() * 8 + AttributeInfo.getLength(this.attributes);
    }

    @Override
    void write(DataOutputStream dataOutputStream) {
        dataOutputStream.writeShort(this.name);
        dataOutputStream.writeInt(this.length() - 6);
        dataOutputStream.writeShort(this.maxStack);
        dataOutputStream.writeShort(this.maxLocals);
        dataOutputStream.writeInt(this.info.length);
        dataOutputStream.write(this.info);
        this.exceptions.write(dataOutputStream);
        dataOutputStream.writeShort(this.attributes.size());
        AttributeInfo.writeAll(this.attributes, dataOutputStream);
    }

    @Override
    public byte[] get() {
        throw new UnsupportedOperationException("CodeAttribute.get()");
    }

    @Override
    public void set(byte[] byArray) {
        throw new UnsupportedOperationException("CodeAttribute.set()");
    }

    @Override
    void renameClass(String string, String string2) {
        AttributeInfo.renameClass(this.attributes, string, string2);
    }

    @Override
    void renameClass(Map<String, String> map) {
        AttributeInfo.renameClass(this.attributes, map);
    }

    @Override
    void getRefClasses(Map<String, String> map) {
        AttributeInfo.getRefClasses(this.attributes, map);
    }

    public String getDeclaringClass() {
        ConstPool constPool = this.getConstPool();
        return constPool.getClassName();
    }

    public int getMaxStack() {
        return this.maxStack;
    }

    public void setMaxStack(int n) {
        this.maxStack = n;
    }

    public int computeMaxStack() {
        this.maxStack = new CodeAnalyzer(this).computeMaxStack();
        return this.maxStack;
    }

    public int getMaxLocals() {
        return this.maxLocals;
    }

    public void setMaxLocals(int n) {
        this.maxLocals = n;
    }

    public int getCodeLength() {
        return this.info.length;
    }

    public byte[] getCode() {
        return this.info;
    }

    void setCode(byte[] byArray) {
        super.set(byArray);
    }

    public CodeIterator iterator() {
        return new CodeIterator(this);
    }

    public ExceptionTable getExceptionTable() {
        return this.exceptions;
    }

    public List<AttributeInfo> getAttributes() {
        return this.attributes;
    }

    public AttributeInfo getAttribute(String string) {
        return AttributeInfo.lookup(this.attributes, string);
    }

    public void setAttribute(StackMapTable stackMapTable) {
        AttributeInfo.remove(this.attributes, "StackMapTable");
        if (stackMapTable != null) {
            this.attributes.add(stackMapTable);
        }
    }

    public void setAttribute(StackMap stackMap) {
        AttributeInfo.remove(this.attributes, "StackMap");
        if (stackMap != null) {
            this.attributes.add(stackMap);
        }
    }

    private byte[] copyCode(ConstPool constPool, Map<String, String> map, ExceptionTable exceptionTable, CodeAttribute codeAttribute) {
        int n = this.getCodeLength();
        byte[] byArray = new byte[n];
        codeAttribute.info = byArray;
        LdcEntry ldcEntry = CodeAttribute.copyCode(this.info, 0, n, this.getConstPool(), byArray, constPool, map);
        return LdcEntry.doit(byArray, ldcEntry, exceptionTable, codeAttribute);
    }

    private static LdcEntry copyCode(byte[] byArray, int n, int n2, ConstPool constPool, byte[] byArray2, ConstPool constPool2, Map<String, String> map) {
        LdcEntry ldcEntry = null;
        int n3 = n;
        while (n3 < n2) {
            byte by;
            int n4 = CodeIterator.nextOpcode(byArray, n3);
            byArray2[n3] = by = byArray[n3];
            switch (by & 0xFF) {
                case 19: 
                case 20: 
                case 178: 
                case 179: 
                case 180: 
                case 181: 
                case 182: 
                case 183: 
                case 184: 
                case 187: 
                case 189: 
                case 192: 
                case 193: {
                    CodeAttribute.copyConstPoolInfo(n3 + 1, byArray, constPool, byArray2, constPool2, map);
                    break;
                }
                case 18: {
                    int n5 = byArray[n3 + 1] & 0xFF;
                    n5 = constPool.copy(n5, constPool2, map);
                    if (n5 < 256) {
                        byArray2[n3 + 1] = (byte)n5;
                        break;
                    }
                    byArray2[n3] = 0;
                    byArray2[n3 + 1] = 0;
                    LdcEntry ldcEntry2 = new LdcEntry();
                    ldcEntry2.where = n3;
                    ldcEntry2.index = n5;
                    ldcEntry2.next = ldcEntry;
                    ldcEntry = ldcEntry2;
                    break;
                }
                case 185: {
                    CodeAttribute.copyConstPoolInfo(n3 + 1, byArray, constPool, byArray2, constPool2, map);
                    byArray2[n3 + 3] = byArray[n3 + 3];
                    byArray2[n3 + 4] = byArray[n3 + 4];
                    break;
                }
                case 186: {
                    CodeAttribute.copyConstPoolInfo(n3 + 1, byArray, constPool, byArray2, constPool2, map);
                    byArray2[n3 + 3] = 0;
                    byArray2[n3 + 4] = 0;
                    break;
                }
                case 197: {
                    CodeAttribute.copyConstPoolInfo(n3 + 1, byArray, constPool, byArray2, constPool2, map);
                    byArray2[n3 + 3] = byArray[n3 + 3];
                    break;
                }
                default: {
                    while (++n3 < n4) {
                        byArray2[n3] = byArray[n3];
                    }
                    break block0;
                }
            }
            n3 = n4;
        }
        return ldcEntry;
    }

    private static void copyConstPoolInfo(int n, byte[] byArray, ConstPool constPool, byte[] byArray2, ConstPool constPool2, Map<String, String> map) {
        int n2 = (byArray[n] & 0xFF) << 8 | byArray[n + 1] & 0xFF;
        n2 = constPool.copy(n2, constPool2, map);
        byArray2[n] = (byte)(n2 >> 8);
        byArray2[n + 1] = (byte)n2;
    }

    public void insertLocalVar(int n, int n2) {
        CodeIterator codeIterator = this.iterator();
        while (codeIterator.hasNext()) {
            CodeAttribute.shiftIndex(codeIterator, n, n2);
        }
        this.setMaxLocals(this.getMaxLocals() + n2);
    }

    private static void shiftIndex(CodeIterator codeIterator, int n, int n2) {
        int n3 = codeIterator.next();
        int n4 = codeIterator.byteAt(n3);
        if (n4 < 21) {
            return;
        }
        if (n4 < 79) {
            if (n4 < 26) {
                CodeAttribute.shiftIndex8(codeIterator, n3, n4, n, n2);
            } else if (n4 < 46) {
                CodeAttribute.shiftIndex0(codeIterator, n3, n4, n, n2, 26, 21);
            } else {
                if (n4 < 54) {
                    return;
                }
                if (n4 < 59) {
                    CodeAttribute.shiftIndex8(codeIterator, n3, n4, n, n2);
                } else {
                    CodeAttribute.shiftIndex0(codeIterator, n3, n4, n, n2, 59, 54);
                }
            }
        } else if (n4 == 132) {
            int n5 = codeIterator.byteAt(n3 + 1);
            if (n5 < n) {
                return;
            }
            if ((n5 += n2) < 256) {
                codeIterator.writeByte(n5, n3 + 1);
            } else {
                byte by = (byte)codeIterator.byteAt(n3 + 2);
                int n6 = codeIterator.insertExGap(3);
                codeIterator.writeByte(196, n6 - 3);
                codeIterator.writeByte(132, n6 - 2);
                codeIterator.write16bit(n5, n6 - 1);
                codeIterator.write16bit(by, n6 + 1);
            }
        } else if (n4 == 169) {
            CodeAttribute.shiftIndex8(codeIterator, n3, n4, n, n2);
        } else if (n4 == 196) {
            int n7 = codeIterator.u16bitAt(n3 + 2);
            if (n7 < n) {
                return;
            }
            codeIterator.write16bit(n7 += n2, n3 + 2);
        }
    }

    private static void shiftIndex8(CodeIterator codeIterator, int n, int n2, int n3, int n4) {
        int n5 = codeIterator.byteAt(n + 1);
        if (n5 < n3) {
            return;
        }
        if ((n5 += n4) < 256) {
            codeIterator.writeByte(n5, n + 1);
        } else {
            int n6 = codeIterator.insertExGap(2);
            codeIterator.writeByte(196, n6 - 2);
            codeIterator.writeByte(n2, n6 - 1);
            codeIterator.write16bit(n5, n6);
        }
    }

    private static void shiftIndex0(CodeIterator codeIterator, int n, int n2, int n3, int n4, int n5, int n6) {
        int n7 = (n2 - n5) % 4;
        if (n7 < n3) {
            return;
        }
        if ((n7 += n4) < 4) {
            codeIterator.writeByte(n2 + n4, n);
        } else {
            n2 = (n2 - n5) / 4 + n6;
            if (n7 < 256) {
                int n8 = codeIterator.insertExGap(1);
                codeIterator.writeByte(n2, n8 - 1);
                codeIterator.writeByte(n7, n8);
            } else {
                int n9 = codeIterator.insertExGap(3);
                codeIterator.writeByte(196, n9 - 1);
                codeIterator.writeByte(n2, n9);
                codeIterator.write16bit(n7, n9 + 1);
            }
        }
    }

    public static class RuntimeCopyException
    extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public RuntimeCopyException(String string) {
            super(string);
        }
    }

    static class LdcEntry {
        LdcEntry next;
        int where;
        int index;

        LdcEntry() {
        }

        static byte[] doit(byte[] byArray, LdcEntry ldcEntry, ExceptionTable exceptionTable, CodeAttribute codeAttribute) {
            if (ldcEntry != null) {
                byArray = CodeIterator.changeLdcToLdcW(byArray, exceptionTable, codeAttribute, ldcEntry);
            }
            return byArray;
        }
    }
}

