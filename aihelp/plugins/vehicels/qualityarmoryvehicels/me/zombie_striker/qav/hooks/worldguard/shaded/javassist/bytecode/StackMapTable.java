/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;

public class StackMapTable
extends AttributeInfo {
    public static final String tag = "StackMapTable";
    public static final int TOP = 0;
    public static final int INTEGER = 1;
    public static final int FLOAT = 2;
    public static final int DOUBLE = 3;
    public static final int LONG = 4;
    public static final int NULL = 5;
    public static final int THIS = 6;
    public static final int OBJECT = 7;
    public static final int UNINIT = 8;

    StackMapTable(ConstPool constPool, byte[] byArray) {
        super(constPool, tag, byArray);
    }

    StackMapTable(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        try {
            return new StackMapTable(constPool, new Copier(this.constPool, this.info, constPool, map).doit());
        } catch (BadBytecode badBytecode) {
            throw new RuntimeCopyException("bad bytecode. fatal?");
        }
    }

    @Override
    void write(DataOutputStream dataOutputStream) {
        super.write(dataOutputStream);
    }

    public void insertLocal(int n, int n2, int n3) {
        byte[] byArray = new InsertLocal(this.get(), n, n2, n3).doit();
        this.set(byArray);
    }

    public static int typeTagOf(char c) {
        switch (c) {
            case 'D': {
                return 3;
            }
            case 'F': {
                return 2;
            }
            case 'J': {
                return 4;
            }
            case 'L': 
            case '[': {
                return 7;
            }
        }
        return 1;
    }

    public void println(PrintWriter printWriter) {
        Printer.print(this, printWriter);
    }

    public void println(PrintStream printStream) {
        Printer.print(this, new PrintWriter(printStream, true));
    }

    void shiftPc(int n, int n2, boolean bl) {
        new OffsetShifter(this, n, n2).parse();
        new Shifter(this, n, n2, bl).doit();
    }

    void shiftForSwitch(int n, int n2) {
        new SwitchShifter(this, n, n2).doit();
    }

    public void removeNew(int n) {
        try {
            byte[] byArray = new NewRemover(this.get(), n).doit();
            this.set(byArray);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException("bad stack map table", badBytecode);
        }
    }

    static class Copier
    extends SimpleCopy {
        private ConstPool srcPool;
        private ConstPool destPool;
        private Map<String, String> classnames;

        public Copier(ConstPool constPool, byte[] byArray, ConstPool constPool2, Map<String, String> map) {
            super(byArray);
            this.srcPool = constPool;
            this.destPool = constPool2;
            this.classnames = map;
        }

        @Override
        protected int copyData(int n, int n2) {
            if (n == 7) {
                return this.srcPool.copy(n2, this.destPool, this.classnames);
            }
            return n2;
        }

        @Override
        protected int[] copyData(int[] nArray, int[] nArray2) {
            int[] nArray3 = new int[nArray2.length];
            for (int i = 0; i < nArray2.length; ++i) {
                nArray3[i] = nArray[i] == 7 ? this.srcPool.copy(nArray2[i], this.destPool, this.classnames) : nArray2[i];
            }
            return nArray3;
        }
    }

    public static class RuntimeCopyException
    extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public RuntimeCopyException(String string) {
            super(string);
        }
    }

    static class InsertLocal
    extends SimpleCopy {
        private int varIndex;
        private int varTag;
        private int varData;

        public InsertLocal(byte[] byArray, int n, int n2, int n3) {
            super(byArray);
            this.varIndex = n;
            this.varTag = n2;
            this.varData = n3;
        }

        @Override
        public void fullFrame(int n, int n2, int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
            int n3 = nArray.length;
            if (n3 < this.varIndex) {
                super.fullFrame(n, n2, nArray, nArray2, nArray3, nArray4);
                return;
            }
            int n4 = this.varTag == 4 || this.varTag == 3 ? 2 : 1;
            int[] nArray5 = new int[n3 + n4];
            int[] nArray6 = new int[n3 + n4];
            int n5 = this.varIndex;
            int n6 = 0;
            for (int i = 0; i < n3; ++i) {
                if (n6 == n5) {
                    n6 += n4;
                }
                nArray5[n6] = nArray[i];
                nArray6[n6++] = nArray2[i];
            }
            nArray5[n5] = this.varTag;
            nArray6[n5] = this.varData;
            if (n4 > 1) {
                nArray5[n5 + 1] = 0;
                nArray6[n5 + 1] = 0;
            }
            super.fullFrame(n, n2, nArray5, nArray6, nArray3, nArray4);
        }
    }

    static class Printer
    extends Walker {
        private PrintWriter writer;
        private int offset;

        public static void print(StackMapTable stackMapTable, PrintWriter printWriter) {
            try {
                new Printer(stackMapTable.get(), printWriter).parse();
            } catch (BadBytecode badBytecode) {
                printWriter.println(badBytecode.getMessage());
            }
        }

        Printer(byte[] byArray, PrintWriter printWriter) {
            super(byArray);
            this.writer = printWriter;
            this.offset = -1;
        }

        @Override
        public void sameFrame(int n, int n2) {
            this.offset += n2 + 1;
            this.writer.println(this.offset + " same frame: " + n2);
        }

        @Override
        public void sameLocals(int n, int n2, int n3, int n4) {
            this.offset += n2 + 1;
            this.writer.println(this.offset + " same locals: " + n2);
            this.printTypeInfo(n3, n4);
        }

        @Override
        public void chopFrame(int n, int n2, int n3) {
            this.offset += n2 + 1;
            this.writer.println(this.offset + " chop frame: " + n2 + ",    " + n3 + " last locals");
        }

        @Override
        public void appendFrame(int n, int n2, int[] nArray, int[] nArray2) {
            this.offset += n2 + 1;
            this.writer.println(this.offset + " append frame: " + n2);
            for (int i = 0; i < nArray.length; ++i) {
                this.printTypeInfo(nArray[i], nArray2[i]);
            }
        }

        @Override
        public void fullFrame(int n, int n2, int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
            int n3;
            this.offset += n2 + 1;
            this.writer.println(this.offset + " full frame: " + n2);
            this.writer.println("[locals]");
            for (n3 = 0; n3 < nArray.length; ++n3) {
                this.printTypeInfo(nArray[n3], nArray2[n3]);
            }
            this.writer.println("[stack]");
            for (n3 = 0; n3 < nArray3.length; ++n3) {
                this.printTypeInfo(nArray3[n3], nArray4[n3]);
            }
        }

        private void printTypeInfo(int n, int n2) {
            String string = null;
            switch (n) {
                case 0: {
                    string = "top";
                    break;
                }
                case 1: {
                    string = "integer";
                    break;
                }
                case 2: {
                    string = "float";
                    break;
                }
                case 3: {
                    string = "double";
                    break;
                }
                case 4: {
                    string = "long";
                    break;
                }
                case 5: {
                    string = "null";
                    break;
                }
                case 6: {
                    string = "this";
                    break;
                }
                case 7: {
                    string = "object (cpool_index " + n2 + ")";
                    break;
                }
                case 8: {
                    string = "uninitialized (offset " + n2 + ")";
                }
            }
            this.writer.print("    ");
            this.writer.println(string);
        }
    }

    static class OffsetShifter
    extends Walker {
        int where;
        int gap;

        public OffsetShifter(StackMapTable stackMapTable, int n, int n2) {
            super(stackMapTable);
            this.where = n;
            this.gap = n2;
        }

        @Override
        public void objectOrUninitialized(int n, int n2, int n3) {
            if (n == 8 && this.where <= n2) {
                ByteArray.write16bit(n2 + this.gap, this.info, n3);
            }
        }
    }

    static class Shifter
    extends Walker {
        private StackMapTable stackMap;
        int where;
        int gap;
        int position;
        byte[] updatedInfo;
        boolean exclusive;

        public Shifter(StackMapTable stackMapTable, int n, int n2, boolean bl) {
            super(stackMapTable);
            this.stackMap = stackMapTable;
            this.where = n;
            this.gap = n2;
            this.position = 0;
            this.updatedInfo = null;
            this.exclusive = bl;
        }

        public void doit() {
            this.parse();
            if (this.updatedInfo != null) {
                this.stackMap.set(this.updatedInfo);
            }
        }

        @Override
        public void sameFrame(int n, int n2) {
            this.update(n, n2, 0, 251);
        }

        @Override
        public void sameLocals(int n, int n2, int n3, int n4) {
            this.update(n, n2, 64, 247);
        }

        void update(int n, int n2, int n3, int n4) {
            boolean bl;
            int n5;
            this.position = n5 + n2 + ((n5 = this.position) == 0 ? 0 : 1);
            if (this.exclusive) {
                bl = n5 == 0 && this.where == 0 || n5 < this.where && this.where <= this.position;
            } else {
                boolean bl2 = bl = n5 <= this.where && this.where < this.position;
            }
            if (bl) {
                int n6 = this.info[n] & 0xFF;
                int n7 = n2 + this.gap;
                this.position += this.gap;
                if (n7 < 64) {
                    this.info[n] = (byte)(n7 + n3);
                } else if (n2 < 64 && n6 != n4) {
                    byte[] byArray = Shifter.insertGap(this.info, n, 2);
                    byArray[n] = (byte)n4;
                    ByteArray.write16bit(n7, byArray, n + 1);
                    this.updatedInfo = byArray;
                } else {
                    ByteArray.write16bit(n7, this.info, n + 1);
                }
            }
        }

        static byte[] insertGap(byte[] byArray, int n, int n2) {
            int n3 = byArray.length;
            byte[] byArray2 = new byte[n3 + n2];
            if (n <= 0) {
                System.arraycopy(byArray, 0, byArray2, n2, n3);
            } else if (n >= n3) {
                System.arraycopy(byArray, 0, byArray2, 0, n3);
            } else {
                assert (n > 0 && n < n3);
                System.arraycopy(byArray, 0, byArray2, 0, n);
                System.arraycopy(byArray, n, byArray2, n + n2, n3 - n);
            }
            return byArray2;
        }

        @Override
        public void chopFrame(int n, int n2, int n3) {
            this.update(n, n2);
        }

        @Override
        public void appendFrame(int n, int n2, int[] nArray, int[] nArray2) {
            this.update(n, n2);
        }

        @Override
        public void fullFrame(int n, int n2, int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
            this.update(n, n2);
        }

        void update(int n, int n2) {
            boolean bl;
            int n3;
            this.position = n3 + n2 + ((n3 = this.position) == 0 ? 0 : 1);
            if (this.exclusive) {
                bl = n3 == 0 && this.where == 0 || n3 < this.where && this.where <= this.position;
            } else {
                boolean bl2 = bl = n3 <= this.where && this.where < this.position;
            }
            if (bl) {
                int n4 = n2 + this.gap;
                ByteArray.write16bit(n4, this.info, n + 1);
                this.position += this.gap;
            }
        }
    }

    static class SwitchShifter
    extends Shifter {
        SwitchShifter(StackMapTable stackMapTable, int n, int n2) {
            super(stackMapTable, n, n2, false);
        }

        @Override
        void update(int n, int n2, int n3, int n4) {
            int n5;
            this.position = n5 + n2 + ((n5 = this.position) == 0 ? 0 : 1);
            int n6 = n2;
            if (this.where == this.position) {
                n6 = n2 - this.gap;
            } else if (this.where == n5) {
                n6 = n2 + this.gap;
            } else {
                return;
            }
            if (n2 < 64) {
                if (n6 < 64) {
                    this.info[n] = (byte)(n6 + n3);
                } else {
                    byte[] byArray = SwitchShifter.insertGap(this.info, n, 2);
                    byArray[n] = (byte)n4;
                    ByteArray.write16bit(n6, byArray, n + 1);
                    this.updatedInfo = byArray;
                }
            } else if (n6 < 64) {
                byte[] byArray = SwitchShifter.deleteGap(this.info, n, 2);
                byArray[n] = (byte)(n6 + n3);
                this.updatedInfo = byArray;
            } else {
                ByteArray.write16bit(n6, this.info, n + 1);
            }
        }

        static byte[] deleteGap(byte[] byArray, int n, int n2) {
            n += n2;
            int n3 = byArray.length;
            byte[] byArray2 = new byte[n3 - n2];
            for (int i = 0; i < n3; ++i) {
                byArray2[i - (i < n ? 0 : n2)] = byArray[i];
            }
            return byArray2;
        }

        @Override
        void update(int n, int n2) {
            int n3;
            this.position = n3 + n2 + ((n3 = this.position) == 0 ? 0 : 1);
            int n4 = n2;
            if (this.where == this.position) {
                n4 = n2 - this.gap;
            } else if (this.where == n3) {
                n4 = n2 + this.gap;
            } else {
                return;
            }
            ByteArray.write16bit(n4, this.info, n + 1);
        }
    }

    static class NewRemover
    extends SimpleCopy {
        int posOfNew;

        public NewRemover(byte[] byArray, int n) {
            super(byArray);
            this.posOfNew = n;
        }

        @Override
        public void sameLocals(int n, int n2, int n3, int n4) {
            if (n3 == 8 && n4 == this.posOfNew) {
                super.sameFrame(n, n2);
            } else {
                super.sameLocals(n, n2, n3, n4);
            }
        }

        @Override
        public void fullFrame(int n, int n2, int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
            int n3 = nArray3.length - 1;
            for (int i = 0; i < n3; ++i) {
                if (nArray3[i] != 8 || nArray4[i] != this.posOfNew || nArray3[i + 1] != 8 || nArray4[i + 1] != this.posOfNew) continue;
                int[] nArray5 = new int[++n3 - 2];
                int[] nArray6 = new int[n3 - 2];
                int n4 = 0;
                for (int j = 0; j < n3; ++j) {
                    if (j == i) {
                        ++j;
                        continue;
                    }
                    nArray5[n4] = nArray3[j];
                    nArray6[n4++] = nArray4[j];
                }
                nArray3 = nArray5;
                nArray4 = nArray6;
                break;
            }
            super.fullFrame(n, n2, nArray, nArray2, nArray3, nArray4);
        }
    }

    public static class Writer {
        ByteArrayOutputStream output;
        int numOfEntries;

        public Writer(int n) {
            this.output = new ByteArrayOutputStream(n);
            this.numOfEntries = 0;
            this.output.write(0);
            this.output.write(0);
        }

        public byte[] toByteArray() {
            byte[] byArray = this.output.toByteArray();
            ByteArray.write16bit(this.numOfEntries, byArray, 0);
            return byArray;
        }

        public StackMapTable toStackMapTable(ConstPool constPool) {
            return new StackMapTable(constPool, this.toByteArray());
        }

        public void sameFrame(int n) {
            ++this.numOfEntries;
            if (n < 64) {
                this.output.write(n);
            } else {
                this.output.write(251);
                this.write16(n);
            }
        }

        public void sameLocals(int n, int n2, int n3) {
            ++this.numOfEntries;
            if (n < 64) {
                this.output.write(n + 64);
            } else {
                this.output.write(247);
                this.write16(n);
            }
            this.writeTypeInfo(n2, n3);
        }

        public void chopFrame(int n, int n2) {
            ++this.numOfEntries;
            this.output.write(251 - n2);
            this.write16(n);
        }

        public void appendFrame(int n, int[] nArray, int[] nArray2) {
            ++this.numOfEntries;
            int n2 = nArray.length;
            this.output.write(n2 + 251);
            this.write16(n);
            for (int i = 0; i < n2; ++i) {
                this.writeTypeInfo(nArray[i], nArray2[i]);
            }
        }

        public void fullFrame(int n, int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
            int n2;
            ++this.numOfEntries;
            this.output.write(255);
            this.write16(n);
            int n3 = nArray.length;
            this.write16(n3);
            for (n2 = 0; n2 < n3; ++n2) {
                this.writeTypeInfo(nArray[n2], nArray2[n2]);
            }
            n3 = nArray3.length;
            this.write16(n3);
            for (n2 = 0; n2 < n3; ++n2) {
                this.writeTypeInfo(nArray3[n2], nArray4[n2]);
            }
        }

        private void writeTypeInfo(int n, int n2) {
            this.output.write(n);
            if (n == 7 || n == 8) {
                this.write16(n2);
            }
        }

        private void write16(int n) {
            this.output.write(n >>> 8 & 0xFF);
            this.output.write(n & 0xFF);
        }
    }

    static class SimpleCopy
    extends Walker {
        private Writer writer;

        public SimpleCopy(byte[] byArray) {
            super(byArray);
            this.writer = new Writer(byArray.length);
        }

        public byte[] doit() {
            this.parse();
            return this.writer.toByteArray();
        }

        @Override
        public void sameFrame(int n, int n2) {
            this.writer.sameFrame(n2);
        }

        @Override
        public void sameLocals(int n, int n2, int n3, int n4) {
            this.writer.sameLocals(n2, n3, this.copyData(n3, n4));
        }

        @Override
        public void chopFrame(int n, int n2, int n3) {
            this.writer.chopFrame(n2, n3);
        }

        @Override
        public void appendFrame(int n, int n2, int[] nArray, int[] nArray2) {
            this.writer.appendFrame(n2, nArray, this.copyData(nArray, nArray2));
        }

        @Override
        public void fullFrame(int n, int n2, int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
            this.writer.fullFrame(n2, nArray, this.copyData(nArray, nArray2), nArray3, this.copyData(nArray3, nArray4));
        }

        protected int copyData(int n, int n2) {
            return n2;
        }

        protected int[] copyData(int[] nArray, int[] nArray2) {
            return nArray2;
        }
    }

    public static class Walker {
        byte[] info;
        int numOfEntries;

        public Walker(StackMapTable stackMapTable) {
            this(stackMapTable.get());
        }

        public Walker(byte[] byArray) {
            this.info = byArray;
            this.numOfEntries = ByteArray.readU16bit(byArray, 0);
        }

        public final int size() {
            return this.numOfEntries;
        }

        public void parse() {
            int n = this.numOfEntries;
            int n2 = 2;
            for (int i = 0; i < n; ++i) {
                n2 = this.stackMapFrames(n2, i);
            }
        }

        int stackMapFrames(int n, int n2) {
            int n3 = this.info[n] & 0xFF;
            if (n3 < 64) {
                this.sameFrame(n, n3);
                ++n;
            } else if (n3 < 128) {
                n = this.sameLocals(n, n3);
            } else {
                if (n3 < 247) {
                    throw new BadBytecode("bad frame_type " + n3 + " in StackMapTable (pos: " + n + ", frame no.:" + n2 + ")");
                }
                if (n3 == 247) {
                    n = this.sameLocals(n, n3);
                } else if (n3 < 251) {
                    int n4 = ByteArray.readU16bit(this.info, n + 1);
                    this.chopFrame(n, n4, 251 - n3);
                    n += 3;
                } else if (n3 == 251) {
                    int n5 = ByteArray.readU16bit(this.info, n + 1);
                    this.sameFrame(n, n5);
                    n += 3;
                } else {
                    n = n3 < 255 ? this.appendFrame(n, n3) : this.fullFrame(n);
                }
            }
            return n;
        }

        public void sameFrame(int n, int n2) {
        }

        private int sameLocals(int n, int n2) {
            int n3;
            int n4 = n;
            if (n2 < 128) {
                n3 = n2 - 64;
            } else {
                n3 = ByteArray.readU16bit(this.info, n + 1);
                n += 2;
            }
            int n5 = this.info[n + 1] & 0xFF;
            int n6 = 0;
            if (n5 == 7 || n5 == 8) {
                n6 = ByteArray.readU16bit(this.info, n + 2);
                this.objectOrUninitialized(n5, n6, n + 2);
                n += 2;
            }
            this.sameLocals(n4, n3, n5, n6);
            return n + 2;
        }

        public void sameLocals(int n, int n2, int n3, int n4) {
        }

        public void chopFrame(int n, int n2, int n3) {
        }

        private int appendFrame(int n, int n2) {
            int n3 = n2 - 251;
            int n4 = ByteArray.readU16bit(this.info, n + 1);
            int[] nArray = new int[n3];
            int[] nArray2 = new int[n3];
            int n5 = n + 3;
            for (int i = 0; i < n3; ++i) {
                int n6;
                nArray[i] = n6 = this.info[n5] & 0xFF;
                if (n6 == 7 || n6 == 8) {
                    nArray2[i] = ByteArray.readU16bit(this.info, n5 + 1);
                    this.objectOrUninitialized(n6, nArray2[i], n5 + 1);
                    n5 += 3;
                    continue;
                }
                nArray2[i] = 0;
                ++n5;
            }
            this.appendFrame(n, n4, nArray, nArray2);
            return n5;
        }

        public void appendFrame(int n, int n2, int[] nArray, int[] nArray2) {
        }

        private int fullFrame(int n) {
            int n2 = ByteArray.readU16bit(this.info, n + 1);
            int n3 = ByteArray.readU16bit(this.info, n + 3);
            int[] nArray = new int[n3];
            int[] nArray2 = new int[n3];
            int n4 = this.verifyTypeInfo(n + 5, n3, nArray, nArray2);
            int n5 = ByteArray.readU16bit(this.info, n4);
            int[] nArray3 = new int[n5];
            int[] nArray4 = new int[n5];
            n4 = this.verifyTypeInfo(n4 + 2, n5, nArray3, nArray4);
            this.fullFrame(n, n2, nArray, nArray2, nArray3, nArray4);
            return n4;
        }

        public void fullFrame(int n, int n2, int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
        }

        private int verifyTypeInfo(int n, int n2, int[] nArray, int[] nArray2) {
            for (int i = 0; i < n2; ++i) {
                int n3;
                nArray[i] = n3 = this.info[n++] & 0xFF;
                if (n3 != 7 && n3 != 8) continue;
                nArray2[i] = ByteArray.readU16bit(this.info, n);
                this.objectOrUninitialized(n3, nArray2[i], n);
                n += 2;
            }
            return n;
        }

        public void objectOrUninitialized(int n, int n2, int n3) {
        }
    }
}

