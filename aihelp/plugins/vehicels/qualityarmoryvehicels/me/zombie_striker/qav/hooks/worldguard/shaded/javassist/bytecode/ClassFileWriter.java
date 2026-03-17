/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteStream;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMapTable;

public class ClassFileWriter {
    private ByteStream output = new ByteStream(512);
    private ConstPoolWriter constPool;
    private FieldWriter fields;
    private MethodWriter methods;
    int thisClass;
    int superClass;

    public ClassFileWriter(int n, int n2) {
        this.output.writeInt(-889275714);
        this.output.writeShort(n2);
        this.output.writeShort(n);
        this.constPool = new ConstPoolWriter(this.output);
        this.fields = new FieldWriter(this.constPool);
        this.methods = new MethodWriter(this.constPool);
    }

    public ConstPoolWriter getConstPool() {
        return this.constPool;
    }

    public FieldWriter getFieldWriter() {
        return this.fields;
    }

    public MethodWriter getMethodWriter() {
        return this.methods;
    }

    public byte[] end(int n, int n2, int n3, int[] nArray, AttributeWriter attributeWriter) {
        this.constPool.end();
        this.output.writeShort(n);
        this.output.writeShort(n2);
        this.output.writeShort(n3);
        if (nArray == null) {
            this.output.writeShort(0);
        } else {
            int n4 = nArray.length;
            this.output.writeShort(n4);
            for (int i = 0; i < n4; ++i) {
                this.output.writeShort(nArray[i]);
            }
        }
        this.output.enlarge(this.fields.dataSize() + this.methods.dataSize() + 6);
        try {
            this.output.writeShort(this.fields.size());
            this.fields.write(this.output);
            this.output.writeShort(this.methods.numOfMethods());
            this.methods.write(this.output);
        } catch (IOException iOException) {
            // empty catch block
        }
        ClassFileWriter.writeAttribute(this.output, attributeWriter, 0);
        return this.output.toByteArray();
    }

    public void end(DataOutputStream dataOutputStream, int n, int n2, int n3, int[] nArray, AttributeWriter attributeWriter) {
        this.constPool.end();
        this.output.writeTo(dataOutputStream);
        dataOutputStream.writeShort(n);
        dataOutputStream.writeShort(n2);
        dataOutputStream.writeShort(n3);
        if (nArray == null) {
            dataOutputStream.writeShort(0);
        } else {
            int n4 = nArray.length;
            dataOutputStream.writeShort(n4);
            for (int i = 0; i < n4; ++i) {
                dataOutputStream.writeShort(nArray[i]);
            }
        }
        dataOutputStream.writeShort(this.fields.size());
        this.fields.write(dataOutputStream);
        dataOutputStream.writeShort(this.methods.numOfMethods());
        this.methods.write(dataOutputStream);
        if (attributeWriter == null) {
            dataOutputStream.writeShort(0);
        } else {
            dataOutputStream.writeShort(attributeWriter.size());
            attributeWriter.write(dataOutputStream);
        }
    }

    static void writeAttribute(ByteStream byteStream, AttributeWriter attributeWriter, int n) {
        if (attributeWriter == null) {
            byteStream.writeShort(n);
            return;
        }
        byteStream.writeShort(attributeWriter.size() + n);
        DataOutputStream dataOutputStream = new DataOutputStream(byteStream);
        try {
            attributeWriter.write(dataOutputStream);
            dataOutputStream.flush();
        } catch (IOException iOException) {
            // empty catch block
        }
    }

    public static final class ConstPoolWriter {
        ByteStream output;
        protected int startPos;
        protected int num;

        ConstPoolWriter(ByteStream byteStream) {
            this.output = byteStream;
            this.startPos = byteStream.getPos();
            this.num = 1;
            this.output.writeShort(1);
        }

        public int[] addClassInfo(String[] stringArray) {
            int n = stringArray.length;
            int[] nArray = new int[n];
            for (int i = 0; i < n; ++i) {
                nArray[i] = this.addClassInfo(stringArray[i]);
            }
            return nArray;
        }

        public int addClassInfo(String string) {
            int n = this.addUtf8Info(string);
            this.output.write(7);
            this.output.writeShort(n);
            return this.num++;
        }

