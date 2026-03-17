/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Collection;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CodeConverter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtBehavior;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtConstructor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtPrimitiveType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.AccessorMaker;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.ExprEditor;

public abstract class CtClass {
    protected String qualifiedName;
    public static String debugDump = null;
    public static final String version = "3.29.2-GA";
    static final String javaLangObject = "java.lang.Object";
    public static CtClass booleanType;
    public static CtClass charType;
    public static CtClass byteType;
    public static CtClass shortType;
    public static CtClass intType;
    public static CtClass longType;
    public static CtClass floatType;
    public static CtClass doubleType;
    public static CtClass voidType;
    static CtClass[] primitiveTypes;

    public static void main(String[] stringArray) {
        System.out.println("Javassist version 3.29.2-GA");
        System.out.println("Copyright (C) 1999-2022 Shigeru Chiba. All Rights Reserved.");
    }

    protected CtClass(String string) {
        this.qualifiedName = string;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.getClass().getName());
        stringBuilder.append('@');
        stringBuilder.append(Integer.toHexString(this.hashCode()));
        stringBuilder.append('[');
        this.extendToString(stringBuilder);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    protected void extendToString(StringBuilder stringBuilder) {
        stringBuilder.append(this.getName());
    }

    public ClassPool getClassPool() {
        return null;
    }

    public ClassFile getClassFile() {
        this.checkModify();
        return this.getClassFile2();
    }

    public ClassFile getClassFile2() {
        return null;
    }

    public AccessorMaker getAccessorMaker() {
        return null;
    }

    public URL getURL() {
        throw new NotFoundException(this.getName());
    }

    public boolean isModified() {
        return false;
    }

    public boolean isFrozen() {
        return true;
    }

    public void freeze() {
    }

    void checkModify() {
        if (this.isFrozen()) {
            throw new RuntimeException(this.getName() + " class is frozen");
        }
    }

    public void defrost() {
        throw new RuntimeException("cannot defrost " + this.getName());
    }

    public boolean isPrimitive() {
        return false;
    }

    public boolean isArray() {
        return false;
    }

    public boolean isKotlin() {
        return this.hasAnnotation("kotlin.Metadata");
    }

    public CtClass getComponentType() {
        return null;
    }

    public boolean subtypeOf(CtClass ctClass) {
        return this == ctClass || this.getName().equals(ctClass.getName());
    }

    public String getName() {
        return this.qualifiedName;
    }

    public final String getSimpleName() {
        String string = this.qualifiedName;
        int n = string.lastIndexOf(46);
        if (n < 0) {
            return string;
        }
        return string.substring(n + 1);
    }

    public final String getPackageName() {
        String string = this.qualifiedName;
        int n = string.lastIndexOf(46);
        if (n < 0) {
            return null;
        }
        return string.substring(0, n);
    }

    public void setName(String string) {
        this.checkModify();
        if (string != null) {
            this.qualifiedName = string;
        }
    }

    public String getGenericSignature() {
        return null;
    }

    public void setGenericSignature(String string) {
        this.checkModify();
    }

    public void replaceClassName(String string, String string2) {
        this.checkModify();
    }

    public void replaceClassName(ClassMap classMap) {
        this.checkModify();
    }

    public synchronized Collection<String> getRefClasses() {
        ClassFile classFile = this.getClassFile2();
        if (classFile != null) {
            ClassMap classMap = new ClassMap(){
                private static final long serialVersionUID = 1L;

                @Override
                public String put(String string, String string2) {
                    return this.put0(string, string2);
                }

                @Override
                public String get(Object object) {
                    String string = 1.toJavaName((String)object);
                    this.put0(string, string);
                    return null;
                }

                @Override
                public void fix(String string) {
                }
            };
            classFile.getRefClasses(classMap);
            return classMap.values();
        }
        return null;
    }

    public boolean isInterface() {
        return false;
    }

    public boolean isAnnotation() {
        return false;
    }

    public boolean isEnum() {
        return false;
    }

    public int getModifiers() {
        return 0;
    }

    public boolean hasAnnotation(Class<?> clazz) {
        return this.hasAnnotation(clazz.getName());
    }

    public boolean hasAnnotation(String string) {
        return false;
    }

    public Object getAnnotation(Class<?> clazz) {
        return null;
    }

    public Object[] getAnnotations() {
        return new Object[0];
    }

    public Object[] getAvailableAnnotations() {
        return new Object[0];
    }

    public CtClass[] getDeclaredClasses() {
        return this.getNestedClasses();
    }

