/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.PrintWriter;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;

public class StackMap
extends AttributeInfo {
    public static final String tag = "StackMap";
    public static final int TOP = 0;
    public static final int INTEGER = 1;
    public static final int FLOAT = 2;
    public static final int DOUBLE = 3;
    public static final int LONG = 4;
    public static final int NULL = 5;
    public static final int THIS = 6;
    public static final int OBJECT = 7;
    public static final int UNINIT = 8;

    StackMap(ConstPool constPool, byte[] byArray) {
        super(constPool, tag, byArray);
    }

    StackMap(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    public int numOfEntries() {
        return ByteArray.readU16bit(this.info, 0);
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        Copier copier = new Copier(this, constPool, map);
        copier.visit();
        return copier.getStackMap();
    }

    public void insertLocal(int n, int n2, int n3) {
        byte[] byArray = new InsertLocal(this, n, n2, n3).doit();
        this.set(byArray);
    }

    void shiftPc(int n, int n2, boolean bl) {
        new Shifter(this, n, n2, bl).visit();
    }

    void shiftForSwitch(int n, int n2) {
        new SwitchShifter(this, n, n2).visit();
    }

    public void removeNew(int n) {
        byte[] byArray = new NewRemover(this, n).doit();
        this.set(byArray);
    }

    public void print(PrintWriter printWriter) {
        new Printer(this, printWriter).print();
    }

    static class Copier
    extends Walker {
        byte[] dest;
        ConstPool srcCp;
        ConstPool destCp;
        Map<String, String> classnames;

        Copier(StackMap stackMap, ConstPool constPool, Map<String, String> map) {
            super(stackMap);
            this.srcCp = stackMap.getConstPool();
            this.dest = new byte[this.info.length];
            this.destCp = constPool;
            this.classnames = map;
        }

        @Override
        public void visit() {
            int n = ByteArray.readU16bit(this.info, 0);
            ByteArray.write16bit(n, this.dest, 0);
            super.visit();
        }

        @Override
        public int locals(int n, int n2, int n3) {
            ByteArray.write16bit(n2, this.dest, n - 4);
            return super.locals(n, n2, n3);
        }

        @Override
        public int typeInfoArray(int n, int n2, int n3, boolean bl) {
            ByteArray.write16bit(n3, this.dest, n - 2);
            return super.typeInfoArray(n, n2, n3, bl);
        }

        @Override
        public void typeInfo(int n, byte by) {
            this.dest[n] = by;
        }

        @Override
        public void objectVariable(int n, int n2) {
            this.dest[n] = 7;
            int n3 = this.srcCp.copy(n2, this.destCp, this.classnames);
            ByteArray.write16bit(n3, this.dest, n + 1);
        }

        @Override
        public void uninitialized(int n, int n2) {
            this.dest[n] = 8;
            ByteArray.write16bit(n2, this.dest, n + 1);
        }

        public StackMap getStackMap() {
            return new StackMap(this.destCp, this.dest);
        }
    }

    static class InsertLocal
    extends SimpleCopy {
        private int varIndex;
        private int varTag;
        private int varData;

        InsertLocal(StackMap stackMap, int n, int n2, int n3) {
            super(stackMap);
            this.varIndex = n;
            this.varTag = n2;
            this.varData = n3;
        }

        @Override
        public int typeInfoArray(int n, int n2, int n3, boolean bl) {
            if (!bl || n3 < this.varIndex) {
                return super.typeInfoArray(n, n2, n3, bl);
            }
            this.writer.write16bit(n3 + 1);
            for (int i = 0; i < n3; ++i) {
                if (i == this.varIndex) {
                    this.writeVarTypeInfo();
                }
                n = this.typeInfoArray2(i, n);
            }
            if (n3 == this.varIndex) {
                this.writeVarTypeInfo();
            }
            return n;
        }

        private void writeVarTypeInfo() {
            if (this.varTag == 7) {
                this.writer.writeVerifyTypeInfo(7, this.varData);
            } else if (this.varTag == 8) {
                this.writer.writeVerifyTypeInfo(8, this.varData);
            } else {
                this.writer.writeVerifyTypeInfo(this.varTag, 0);
            }
        }
    }

    static class Shifter
    extends Walker {
        private int where;
        private int gap;
        private boolean exclusive;

        public Shifter(StackMap stackMap, int n, int n2, boolean bl) {
            super(stackMap);
            this.where = n;
            this.gap = n2;
            this.exclusive = bl;
        }

        @Override
        public int locals(int n, int n2, int n3) {
            if (this.exclusive ? this.where <= n2 : this.where < n2) {
                ByteArray.write16bit(n2 + this.gap, this.info, n - 4);
            }
            return super.locals(n, n2, n3);
        }

        @Override
        public void uninitialized(int n, int n2) {
            if (this.where <= n2) {
                ByteArray.write16bit(n2 + this.gap, this.info, n + 1);
            }
        }
    }

    static class SwitchShifter
    extends Walker {
        private int where;
        private int gap;

        public SwitchShifter(StackMap stackMap, int n, int n2) {
            super(stackMap);
            this.where = n;
            this.gap = n2;
        }

        @Override
        public int locals(int n, int n2, int n3) {
            if (this.where == n + n2) {
                ByteArray.write16bit(n2 - this.gap, this.info, n - 4);
            } else if (this.where == n) {
                ByteArray.write16bit(n2 + this.gap, this.info, n - 4);
            }
            return super.locals(n, n2, n3);
        }
    }

    static class NewRemover
    extends SimpleCopy {
        int posOfNew;

        NewRemover(StackMap stackMap, int n) {
            super(stackMap);
            this.posOfNew = n;
        }

        @Override
        public int stack(int n, int n2, int n3) {
            return this.stackTypeInfoArray(n, n2, n3);
        }

        private int stackTypeInfoArray(int n, int n2, int n3) {
            int n4;
            byte by;
            int n5;
            int n6 = n;
            int n7 = 0;
            for (n5 = 0; n5 < n3; ++n5) {
                by = this.info[n6];
                if (by == 7) {
                    n6 += 3;
                    continue;
                }
                if (by == 8) {
                    n4 = ByteArray.readU16bit(this.info, n6 + 1);
                    if (n4 == this.posOfNew) {
                        ++n7;
                    }
                    n6 += 3;
                    continue;
                }
                ++n6;
            }
            this.writer.write16bit(n3 - n7);
            for (n5 = 0; n5 < n3; ++n5) {
                by = this.info[n];
                if (by == 7) {
                    n4 = ByteArray.readU16bit(this.info, n + 1);
                    this.objectVariable(n, n4);
                    n += 3;
                    continue;
                }
                if (by == 8) {
                    n4 = ByteArray.readU16bit(this.info, n + 1);
                    if (n4 != this.posOfNew) {
                        this.uninitialized(n, n4);
                    }
                    n += 3;
                    continue;
                }
                this.typeInfo(n, by);
                ++n;
            }
            return n;
        }
    }

    static class Printer
    extends Walker {
        private PrintWriter writer;

        public Printer(StackMap stackMap, PrintWriter printWriter) {
            super(stackMap);
            this.writer = printWriter;
        }

        public void print() {
            int n = ByteArray.readU16bit(this.info, 0);
            this.writer.println(n + " entries");
            this.visit();
        }

        @Override
        public int locals(int n, int n2, int n3) {
            this.writer.println("  * offset " + n2);
            return super.locals(n, n2, n3);
        }
    }

    public static class Writer {
        private ByteArrayOutputStream output = new ByteArrayOutputStream();

        public byte[] toByteArray() {
            return this.output.toByteArray();
        }

        public StackMap toStackMap(ConstPool constPool) {
            return new StackMap(constPool, this.output.toByteArray());
        }

        public void writeVerifyTypeInfo(int n, int n2) {
            this.output.write(n);
            if (n == 7 || n == 8) {
                this.write16bit(n2);
            }
        }

        public void write16bit(int n) {
            this.output.write(n >>> 8 & 0xFF);
            this.output.write(n & 0xFF);
        }
    }

    static class SimpleCopy
    extends Walker {
        Writer writer = new Writer();

        SimpleCopy(StackMap stackMap) {
            super(stackMap);
        }

        byte[] doit() {
            this.visit();
            return this.writer.toByteArray();
        }

        @Override
        public void visit() {
            int n = ByteArray.readU16bit(this.info, 0);
            this.writer.write16bit(n);
            super.visit();
        }

        @Override
        public int locals(int n, int n2, int n3) {
            this.writer.write16bit(n2);
            return super.locals(n, n2, n3);
        }

        @Override
        public int typeInfoArray(int n, int n2, int n3, boolean bl) {
            this.writer.write16bit(n3);
            return super.typeInfoArray(n, n2, n3, bl);
        }

        @Override
        public void typeInfo(int n, byte by) {
            this.writer.writeVerifyTypeInfo(by, 0);
        }

        @Override
        public void objectVariable(int n, int n2) {
            this.writer.writeVerifyTypeInfo(7, n2);
        }

        @Override
        public void uninitialized(int n, int n2) {
            this.writer.writeVerifyTypeInfo(8, n2);
        }
    }

    public static class Walker {
        byte[] info;

        public Walker(StackMap stackMap) {
            this.info = stackMap.get();
        }

        public void visit() {
            int n = ByteArray.readU16bit(this.info, 0);
            int n2 = 2;
            for (int i = 0; i < n; ++i) {
                int n3 = ByteArray.readU16bit(this.info, n2);
                int n4 = ByteArray.readU16bit(this.info, n2 + 2);
                n2 = this.locals(n2 + 4, n3, n4);
                int n5 = ByteArray.readU16bit(this.info, n2);
                n2 = this.stack(n2 + 2, n3, n5);
            }
        }

        public int locals(int n, int n2, int n3) {
            return this.typeInfoArray(n, n2, n3, true);
        }

        public int stack(int n, int n2, int n3) {
            return this.typeInfoArray(n, n2, n3, false);
        }

        public int typeInfoArray(int n, int n2, int n3, boolean bl) {
            for (int i = 0; i < n3; ++i) {
                n = this.typeInfoArray2(i, n);
            }
            return n;
        }

        int typeInfoArray2(int n, int n2) {
            byte by = this.info[n2];
            if (by == 7) {
                int n3 = ByteArray.readU16bit(this.info, n2 + 1);
                this.objectVariable(n2, n3);
                n2 += 3;
            } else if (by == 8) {
                int n4 = ByteArray.readU16bit(this.info, n2 + 1);
                this.uninitialized(n2, n4);
                n2 += 3;
            } else {
                this.typeInfo(n2, by);
                ++n2;
            }
            return n2;
        }

        public void typeInfo(int n, byte by) {
        }

        public void objectVariable(int n, int n2) {
        }

        public void uninitialized(int n, int n2) {
        }
    }
}