        public int addClassInfo(int n) {
            this.output.write(7);
            this.output.writeShort(n);
            return this.num++;
        }

        public int addNameAndTypeInfo(String string, String string2) {
            return this.addNameAndTypeInfo(this.addUtf8Info(string), this.addUtf8Info(string2));
        }

        public int addNameAndTypeInfo(int n, int n2) {
            this.output.write(12);
            this.output.writeShort(n);
            this.output.writeShort(n2);
            return this.num++;
        }

        public int addFieldrefInfo(int n, int n2) {
            this.output.write(9);
            this.output.writeShort(n);
            this.output.writeShort(n2);
            return this.num++;
        }

        public int addMethodrefInfo(int n, int n2) {
            this.output.write(10);
            this.output.writeShort(n);
            this.output.writeShort(n2);
            return this.num++;
        }

        public int addInterfaceMethodrefInfo(int n, int n2) {
            this.output.write(11);
            this.output.writeShort(n);
            this.output.writeShort(n2);
            return this.num++;
        }

        public int addMethodHandleInfo(int n, int n2) {
            this.output.write(15);
            this.output.write(n);
            this.output.writeShort(n2);
            return this.num++;
        }

        public int addMethodTypeInfo(int n) {
            this.output.write(16);
            this.output.writeShort(n);
            return this.num++;
        }

        public int addInvokeDynamicInfo(int n, int n2) {
            this.output.write(18);
            this.output.writeShort(n);
            this.output.writeShort(n2);
            return this.num++;
        }

        public int addDynamicInfo(int n, int n2) {
            this.output.write(17);
            this.output.writeShort(n);
            this.output.writeShort(n2);
            return this.num++;
        }

        public int addStringInfo(String string) {
            int n = this.addUtf8Info(string);
            this.output.write(8);
            this.output.writeShort(n);
            return this.num++;
        }

        public int addIntegerInfo(int n) {
            this.output.write(3);
            this.output.writeInt(n);
            return this.num++;
        }

        public int addFloatInfo(float f) {
            this.output.write(4);
            this.output.writeFloat(f);
            return this.num++;
        }

        public int addLongInfo(long l) {
            this.output.write(5);
            this.output.writeLong(l);
            int n = this.num;
            this.num += 2;
            return n;
        }

        public int addDoubleInfo(double d) {
            this.output.write(6);
            this.output.writeDouble(d);
            int n = this.num;
            this.num += 2;
            return n;
        }

        public int addUtf8Info(String string) {
            this.output.write(1);
            this.output.writeUTF(string);
            return this.num++;
        }

        void end() {
            this.output.writeShort(this.startPos, this.num);
        }
    }

    public static final class FieldWriter {
        protected ByteStream output = new ByteStream(128);
        protected ConstPoolWriter constPool;
        private int fieldCount;

        FieldWriter(ConstPoolWriter constPoolWriter) {
            this.constPool = constPoolWriter;
            this.fieldCount = 0;
        }

        public void add(int n, String string, String string2, AttributeWriter attributeWriter) {
            int n2 = this.constPool.addUtf8Info(string);
            int n3 = this.constPool.addUtf8Info(string2);
            this.add(n, n2, n3, attributeWriter);
        }

        public void add(int n, int n2, int n3, AttributeWriter attributeWriter) {
            ++this.fieldCount;
            this.output.writeShort(n);
            this.output.writeShort(n2);
            this.output.writeShort(n3);
            ClassFileWriter.writeAttribute(this.output, attributeWriter, 0);
        }

        int size() {
            return this.fieldCount;
        }

        int dataSize() {
            return this.output.size();
        }

        void write(OutputStream outputStream) {
            this.output.writeTo(outputStream);
        }
    }

    public static final class MethodWriter {
        protected ByteStream output = new ByteStream(256);
        protected ConstPoolWriter constPool;
        private int methodCount;
        protected int codeIndex;
        protected int throwsIndex;
        protected int stackIndex;
        private int startPos;
        private boolean isAbstract;
        private int catchPos;
        private int catchCount;

        MethodWriter(ConstPoolWriter constPoolWriter) {
            this.constPool = constPoolWriter;
            this.methodCount = 0;
            this.codeIndex = 0;
            this.throwsIndex = 0;
            this.stackIndex = 0;
        }