    public CtClass[] getNestedClasses() {
        return new CtClass[0];
    }

    public void setModifiers(int n) {
        this.checkModify();
    }

    public boolean subclassOf(CtClass ctClass) {
        return false;
    }

    public CtClass getSuperclass() {
        return null;
    }

    public void setSuperclass(CtClass ctClass) {
        this.checkModify();
    }

    public CtClass[] getInterfaces() {
        return new CtClass[0];
    }

    public void setInterfaces(CtClass[] ctClassArray) {
        this.checkModify();
    }

    public void addInterface(CtClass ctClass) {
        this.checkModify();
    }

    public CtClass getDeclaringClass() {
        return null;
    }

    @Deprecated
    public final CtMethod getEnclosingMethod() {
        CtBehavior ctBehavior = this.getEnclosingBehavior();
        if (ctBehavior == null) {
            return null;
        }
        if (ctBehavior instanceof CtMethod) {
            return (CtMethod)ctBehavior;
        }
        throw new NotFoundException(ctBehavior.getLongName() + " is enclosing " + this.getName());
    }

    public CtBehavior getEnclosingBehavior() {
        return null;
    }

    public CtClass makeNestedClass(String string, boolean bl) {
        throw new RuntimeException(this.getName() + " is not a class");
    }

    public CtField[] getFields() {
        return new CtField[0];
    }

    public CtField getField(String string) {
        return this.getField(string, null);
    }

    public CtField getField(String string, String string2) {
        throw new NotFoundException(string);
    }

    CtField getField2(String string, String string2) {
        return null;
    }

    public CtField[] getDeclaredFields() {
        return new CtField[0];
    }

    public CtField getDeclaredField(String string) {
        throw new NotFoundException(string);
    }

    public CtField getDeclaredField(String string, String string2) {
        throw new NotFoundException(string);
    }

    public CtBehavior[] getDeclaredBehaviors() {
        return new CtBehavior[0];
    }

    public CtConstructor[] getConstructors() {
        return new CtConstructor[0];
    }

    public CtConstructor getConstructor(String string) {
        throw new NotFoundException("no such constructor");
    }

    public CtConstructor[] getDeclaredConstructors() {
        return new CtConstructor[0];
    }

    public CtConstructor getDeclaredConstructor(CtClass[] ctClassArray) {
        String string = Descriptor.ofConstructor(ctClassArray);
        return this.getConstructor(string);
    }

    public CtConstructor getClassInitializer() {
        return null;
    }

    public CtMethod[] getMethods() {
        return new CtMethod[0];
    }

    public CtMethod getMethod(String string, String string2) {
        throw new NotFoundException(string);
    }

    public CtMethod[] getDeclaredMethods() {
        return new CtMethod[0];
    }

    public CtMethod getDeclaredMethod(String string, CtClass[] ctClassArray) {
        throw new NotFoundException(string);
    }

    public CtMethod[] getDeclaredMethods(String string) {
        throw new NotFoundException(string);
    }

    public CtMethod getDeclaredMethod(String string) {
        throw new NotFoundException(string);
    }

    public CtConstructor makeClassInitializer() {
        throw new CannotCompileException("not a class");
    }

    public void addConstructor(CtConstructor ctConstructor) {
        this.checkModify();
    }

    public void removeConstructor(CtConstructor ctConstructor) {
        this.checkModify();
    }

    public void addMethod(CtMethod ctMethod) {
        this.checkModify();
    }

    public void removeMethod(CtMethod ctMethod) {
        this.checkModify();
    }

    public void addField(CtField ctField) {
        this.addField(ctField, (CtField.Initializer)null);
    }

    public void addField(CtField ctField, String string) {
        this.checkModify();
    }

    public void addField(CtField ctField, CtField.Initializer initializer) {
        this.checkModify();
    }

    public void removeField(CtField ctField) {
        this.checkModify();
    }

    public byte[] getAttribute(String string) {
        return null;
    }

    public void setAttribute(String string, byte[] byArray) {
        this.checkModify();
    }

    public void instrument(CodeConverter codeConverter) {
        this.checkModify();
    }

    public void instrument(ExprEditor exprEditor) {
        this.checkModify();
    }

    public Class<?> toClass() {
        return this.getClassPool().toClass(this);
    }

    public Class<?> toClass(Class<?> clazz) {
        return this.getClassPool().toClass(this, clazz);
    }

    public Class<?> toClass(MethodHandles.Lookup lookup) {
        return this.getClassPool().toClass(this, lookup);
    }

