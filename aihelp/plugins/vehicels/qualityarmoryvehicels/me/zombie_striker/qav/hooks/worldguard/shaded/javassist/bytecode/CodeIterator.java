/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.util.ArrayList;
import java.util.List;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.LineNumberAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.LocalVariableAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Opcode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMapTable;

public class CodeIterator
implements Opcode {
    protected CodeAttribute codeAttr;
    protected byte[] bytecode;
    protected int endPos;
    protected int currentPos;
    protected int mark;
    protected int mark2;
    private static final int[] opcodeLength = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 2, 3, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 0, 0, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 5, 5, 3, 2, 3, 1, 1, 3, 3, 1, 1, 0, 4, 3, 3, 5, 5};

    protected CodeIterator(CodeAttribute codeAttribute) {
        this.codeAttr = codeAttribute;
        this.bytecode = codeAttribute.getCode();
        this.begin();
    }

    public void begin() {
        this.mark2 = 0;
        this.mark = 0;
        this.currentPos = 0;
        this.endPos = this.getCodeLength();
    }

    public void move(int n) {
        this.currentPos = n;
    }

    public void setMark(int n) {
        this.mark = n;
    }

    public void setMark2(int n) {
        this.mark2 = n;
    }

    public int getMark() {
        return this.mark;
    }

    public int getMark2() {
        return this.mark2;
    }

    public CodeAttribute get() {
        return this.codeAttr;
    }

    public int getCodeLength() {
        return this.bytecode.length;
    }

    public int byteAt(int n) {
        return this.bytecode[n] & 0xFF;
    }

    public int signedByteAt(int n) {
        return this.bytecode[n];
    }

    public void writeByte(int n, int n2) {
        this.bytecode[n2] = (byte)n;
    }

    public int u16bitAt(int n) {
        return ByteArray.readU16bit(this.bytecode, n);
    }

    public int s16bitAt(int n) {
        return ByteArray.readS16bit(this.bytecode, n);
    }

    public void write16bit(int n, int n2) {
        ByteArray.write16bit(n, this.bytecode, n2);
    }

    public int s32bitAt(int n) {
        return ByteArray.read32bit(this.bytecode, n);
    }

    public void write32bit(int n, int n2) {
        ByteArray.write32bit(n, this.bytecode, n2);
    }

    public void write(byte[] byArray, int n) {
        for (byte this.bytecode[n++] : byArray) {
        }
    }

    public boolean hasNext() {
        return this.currentPos < this.endPos;
    }

    public int next() {
        int n = this.currentPos;
        this.currentPos = CodeIterator.nextOpcode(this.bytecode, n);
        return n;
    }

    public int lookAhead() {
        return this.currentPos;
    }

    public int skipConstructor() {
        return this.skipSuperConstructor0(-1);
    }

    public int skipSuperConstructor() {
        return this.skipSuperConstructor0(0);
    }

    public int skipThisConstructor() {
        return this.skipSuperConstructor0(1);
    }

    private int skipSuperConstructor0(int n) {
        this.begin();
        ConstPool constPool = this.codeAttr.getConstPool();
        String string = this.codeAttr.getDeclaringClass();
        int n2 = 0;
        while (this.hasNext()) {
            int n3;
            int n4 = this.next();
            int n5 = this.byteAt(n4);
            if (n5 == 187) {
                ++n2;
                continue;
            }
            if (n5 != 183 || !constPool.getMethodrefName(n3 = ByteArray.readU16bit(this.bytecode, n4 + 1)).equals("<init>") || --n2 >= 0) continue;
            if (n < 0) {
                return n4;
            }
            String string2 = constPool.getMethodrefClassName(n3);
            if (string2.equals(string) != n > 0) break;
            return n4;
        }
        this.begin();
        return -1;
    }

    public int insert(byte[] byArray) {
        return this.insert0(this.currentPos, byArray, false);
    }

    public void insert(int n, byte[] byArray) {
        this.insert0(n, byArray, false);
    }

    public int insertAt(int n, byte[] byArray) {
        return this.insert0(n, byArray, false);
    }

    public int insertEx(byte[] byArray) {
        return this.insert0(this.currentPos, byArray, true);
    }

    public void insertEx(int n, byte[] byArray) {
        this.insert0(n, byArray, true);
    }

    public int insertExAt(int n, byte[] byArray) {
        return this.insert0(n, byArray, true);
    }

    private int insert0(int n, byte[] byArray, boolean bl) {
        int n2 = byArray.length;
        if (n2 <= 0) {
            return n;
        }
        int n3 = n = this.insertGapAt((int)n, (int)n2, (boolean)bl).position;
        for (int i = 0; i < n2; ++i) {
            this.bytecode[n3++] = byArray[i];
        }
        return n;
    }

    public int insertGap(int n) {
        return this.insertGapAt((int)this.currentPos, (int)n, (boolean)false).position;
    }

    public int insertGap(int n, int n2) {
        return this.insertGapAt((int)n, (int)n2, (boolean)false).length;
    }

    public int insertExGap(int n) {
        return this.insertGapAt((int)this.currentPos, (int)n, (boolean)true).position;
    }

    public int insertExGap(int n, int n2) {
        return this.insertGapAt((int)n, (int)n2, (boolean)true).length;
    }

    public Gap insertGapAt(int n, int n2, boolean bl) {
        int n3;
        byte[] byArray;
        Gap gap = new Gap();
        if (n2 <= 0) {
            gap.position = n;
            gap.length = 0;
            return gap;
        }
        if (this.bytecode.length + n2 > Short.MAX_VALUE) {
            byArray = this.insertGapCore0w(this.bytecode, n, n2, bl, this.get().getExceptionTable(), this.codeAttr, gap);
            n = gap.position;
            n3 = n2;
        } else {
            int n4 = this.currentPos;
            byArray = CodeIterator.insertGapCore0(this.bytecode, n, n2, bl, this.get().getExceptionTable(), this.codeAttr);
            n3 = byArray.length - this.bytecode.length;
            gap.position = n;
            gap.length = n3;
            if (n4 >= n) {
                this.currentPos = n4 + n3;
            }
            if (this.mark > n || this.mark == n && bl) {
                this.mark += n3;
            }
            if (this.mark2 > n || this.mark2 == n && bl) {
                this.mark2 += n3;
            }
        }
        this.codeAttr.setCode(byArray);
        this.bytecode = byArray;
        this.endPos = this.getCodeLength();
        this.updateCursors(n, n3);
        return gap;
    }

    protected void updateCursors(int n, int n2) {
    }

    public void insert(ExceptionTable exceptionTable, int n) {
        this.codeAttr.getExceptionTable().add(0, exceptionTable, n);
    }

    public int append(byte[] byArray) {
        int n = this.getCodeLength();
        int n2 = byArray.length;
        if (n2 <= 0) {
            return n;
        }
        this.appendGap(n2);
        byte[] byArray2 = this.bytecode;
        for (int i = 0; i < n2; ++i) {
            byArray2[i + n] = byArray[i];
        }
        return n;
    }

    public void appendGap(int n) {
        int n2;
        byte[] byArray = this.bytecode;
        int n3 = byArray.length;
        byte[] byArray2 = new byte[n3 + n];
        for (n2 = 0; n2 < n3; ++n2) {
            byArray2[n2] = byArray[n2];
        }
        for (n2 = n3; n2 < n3 + n; ++n2) {
            byArray2[n2] = 0;
        }
        this.codeAttr.setCode(byArray2);
        this.bytecode = byArray2;
        this.endPos = this.getCodeLength();
    }

    public void append(ExceptionTable exceptionTable, int n) {
        ExceptionTable exceptionTable2 = this.codeAttr.getExceptionTable();
        exceptionTable2.add(exceptionTable2.size(), exceptionTable, n);
    }

    static int nextOpcode(byte[] byArray, int n) {
        int n2;
        try {
            n2 = byArray[n] & 0xFF;
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw new BadBytecode("invalid opcode address");
        }
        try {
            int n3 = opcodeLength[n2];
            if (n3 > 0) {
                return n + n3;
            }
            if (n2 == 196) {
                if (byArray[n + 1] == -124) {
                    return n + 6;
                }
                return n + 4;
            }
            int n4 = (n & 0xFFFFFFFC) + 8;
            if (n2 == 171) {
                int n5 = ByteArray.read32bit(byArray, n4);
                return n4 + n5 * 8 + 4;
            }
            if (n2 == 170) {
                int n6 = ByteArray.read32bit(byArray, n4);
                int n7 = ByteArray.read32bit(byArray, n4 + 4);
                return n4 + (n7 - n6 + 1) * 4 + 8;
            }
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            // empty catch block
        }
        throw new BadBytecode(n2);
    }

    static byte[] insertGapCore0(byte[] byArray, int n, int n2, boolean bl, ExceptionTable exceptionTable, CodeAttribute codeAttribute) {
        if (n2 <= 0) {
            return byArray;
        }
        try {
            return CodeIterator.insertGapCore1(byArray, n, n2, bl, exceptionTable, codeAttribute);
        } catch (AlignmentException alignmentException) {
            try {
                return CodeIterator.insertGapCore1(byArray, n, n2 + 3 & 0xFFFFFFFC, bl, exceptionTable, codeAttribute);
            } catch (AlignmentException alignmentException2) {
                throw new RuntimeException("fatal error?");
            }
        }
    }

    private static byte[] insertGapCore1(byte[] byArray, int n, int n2, boolean bl, ExceptionTable exceptionTable, CodeAttribute codeAttribute) {
        StackMap stackMap;
        StackMapTable stackMapTable;
        LocalVariableAttribute localVariableAttribute;
        LocalVariableAttribute localVariableAttribute2;
        int n3 = byArray.length;
        byte[] byArray2 = new byte[n3 + n2];
        CodeIterator.insertGap2(byArray, n, n2, n3, byArray2, bl);
        exceptionTable.shiftPc(n, n2, bl);
        LineNumberAttribute lineNumberAttribute = (LineNumberAttribute)codeAttribute.getAttribute("LineNumberTable");
        if (lineNumberAttribute != null) {
            lineNumberAttribute.shiftPc(n, n2, bl);
        }
        if ((localVariableAttribute2 = (LocalVariableAttribute)codeAttribute.getAttribute("LocalVariableTable")) != null) {
            localVariableAttribute2.shiftPc(n, n2, bl);
        }
        if ((localVariableAttribute = (LocalVariableAttribute)codeAttribute.getAttribute("LocalVariableTypeTable")) != null) {
            localVariableAttribute.shiftPc(n, n2, bl);
        }
        if ((stackMapTable = (StackMapTable)codeAttribute.getAttribute("StackMapTable")) != null) {
            stackMapTable.shiftPc(n, n2, bl);
        }
        if ((stackMap = (StackMap)codeAttribute.getAttribute("StackMap")) != null) {
            stackMap.shiftPc(n, n2, bl);
        }
        return byArray2;
    }

    private static void insertGap2(byte[] byArray, int n, int n2, int n3, byte[] byArray2, boolean bl) {
        int n4 = 0;
        int n5 = 0;
        while (n4 < n3) {
            int n6;
            int n7;
            int n8;
            int n9;
            int n10;
            int n11;
            if (n4 == n) {
                n11 = n5 + n2;
                while (n5 < n11) {
                    byArray2[n5++] = 0;
                }
            }
            int n12 = CodeIterator.nextOpcode(byArray, n4);
            n11 = byArray[n4] & 0xFF;
            if (153 <= n11 && n11 <= 168 || n11 == 198 || n11 == 199) {
                n10 = byArray[n4 + 1] << 8 | byArray[n4 + 2] & 0xFF;
                n10 = CodeIterator.newOffset(n4, n10, n, n2, bl);
                byArray2[n5] = byArray[n4];
                ByteArray.write16bit(n10, byArray2, n5 + 1);
                n5 += 3;
            } else if (n11 == 200 || n11 == 201) {
                n10 = ByteArray.read32bit(byArray, n4 + 1);
                n10 = CodeIterator.newOffset(n4, n10, n, n2, bl);
                byArray2[n5++] = byArray[n4];
                ByteArray.write32bit(n10, byArray2, n5);
                n5 += 4;
            } else if (n11 == 170) {
                if (n4 != n5 && (n2 & 3) != 0) {
                    throw new AlignmentException();
                }
                n10 = (n4 & 0xFFFFFFFC) + 4;
                n5 = CodeIterator.copyGapBytes(byArray2, n5, byArray, n4, n10);
                n9 = CodeIterator.newOffset(n4, ByteArray.read32bit(byArray, n10), n, n2, bl);
                ByteArray.write32bit(n9, byArray2, n5);
                n8 = ByteArray.read32bit(byArray, n10 + 4);
                ByteArray.write32bit(n8, byArray2, n5 + 4);
                n7 = ByteArray.read32bit(byArray, n10 + 8);
                ByteArray.write32bit(n7, byArray2, n5 + 8);
                n5 += 12;
                n6 = n10 + 12;
                n10 = n6 + (n7 - n8 + 1) * 4;
                while (n6 < n10) {
                    int n13 = CodeIterator.newOffset(n4, ByteArray.read32bit(byArray, n6), n, n2, bl);
                    ByteArray.write32bit(n13, byArray2, n5);
                    n5 += 4;
                    n6 += 4;
                }
            } else if (n11 == 171) {
                if (n4 != n5 && (n2 & 3) != 0) {
                    throw new AlignmentException();
                }
                n10 = (n4 & 0xFFFFFFFC) + 4;
                n5 = CodeIterator.copyGapBytes(byArray2, n5, byArray, n4, n10);
                n9 = CodeIterator.newOffset(n4, ByteArray.read32bit(byArray, n10), n, n2, bl);
                ByteArray.write32bit(n9, byArray2, n5);
                n8 = ByteArray.read32bit(byArray, n10 + 4);
                ByteArray.write32bit(n8, byArray2, n5 + 4);
                n5 += 8;
                n7 = n10 + 8;
                n10 = n7 + n8 * 8;
                while (n7 < n10) {
                    ByteArray.copy32bit(byArray, n7, byArray2, n5);
                    n6 = CodeIterator.newOffset(n4, ByteArray.read32bit(byArray, n7 + 4), n, n2, bl);
                    ByteArray.write32bit(n6, byArray2, n5 + 4);
                    n5 += 8;
                    n7 += 8;
                }
            } else {
                while (n4 < n12) {
                    byArray2[n5++] = byArray[n4++];
                }
            }
            n4 = n12;
        }
    }

    private static int copyGapBytes(byte[] byArray, int n, byte[] byArray2, int n2, int n3) {
        switch (n3 - n2) {
            case 4: {
                byArray[n++] = byArray2[n2++];
            }
            case 3: {
                byArray[n++] = byArray2[n2++];
            }
            case 2: {
                byArray[n++] = byArray2[n2++];
            }
            case 1: {
                byArray[n++] = byArray2[n2++];
            }
        }
        return n;
    }

    private static int newOffset(int n, int n2, int n3, int n4, boolean bl) {
        int n5 = n + n2;
        if (n < n3) {
            if (n3 < n5 || bl && n3 == n5) {
                n2 += n4;
            }
        } else if (n == n3) {
            if (n5 < n3) {
                n2 -= n4;
            }
        } else if (n5 < n3 || !bl && n3 == n5) {
            n2 -= n4;
        }
        return n2;
    }

    static byte[] changeLdcToLdcW(byte[] byArray, ExceptionTable exceptionTable, CodeAttribute codeAttribute, CodeAttribute.LdcEntry ldcEntry) {
        Pointers pointers = new Pointers(0, 0, 0, 0, exceptionTable, codeAttribute);
        List<Branch> list = CodeIterator.makeJumpList(byArray, byArray.length, pointers);
        while (ldcEntry != null) {
            CodeIterator.addLdcW(ldcEntry, list);
            ldcEntry = ldcEntry.next;
        }
        byte[] byArray2 = CodeIterator.insertGap2w(byArray, 0, 0, false, list, pointers);
        return byArray2;
    }

    private static void addLdcW(CodeAttribute.LdcEntry ldcEntry, List<Branch> list) {
        int n = ldcEntry.where;
        LdcW ldcW = new LdcW(n, ldcEntry.index);
        int n2 = list.size();
        for (int i = 0; i < n2; ++i) {
            if (n >= list.get((int)i).orgPos) continue;
            list.add(i, ldcW);
            return;
        }
        list.add(ldcW);
    }

    private byte[] insertGapCore0w(byte[] byArray, int n, int n2, boolean bl, ExceptionTable exceptionTable, CodeAttribute codeAttribute, Gap gap) {
        if (n2 <= 0) {
            return byArray;
        }
        Pointers pointers = new Pointers(this.currentPos, this.mark, this.mark2, n, exceptionTable, codeAttribute);
        List<Branch> list = CodeIterator.makeJumpList(byArray, byArray.length, pointers);
        byte[] byArray2 = CodeIterator.insertGap2w(byArray, n, n2, bl, list, pointers);
        this.currentPos = pointers.cursor;
        this.mark = pointers.mark;
        this.mark2 = pointers.mark2;
        int n3 = pointers.mark0;
        if (n3 == this.currentPos && !bl) {
            this.currentPos += n2;
        }
        if (bl) {
            n3 -= n2;
        }
        gap.position = n3;
        gap.length = n2;
        return byArray2;
    }

    /*
     * Could not resolve type clashes
     * Unable to fully structure code
     */
    private static byte[] insertGap2w(byte[] var0, int var1_1, int var2_2, boolean var3_3, List<Branch> var4_4, Pointers var5_5) {
        if (var2_2 > 0) {
            var5_5.shiftPc(var1_1, var2_2, var3_3);
            for (Object var7_8 : var4_4) {
                var7_8.shift(var1_1, var2_2, var3_3);
            }
        }
        var6_7 = true;
        block1: while (true) {
            if (var6_7) {
                var6_7 = false;
                var7_8 = var4_4.iterator();
                block2: while (true) {
                    if (!var7_8.hasNext()) continue block1;
                    var8_9 = var7_8.next();
                    if (!var8_9.expanded()) continue;
                    var6_7 = true;
                    var9_10 = var8_9.pos;
                    var10_11 = var8_9.deltaSize();
                    var5_5.shiftPc(var9_10, var10_11, false);
                    var11_12 = var4_4.iterator();
                    while (true) {
                        if (var11_12.hasNext()) ** break;
                        continue block2;
                        var12_13 = var11_12.next();
                        var12_13.shift(var9_10, var10_11, false);
                    }
                    break;
                }
            }
            for (Branch var8_9 : var4_4) {
                var9_10 = var8_9.gapChanged();
                if (var9_10 <= 0) continue;
                var6_7 = true;
                var10_11 = var8_9.pos;
                var5_5.shiftPc(var10_11, var9_10, false);
                for (Branch var12_13 : var4_4) {
                    var12_13.shift(var10_11, var9_10, false);
                }
            }
            if (!var6_7) break;
        }
        return CodeIterator.makeExapndedCode(var0, var4_4, var1_1, var2_2);
    }

    private static List<Branch> makeJumpList(byte[] byArray, int n, Pointers pointers) {
        ArrayList<Branch> arrayList = new ArrayList<Branch>();
        int n2 = 0;
        while (n2 < n) {
            int n3;
            int n4;
            int n5;
            int n6 = CodeIterator.nextOpcode(byArray, n2);
            int n7 = byArray[n2] & 0xFF;
            if (153 <= n7 && n7 <= 168 || n7 == 198 || n7 == 199) {
                n5 = byArray[n2 + 1] << 8 | byArray[n2 + 2] & 0xFF;
                Branch16 branch16 = n7 == 167 || n7 == 168 ? new Jump16(n2, n5) : new If16(n2, n5);
                arrayList.add(branch16);
            } else if (n7 == 200 || n7 == 201) {
                n5 = ByteArray.read32bit(byArray, n2 + 1);
                arrayList.add(new Jump32(n2, n5));
            } else if (n7 == 170) {
                n5 = (n2 & 0xFFFFFFFC) + 4;
                int n8 = ByteArray.read32bit(byArray, n5);
                n4 = ByteArray.read32bit(byArray, n5 + 4);
                n3 = ByteArray.read32bit(byArray, n5 + 8);
                int n9 = n5 + 12;
                int n10 = n3 - n4 + 1;
                int[] nArray = new int[n10];
                for (int i = 0; i < n10; ++i) {
                    nArray[i] = ByteArray.read32bit(byArray, n9);
                    n9 += 4;
                }
                arrayList.add(new Table(n2, n8, n4, n3, nArray, pointers));
            } else if (n7 == 171) {
                n5 = (n2 & 0xFFFFFFFC) + 4;
                int n11 = ByteArray.read32bit(byArray, n5);
                n4 = ByteArray.read32bit(byArray, n5 + 4);
                n3 = n5 + 8;
                int[] nArray = new int[n4];
                int[] nArray2 = new int[n4];
                for (int i = 0; i < n4; ++i) {
                    nArray[i] = ByteArray.read32bit(byArray, n3);
                    nArray2[i] = ByteArray.read32bit(byArray, n3 + 4);
                    n3 += 8;
                }
                arrayList.add(new Lookup(n2, n11, nArray, nArray2, pointers));
            }
            n2 = n6;
        }
        return arrayList;
    }

    private static byte[] makeExapndedCode(byte[] byArray, List<Branch> list, int n, int n2) {
        int n3;
        Branch branch;
        int n4 = list.size();
        int n5 = byArray.length + n2;
        for (Branch branch2 : list) {
            n5 += branch2.deltaSize();
        }
        Object object = new byte[n5];
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = byArray.length;
        if (0 < n4) {
            branch = list.get(0);
            n3 = branch.orgPos;
        } else {
            branch = null;
            n3 = n9;
        }
        while (n6 < n9) {
            int n10;
            if (n6 == n) {
                n10 = n7 + n2;
                while (n7 < n10) {
                    object[n7++] = false;
                }
            }
            if (n6 != n3) {
                object[n7++] = byArray[n6++];
                continue;
            }
            n10 = branch.write(n6, byArray, n7, (byte[])object);
            n6 += n10;
            n7 += n10 + branch.deltaSize();
            if (++n8 < n4) {
                branch = list.get(n8);
                n3 = branch.orgPos;
                continue;
            }
            branch = null;
            n3 = n9;
        }
        return object;
    }

    public static class Gap {
        public int position;
        public int length;
    }

    static class AlignmentException
    extends Exception {
        private static final long serialVersionUID = 1L;

        AlignmentException() {
        }
    }

    static class Pointers {
        int cursor;
        int mark0;
        int mark;
        int mark2;
        ExceptionTable etable;
        LineNumberAttribute line;
        LocalVariableAttribute vars;
        LocalVariableAttribute types;
        StackMapTable stack;
        StackMap stack2;

        Pointers(int n, int n2, int n3, int n4, ExceptionTable exceptionTable, CodeAttribute codeAttribute) {
            this.cursor = n;
            this.mark = n2;
            this.mark2 = n3;
            this.mark0 = n4;
            this.etable = exceptionTable;
            this.line = (LineNumberAttribute)codeAttribute.getAttribute("LineNumberTable");
            this.vars = (LocalVariableAttribute)codeAttribute.getAttribute("LocalVariableTable");
            this.types = (LocalVariableAttribute)codeAttribute.getAttribute("LocalVariableTypeTable");
            this.stack = (StackMapTable)codeAttribute.getAttribute("StackMapTable");
            this.stack2 = (StackMap)codeAttribute.getAttribute("StackMap");
        }

        void shiftPc(int n, int n2, boolean bl) {
            if (n < this.cursor || n == this.cursor && bl) {
                this.cursor += n2;
            }
            if (n < this.mark || n == this.mark && bl) {
                this.mark += n2;
            }
            if (n < this.mark2 || n == this.mark2 && bl) {
                this.mark2 += n2;
            }
            if (n < this.mark0 || n == this.mark0 && bl) {
                this.mark0 += n2;
            }
            this.etable.shiftPc(n, n2, bl);
            if (this.line != null) {
                this.line.shiftPc(n, n2, bl);
            }
            if (this.vars != null) {
                this.vars.shiftPc(n, n2, bl);
            }
            if (this.types != null) {
                this.types.shiftPc(n, n2, bl);
            }
            if (this.stack != null) {
                this.stack.shiftPc(n, n2, bl);
            }
            if (this.stack2 != null) {
                this.stack2.shiftPc(n, n2, bl);
            }
        }

        void shiftForSwitch(int n, int n2) {
            if (this.stack != null) {
                this.stack.shiftForSwitch(n, n2);
            }
            if (this.stack2 != null) {
                this.stack2.shiftForSwitch(n, n2);
            }
        }
    }

    static class LdcW
    extends Branch {
        int index;
        boolean state;

        LdcW(int n, int n2) {
            super(n);
            this.index = n2;
            this.state = true;
        }

        @Override
        boolean expanded() {
            if (this.state) {
                this.state = false;
                return true;
            }
            return false;
        }

        @Override
        int deltaSize() {
            return 1;
        }

        @Override
        int write(int n, byte[] byArray, int n2, byte[] byArray2) {
            byArray2[n2] = 19;
            ByteArray.write16bit(this.index, byArray2, n2 + 1);
            return 2;
        }
    }

    static abstract class Branch {
        int pos;
        int orgPos;

        Branch(int n) {
            this.pos = this.orgPos = n;
        }

        void shift(int n, int n2, boolean bl) {
            if (n < this.pos || n == this.pos && bl) {
                this.pos += n2;
            }
        }

        static int shiftOffset(int n, int n2, int n3, int n4, boolean bl) {
            int n5 = n + n2;
            if (n < n3) {
                if (n3 < n5 || bl && n3 == n5) {
                    n2 += n4;
                }
            } else if (n == n3) {
                if (n5 < n3 && bl) {
                    n2 -= n4;
                } else if (n3 < n5 && !bl) {
                    n2 += n4;
                }
            } else if (n5 < n3 || !bl && n3 == n5) {
                n2 -= n4;
            }
            return n2;
        }

        boolean expanded() {
            return false;
        }

        int gapChanged() {
            return 0;
        }

        int deltaSize() {
            return 0;
        }

        abstract int write(int var1, byte[] var2, int var3, byte[] var4);
    }

    static class Jump16
    extends Branch16 {
        Jump16(int n, int n2) {
            super(n, n2);
        }

        @Override
        int deltaSize() {
            return this.state == 2 ? 2 : 0;
        }

        @Override
        void write32(int n, byte[] byArray, int n2, byte[] byArray2) {
            byArray2[n2] = (byte)((byArray[n] & 0xFF) == 167 ? 200 : 201);
            ByteArray.write32bit(this.offset, byArray2, n2 + 1);
        }
    }

    static class If16
    extends Branch16 {
        If16(int n, int n2) {
            super(n, n2);
        }

        @Override
        int deltaSize() {
            return this.state == 2 ? 5 : 0;
        }

        @Override
        void write32(int n, byte[] byArray, int n2, byte[] byArray2) {
            byArray2[n2] = (byte)this.opcode(byArray[n] & 0xFF);
            byArray2[n2 + 1] = 0;
            byArray2[n2 + 2] = 8;
            byArray2[n2 + 3] = -56;
            ByteArray.write32bit(this.offset - 3, byArray2, n2 + 4);
        }

        int opcode(int n) {
            if (n == 198) {
                return 199;
            }
            if (n == 199) {
                return 198;
            }
            if ((n - 153 & 1) == 0) {
                return n + 1;
            }
            return n - 1;
        }
    }

    static class Jump32
    extends Branch {
        int offset;

        Jump32(int n, int n2) {
            super(n);
            this.offset = n2;
        }

        @Override
        void shift(int n, int n2, boolean bl) {
            this.offset = Jump32.shiftOffset(this.pos, this.offset, n, n2, bl);
            super.shift(n, n2, bl);
        }

        @Override
        int write(int n, byte[] byArray, int n2, byte[] byArray2) {
            byArray2[n2] = byArray[n];
            ByteArray.write32bit(this.offset, byArray2, n2 + 1);
            return 5;
        }
    }

    static class Table
    extends Switcher {
        int low;
        int high;

        Table(int n, int n2, int n3, int n4, int[] nArray, Pointers pointers) {
            super(n, n2, nArray, pointers);
            this.low = n3;
            this.high = n4;
        }

        @Override
        int write2(int n, byte[] byArray) {
            ByteArray.write32bit(this.low, byArray, n);
            ByteArray.write32bit(this.high, byArray, n + 4);
            int n2 = this.offsets.length;
            n += 8;
            for (int i = 0; i < n2; ++i) {
                ByteArray.write32bit(this.offsets[i], byArray, n);
                n += 4;
            }
            return 8 + 4 * n2;
        }

        @Override
        int tableSize() {
            return 8 + 4 * this.offsets.length;
        }
    }

    static class Lookup
    extends Switcher {
        int[] matches;

        Lookup(int n, int n2, int[] nArray, int[] nArray2, Pointers pointers) {
            super(n, n2, nArray2, pointers);
            this.matches = nArray;
        }

        @Override
        int write2(int n, byte[] byArray) {
            int n2 = this.matches.length;
            ByteArray.write32bit(n2, byArray, n);
            n += 4;
            for (int i = 0; i < n2; ++i) {
                ByteArray.write32bit(this.matches[i], byArray, n);
                ByteArray.write32bit(this.offsets[i], byArray, n + 4);
                n += 8;
            }
            return 4 + 8 * n2;
        }

        @Override
        int tableSize() {
            return 4 + 8 * this.matches.length;
        }
    }

    static abstract class Switcher
    extends Branch {
        int gap;
        int defaultByte;
        int[] offsets;
        Pointers pointers;

        Switcher(int n, int n2, int[] nArray, Pointers pointers) {
            super(n);
            this.gap = 3 - (n & 3);
            this.defaultByte = n2;
            this.offsets = nArray;
            this.pointers = pointers;
        }

        @Override
        void shift(int n, int n2, boolean bl) {
            int n3 = this.pos;
            this.defaultByte = Switcher.shiftOffset(n3, this.defaultByte, n, n2, bl);
            int n4 = this.offsets.length;
            for (int i = 0; i < n4; ++i) {
                this.offsets[i] = Switcher.shiftOffset(n3, this.offsets[i], n, n2, bl);
            }
            super.shift(n, n2, bl);
        }

        @Override
        int gapChanged() {
            int n = 3 - (this.pos & 3);
            if (n > this.gap) {
                int n2 = n - this.gap;
                this.gap = n;
                return n2;
            }
            return 0;
        }

        @Override
        int deltaSize() {
            return this.gap - (3 - (this.orgPos & 3));
        }

        @Override
        int write(int n, byte[] byArray, int n2, byte[] byArray2) {
            int n3 = 3 - (this.pos & 3);
            int n4 = this.gap - n3;
            int n5 = 5 + (3 - (this.orgPos & 3)) + this.tableSize();
            if (n4 > 0) {
                this.adjustOffsets(n5, n4);
            }
            byArray2[n2++] = byArray[n];
            while (n3-- > 0) {
                byArray2[n2++] = 0;
            }
            ByteArray.write32bit(this.defaultByte, byArray2, n2);
            int n6 = this.write2(n2 + 4, byArray2);
            n2 += n6 + 4;
            while (n4-- > 0) {
                byArray2[n2++] = 0;
            }
            return 5 + (3 - (this.orgPos & 3)) + n6;
        }

        abstract int write2(int var1, byte[] var2);

        abstract int tableSize();

        void adjustOffsets(int n, int n2) {
            this.pointers.shiftForSwitch(this.pos + n, n2);
            if (this.defaultByte == n) {
                this.defaultByte -= n2;
            }
            for (int i = 0; i < this.offsets.length; ++i) {
                if (this.offsets[i] != n) continue;
                int n3 = i;
                this.offsets[n3] = this.offsets[n3] - n2;
            }
        }
    }

    static abstract class Branch16
    extends Branch {
        int offset;
        int state;
        static final int BIT16 = 0;
        static final int EXPAND = 1;
        static final int BIT32 = 2;

        Branch16(int n, int n2) {
            super(n);
            this.offset = n2;
            this.state = 0;
        }

        @Override
        void shift(int n, int n2, boolean bl) {
            this.offset = Branch16.shiftOffset(this.pos, this.offset, n, n2, bl);
            super.shift(n, n2, bl);
            if (this.state == 0 && (this.offset < Short.MIN_VALUE || Short.MAX_VALUE < this.offset)) {
                this.state = 1;
            }
        }

        @Override
        boolean expanded() {
            if (this.state == 1) {
                this.state = 2;
                return true;
            }
            return false;
        }

        @Override
        abstract int deltaSize();

        abstract void write32(int var1, byte[] var2, int var3, byte[] var4);

        @Override
        int write(int n, byte[] byArray, int n2, byte[] byArray2) {
            if (this.state == 2) {
                this.write32(n, byArray, n2, byArray2);
            } else {
                byArray2[n2] = byArray[n];
                ByteArray.write16bit(this.offset, byArray2, n2 + 1);
            }
            return 3;
        }
    }
}