        public void begin(int n, String string, String string2, String[] stringArray, AttributeWriter attributeWriter) {
            int n2 = this.constPool.addUtf8Info(string);
            int n3 = this.constPool.addUtf8Info(string2);
            int[] nArray = stringArray == null ? null : this.constPool.addClassInfo(stringArray);
            this.begin(n, n2, n3, nArray, attributeWriter);
        }

        public void begin(int n, int n2, int n3, int[] nArray, AttributeWriter attributeWriter) {
            int n4;
            ++this.methodCount;
            this.output.writeShort(n);
            this.output.writeShort(n2);
            this.output.writeShort(n3);
            this.isAbstract = (n & 0x400) != 0;
            int n5 = n4 = this.isAbstract ? 0 : 1;
            if (nArray != null) {
                ++n4;
            }
            ClassFileWriter.writeAttribute(this.output, attributeWriter, n4);
            if (nArray != null) {
                this.writeThrows(nArray);
            }
            if (!this.isAbstract) {
                if (this.codeIndex == 0) {
                    this.codeIndex = this.constPool.addUtf8Info("Code");
                }
                this.startPos = this.output.getPos();
                this.output.writeShort(this.codeIndex);
                this.output.writeBlank(12);
            }
            this.catchPos = -1;
            this.catchCount = 0;
        }

        private void writeThrows(int[] nArray) {
            if (this.throwsIndex == 0) {
                this.throwsIndex = this.constPool.addUtf8Info("Exceptions");
            }
            this.output.writeShort(this.throwsIndex);
            this.output.writeInt(nArray.length * 2 + 2);
            this.output.writeShort(nArray.length);
            for (int i = 0; i < nArray.length; ++i) {
                this.output.writeShort(nArray[i]);
            }
        }

        public void add(int n) {
            this.output.write(n);
        }

        public void add16(int n) {
            this.output.writeShort(n);
        }

        public void add32(int n) {
            this.output.writeInt(n);
        }

        public void addInvoke(int n, String string, String string2, String string3) {
            int n2 = this.constPool.addClassInfo(string);
            int n3 = this.constPool.addNameAndTypeInfo(string2, string3);
            int n4 = this.constPool.addMethodrefInfo(n2, n3);
            this.add(n);
            this.add16(n4);
        }

        public void codeEnd(int n, int n2) {
            if (!this.isAbstract) {
                this.output.writeShort(this.startPos + 6, n);
                this.output.writeShort(this.startPos + 8, n2);
                this.output.writeInt(this.startPos + 10, this.output.getPos() - this.startPos - 14);
                this.catchPos = this.output.getPos();
                this.catchCount = 0;
                this.output.writeShort(0);
            }
        }

        public void addCatch(int n, int n2, int n3, int n4) {
            ++this.catchCount;
            this.output.writeShort(n);
            this.output.writeShort(n2);
            this.output.writeShort(n3);
            this.output.writeShort(n4);
        }

        public void end(StackMapTable.Writer writer, AttributeWriter attributeWriter) {
            if (this.isAbstract) {
                return;
            }
            this.output.writeShort(this.catchPos, this.catchCount);
            int n = writer == null ? 0 : 1;
            ClassFileWriter.writeAttribute(this.output, attributeWriter, n);
            if (writer != null) {
                if (this.stackIndex == 0) {
                    this.stackIndex = this.constPool.addUtf8Info("StackMapTable");
                }
                this.output.writeShort(this.stackIndex);
                byte[] byArray = writer.toByteArray();
                this.output.writeInt(byArray.length);
                this.output.write(byArray);
            }
            this.output.writeInt(this.startPos + 2, this.output.getPos() - this.startPos - 6);
        }

        public int size() {
            return this.output.getPos() - this.startPos - 14;
        }

        int numOfMethods() {
            return this.methodCount;
        }

        int dataSize() {
            return this.output.size();
        }

        void write(OutputStream outputStream) {
            this.output.writeTo(outputStream);
        }
    }

    public static interface AttributeWriter {
        public int size();

        public void write(DataOutputStream var1) throws IOException;
    }
}

