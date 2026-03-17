/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AnnotationDefaultAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.LineNumberAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMapTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.stackmap.MapMaker;

public class MethodInfo {
    ConstPool constPool;
    int accessFlags;
    int name;
    String cachedName;
    int descriptor;
    List<AttributeInfo> attribute;
    public static boolean doPreverify = false;
    public static final String nameInit = "<init>";
    public static final String nameClinit = "<clinit>";

    private MethodInfo(ConstPool constPool) {
        this.constPool = constPool;
        this.attribute = null;
    }

    public MethodInfo(ConstPool constPool, String string, String string2) {
        this(constPool);
        this.accessFlags = 0;
        this.name = constPool.addUtf8Info(string);
        this.cachedName = string;
        this.descriptor = this.constPool.addUtf8Info(string2);
    }

    MethodInfo(ConstPool constPool, DataInputStream dataInputStream) {
        this(constPool);
        this.read(dataInputStream);
    }

    public MethodInfo(ConstPool constPool, String string, MethodInfo methodInfo, Map<String, String> map) {
        this(constPool);
        this.read(methodInfo, string, map);
    }

    public String toString() {
        return this.getName() + " " + this.getDescriptor();
    }

    void compact(ConstPool constPool) {
        this.name = constPool.addUtf8Info(this.getName());
        this.descriptor = constPool.addUtf8Info(this.getDescriptor());
        this.attribute = AttributeInfo.copyAll(this.attribute, constPool);
        this.constPool = constPool;
    }

    void prune(ConstPool constPool) {
        AttributeInfo attributeInfo;
        ExceptionsAttribute exceptionsAttribute;
        AnnotationDefaultAttribute annotationDefaultAttribute;
        AttributeInfo attributeInfo2;
        AttributeInfo attributeInfo3;
        AttributeInfo attributeInfo4;
        ArrayList<AttributeInfo> arrayList = new ArrayList<AttributeInfo>();
        AttributeInfo attributeInfo5 = this.getAttribute("RuntimeInvisibleAnnotations");
        if (attributeInfo5 != null) {
            attributeInfo5 = attributeInfo5.copy(constPool, null);
            arrayList.add(attributeInfo5);
        }
        if ((attributeInfo4 = this.getAttribute("RuntimeVisibleAnnotations")) != null) {
            attributeInfo4 = attributeInfo4.copy(constPool, null);
            arrayList.add(attributeInfo4);
        }
        if ((attributeInfo3 = this.getAttribute("RuntimeInvisibleParameterAnnotations")) != null) {
            attributeInfo3 = attributeInfo3.copy(constPool, null);
            arrayList.add(attributeInfo3);
        }
        if ((attributeInfo2 = this.getAttribute("RuntimeVisibleParameterAnnotations")) != null) {
            attributeInfo2 = attributeInfo2.copy(constPool, null);
            arrayList.add(attributeInfo2);
        }
        if ((annotationDefaultAttribute = (AnnotationDefaultAttribute)this.getAttribute("AnnotationDefault")) != null) {
            arrayList.add(annotationDefaultAttribute);
        }
        if ((exceptionsAttribute = this.getExceptionsAttribute()) != null) {
            arrayList.add(exceptionsAttribute);
        }
        if ((attributeInfo = this.getAttribute("Signature")) != null) {
            attributeInfo = attributeInfo.copy(constPool, null);
            arrayList.add(attributeInfo);
        }
        this.attribute = arrayList;
        this.name = constPool.addUtf8Info(this.getName());
        this.descriptor = constPool.addUtf8Info(this.getDescriptor());
        this.constPool = constPool;
    }

    public String getName() {
        if (this.cachedName == null) {
            this.cachedName = this.constPool.getUtf8Info(this.name);
        }
        return this.cachedName;
    }

    public void setName(String string) {
        this.name = this.constPool.addUtf8Info(string);
        this.cachedName = string;
    }

    public boolean isMethod() {
        String string = this.getName();
        return !string.equals(nameInit) && !string.equals(nameClinit);
    }

    public ConstPool getConstPool() {
        return this.constPool;
    }

    public boolean isConstructor() {
        return this.getName().equals(nameInit);
    }

    public boolean isStaticInitializer() {
        return this.getName().equals(nameClinit);
    }

    public int getAccessFlags() {
        return this.accessFlags;
    }

    public void setAccessFlags(int n) {
        this.accessFlags = n;
    }

    public String getDescriptor() {
        return this.constPool.getUtf8Info(this.descriptor);
    }

    public void setDescriptor(String string) {
        if (!string.equals(this.getDescriptor())) {
            this.descriptor = this.constPool.addUtf8Info(string);
        }
    }

    public List<AttributeInfo> getAttributes() {
        if (this.attribute == null) {
            this.attribute = new ArrayList<AttributeInfo>();
        }
        return this.attribute;
    }

    public AttributeInfo getAttribute(String string) {
        return AttributeInfo.lookup(this.attribute, string);
    }

    public AttributeInfo removeAttribute(String string) {
        return AttributeInfo.remove(this.attribute, string);
    }

    public void addAttribute(AttributeInfo attributeInfo) {
        if (this.attribute == null) {
            this.attribute = new ArrayList<AttributeInfo>();
        }
        AttributeInfo.remove(this.attribute, attributeInfo.getName());
        this.attribute.add(attributeInfo);
    }

