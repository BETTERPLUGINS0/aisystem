/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AnnotationDefaultAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AnnotationsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BootstrapMethodsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstantAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.DeprecatedAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.EnclosingMethodAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.InnerClassesAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.LineNumberAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.LocalVariableAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.LocalVariableTypeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodParametersAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.NestHostAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.NestMembersAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ParameterAnnotationsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.SignatureAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.SourceFileAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMapTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.SyntheticAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.TypeAnnotationsAttribute;

public class AttributeInfo {
    protected ConstPool constPool;
    int name;
    byte[] info;

    protected AttributeInfo(ConstPool constPool, int n, byte[] byArray) {
        this.constPool = constPool;
        this.name = n;
        this.info = byArray;
    }

    protected AttributeInfo(ConstPool constPool, String string) {
        this(constPool, string, (byte[])null);
    }

    public AttributeInfo(ConstPool constPool, String string, byte[] byArray) {
        this(constPool, constPool.addUtf8Info(string), byArray);
    }

    protected AttributeInfo(ConstPool constPool, int n, DataInputStream dataInputStream) {
        this.constPool = constPool;
        this.name = n;
        int n2 = dataInputStream.readInt();
        this.info = new byte[n2];
        if (n2 > 0) {
            dataInputStream.readFully(this.info);
        }
    }

    static AttributeInfo read(ConstPool constPool, DataInputStream dataInputStream) {
        int n = dataInputStream.readUnsignedShort();
        String string = constPool.getUtf8Info(n);
        char c = string.charAt(0);
        if (c < 'E') {
            if (string.equals("AnnotationDefault")) {
                return new AnnotationDefaultAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("BootstrapMethods")) {
                return new BootstrapMethodsAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("Code")) {
                return new CodeAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("ConstantValue")) {
                return new ConstantAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("Deprecated")) {
                return new DeprecatedAttribute(constPool, n, dataInputStream);
            }
        }
        if (c < 'M') {
            if (string.equals("EnclosingMethod")) {
                return new EnclosingMethodAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("Exceptions")) {
                return new ExceptionsAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("InnerClasses")) {
                return new InnerClassesAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("LineNumberTable")) {
                return new LineNumberAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("LocalVariableTable")) {
                return new LocalVariableAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("LocalVariableTypeTable")) {
                return new LocalVariableTypeAttribute(constPool, n, dataInputStream);
            }
        }
        if (c < 'S') {
            if (string.equals("MethodParameters")) {
                return new MethodParametersAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("NestHost")) {
                return new NestHostAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("NestMembers")) {
                return new NestMembersAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("RuntimeVisibleAnnotations") || string.equals("RuntimeInvisibleAnnotations")) {
                return new AnnotationsAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("RuntimeVisibleParameterAnnotations") || string.equals("RuntimeInvisibleParameterAnnotations")) {
                return new ParameterAnnotationsAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("RuntimeVisibleTypeAnnotations") || string.equals("RuntimeInvisibleTypeAnnotations")) {
                return new TypeAnnotationsAttribute(constPool, n, dataInputStream);
            }
        }
        if (c >= 'S') {
            if (string.equals("Signature")) {
                return new SignatureAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("SourceFile")) {
                return new SourceFileAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("Synthetic")) {
                return new SyntheticAttribute(constPool, n, dataInputStream);
            }
            if (string.equals("StackMap")) {
                return new StackMap(constPool, n, dataInputStream);
            }
            if (string.equals("StackMapTable")) {
                return new StackMapTable(constPool, n, dataInputStream);
            }
        }
        return new AttributeInfo(constPool, n, dataInputStream);
    }

    public String getName() {
        return this.constPool.getUtf8Info(this.name);
    }

    public ConstPool getConstPool() {
        return this.constPool;
    }

    public int length() {
        return this.info.length + 6;
    }

    public byte[] get() {
        return this.info;
    }

    public void set(byte[] byArray) {
        this.info = byArray;
    }

    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        return new AttributeInfo(constPool, this.getName(), Arrays.copyOf(this.info, this.info.length));
    }

    void write(DataOutputStream dataOutputStream) {
        dataOutputStream.writeShort(this.name);
        dataOutputStream.writeInt(this.info.length);
        if (this.info.length > 0) {
            dataOutputStream.write(this.info);
        }
    }

    static int getLength(List<AttributeInfo> list) {
        int n = 0;
        for (AttributeInfo attributeInfo : list) {
            n += attributeInfo.length();
        }
        return n;
    }

    static AttributeInfo lookup(List<AttributeInfo> list, String string) {
        if (list == null) {
            return null;
        }
        for (AttributeInfo attributeInfo : list) {
            if (!attributeInfo.getName().equals(string)) continue;
            return attributeInfo;
        }
        return null;
    }

    static synchronized AttributeInfo remove(List<AttributeInfo> list, String string) {
        if (list == null) {
            return null;
        }
        for (AttributeInfo attributeInfo : list) {
            if (!attributeInfo.getName().equals(string) || !list.remove(attributeInfo)) continue;
            return attributeInfo;
        }
        return null;
    }

    static void writeAll(List<AttributeInfo> list, DataOutputStream dataOutputStream) {
        if (list == null) {
            return;
        }
        for (AttributeInfo attributeInfo : list) {
            attributeInfo.write(dataOutputStream);
        }
    }

    static List<AttributeInfo> copyAll(List<AttributeInfo> list, ConstPool constPool) {
        if (list == null) {
            return null;
        }
        ArrayList<AttributeInfo> arrayList = new ArrayList<AttributeInfo>();
        for (AttributeInfo attributeInfo : list) {
            arrayList.add(attributeInfo.copy(constPool, null));
        }
        return arrayList;
    }

    void renameClass(String string, String string2) {
    }

    void renameClass(Map<String, String> map) {
    }

    static void renameClass(List<AttributeInfo> list, String string, String string2) {
        if (list == null) {
            return;
        }
        for (AttributeInfo attributeInfo : list) {
            attributeInfo.renameClass(string, string2);
        }
    }

    static void renameClass(List<AttributeInfo> list, Map<String, String> map) {
        if (list == null) {
            return;
        }
        for (AttributeInfo attributeInfo : list) {
            attributeInfo.renameClass(map);
        }
    }

    void getRefClasses(Map<String, String> map) {
    }

    static void getRefClasses(List<AttributeInfo> list, Map<String, String> map) {
        if (list == null) {
            return;
        }
        for (AttributeInfo attributeInfo : list) {
            attributeInfo.getRefClasses(map);
        }
    }
}

