/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CodeConverter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtBehavior;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtConstructor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMember;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.FieldInitLink;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AccessFlag;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AnnotationsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstantAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.EnclosingMethodAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.FieldInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.InnerClassesAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ParameterAnnotationsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.SignatureAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.Annotation;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationImpl;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.AccessorMaker;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.Javac;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.ExprEditor;

class CtClassType
extends CtClass {
    ClassPool classPool;
    boolean wasChanged;
    private boolean wasFrozen;
    boolean wasPruned;
    boolean gcConstPool;
    ClassFile classfile;
    byte[] rawClassfile;
    private Reference<CtMember.Cache> memberCache;
    private AccessorMaker accessors;
    private FieldInitLink fieldInitializers;
    private Map<CtMethod, String> hiddenMethods;
    private int uniqueNumberSeed;
    private boolean doPruning = ClassPool.doPruning;
    private int getCount;
    private static final int GET_THRESHOLD = 2;

    CtClassType(String string, ClassPool classPool) {
        super(string);
        this.classPool = classPool;
        this.gcConstPool = false;
        this.wasPruned = false;
        this.wasFrozen = false;
        this.wasChanged = false;
        this.classfile = null;
        this.rawClassfile = null;
        this.memberCache = null;
        this.accessors = null;
        this.fieldInitializers = null;
        this.hiddenMethods = null;
        this.uniqueNumberSeed = 0;
        this.getCount = 0;
    }

    CtClassType(InputStream inputStream, ClassPool classPool) {
        this((String)null, classPool);
        this.classfile = new ClassFile(new DataInputStream(inputStream));
        this.qualifiedName = this.classfile.getName();
    }

    CtClassType(ClassFile classFile, ClassPool classPool) {
        this((String)null, classPool);
        this.classfile = classFile;
        this.qualifiedName = this.classfile.getName();
    }

    @Override
    protected void extendToString(StringBuilder stringBuilder) {
        Object object;
        if (this.wasChanged) {
            stringBuilder.append("changed ");
        }
        if (this.wasFrozen) {
            stringBuilder.append("frozen ");
        }
        if (this.wasPruned) {
            stringBuilder.append("pruned ");
        }
        stringBuilder.append(Modifier.toString(this.getModifiers()));
        stringBuilder.append(" class ");
        stringBuilder.append(this.getName());
        try {
            String string;
            object = this.getSuperclass();
            if (object != null && !(string = ((CtClass)object).getName()).equals("java.lang.Object")) {
                stringBuilder.append(" extends ").append(((CtClass)object).getName());
            }
        } catch (NotFoundException notFoundException) {
            stringBuilder.append(" extends ??");
        }
        try {
            object = this.getInterfaces();
            if (((CtClass[])object).length > 0) {
                stringBuilder.append(" implements ");
            }
            for (int i = 0; i < ((CtClass[])object).length; ++i) {
                stringBuilder.append(((CtClass)object[i]).getName());
                stringBuilder.append(", ");
            }
        } catch (NotFoundException notFoundException) {
            stringBuilder.append(" extends ??");
        }
        object = this.getMembers();
        this.exToString(stringBuilder, " fields=", ((CtMember.Cache)object).fieldHead(), ((CtMember.Cache)object).lastField());
        this.exToString(stringBuilder, " constructors=", ((CtMember.Cache)object).consHead(), ((CtMember.Cache)object).lastCons());
        this.exToString(stringBuilder, " methods=", ((CtMember.Cache)object).methodHead(), ((CtMember.Cache)object).lastMethod());
    }

    private void exToString(StringBuilder stringBuilder, String string, CtMember ctMember, CtMember ctMember2) {
        stringBuilder.append(string);
        while (ctMember != ctMember2) {
            ctMember = ctMember.next();
            stringBuilder.append(ctMember);
            stringBuilder.append(", ");
        }
    }

    @Override
    public AccessorMaker getAccessorMaker() {
        if (this.accessors == null) {
            this.accessors = new AccessorMaker(this);
        }
        return this.accessors;
    }

    @Override
    public ClassFile getClassFile2() {
        return this.getClassFile3(true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ClassFile getClassFile3(boolean bl) {
        byte[] byArray;
        ClassFile classFile = this.classfile;
        if (classFile != null) {
            return classFile;
        }
        if (bl) {
            this.classPool.compress();
        }
        Object object = this;
        synchronized (object) {
            classFile = this.classfile;
            if (classFile != null) {
                return classFile;
            }
            byArray = this.rawClassfile;
        }
        if (byArray != null) {
            try {
                object = new ClassFile(new DataInputStream(new ByteArrayInputStream(byArray)));
            } catch (IOException iOException) {
                throw new RuntimeException(iOException.toString(), iOException);
            }
            this.getCount = 2;
            CtClassType ctClassType = this;
            synchronized (ctClassType) {
                this.rawClassfile = null;
                return this.setClassFile((ClassFile)object);
            }
        }
        object = null;
        try {
            object = this.classPool.openClassfile(this.getName());
            if (object == null) {
                throw new NotFoundException(this.getName());
            }
            ClassFile classFile2 = new ClassFile(new DataInputStream((InputStream)(object = new BufferedInputStream((InputStream)object))));
            if (!classFile2.getName().equals(this.qualifiedName)) {
                throw new RuntimeException("cannot find " + this.qualifiedName + ": " + classFile2.getName() + " found in " + this.qualifiedName.replace('.', '/') + ".class");
            }
            ClassFile classFile3 = this.setClassFile(classFile2);
            return classFile3;
        } catch (NotFoundException notFoundException) {
            throw new RuntimeException(notFoundException.toString(), notFoundException);
        } catch (IOException iOException) {
            throw new RuntimeException(iOException.toString(), iOException);
        } finally {
            if (object != null) {
                try {
                    ((InputStream)object).close();
                } catch (IOException iOException) {}
            }
        }
    }

    @Override
    final void incGetCounter() {
        ++this.getCount;
    }

    @Override
    void compress() {
        if (this.getCount < 2) {
            if (!this.isModified() && ClassPool.releaseUnmodifiedClassFile) {
                this.removeClassFile();
            } else if (this.isFrozen() && !this.wasPruned) {
                this.saveClassFile();
            }
        }
        this.getCount = 0;
    }

    private synchronized void saveClassFile() {
        if (this.classfile == null || this.hasMemberCache() != null) {
            return;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            this.classfile.write(dataOutputStream);
            byteArrayOutputStream.close();
            this.rawClassfile = byteArrayOutputStream.toByteArray();
            this.classfile = null;
        } catch (IOException iOException) {
            // empty catch block
        }
    }

    private synchronized void removeClassFile() {
        if (this.classfile != null && !this.isModified() && this.hasMemberCache() == null) {
            this.classfile = null;
        }
    }

    private synchronized ClassFile setClassFile(ClassFile classFile) {
        if (this.classfile == null) {
            this.classfile = classFile;
        }
        return this.classfile;
    }

    @Override
    public ClassPool getClassPool() {
        return this.classPool;
    }

    void setClassPool(ClassPool classPool) {
        this.classPool = classPool;
    }

    @Override
    public URL getURL() {
        URL uRL = this.classPool.find(this.getName());
        if (uRL == null) {
            throw new NotFoundException(this.getName());
        }
        return uRL;
    }

    @Override
    public boolean isModified() {
        return this.wasChanged;
    }

    @Override
    public boolean isFrozen() {
        return this.wasFrozen;
    }

    @Override
    public void freeze() {
        this.wasFrozen = true;
    }

    @Override
    void checkModify() {
        if (this.isFrozen()) {
            String string = this.getName() + " class is frozen";
            if (this.wasPruned) {
                string = string + " and pruned";
            }
            throw new RuntimeException(string);
        }
        this.wasChanged = true;
    }

    @Override
    public void defrost() {
        this.checkPruned("defrost");
        this.wasFrozen = false;
    }

    @Override
    public boolean subtypeOf(CtClass ctClass) {
        int n;
        String string = ctClass.getName();
        if (this == ctClass || this.getName().equals(string)) {
            return true;
        }
        ClassFile classFile = this.getClassFile2();
        String string2 = classFile.getSuperclass();
        if (string2 != null && string2.equals(string)) {
            return true;
        }
        String[] stringArray = classFile.getInterfaces();
        int n2 = stringArray.length;
        for (n = 0; n < n2; ++n) {
            if (!stringArray[n].equals(string)) continue;
            return true;
        }
        if (string2 != null && this.classPool.get(string2).subtypeOf(ctClass)) {
            return true;
        }
        for (n = 0; n < n2; ++n) {
            if (!this.classPool.get(stringArray[n]).subtypeOf(ctClass)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void setName(String string) {
        String string2 = this.getName();
        if (string.equals(string2)) {
            return;
        }
        this.classPool.checkNotFrozen(string);
        ClassFile classFile = this.getClassFile2();
        super.setName(string);
        classFile.setName(string);
        this.nameReplaced();
        this.classPool.classNameChanged(string2, this);
    }

    @Override
    public String getGenericSignature() {
        SignatureAttribute signatureAttribute = (SignatureAttribute)this.getClassFile2().getAttribute("Signature");
        return signatureAttribute == null ? null : signatureAttribute.getSignature();
    }

    @Override
    public void setGenericSignature(String string) {
        ClassFile classFile = this.getClassFile();
        SignatureAttribute signatureAttribute = new SignatureAttribute(classFile.getConstPool(), string);
        classFile.addAttribute(signatureAttribute);
    }

    @Override
    public void replaceClassName(ClassMap classMap) {
        String string = this.getName();
        String string2 = classMap.get(Descriptor.toJvmName(string));
        if (string2 != null) {
            string2 = Descriptor.toJavaName(string2);
            this.classPool.checkNotFrozen(string2);
        }
        super.replaceClassName(classMap);
        ClassFile classFile = this.getClassFile2();
        classFile.renameClass(classMap);
        this.nameReplaced();
        if (string2 != null) {
            super.setName(string2);
            this.classPool.classNameChanged(string, this);
        }
    }

    @Override
    public void replaceClassName(String string, String string2) {
        String string3 = this.getName();
        if (string3.equals(string)) {
            this.setName(string2);
        } else {
            super.replaceClassName(string, string2);
            this.getClassFile2().renameClass(string, string2);
            this.nameReplaced();
        }
    }

    @Override
    public boolean isInterface() {
        return Modifier.isInterface(this.getModifiers());
    }

    @Override
    public boolean isAnnotation() {
        return Modifier.isAnnotation(this.getModifiers());
    }

    @Override
    public boolean isEnum() {
        return Modifier.isEnum(this.getModifiers());
    }

    @Override
    public int getModifiers() {
        ClassFile classFile = this.getClassFile2();
        int n = classFile.getAccessFlags();
        n = AccessFlag.clear(n, 32);
        int n2 = classFile.getInnerAccessFlags();
        if (n2 != -1) {
            if ((n2 & 8) != 0) {
                n |= 8;
            }
            if ((n2 & 1) != 0) {
                n |= 1;
            } else {
                n &= 0xFFFFFFFE;
                if ((n2 & 4) != 0) {
                    n |= 4;
                } else if ((n2 & 2) != 0) {
                    n |= 2;
                }
            }
        }
        return AccessFlag.toModifier(n);
    }

    @Override
    public CtClass[] getNestedClasses() {
        ClassFile classFile = this.getClassFile2();
        InnerClassesAttribute innerClassesAttribute = (InnerClassesAttribute)classFile.getAttribute("InnerClasses");
        if (innerClassesAttribute == null) {
            return new CtClass[0];
        }
        String string = classFile.getName() + "$";
        int n = innerClassesAttribute.tableLength();
        ArrayList<CtClass> arrayList = new ArrayList<CtClass>(n);
        for (int i = 0; i < n; ++i) {
            String string2 = innerClassesAttribute.innerClass(i);
            if (string2 == null || !string2.startsWith(string) || string2.lastIndexOf(36) >= string.length()) continue;
            arrayList.add(this.classPool.get(string2));
        }
        return arrayList.toArray(new CtClass[arrayList.size()]);
    }

    @Override
    public void setModifiers(int n) {
        this.checkModify();
        CtClassType.updateInnerEntry(n, this.getName(), this, true);
        ClassFile classFile = this.getClassFile2();
        classFile.setAccessFlags(AccessFlag.of(n & 0xFFFFFFF7));
    }

    private static void updateInnerEntry(int n, String string, CtClass ctClass, boolean bl) {
        ClassFile classFile = ctClass.getClassFile2();
        InnerClassesAttribute innerClassesAttribute = (InnerClassesAttribute)classFile.getAttribute("InnerClasses");
        if (innerClassesAttribute != null) {
            int n2;
            int n3 = n & 0xFFFFFFF7;
            int n4 = innerClassesAttribute.find(string);
            if (!(n4 < 0 || (n2 = innerClassesAttribute.accessFlags(n4) & 8) == 0 && Modifier.isStatic(n))) {
                ctClass.checkModify();
                innerClassesAttribute.setAccessFlags(n4, AccessFlag.of(n3) | n2);
                String string2 = innerClassesAttribute.outerClass(n4);
                if (string2 != null && bl) {
                    try {
                        CtClass ctClass2 = ctClass.getClassPool().get(string2);
                        CtClassType.updateInnerEntry(n3, string, ctClass2, false);
                    } catch (NotFoundException notFoundException) {
                        throw new RuntimeException("cannot find the declaring class: " + string2);
                    }
                }
                return;
            }
        }
        if (Modifier.isStatic(n)) {
            throw new RuntimeException("cannot change " + Descriptor.toJavaName(string) + " into a static class");
        }
    }

    @Override
    public boolean hasAnnotation(String string) {
        ClassFile classFile = this.getClassFile2();
        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute)classFile.getAttribute("RuntimeInvisibleAnnotations");
        AnnotationsAttribute annotationsAttribute2 = (AnnotationsAttribute)classFile.getAttribute("RuntimeVisibleAnnotations");
        return CtClassType.hasAnnotationType(string, this.getClassPool(), annotationsAttribute, annotationsAttribute2);
    }

    @Deprecated
    static boolean hasAnnotationType(Class<?> clazz, ClassPool classPool, AnnotationsAttribute annotationsAttribute, AnnotationsAttribute annotationsAttribute2) {
        return CtClassType.hasAnnotationType(clazz.getName(), classPool, annotationsAttribute, annotationsAttribute2);
    }

    static boolean hasAnnotationType(String string, ClassPool classPool, AnnotationsAttribute annotationsAttribute, AnnotationsAttribute annotationsAttribute2) {
        int n;
        Annotation[] annotationArray = annotationsAttribute == null ? null : annotationsAttribute.getAnnotations();
        Annotation[] annotationArray2 = annotationsAttribute2 == null ? null : annotationsAttribute2.getAnnotations();
        if (annotationArray != null) {
            for (n = 0; n < annotationArray.length; ++n) {
                if (!annotationArray[n].getTypeName().equals(string)) continue;
                return true;
            }
        }
        if (annotationArray2 != null) {
            for (n = 0; n < annotationArray2.length; ++n) {
                if (!annotationArray2[n].getTypeName().equals(string)) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public Object getAnnotation(Class<?> clazz) {
        ClassFile classFile = this.getClassFile2();
        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute)classFile.getAttribute("RuntimeInvisibleAnnotations");
        AnnotationsAttribute annotationsAttribute2 = (AnnotationsAttribute)classFile.getAttribute("RuntimeVisibleAnnotations");
        return CtClassType.getAnnotationType(clazz, this.getClassPool(), annotationsAttribute, annotationsAttribute2);
    }

    static Object getAnnotationType(Class<?> clazz, ClassPool classPool, AnnotationsAttribute annotationsAttribute, AnnotationsAttribute annotationsAttribute2) {
        int n;
        Annotation[] annotationArray = annotationsAttribute == null ? null : annotationsAttribute.getAnnotations();
        Annotation[] annotationArray2 = annotationsAttribute2 == null ? null : annotationsAttribute2.getAnnotations();
        String string = clazz.getName();
        if (annotationArray != null) {
            for (n = 0; n < annotationArray.length; ++n) {
                if (!annotationArray[n].getTypeName().equals(string)) continue;
                return CtClassType.toAnnoType(annotationArray[n], classPool);
            }
        }
        if (annotationArray2 != null) {
            for (n = 0; n < annotationArray2.length; ++n) {
                if (!annotationArray2[n].getTypeName().equals(string)) continue;
                return CtClassType.toAnnoType(annotationArray2[n], classPool);
            }
        }
        return null;
    }

    @Override
    public Object[] getAnnotations() {
        return this.getAnnotations(false);
    }

    @Override
    public Object[] getAvailableAnnotations() {
        try {
            return this.getAnnotations(true);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new RuntimeException("Unexpected exception ", classNotFoundException);
        }
    }

    private Object[] getAnnotations(boolean bl) {
        ClassFile classFile = this.getClassFile2();
        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute)classFile.getAttribute("RuntimeInvisibleAnnotations");
        AnnotationsAttribute annotationsAttribute2 = (AnnotationsAttribute)classFile.getAttribute("RuntimeVisibleAnnotations");
        return CtClassType.toAnnotationType(bl, this.getClassPool(), annotationsAttribute, annotationsAttribute2);
    }

    static Object[] toAnnotationType(boolean bl, ClassPool classPool, AnnotationsAttribute annotationsAttribute, AnnotationsAttribute annotationsAttribute2) {
        int n;
        int n2;
        Annotation[] annotationArray;
        int n3;
        Annotation[] annotationArray2;
        if (annotationsAttribute == null) {
            annotationArray2 = null;
            n3 = 0;
        } else {
            annotationArray2 = annotationsAttribute.getAnnotations();
            n3 = annotationArray2.length;
        }
        if (annotationsAttribute2 == null) {
            annotationArray = null;
            n2 = 0;
        } else {
            annotationArray = annotationsAttribute2.getAnnotations();
            n2 = annotationArray.length;
        }
        if (!bl) {
            int n4;
            Object[] objectArray = new Object[n3 + n2];
            for (n4 = 0; n4 < n3; ++n4) {
                objectArray[n4] = CtClassType.toAnnoType(annotationArray2[n4], classPool);
            }
            for (n4 = 0; n4 < n2; ++n4) {
                objectArray[n4 + n3] = CtClassType.toAnnoType(annotationArray[n4], classPool);
            }
            return objectArray;
        }
        ArrayList<Object> arrayList = new ArrayList<Object>();
        for (n = 0; n < n3; ++n) {
            try {
                arrayList.add(CtClassType.toAnnoType(annotationArray2[n], classPool));
                continue;
            } catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
        for (n = 0; n < n2; ++n) {
            try {
                arrayList.add(CtClassType.toAnnoType(annotationArray[n], classPool));
                continue;
            } catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
        return arrayList.toArray();
    }

    static Object[][] toAnnotationType(boolean bl, ClassPool classPool, ParameterAnnotationsAttribute parameterAnnotationsAttribute, ParameterAnnotationsAttribute parameterAnnotationsAttribute2, MethodInfo methodInfo) {
        int n = 0;
        n = parameterAnnotationsAttribute != null ? parameterAnnotationsAttribute.numParameters() : (parameterAnnotationsAttribute2 != null ? parameterAnnotationsAttribute2.numParameters() : Descriptor.numOfParameters(methodInfo.getDescriptor()));
        Object[][] objectArray = new Object[n][];
        for (int i = 0; i < n; ++i) {
            int n2;
            int n3;
            Annotation[] annotationArray;
            int n4;
            Annotation[] annotationArray2;
            if (parameterAnnotationsAttribute == null) {
                annotationArray2 = null;
                n4 = 0;
            } else {
                annotationArray2 = parameterAnnotationsAttribute.getAnnotations()[i];
                n4 = annotationArray2.length;
            }
            if (parameterAnnotationsAttribute2 == null) {
                annotationArray = null;
                n3 = 0;
            } else {
                annotationArray = parameterAnnotationsAttribute2.getAnnotations()[i];
                n3 = annotationArray.length;
            }
            if (!bl) {
                int n5;
                objectArray[i] = new Object[n4 + n3];
                for (n5 = 0; n5 < n4; ++n5) {
                    objectArray[i][n5] = CtClassType.toAnnoType(annotationArray2[n5], classPool);
                }
                for (n5 = 0; n5 < n3; ++n5) {
                    objectArray[i][n5 + n4] = CtClassType.toAnnoType(annotationArray[n5], classPool);
                }
                continue;
            }
            ArrayList<Object> arrayList = new ArrayList<Object>();
            for (n2 = 0; n2 < n4; ++n2) {
                try {
                    arrayList.add(CtClassType.toAnnoType(annotationArray2[n2], classPool));
                    continue;
                } catch (ClassNotFoundException classNotFoundException) {
                    // empty catch block
                }
            }
            for (n2 = 0; n2 < n3; ++n2) {
                try {
                    arrayList.add(CtClassType.toAnnoType(annotationArray[n2], classPool));
                    continue;
                } catch (ClassNotFoundException classNotFoundException) {
                    // empty catch block
                }
            }
            objectArray[i] = arrayList.toArray();
        }
        return objectArray;
    }

    private static Object toAnnoType(Annotation annotation, ClassPool classPool) {
        try {
            ClassLoader classLoader = classPool.getClassLoader();
            return annotation.toAnnotationType(classLoader, classPool);
        } catch (ClassNotFoundException classNotFoundException) {
            ClassLoader classLoader = classPool.getClass().getClassLoader();
            try {
                return annotation.toAnnotationType(classLoader, classPool);
            } catch (ClassNotFoundException classNotFoundException2) {
                try {
                    Class<?> clazz = classPool.get(annotation.getTypeName()).toClass();
                    return AnnotationImpl.make(clazz.getClassLoader(), clazz, classPool, annotation);
                } catch (Throwable throwable) {
                    throw new ClassNotFoundException(annotation.getTypeName());
                }
            }
        }
    }

    @Override
    public boolean subclassOf(CtClass ctClass) {
        if (ctClass == null) {
            return false;
        }
        String string = ctClass.getName();
        try {
            for (CtClass ctClass2 = this; ctClass2 != null; ctClass2 = ((CtClass)ctClass2).getSuperclass()) {
                if (!ctClass2.getName().equals(string)) continue;
                return true;
            }
        } catch (Exception exception) {
            // empty catch block
        }
        return false;
    }

    @Override
    public CtClass getSuperclass() {
        String string = this.getClassFile2().getSuperclass();
        if (string == null) {
            return null;
        }
        return this.classPool.get(string);
    }

    @Override
    public void setSuperclass(CtClass ctClass) {
        this.checkModify();
        if (this.isInterface()) {
            this.addInterface(ctClass);
        } else {
            this.getClassFile2().setSuperclass(ctClass.getName());
        }
    }

    @Override
    public CtClass[] getInterfaces() {
        String[] stringArray = this.getClassFile2().getInterfaces();
        int n = stringArray.length;
        CtClass[] ctClassArray = new CtClass[n];
        for (int i = 0; i < n; ++i) {
            ctClassArray[i] = this.classPool.get(stringArray[i]);
        }
        return ctClassArray;
    }

    @Override
    public void setInterfaces(CtClass[] ctClassArray) {
        String[] stringArray;
        this.checkModify();
        if (ctClassArray == null) {
            stringArray = new String[]{};
        } else {
            int n = ctClassArray.length;
            stringArray = new String[n];
            for (int i = 0; i < n; ++i) {
                stringArray[i] = ctClassArray[i].getName();
            }
        }
        this.getClassFile2().setInterfaces(stringArray);
    }

    @Override
    public void addInterface(CtClass ctClass) {
        this.checkModify();
        if (ctClass != null) {
            this.getClassFile2().addInterface(ctClass.getName());
        }
    }

    @Override
    public CtClass getDeclaringClass() {
        ClassFile classFile = this.getClassFile2();
        InnerClassesAttribute innerClassesAttribute = (InnerClassesAttribute)classFile.getAttribute("InnerClasses");
        if (innerClassesAttribute == null) {
            return null;
        }
        String string = this.getName();
        int n = innerClassesAttribute.tableLength();
        for (int i = 0; i < n; ++i) {
            if (!string.equals(innerClassesAttribute.innerClass(i))) continue;
            String string2 = innerClassesAttribute.outerClass(i);
            if (string2 != null) {
                return this.classPool.get(string2);
            }
            EnclosingMethodAttribute enclosingMethodAttribute = (EnclosingMethodAttribute)classFile.getAttribute("EnclosingMethod");
            if (enclosingMethodAttribute == null) continue;
            return this.classPool.get(enclosingMethodAttribute.className());
        }
        return null;
    }

    @Override
    public CtBehavior getEnclosingBehavior() {
        ClassFile classFile = this.getClassFile2();
        EnclosingMethodAttribute enclosingMethodAttribute = (EnclosingMethodAttribute)classFile.getAttribute("EnclosingMethod");
        if (enclosingMethodAttribute == null) {
            return null;
        }
        CtClass ctClass = this.classPool.get(enclosingMethodAttribute.className());
        String string = enclosingMethodAttribute.methodName();
        if ("<init>".equals(string)) {
            return ctClass.getConstructor(enclosingMethodAttribute.methodDescriptor());
        }
        if ("<clinit>".equals(string)) {
            return ctClass.getClassInitializer();
        }
        return ctClass.getMethod(string, enclosingMethodAttribute.methodDescriptor());
    }

    @Override
    public CtClass makeNestedClass(String string, boolean bl) {
        if (!bl) {
            throw new RuntimeException("sorry, only nested static class is supported");
        }
        this.checkModify();
        CtClass ctClass = this.classPool.makeNestedClass(this.getName() + "$" + string);
        ClassFile classFile = this.getClassFile2();
        ClassFile classFile2 = ctClass.getClassFile2();
        InnerClassesAttribute innerClassesAttribute = (InnerClassesAttribute)classFile.getAttribute("InnerClasses");
        if (innerClassesAttribute == null) {
            innerClassesAttribute = new InnerClassesAttribute(classFile.getConstPool());
            classFile.addAttribute(innerClassesAttribute);
        }
        innerClassesAttribute.append(ctClass.getName(), this.getName(), string, classFile2.getAccessFlags() & 0xFFFFFFDF | 8);
        classFile2.addAttribute(innerClassesAttribute.copy(classFile2.getConstPool(), null));
        return ctClass;
    }

    private void nameReplaced() {
        CtMember.Cache cache = this.hasMemberCache();
        if (cache != null) {
            CtMember ctMember = cache.lastMethod();
            for (CtMember ctMember2 = cache.methodHead(); ctMember2 != ctMember; ctMember2 = ctMember2.next()) {
                ctMember2.nameReplaced();
            }
        }
    }

    protected CtMember.Cache hasMemberCache() {
        if (this.memberCache != null) {
            return this.memberCache.get();
        }
        return null;
    }

    protected synchronized CtMember.Cache getMembers() {
        CtMember.Cache cache = null;
        if (this.memberCache == null || (cache = this.memberCache.get()) == null) {
            cache = new CtMember.Cache(this);
            this.makeFieldCache(cache);
            this.makeBehaviorCache(cache);
            this.memberCache = new WeakReference<CtMember.Cache>(cache);
        }
        return cache;
    }

    private void makeFieldCache(CtMember.Cache cache) {
        List<FieldInfo> list = this.getClassFile3(false).getFields();
        for (FieldInfo fieldInfo : list) {
            cache.addField(new CtField(fieldInfo, (CtClass)this));
        }
    }

    private void makeBehaviorCache(CtMember.Cache cache) {
        List<MethodInfo> list = this.getClassFile3(false).getMethods();
        for (MethodInfo methodInfo : list) {
            if (methodInfo.isMethod()) {
                cache.addMethod(new CtMethod(methodInfo, this));
                continue;
            }
            cache.addConstructor(new CtConstructor(methodInfo, (CtClass)this));
        }
    }

    @Override
    public CtField[] getFields() {
        ArrayList<CtMember> arrayList = new ArrayList<CtMember>();
        CtClassType.getFields(arrayList, this);
        return arrayList.toArray(new CtField[arrayList.size()]);
    }

    private static void getFields(List<CtMember> list, CtClass ctClass) {
        Object object;
        if (ctClass == null) {
            return;
        }
        try {
            CtClassType.getFields(list, ctClass.getSuperclass());
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        try {
            for (CtClass ctClass2 : object = ctClass.getInterfaces()) {
                CtClassType.getFields(list, ctClass2);
            }
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        object = ((CtClassType)ctClass).getMembers();
        Object object2 = ((CtMember.Cache)object).fieldHead();
        CtMember ctMember = ((CtMember.Cache)object).lastField();
        while (object2 != ctMember) {
            if (Modifier.isPrivate(((CtMember)(object2 = ((CtMember)object2).next())).getModifiers())) continue;
            list.add((CtMember)object2);
        }
    }

    @Override
    public CtField getField(String string, String string2) {
        CtField ctField = this.getField2(string, string2);
        return this.checkGetField(ctField, string, string2);
    }

    private CtField checkGetField(CtField ctField, String string, String string2) {
        if (ctField == null) {
            String string3 = "field: " + string;
            if (string2 != null) {
                string3 = string3 + " type " + string2;
            }
            throw new NotFoundException(string3 + " in " + this.getName());
        }
        return ctField;
    }

    @Override
    CtField getField2(String string, String string2) {
        CtField ctField = this.getDeclaredField2(string, string2);
        if (ctField != null) {
            return ctField;
        }
        try {
            CtClass[] ctClassArray = this.getInterfaces();
            for (CtClass ctClass : ctClassArray) {
                CtField ctField2 = ctClass.getField2(string, string2);
                if (ctField2 == null) continue;
                return ctField2;
            }
            CtClass ctClass = this.getSuperclass();
            if (ctClass != null) {
                return ctClass.getField2(string, string2);
            }
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        return null;
    }

    @Override
    public CtField[] getDeclaredFields() {
        CtMember ctMember;
        CtMember.Cache cache = this.getMembers();
        CtMember ctMember2 = cache.lastField();
        int n = CtMember.Cache.count(ctMember, ctMember2);
        CtField[] ctFieldArray = new CtField[n];
        int n2 = 0;
        for (ctMember = cache.fieldHead(); ctMember != ctMember2; ctMember = ctMember.next()) {
            ctFieldArray[n2++] = (CtField)ctMember;
        }
        return ctFieldArray;
    }

    @Override
    public CtField getDeclaredField(String string) {
        return this.getDeclaredField(string, null);
    }

    @Override
    public CtField getDeclaredField(String string, String string2) {
        CtField ctField = this.getDeclaredField2(string, string2);
        return this.checkGetField(ctField, string, string2);
    }

    private CtField getDeclaredField2(String string, String string2) {
        CtMember.Cache cache = this.getMembers();
        CtMember ctMember = cache.fieldHead();
        CtMember ctMember2 = cache.lastField();
        while (ctMember != ctMember2) {
            if (!(ctMember = ctMember.next()).getName().equals(string) || string2 != null && !string2.equals(ctMember.getSignature())) continue;
            return (CtField)ctMember;
        }
        return null;
    }

    @Override
    public CtBehavior[] getDeclaredBehaviors() {
        CtMember ctMember;
        CtMember.Cache cache = this.getMembers();
        CtMember ctMember2 = cache.lastCons();
        int n = CtMember.Cache.count(ctMember, ctMember2);
        CtMember ctMember3 = cache.methodHead();
        CtMember ctMember4 = cache.lastMethod();
        int n2 = CtMember.Cache.count(ctMember3, ctMember4);
        CtBehavior[] ctBehaviorArray = new CtBehavior[n + n2];
        int n3 = 0;
        for (ctMember = cache.consHead(); ctMember != ctMember2; ctMember = ctMember.next()) {
            ctBehaviorArray[n3++] = (CtBehavior)ctMember;
        }
        while (ctMember3 != ctMember4) {
            ctMember3 = ctMember3.next();
            ctBehaviorArray[n3++] = (CtBehavior)ctMember3;
        }
        return ctBehaviorArray;
    }

    @Override
    public CtConstructor[] getConstructors() {
        CtMember.Cache cache = this.getMembers();
        CtMember ctMember = cache.consHead();
        CtMember ctMember2 = cache.lastCons();
        int n = 0;
        CtMember ctMember3 = ctMember;
        while (ctMember3 != ctMember2) {
            if (!CtClassType.isPubCons((CtConstructor)(ctMember3 = ctMember3.next()))) continue;
            ++n;
        }
        CtConstructor[] ctConstructorArray = new CtConstructor[n];
        int n2 = 0;
        ctMember3 = ctMember;
        while (ctMember3 != ctMember2) {
            CtConstructor ctConstructor = (CtConstructor)(ctMember3 = ctMember3.next());
            if (!CtClassType.isPubCons(ctConstructor)) continue;
            ctConstructorArray[n2++] = ctConstructor;
        }
        return ctConstructorArray;
    }

    private static boolean isPubCons(CtConstructor ctConstructor) {
        return !Modifier.isPrivate(ctConstructor.getModifiers()) && ctConstructor.isConstructor();
    }

    @Override
    public CtConstructor getConstructor(String string) {
        CtMember.Cache cache = this.getMembers();
        CtMember ctMember = cache.consHead();
        CtMember ctMember2 = cache.lastCons();
        while (ctMember != ctMember2) {
            CtConstructor ctConstructor = (CtConstructor)(ctMember = ctMember.next());
            if (!ctConstructor.getMethodInfo2().getDescriptor().equals(string) || !ctConstructor.isConstructor()) continue;
            return ctConstructor;
        }
        return super.getConstructor(string);
    }

    @Override
    public CtConstructor[] getDeclaredConstructors() {
        Object object;
        CtMember.Cache cache = this.getMembers();
        CtMember ctMember = cache.consHead();
        CtMember ctMember2 = cache.lastCons();
        int n = 0;
        CtMember ctMember3 = ctMember;
        while (ctMember3 != ctMember2) {
            object = (CtConstructor)(ctMember3 = ctMember3.next());
            if (!((CtConstructor)object).isConstructor()) continue;
            ++n;
        }
        object = new CtConstructor[n];
        int n2 = 0;
        ctMember3 = ctMember;
        while (ctMember3 != ctMember2) {
            CtConstructor ctConstructor = (CtConstructor)(ctMember3 = ctMember3.next());
            if (!ctConstructor.isConstructor()) continue;
            object[n2++] = ctConstructor;
        }
        return object;
    }

    @Override
    public CtConstructor getClassInitializer() {
        CtMember.Cache cache = this.getMembers();
        CtMember ctMember = cache.consHead();
        CtMember ctMember2 = cache.lastCons();
        while (ctMember != ctMember2) {
            CtConstructor ctConstructor = (CtConstructor)(ctMember = ctMember.next());
            if (!ctConstructor.isClassInitializer()) continue;
            return ctConstructor;
        }
        return null;
    }

    @Override
    public CtMethod[] getMethods() {
        HashMap<String, CtMember> hashMap = new HashMap<String, CtMember>();
        CtClassType.getMethods0(hashMap, this);
        return hashMap.values().toArray(new CtMethod[hashMap.size()]);
    }

    private static void getMethods0(Map<String, CtMember> map, CtClass ctClass) {
        Object object;
        try {
            for (CtClass ctClass2 : object = ctClass.getInterfaces()) {
                CtClassType.getMethods0(map, ctClass2);
            }
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        try {
            object = ctClass.getSuperclass();
            if (object != null) {
                CtClassType.getMethods0(map, (CtClass)object);
            }
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        if (ctClass instanceof CtClassType) {
            object = ((CtClassType)ctClass).getMembers();
            Object object2 = ((CtMember.Cache)object).methodHead();
            CtMember ctMember = ((CtMember.Cache)object).lastMethod();
            while (object2 != ctMember) {
                if (Modifier.isPrivate(((CtMember)(object2 = ((CtMember)object2).next())).getModifiers())) continue;
                map.put(((CtMethod)object2).getStringRep(), (CtMember)object2);
            }
        }
    }

    @Override
    public CtMethod getMethod(String string, String string2) {
        CtMethod ctMethod = CtClassType.getMethod0(this, string, string2);
        if (ctMethod != null) {
            return ctMethod;
        }
        throw new NotFoundException(string + "(..) is not found in " + this.getName());
    }

    private static CtMethod getMethod0(CtClass ctClass, String string, String string2) {
        Object object;
        CtClass[] ctClassArray;
        if (ctClass instanceof CtClassType) {
            ctClassArray = ((CtClassType)ctClass).getMembers();
            object = ctClassArray.methodHead();
            CtMember ctMember = ctClassArray.lastMethod();
            while (object != ctMember) {
                if (!((CtMember)(object = ((CtMember)object).next())).getName().equals(string) || !((CtMethod)object).getMethodInfo2().getDescriptor().equals(string2)) continue;
                return (CtMethod)object;
            }
        }
        try {
            ctClassArray = ctClass.getSuperclass();
            if (ctClassArray != null && (object = CtClassType.getMethod0((CtClass)ctClassArray, string, string2)) != null) {
                return object;
            }
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        try {
            ctClassArray = ctClass.getInterfaces();
            for (CtClass ctClass2 : ctClassArray) {
                CtMethod ctMethod = CtClassType.getMethod0(ctClass2, string, string2);
                if (ctMethod == null) continue;
                return ctMethod;
            }
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        return null;
    }

    @Override
    public CtMethod[] getDeclaredMethods() {
        CtMember.Cache cache = this.getMembers();
        CtMember ctMember = cache.lastMethod();
        ArrayList<CtMember> arrayList = new ArrayList<CtMember>();
        for (CtMember ctMember2 = cache.methodHead(); ctMember2 != ctMember; ctMember2 = ctMember2.next()) {
            arrayList.add(ctMember2);
        }
        return arrayList.toArray(new CtMethod[arrayList.size()]);
    }

    @Override
    public CtMethod[] getDeclaredMethods(String string) {
        CtMember.Cache cache = this.getMembers();
        CtMember ctMember = cache.methodHead();
        CtMember ctMember2 = cache.lastMethod();
        ArrayList<CtMember> arrayList = new ArrayList<CtMember>();
        while (ctMember != ctMember2) {
            if (!(ctMember = ctMember.next()).getName().equals(string)) continue;
            arrayList.add(ctMember);
        }
        return arrayList.toArray(new CtMethod[arrayList.size()]);
    }

    @Override
    public CtMethod getDeclaredMethod(String string) {
        CtMember.Cache cache = this.getMembers();
        CtMember ctMember = cache.methodHead();
        CtMember ctMember2 = cache.lastMethod();
        while (ctMember != ctMember2) {
            if (!(ctMember = ctMember.next()).getName().equals(string)) continue;
            return (CtMethod)ctMember;
        }
        throw new NotFoundException(string + "(..) is not found in " + this.getName());
    }

    @Override
    public CtMethod getDeclaredMethod(String string, CtClass[] ctClassArray) {
        String string2 = Descriptor.ofParameters(ctClassArray);
        CtMember.Cache cache = this.getMembers();
        CtMember ctMember = cache.methodHead();
        CtMember ctMember2 = cache.lastMethod();
        while (ctMember != ctMember2) {
            if (!(ctMember = ctMember.next()).getName().equals(string) || !((CtMethod)ctMember).getMethodInfo2().getDescriptor().startsWith(string2)) continue;
            return (CtMethod)ctMember;
        }
        throw new NotFoundException(string + "(..) is not found in " + this.getName());
    }

    @Override
    public void addField(CtField ctField, String string) {
        this.addField(ctField, CtField.Initializer.byExpr(string));
    }

    @Override
    public void addField(CtField ctField, CtField.Initializer initializer) {
        Object object;
        this.checkModify();
        if (ctField.getDeclaringClass() != this) {
            throw new CannotCompileException("cannot add");
        }
        if (initializer == null) {
            initializer = ctField.getInit();
        }
        if (initializer != null) {
            initializer.check(ctField.getSignature());
            int n = ctField.getModifiers();
            if (Modifier.isStatic(n) && Modifier.isFinal(n)) {
                try {
                    object = this.getClassFile2().getConstPool();
                    int n2 = initializer.getConstantValue((ConstPool)object, ctField.getType());
                    if (n2 != 0) {
                        ctField.getFieldInfo2().addAttribute(new ConstantAttribute((ConstPool)object, n2));
                        initializer = null;
                    }
                } catch (NotFoundException notFoundException) {
                    // empty catch block
                }
            }
        }
        this.getMembers().addField(ctField);
        this.getClassFile2().addField(ctField.getFieldInfo2());
        if (initializer != null) {
            FieldInitLink fieldInitLink = new FieldInitLink(ctField, initializer);
            object = this.fieldInitializers;
            if (object == null) {
                this.fieldInitializers = fieldInitLink;
            } else {
                while (((FieldInitLink)object).next != null) {
                    object = ((FieldInitLink)object).next;
                }
                ((FieldInitLink)object).next = fieldInitLink;
            }
        }
    }

    @Override
    public void removeField(CtField ctField) {
        this.checkModify();
        FieldInfo fieldInfo = ctField.getFieldInfo2();
        ClassFile classFile = this.getClassFile2();
        if (!classFile.getFields().remove(fieldInfo)) {
            throw new NotFoundException(ctField.toString());
        }
        this.getMembers().remove(ctField);
        this.gcConstPool = true;
    }

    @Override
    public CtConstructor makeClassInitializer() {
        CtConstructor ctConstructor = this.getClassInitializer();
        if (ctConstructor != null) {
            return ctConstructor;
        }
        this.checkModify();
        ClassFile classFile = this.getClassFile2();
        Bytecode bytecode = new Bytecode(classFile.getConstPool(), 0, 0);
        this.modifyClassConstructor(classFile, bytecode, 0, 0);
        return this.getClassInitializer();
    }

    @Override
    public void addConstructor(CtConstructor ctConstructor) {
        this.checkModify();
        if (ctConstructor.getDeclaringClass() != this) {
            throw new CannotCompileException("cannot add");
        }
        this.getMembers().addConstructor(ctConstructor);
        this.getClassFile2().addMethod(ctConstructor.getMethodInfo2());
    }

    @Override
    public void removeConstructor(CtConstructor ctConstructor) {
        this.checkModify();
        MethodInfo methodInfo = ctConstructor.getMethodInfo2();
        ClassFile classFile = this.getClassFile2();
        if (!classFile.getMethods().remove(methodInfo)) {
            throw new NotFoundException(ctConstructor.toString());
        }
        this.getMembers().remove(ctConstructor);
        this.gcConstPool = true;
    }

    @Override
    public void addMethod(CtMethod ctMethod) {
        this.checkModify();
        if (ctMethod.getDeclaringClass() != this) {
            throw new CannotCompileException("bad declaring class");
        }
        int n = ctMethod.getModifiers();
        if ((this.getModifiers() & 0x200) != 0) {
            if (Modifier.isProtected(n) || Modifier.isPrivate(n)) {
                throw new CannotCompileException("an interface method must be public: " + ctMethod.toString());
            }
            ctMethod.setModifiers(n | 1);
        }
        this.getMembers().addMethod(ctMethod);
        this.getClassFile2().addMethod(ctMethod.getMethodInfo2());
        if ((n & 0x400) != 0) {
            this.setModifiers(this.getModifiers() | 0x400);
        }
    }

    @Override
    public void removeMethod(CtMethod ctMethod) {
        this.checkModify();
        MethodInfo methodInfo = ctMethod.getMethodInfo2();
        ClassFile classFile = this.getClassFile2();
        if (!classFile.getMethods().remove(methodInfo)) {
            throw new NotFoundException(ctMethod.toString());
        }
        this.getMembers().remove(ctMethod);
        this.gcConstPool = true;
    }

    @Override
    public byte[] getAttribute(String string) {
        AttributeInfo attributeInfo = this.getClassFile2().getAttribute(string);
        if (attributeInfo == null) {
            return null;
        }
        return attributeInfo.get();
    }

    @Override
    public void setAttribute(String string, byte[] byArray) {
        this.checkModify();
        ClassFile classFile = this.getClassFile2();
        classFile.addAttribute(new AttributeInfo(classFile.getConstPool(), string, byArray));
    }

    @Override
    public void instrument(CodeConverter codeConverter) {
        this.checkModify();
        ClassFile classFile = this.getClassFile2();
        ConstPool constPool = classFile.getConstPool();
        List<MethodInfo> list = classFile.getMethods();
        for (MethodInfo methodInfo : list.toArray(new MethodInfo[list.size()])) {
            codeConverter.doit(this, methodInfo, constPool);
        }
    }

    @Override
    public void instrument(ExprEditor exprEditor) {
        this.checkModify();
        ClassFile classFile = this.getClassFile2();
        List<MethodInfo> list = classFile.getMethods();
        for (MethodInfo methodInfo : list.toArray(new MethodInfo[list.size()])) {
            exprEditor.doit(this, methodInfo);
        }
    }

    @Override
    public void prune() {
        if (this.wasPruned) {
            return;
        }
        this.wasFrozen = true;
        this.wasPruned = true;
        this.getClassFile2().prune();
    }

    @Override
    public void rebuildClassFile() {
        this.gcConstPool = true;
    }

    @Override
    public void toBytecode(DataOutputStream dataOutputStream) {
        try {
            if (this.isModified()) {
                this.checkPruned("toBytecode");
                ClassFile classFile = this.getClassFile2();
                if (this.gcConstPool) {
                    classFile.compact();
                    this.gcConstPool = false;
                }
                this.modifyClassConstructor(classFile);
                this.modifyConstructors(classFile);
                if (debugDump != null) {
                    this.dumpClassFile(classFile);
                }
                classFile.write(dataOutputStream);
                dataOutputStream.flush();
                this.fieldInitializers = null;
                if (this.doPruning) {
                    classFile.prune();
                    this.wasPruned = true;
                }
            } else {
                this.classPool.writeClassfile(this.getName(), dataOutputStream);
            }
            this.getCount = 0;
            this.wasFrozen = true;
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        } catch (IOException iOException) {
            throw new CannotCompileException(iOException);
        }
    }

    private void dumpClassFile(ClassFile classFile) {
        try (DataOutputStream dataOutputStream = this.makeFileOutput(debugDump);){
            classFile.write(dataOutputStream);
        }
    }

    private void checkPruned(String string) {
        if (this.wasPruned) {
            throw new RuntimeException(string + "(): " + this.getName() + " was pruned.");
        }
    }

    @Override
    public boolean stopPruning(boolean bl) {
        boolean bl2 = !this.doPruning;
        this.doPruning = !bl;
        return bl2;
    }

    private void modifyClassConstructor(ClassFile classFile) {
        if (this.fieldInitializers == null) {
            return;
        }
        Bytecode bytecode = new Bytecode(classFile.getConstPool(), 0, 0);
        Javac javac = new Javac(bytecode, this);
        int n = 0;
        boolean bl = false;
        FieldInitLink fieldInitLink = this.fieldInitializers;
        while (fieldInitLink != null) {
            CtField ctField = fieldInitLink.field;
            if (Modifier.isStatic(ctField.getModifiers())) {
                bl = true;
                int n2 = fieldInitLink.init.compileIfStatic(ctField.getType(), ctField.getName(), bytecode, javac);
                if (n < n2) {
                    n = n2;
                }
            }
            fieldInitLink = fieldInitLink.next;
        }
        if (bl) {
            this.modifyClassConstructor(classFile, bytecode, n, 0);
        }
    }

    private void modifyClassConstructor(ClassFile classFile, Bytecode bytecode, int n, int n2) {
        Object object;
        MethodInfo methodInfo = classFile.getStaticInitializer();
        if (methodInfo == null) {
            bytecode.add(177);
            bytecode.setMaxStack(n);
            bytecode.setMaxLocals(n2);
            methodInfo = new MethodInfo(classFile.getConstPool(), "<clinit>", "()V");
            methodInfo.setAccessFlags(8);
            methodInfo.setCodeAttribute(bytecode.toCodeAttribute());
            classFile.addMethod(methodInfo);
            object = this.hasMemberCache();
            if (object != null) {
                ((CtMember.Cache)object).addConstructor(new CtConstructor(methodInfo, (CtClass)this));
            }
        } else {
            object = methodInfo.getCodeAttribute();
            if (object == null) {
                throw new CannotCompileException("empty <clinit>");
            }
            try {
                int n3;
                CodeIterator codeIterator = ((CodeAttribute)object).iterator();
                int n4 = codeIterator.insertEx(bytecode.get());
                codeIterator.insert(bytecode.getExceptionTable(), n4);
                int n5 = ((CodeAttribute)object).getMaxStack();
                if (n5 < n) {
                    ((CodeAttribute)object).setMaxStack(n);
                }
                if ((n3 = ((CodeAttribute)object).getMaxLocals()) < n2) {
                    ((CodeAttribute)object).setMaxLocals(n2);
                }
            } catch (BadBytecode badBytecode) {
                throw new CannotCompileException(badBytecode);
            }
        }
        try {
            methodInfo.rebuildStackMapIf6(this.classPool, classFile);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode);
        }
    }

    private void modifyConstructors(ClassFile classFile) {
        if (this.fieldInitializers == null) {
            return;
        }
        ConstPool constPool = classFile.getConstPool();
        List<MethodInfo> list = classFile.getMethods();
        for (MethodInfo methodInfo : list) {
            CodeAttribute codeAttribute;
            if (!methodInfo.isConstructor() || (codeAttribute = methodInfo.getCodeAttribute()) == null) continue;
            try {
                Bytecode bytecode = new Bytecode(constPool, 0, codeAttribute.getMaxLocals());
                CtClass[] ctClassArray = Descriptor.getParameterTypes(methodInfo.getDescriptor(), this.classPool);
                int n = this.makeFieldInitializer(bytecode, ctClassArray);
                CtClassType.insertAuxInitializer(codeAttribute, bytecode, n);
                methodInfo.rebuildStackMapIf6(this.classPool, classFile);
            } catch (BadBytecode badBytecode) {
                throw new CannotCompileException(badBytecode);
            }
        }
    }

    private static void insertAuxInitializer(CodeAttribute codeAttribute, Bytecode bytecode, int n) {
        CodeIterator codeIterator = codeAttribute.iterator();
        int n2 = codeIterator.skipSuperConstructor();
        if (n2 < 0 && (n2 = codeIterator.skipThisConstructor()) >= 0) {
            return;
        }
        int n3 = codeIterator.insertEx(bytecode.get());
        codeIterator.insert(bytecode.getExceptionTable(), n3);
        int n4 = codeAttribute.getMaxStack();
        if (n4 < n) {
            codeAttribute.setMaxStack(n);
        }
    }

    private int makeFieldInitializer(Bytecode bytecode, CtClass[] ctClassArray) {
        int n = 0;
        Javac javac = new Javac(bytecode, this);
        try {
            javac.recordParams(ctClassArray, false);
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        }
        FieldInitLink fieldInitLink = this.fieldInitializers;
        while (fieldInitLink != null) {
            int n2;
            CtField ctField = fieldInitLink.field;
            if (!Modifier.isStatic(ctField.getModifiers()) && n < (n2 = fieldInitLink.init.compile(ctField.getType(), ctField.getName(), bytecode, ctClassArray, javac))) {
                n = n2;
            }
            fieldInitLink = fieldInitLink.next;
        }
        return n;
    }

    Map<CtMethod, String> getHiddenMethods() {
        if (this.hiddenMethods == null) {
            this.hiddenMethods = new Hashtable<CtMethod, String>();
        }
        return this.hiddenMethods;
    }

    int getUniqueNumber() {
        return this.uniqueNumberSeed++;
    }

    @Override
    public String makeUniqueName(String string) {
        String string2;
        HashMap<Object, CtClassType> hashMap = new HashMap<Object, CtClassType>();
        this.makeMemberList(hashMap);
        Set set = hashMap.keySet();
        String[] stringArray = new String[set.size()];
        set.toArray(stringArray);
        if (CtClassType.notFindInArray(string, stringArray)) {
            return string;
        }
        int n = 100;
        do {
            if (n <= 999) continue;
            throw new RuntimeException("too many unique name");
        } while (!CtClassType.notFindInArray(string2 = string + n++, stringArray));
        return string2;
    }

    private static boolean notFindInArray(String string, String[] stringArray) {
        int n = stringArray.length;
        for (int i = 0; i < n; ++i) {
            if (!stringArray[i].startsWith(string)) continue;
            return false;
        }
        return true;
    }

    private void makeMemberList(Map<Object, CtClassType> map) {
        Object object;
        int n = this.getModifiers();
        if (Modifier.isAbstract(n) || Modifier.isInterface(n)) {
            try {
                for (Object object2 : object = this.getInterfaces()) {
                    if (object2 == null || !(object2 instanceof CtClassType)) continue;
                    ((CtClassType)object2).makeMemberList(map);
                }
            } catch (NotFoundException notFoundException) {
                // empty catch block
            }
        }
        try {
            object = this.getSuperclass();
            if (object != null && object instanceof CtClassType) {
                ((CtClassType)object).makeMemberList(map);
            }
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        object = this.getClassFile2().getMethods();
        Object object3 = object.iterator();
        while (object3.hasNext()) {
            MethodInfo methodInfo = (MethodInfo)object3.next();
            map.put(methodInfo.getName(), this);
        }
        object3 = this.getClassFile2().getFields();
        Iterator iterator = object3.iterator();
        while (iterator.hasNext()) {
            FieldInfo fieldInfo = (FieldInfo)iterator.next();
            map.put(fieldInfo.getName(), this);
        }
    }
}