    public ExceptionsAttribute getExceptionsAttribute() {
        AttributeInfo attributeInfo = AttributeInfo.lookup(this.attribute, "Exceptions");
        return (ExceptionsAttribute)attributeInfo;
    }

    public CodeAttribute getCodeAttribute() {
        AttributeInfo attributeInfo = AttributeInfo.lookup(this.attribute, "Code");
        return (CodeAttribute)attributeInfo;
    }

    public void removeExceptionsAttribute() {
        AttributeInfo.remove(this.attribute, "Exceptions");
    }

    public void setExceptionsAttribute(ExceptionsAttribute exceptionsAttribute) {
        this.removeExceptionsAttribute();
        if (this.attribute == null) {
            this.attribute = new ArrayList<AttributeInfo>();
        }
        this.attribute.add(exceptionsAttribute);
    }

    public void removeCodeAttribute() {
        AttributeInfo.remove(this.attribute, "Code");
    }

    public void setCodeAttribute(CodeAttribute codeAttribute) {
        this.removeCodeAttribute();
        if (this.attribute == null) {
            this.attribute = new ArrayList<AttributeInfo>();
        }
        this.attribute.add(codeAttribute);
    }

    public void rebuildStackMapIf6(ClassPool classPool, ClassFile classFile) {
        if (classFile.getMajorVersion() >= 50) {
            this.rebuildStackMap(classPool);
        }
        if (doPreverify) {
            this.rebuildStackMapForME(classPool);
        }
    }

    public void rebuildStackMap(ClassPool classPool) {
        CodeAttribute codeAttribute = this.getCodeAttribute();
        if (codeAttribute != null) {
            StackMapTable stackMapTable = MapMaker.make(classPool, this);
            codeAttribute.setAttribute(stackMapTable);
        }
    }

    public void rebuildStackMapForME(ClassPool classPool) {
        CodeAttribute codeAttribute = this.getCodeAttribute();
        if (codeAttribute != null) {
            StackMap stackMap = MapMaker.make2(classPool, this);
            codeAttribute.setAttribute(stackMap);
        }
    }

    public int getLineNumber(int n) {
        CodeAttribute codeAttribute = this.getCodeAttribute();
        if (codeAttribute == null) {
            return -1;
        }
        LineNumberAttribute lineNumberAttribute = (LineNumberAttribute)codeAttribute.getAttribute("LineNumberTable");
        if (lineNumberAttribute == null) {
            return -1;
        }
        return lineNumberAttribute.toLineNumber(n);
    }

    public void setSuperclass(String string) {
        if (!this.isConstructor()) {
            return;
        }
        CodeAttribute codeAttribute = this.getCodeAttribute();
        byte[] byArray = codeAttribute.getCode();
        CodeIterator codeIterator = codeAttribute.iterator();
        int n = codeIterator.skipSuperConstructor();
        if (n >= 0) {
            ConstPool constPool = this.constPool;
            int n2 = ByteArray.readU16bit(byArray, n + 1);
            int n3 = constPool.getMethodrefNameAndType(n2);
            int n4 = constPool.addClassInfo(string);
            int n5 = constPool.addMethodrefInfo(n4, n3);
            ByteArray.write16bit(n5, byArray, n + 1);
        }
    }

    private void read(MethodInfo methodInfo, String string, Map<String, String> map) {
        CodeAttribute codeAttribute;
        ConstPool constPool = this.constPool;
        this.accessFlags = methodInfo.accessFlags;
        this.name = constPool.addUtf8Info(string);
        this.cachedName = string;
        ConstPool constPool2 = methodInfo.constPool;
        String string2 = constPool2.getUtf8Info(methodInfo.descriptor);
        String string3 = Descriptor.rename(string2, map);
        this.descriptor = constPool.addUtf8Info(string3);
        this.attribute = new ArrayList<AttributeInfo>();
        ExceptionsAttribute exceptionsAttribute = methodInfo.getExceptionsAttribute();
        if (exceptionsAttribute != null) {
            this.attribute.add(exceptionsAttribute.copy(constPool, map));
        }
        if ((codeAttribute = methodInfo.getCodeAttribute()) != null) {
            this.attribute.add(codeAttribute.copy(constPool, map));
        }
    }

    private void read(DataInputStream dataInputStream) {
        this.accessFlags = dataInputStream.readUnsignedShort();
        this.name = dataInputStream.readUnsignedShort();
        this.descriptor = dataInputStream.readUnsignedShort();
        int n = dataInputStream.readUnsignedShort();
        this.attribute = new ArrayList<AttributeInfo>();
        for (int i = 0; i < n; ++i) {
            this.attribute.add(AttributeInfo.read(this.constPool, dataInputStream));
        }
    }

    void write(DataOutputStream dataOutputStream) {
        dataOutputStream.writeShort(this.accessFlags);
        dataOutputStream.writeShort(this.name);
        dataOutputStream.writeShort(this.descriptor);
        if (this.attribute == null) {
            dataOutputStream.writeShort(0);
        } else {
            dataOutputStream.writeShort(this.attribute.size());
            AttributeInfo.writeAll(this.attribute, dataOutputStream);
        }
    }
}