    public Class<?> toClass(ClassLoader classLoader, ProtectionDomain protectionDomain) {
        ClassPool classPool = this.getClassPool();
        if (classLoader == null) {
            classLoader = classPool.getClassLoader();
        }
        return classPool.toClass(this, null, classLoader, protectionDomain);
    }

    @Deprecated
    public final Class<?> toClass(ClassLoader classLoader) {
        return this.getClassPool().toClass(this, null, classLoader, null);
    }

    public void detach() {
        ClassPool classPool = this.getClassPool();
        CtClass ctClass = classPool.removeCached(this.getName());
        if (ctClass != null && ctClass != this) {
            classPool.cacheCtClass(this.getName(), ctClass, false);
        }
    }

    public boolean stopPruning(boolean bl) {
        return true;
    }

    public void prune() {
    }

    void incGetCounter() {
    }

    public void rebuildClassFile() {
    }

    public byte[] toBytecode() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);){
            this.toBytecode(dataOutputStream);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public void writeFile() {
        this.writeFile(".");
    }

    public void writeFile(String string) {
        try (DataOutputStream dataOutputStream = this.makeFileOutput(string);){
            this.toBytecode(dataOutputStream);
        }
    }

    protected DataOutputStream makeFileOutput(String string) {
        String string2;
        String string3 = this.getName();
        String string4 = string + File.separatorChar + string3.replace('.', File.separatorChar) + ".class";
        int n = string4.lastIndexOf(File.separatorChar);
        if (n > 0 && !(string2 = string4.substring(0, n)).equals(".")) {
            new File(string2).mkdirs();
        }
        return new DataOutputStream(new BufferedOutputStream(new DelayedFileOutputStream(string4)));
    }

    public void debugWriteFile() {
        this.debugWriteFile(".");
    }

    public void debugWriteFile(String string) {
        try {
            boolean bl = this.stopPruning(true);
            this.writeFile(string);
            this.defrost();
            this.stopPruning(bl);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public void toBytecode(DataOutputStream dataOutputStream) {
        throw new CannotCompileException("not a class");
    }

    public String makeUniqueName(String string) {
        throw new RuntimeException("not available in " + this.getName());
    }

    void compress() {
    }

    static {
        primitiveTypes = new CtClass[9];
        CtClass.primitiveTypes[0] = booleanType = new CtPrimitiveType("boolean", 'Z', "java.lang.Boolean", "booleanValue", "()Z", 172, 4, 1);
        CtClass.primitiveTypes[1] = charType = new CtPrimitiveType("char", 'C', "java.lang.Character", "charValue", "()C", 172, 5, 1);
        CtClass.primitiveTypes[2] = byteType = new CtPrimitiveType("byte", 'B', "java.lang.Byte", "byteValue", "()B", 172, 8, 1);
        CtClass.primitiveTypes[3] = shortType = new CtPrimitiveType("short", 'S', "java.lang.Short", "shortValue", "()S", 172, 9, 1);
        CtClass.primitiveTypes[4] = intType = new CtPrimitiveType("int", 'I', "java.lang.Integer", "intValue", "()I", 172, 10, 1);
        CtClass.primitiveTypes[5] = longType = new CtPrimitiveType("long", 'J', "java.lang.Long", "longValue", "()J", 173, 11, 2);
        CtClass.primitiveTypes[6] = floatType = new CtPrimitiveType("float", 'F', "java.lang.Float", "floatValue", "()F", 174, 6, 1);
        CtClass.primitiveTypes[7] = doubleType = new CtPrimitiveType("double", 'D', "java.lang.Double", "doubleValue", "()D", 175, 7, 2);
        CtClass.primitiveTypes[8] = voidType = new CtPrimitiveType("void", 'V', "java.lang.Void", null, null, 177, 0, 0);
    }

    static class DelayedFileOutputStream
    extends OutputStream {
        private FileOutputStream file = null;
        private String filename;

        DelayedFileOutputStream(String string) {
            this.filename = string;
        }

        private void init() {
            if (this.file == null) {
                this.file = new FileOutputStream(this.filename);
            }
        }

        @Override
        public void write(int n) {
            this.init();
            this.file.write(n);
        }

        @Override
        public void write(byte[] byArray) {
            this.init();
            this.file.write(byArray);
        }

        @Override
        public void write(byte[] byArray, int n, int n2) {
            this.init();
            this.file.write(byArray, n, n2);
        }

        @Override
        public void flush() {
            this.init();
            this.file.flush();
        }

        @Override
        public void close() {
            this.init();
            this.file.close();
        }
    }
}

