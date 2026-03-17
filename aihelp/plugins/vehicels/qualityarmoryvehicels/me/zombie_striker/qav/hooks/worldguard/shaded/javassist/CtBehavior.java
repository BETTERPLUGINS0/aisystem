/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CodeConverter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClassType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMember;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtPrimitiveType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AccessFlag;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AnnotationsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.LineNumberAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.LocalVariableAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.LocalVariableTypeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ParameterAnnotationsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.SignatureAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMapTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.Javac;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.ExprEditor;

public abstract class CtBehavior
extends CtMember {
    protected MethodInfo methodInfo;

    protected CtBehavior(CtClass ctClass, MethodInfo methodInfo) {
        super(ctClass);
        this.methodInfo = methodInfo;
    }

    void copy(CtBehavior ctBehavior, boolean bl, ClassMap classMap) {
        CtClass ctClass = this.declaringClass;
        MethodInfo methodInfo = ctBehavior.methodInfo;
        CtClass ctClass2 = ctBehavior.getDeclaringClass();
        ConstPool constPool = ctClass.getClassFile2().getConstPool();
        classMap = new ClassMap(classMap);
        classMap.put(ctClass2.getName(), ctClass.getName());
        try {
            String string;
            boolean bl2 = false;
            CtClass ctClass3 = ctClass2.getSuperclass();
            CtClass ctClass4 = ctClass.getSuperclass();
            String string2 = null;
            if (ctClass3 != null && ctClass4 != null && !(string = ctClass3.getName()).equals(string2 = ctClass4.getName())) {
                if (string.equals("java.lang.Object")) {
                    bl2 = true;
                } else {
                    classMap.putIfNone(string, string2);
                }
            }
            this.methodInfo = new MethodInfo(constPool, methodInfo.getName(), methodInfo, classMap);
            if (bl && bl2) {
                this.methodInfo.setSuperclass(string2);
            }
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode);
        }
    }

    @Override
    protected void extendToString(StringBuilder stringBuilder) {
        stringBuilder.append(' ');
        stringBuilder.append(this.getName());
        stringBuilder.append(' ');
        stringBuilder.append(this.methodInfo.getDescriptor());
    }

    public abstract String getLongName();

    public MethodInfo getMethodInfo() {
        this.declaringClass.checkModify();
        return this.methodInfo;
    }

    public MethodInfo getMethodInfo2() {
        return this.methodInfo;
    }

    @Override
    public int getModifiers() {
        return AccessFlag.toModifier(this.methodInfo.getAccessFlags());
    }

    @Override
    public void setModifiers(int n) {
        this.declaringClass.checkModify();
        this.methodInfo.setAccessFlags(AccessFlag.of(n));
    }

    @Override
    public boolean hasAnnotation(String string) {
        MethodInfo methodInfo = this.getMethodInfo2();
        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute)methodInfo.getAttribute("RuntimeInvisibleAnnotations");
        AnnotationsAttribute annotationsAttribute2 = (AnnotationsAttribute)methodInfo.getAttribute("RuntimeVisibleAnnotations");
        return CtClassType.hasAnnotationType(string, this.getDeclaringClass().getClassPool(), annotationsAttribute, annotationsAttribute2);
    }

    @Override
    public Object getAnnotation(Class<?> clazz) {
        MethodInfo methodInfo = this.getMethodInfo2();
        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute)methodInfo.getAttribute("RuntimeInvisibleAnnotations");
        AnnotationsAttribute annotationsAttribute2 = (AnnotationsAttribute)methodInfo.getAttribute("RuntimeVisibleAnnotations");
        return CtClassType.getAnnotationType(clazz, this.getDeclaringClass().getClassPool(), annotationsAttribute, annotationsAttribute2);
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
            throw new RuntimeException("Unexpected exception", classNotFoundException);
        }
    }

    private Object[] getAnnotations(boolean bl) {
        MethodInfo methodInfo = this.getMethodInfo2();
        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute)methodInfo.getAttribute("RuntimeInvisibleAnnotations");
        AnnotationsAttribute annotationsAttribute2 = (AnnotationsAttribute)methodInfo.getAttribute("RuntimeVisibleAnnotations");
        return CtClassType.toAnnotationType(bl, this.getDeclaringClass().getClassPool(), annotationsAttribute, annotationsAttribute2);
    }

    public Object[][] getParameterAnnotations() {
        return this.getParameterAnnotations(false);
    }

    public Object[][] getAvailableParameterAnnotations() {
        try {
            return this.getParameterAnnotations(true);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new RuntimeException("Unexpected exception", classNotFoundException);
        }
    }

    Object[][] getParameterAnnotations(boolean bl) {
        MethodInfo methodInfo = this.getMethodInfo2();
        ParameterAnnotationsAttribute parameterAnnotationsAttribute = (ParameterAnnotationsAttribute)methodInfo.getAttribute("RuntimeInvisibleParameterAnnotations");
        ParameterAnnotationsAttribute parameterAnnotationsAttribute2 = (ParameterAnnotationsAttribute)methodInfo.getAttribute("RuntimeVisibleParameterAnnotations");
        return CtClassType.toAnnotationType(bl, this.getDeclaringClass().getClassPool(), parameterAnnotationsAttribute, parameterAnnotationsAttribute2, methodInfo);
    }

    public CtClass[] getParameterTypes() {
        return Descriptor.getParameterTypes(this.methodInfo.getDescriptor(), this.declaringClass.getClassPool());
    }

    CtClass getReturnType0() {
        return Descriptor.getReturnType(this.methodInfo.getDescriptor(), this.declaringClass.getClassPool());
    }

    @Override
    public String getSignature() {
        return this.methodInfo.getDescriptor();
    }

    @Override
    public String getGenericSignature() {
        SignatureAttribute signatureAttribute = (SignatureAttribute)this.methodInfo.getAttribute("Signature");
        return signatureAttribute == null ? null : signatureAttribute.getSignature();
    }

    @Override
    public void setGenericSignature(String string) {
        this.declaringClass.checkModify();
        this.methodInfo.addAttribute(new SignatureAttribute(this.methodInfo.getConstPool(), string));
    }

    public CtClass[] getExceptionTypes() {
        ExceptionsAttribute exceptionsAttribute = this.methodInfo.getExceptionsAttribute();
        String[] stringArray = exceptionsAttribute == null ? null : exceptionsAttribute.getExceptions();
        return this.declaringClass.getClassPool().get(stringArray);
    }

    public void setExceptionTypes(CtClass[] ctClassArray) {
        this.declaringClass.checkModify();
        if (ctClassArray == null || ctClassArray.length == 0) {
            this.methodInfo.removeExceptionsAttribute();
            return;
        }
        String[] stringArray = new String[ctClassArray.length];
        for (int i = 0; i < ctClassArray.length; ++i) {
            stringArray[i] = ctClassArray[i].getName();
        }
        ExceptionsAttribute exceptionsAttribute = this.methodInfo.getExceptionsAttribute();
        if (exceptionsAttribute == null) {
            exceptionsAttribute = new ExceptionsAttribute(this.methodInfo.getConstPool());
            this.methodInfo.setExceptionsAttribute(exceptionsAttribute);
        }
        exceptionsAttribute.setExceptions(stringArray);
    }

    public abstract boolean isEmpty();

    public void setBody(String string) {
        this.setBody(string, null, null);
    }

    public void setBody(String string, String string2, String string3) {
        CtClass ctClass = this.declaringClass;
        ctClass.checkModify();
        try {
            Javac javac = new Javac(ctClass);
            if (string3 != null) {
                javac.recordProceed(string2, string3);
            }
            Bytecode bytecode = javac.compileBody(this, string);
            this.methodInfo.setCodeAttribute(bytecode.toCodeAttribute());
            this.methodInfo.setAccessFlags(this.methodInfo.getAccessFlags() & 0xFFFFFBFF);
            this.methodInfo.rebuildStackMapIf6(ctClass.getClassPool(), ctClass.getClassFile2());
            this.declaringClass.rebuildClassFile();
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode);
        }
    }

    static void setBody0(CtClass ctClass, MethodInfo methodInfo, CtClass ctClass2, MethodInfo methodInfo2, ClassMap classMap) {
        ctClass2.checkModify();
        classMap = new ClassMap(classMap);
        classMap.put(ctClass.getName(), ctClass2.getName());
        try {
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            if (codeAttribute != null) {
                ConstPool constPool = methodInfo2.getConstPool();
                CodeAttribute codeAttribute2 = (CodeAttribute)codeAttribute.copy(constPool, classMap);
                methodInfo2.setCodeAttribute(codeAttribute2);
            }
        } catch (CodeAttribute.RuntimeCopyException runtimeCopyException) {
            throw new CannotCompileException(runtimeCopyException);
        }
        methodInfo2.setAccessFlags(methodInfo2.getAccessFlags() & 0xFFFFFBFF);
        ctClass2.rebuildClassFile();
    }

    @Override
    public byte[] getAttribute(String string) {
        AttributeInfo attributeInfo = this.methodInfo.getAttribute(string);
        if (attributeInfo == null) {
            return null;
        }
        return attributeInfo.get();
    }

    @Override
    public void setAttribute(String string, byte[] byArray) {
        this.declaringClass.checkModify();
        this.methodInfo.addAttribute(new AttributeInfo(this.methodInfo.getConstPool(), string, byArray));
    }

    public void useCflow(String string) {
        CtClass ctClass = this.declaringClass;
        ctClass.checkModify();
        ClassPool classPool = ctClass.getClassPool();
        int n = 0;
        while (true) {
            String string2 = "_cflow$" + n++;
            try {
                ctClass.getDeclaredField(string2);
            } catch (NotFoundException notFoundException) {
                classPool.recordCflow(string, this.declaringClass.getName(), string2);
                try {
                    CtClass ctClass2 = classPool.get("me.zombie_striker.qav.hooks.worldguard.shaded.javassist.runtime.Cflow");
                    CtField ctField = new CtField(ctClass2, string2, ctClass);
                    ctField.setModifiers(9);
                    ctClass.addField(ctField, CtField.Initializer.byNew(ctClass2));
                    this.insertBefore(string2 + ".enter();", false);
                    String string3 = string2 + ".exit();";
                    this.insertAfter(string3, true);
                } catch (NotFoundException notFoundException2) {
                    throw new CannotCompileException(notFoundException2);
                }
                return;
            }
        }
    }

    public void addLocalVariable(String string, CtClass ctClass) {
        this.declaringClass.checkModify();
        ConstPool constPool = this.methodInfo.getConstPool();
        CodeAttribute codeAttribute = this.methodInfo.getCodeAttribute();
        if (codeAttribute == null) {
            throw new CannotCompileException("no method body");
        }
        LocalVariableAttribute localVariableAttribute = (LocalVariableAttribute)codeAttribute.getAttribute("LocalVariableTable");
        if (localVariableAttribute == null) {
            localVariableAttribute = new LocalVariableAttribute(constPool);
            codeAttribute.getAttributes().add(localVariableAttribute);
        }
        int n = codeAttribute.getMaxLocals();
        String string2 = Descriptor.of(ctClass);
        localVariableAttribute.addEntry(0, codeAttribute.getCodeLength(), constPool.addUtf8Info(string), constPool.addUtf8Info(string2), n);
        codeAttribute.setMaxLocals(n + Descriptor.dataSize(string2));
    }

    public void insertParameter(CtClass ctClass) {
        this.declaringClass.checkModify();
        String string = this.methodInfo.getDescriptor();
        String string2 = Descriptor.insertParameter(ctClass, string);
        try {
            this.addParameter2(Modifier.isStatic(this.getModifiers()) ? 0 : 1, ctClass, string);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode);
        }
        this.methodInfo.setDescriptor(string2);
    }

    public void addParameter(CtClass ctClass) {
        this.declaringClass.checkModify();
        String string = this.methodInfo.getDescriptor();
        String string2 = Descriptor.appendParameter(ctClass, string);
        int n = Modifier.isStatic(this.getModifiers()) ? 0 : 1;
        try {
            this.addParameter2(n + Descriptor.paramSize(string), ctClass, string);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode);
        }
        this.methodInfo.setDescriptor(string2);
    }

    private void addParameter2(int n, CtClass ctClass, String string) {
        CodeAttribute codeAttribute = this.methodInfo.getCodeAttribute();
        if (codeAttribute != null) {
            StackMap stackMap;
            StackMapTable stackMapTable;
            LocalVariableTypeAttribute localVariableTypeAttribute;
            Object object;
            int n2 = 1;
            char c = 'L';
            int n3 = 0;
            if (ctClass.isPrimitive()) {
                object = (CtPrimitiveType)ctClass;
                n2 = ((CtPrimitiveType)object).getDataSize();
                c = ((CtPrimitiveType)object).getDescriptor();
            } else {
                n3 = this.methodInfo.getConstPool().addClassInfo(ctClass);
            }
            codeAttribute.insertLocalVar(n, n2);
            object = (LocalVariableAttribute)codeAttribute.getAttribute("LocalVariableTable");
            if (object != null) {
                ((LocalVariableAttribute)object).shiftIndex(n, n2);
            }
            if ((localVariableTypeAttribute = (LocalVariableTypeAttribute)codeAttribute.getAttribute("LocalVariableTypeTable")) != null) {
                localVariableTypeAttribute.shiftIndex(n, n2);
            }
            if ((stackMapTable = (StackMapTable)codeAttribute.getAttribute("StackMapTable")) != null) {
                stackMapTable.insertLocal(n, StackMapTable.typeTagOf(c), n3);
            }
            if ((stackMap = (StackMap)codeAttribute.getAttribute("StackMap")) != null) {
                stackMap.insertLocal(n, StackMapTable.typeTagOf(c), n3);
            }
        }
    }

    public void instrument(CodeConverter codeConverter) {
        this.declaringClass.checkModify();
        ConstPool constPool = this.methodInfo.getConstPool();
        codeConverter.doit(this.getDeclaringClass(), this.methodInfo, constPool);
    }

    public void instrument(ExprEditor exprEditor) {
        if (this.declaringClass.isFrozen()) {
            this.declaringClass.checkModify();
        }
        if (exprEditor.doit(this.declaringClass, this.methodInfo)) {
            this.declaringClass.checkModify();
        }
    }

    public void insertBefore(String string) {
        this.insertBefore(string, true);
    }

    private void insertBefore(String string, boolean bl) {
        CtClass ctClass = this.declaringClass;
        ctClass.checkModify();
        CodeAttribute codeAttribute = this.methodInfo.getCodeAttribute();
        if (codeAttribute == null) {
            throw new CannotCompileException("no method body");
        }
        CodeIterator codeIterator = codeAttribute.iterator();
        Javac javac = new Javac(ctClass);
        try {
            int n = javac.recordParams(this.getParameterTypes(), Modifier.isStatic(this.getModifiers()));
            javac.recordParamNames(codeAttribute, n);
            javac.recordLocalVariables(codeAttribute, 0);
            javac.recordReturnType(this.getReturnType0(), false);
            javac.compileStmnt(string);
            Bytecode bytecode = javac.getBytecode();
            int n2 = bytecode.getMaxStack();
            int n3 = bytecode.getMaxLocals();
            if (n2 > codeAttribute.getMaxStack()) {
                codeAttribute.setMaxStack(n2);
            }
            if (n3 > codeAttribute.getMaxLocals()) {
                codeAttribute.setMaxLocals(n3);
            }
            int n4 = codeIterator.insertEx(bytecode.get());
            codeIterator.insert(bytecode.getExceptionTable(), n4);
            if (bl) {
                this.methodInfo.rebuildStackMapIf6(ctClass.getClassPool(), ctClass.getClassFile2());
            }
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode);
        }
    }

    public void insertAfter(String string) {
        this.insertAfter(string, false, false);
    }

    public void insertAfter(String string, boolean bl) {
        this.insertAfter(string, bl, false);
    }

    public void insertAfter(String string, boolean bl, boolean bl2) {
        CtClass ctClass = this.declaringClass;
        ctClass.checkModify();
        ConstPool constPool = this.methodInfo.getConstPool();
        CodeAttribute codeAttribute = this.methodInfo.getCodeAttribute();
        if (codeAttribute == null) {
            throw new CannotCompileException("no method body");
        }
        CodeIterator codeIterator = codeAttribute.iterator();
        int n = codeAttribute.getMaxLocals();
        Bytecode bytecode = new Bytecode(constPool, 0, n + 1);
        bytecode.setStackDepth(codeAttribute.getMaxStack() + 1);
        Javac javac = new Javac(bytecode, ctClass);
        try {
            int n2;
            int n3 = javac.recordParams(this.getParameterTypes(), Modifier.isStatic(this.getModifiers()));
            javac.recordParamNames(codeAttribute, n3);
            CtClass ctClass2 = this.getReturnType0();
            int n4 = javac.recordReturnType(ctClass2, true);
            javac.recordLocalVariables(codeAttribute, 0);
            int n5 = this.insertAfterHandler(bl, bytecode, ctClass2, n4, javac, string);
            int n6 = codeIterator.getCodeLength();
            if (bl) {
                codeAttribute.getExceptionTable().add(this.getStartPosOfBody(codeAttribute), n6, n6, 0);
            }
            int n7 = 0;
            int n8 = 0;
            boolean bl3 = true;
            while (codeIterator.hasNext() && (n2 = codeIterator.next()) < n6) {
                int n9 = codeIterator.byteAt(n2);
                if (n9 != 176 && n9 != 172 && n9 != 174 && n9 != 173 && n9 != 175 && n9 != 177) continue;
                if (bl2) {
                    int n10;
                    int n11;
                    Javac javac2;
                    Bytecode bytecode2;
                    codeIterator.setMark2(n6);
                    if (bl3) {
                        bl3 = false;
                        bytecode2 = bytecode;
                        javac2 = javac;
                        n11 = n4;
                    } else {
                        bytecode2 = new Bytecode(constPool, 0, n + 1);
                        bytecode2.setStackDepth(codeAttribute.getMaxStack() + 1);
                        javac2 = new Javac(bytecode2, ctClass);
                        n10 = javac2.recordParams(this.getParameterTypes(), Modifier.isStatic(this.getModifiers()));
                        javac2.recordParamNames(codeAttribute, n10);
                        n11 = javac2.recordReturnType(ctClass2, true);
                        javac2.recordLocalVariables(codeAttribute, 0);
                    }
                    n10 = this.insertAfterAdvice(bytecode2, javac2, string, constPool, ctClass2, n11);
                    int n12 = codeIterator.append(bytecode2.get());
                    codeIterator.append(bytecode2.getExceptionTable(), n12);
                    int n13 = codeIterator.getCodeLength() - n10;
                    this.insertGoto(codeIterator, n13, n2);
                    n6 = codeIterator.getMark2();
                    continue;
                }
                if (bl3) {
                    n7 = this.insertAfterAdvice(bytecode, javac, string, constPool, ctClass2, n4);
                    n6 = codeIterator.append(bytecode.get());
                    codeIterator.append(bytecode.getExceptionTable(), n6);
                    n8 = codeIterator.getCodeLength() - n7;
                    n5 = n8 - n6;
                    bl3 = false;
                }
                this.insertGoto(codeIterator, n8, n2);
                n8 = codeIterator.getCodeLength() - n7;
                n6 = n8 - n5;
            }
            if (bl3) {
                n6 = codeIterator.append(bytecode.get());
                codeIterator.append(bytecode.getExceptionTable(), n6);
            }
            codeAttribute.setMaxStack(bytecode.getMaxStack());
            codeAttribute.setMaxLocals(bytecode.getMaxLocals());
            this.methodInfo.rebuildStackMapIf6(ctClass.getClassPool(), ctClass.getClassFile2());
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode);
        }
    }

    private int insertAfterAdvice(Bytecode bytecode, Javac javac, String string, ConstPool constPool, CtClass ctClass, int n) {
        int n2 = bytecode.currentPc();
        if (ctClass == CtClass.voidType) {
            bytecode.addOpcode(1);
            bytecode.addAstore(n);
            javac.compileStmnt(string);
            bytecode.addOpcode(177);
            if (bytecode.getMaxLocals() < 1) {
                bytecode.setMaxLocals(1);
            }
        } else {
            bytecode.addStore(n, ctClass);
            javac.compileStmnt(string);
            bytecode.addLoad(n, ctClass);
            if (ctClass.isPrimitive()) {
                bytecode.addOpcode(((CtPrimitiveType)ctClass).getReturnOp());
            } else {
                bytecode.addOpcode(176);
            }
        }
        return bytecode.currentPc() - n2;
    }

    private void insertGoto(CodeIterator codeIterator, int n, int n2) {
        codeIterator.setMark(n);
        codeIterator.writeByte(0, n2);
        boolean bl = n + 2 - n2 > Short.MAX_VALUE;
        int n3 = bl ? 4 : 2;
        CodeIterator.Gap gap = codeIterator.insertGapAt(n2, n3, false);
        n2 = gap.position + gap.length - n3;
        int n4 = codeIterator.getMark() - n2;
        if (bl) {
            codeIterator.writeByte(200, n2);
            codeIterator.write32bit(n4, n2 + 1);
        } else if (n4 <= Short.MAX_VALUE) {
            codeIterator.writeByte(167, n2);
            codeIterator.write16bit(n4, n2 + 1);
        } else {
            if (gap.length < 4) {
                CodeIterator.Gap gap2 = codeIterator.insertGapAt(gap.position, 2, false);
                n2 = gap2.position + gap2.length + gap.length - 4;
            }
            codeIterator.writeByte(200, n2);
            codeIterator.write32bit(codeIterator.getMark() - n2, n2 + 1);
        }
    }

    private int insertAfterHandler(boolean bl, Bytecode bytecode, CtClass ctClass, int n, Javac javac, String string) {
        if (!bl) {
            return 0;
        }
        int n2 = bytecode.getMaxLocals();
        bytecode.incMaxLocals(1);
        int n3 = bytecode.currentPc();
        bytecode.addAstore(n2);
        if (ctClass.isPrimitive()) {
            char c = ((CtPrimitiveType)ctClass).getDescriptor();
            if (c == 'D') {
                bytecode.addDconst(0.0);
                bytecode.addDstore(n);
            } else if (c == 'F') {
                bytecode.addFconst(0.0f);
                bytecode.addFstore(n);
            } else if (c == 'J') {
                bytecode.addLconst(0L);
                bytecode.addLstore(n);
            } else if (c == 'V') {
                bytecode.addOpcode(1);
                bytecode.addAstore(n);
            } else {
                bytecode.addIconst(0);
                bytecode.addIstore(n);
            }
        } else {
            bytecode.addOpcode(1);
            bytecode.addAstore(n);
        }
        javac.compileStmnt(string);
        bytecode.addAload(n2);
        bytecode.addOpcode(191);
        return bytecode.currentPc() - n3;
    }

    public void addCatch(String string, CtClass ctClass) {
        this.addCatch(string, ctClass, "$e");
    }

    public void addCatch(String string, CtClass ctClass, String string2) {
        CtClass ctClass2 = this.declaringClass;
        ctClass2.checkModify();
        ConstPool constPool = this.methodInfo.getConstPool();
        CodeAttribute codeAttribute = this.methodInfo.getCodeAttribute();
        CodeIterator codeIterator = codeAttribute.iterator();
        Bytecode bytecode = new Bytecode(constPool, codeAttribute.getMaxStack(), codeAttribute.getMaxLocals());
        bytecode.setStackDepth(1);
        Javac javac = new Javac(bytecode, ctClass2);
        try {
            javac.recordParams(this.getParameterTypes(), Modifier.isStatic(this.getModifiers()));
            int n = javac.recordVariable(ctClass, string2);
            bytecode.addAstore(n);
            javac.compileStmnt(string);
            int n2 = bytecode.getMaxStack();
            int n3 = bytecode.getMaxLocals();
            if (n2 > codeAttribute.getMaxStack()) {
                codeAttribute.setMaxStack(n2);
            }
            if (n3 > codeAttribute.getMaxLocals()) {
                codeAttribute.setMaxLocals(n3);
            }
            int n4 = codeIterator.getCodeLength();
            int n5 = codeIterator.append(bytecode.get());
            codeAttribute.getExceptionTable().add(this.getStartPosOfBody(codeAttribute), n4, n4, constPool.addClassInfo(ctClass));
            codeIterator.append(bytecode.getExceptionTable(), n5);
            this.methodInfo.rebuildStackMapIf6(ctClass2.getClassPool(), ctClass2.getClassFile2());
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode);
        }
    }

    int getStartPosOfBody(CodeAttribute codeAttribute) {
        return 0;
    }

    public int insertAt(int n, String string) {
        return this.insertAt(n, true, string);
    }

    public int insertAt(int n, boolean bl, String string) {
        CodeAttribute codeAttribute = this.methodInfo.getCodeAttribute();
        if (codeAttribute == null) {
            throw new CannotCompileException("no method body");
        }
        LineNumberAttribute lineNumberAttribute = (LineNumberAttribute)codeAttribute.getAttribute("LineNumberTable");
        if (lineNumberAttribute == null) {
            throw new CannotCompileException("no line number info");
        }
        LineNumberAttribute.Pc pc = lineNumberAttribute.toNearPc(n);
        n = pc.line;
        int n2 = pc.index;
        if (!bl) {
            return n;
        }
        CtClass ctClass = this.declaringClass;
        ctClass.checkModify();
        CodeIterator codeIterator = codeAttribute.iterator();
        Javac javac = new Javac(ctClass);
        try {
            javac.recordLocalVariables(codeAttribute, n2);
            javac.recordParams(this.getParameterTypes(), Modifier.isStatic(this.getModifiers()));
            javac.setMaxLocals(codeAttribute.getMaxLocals());
            javac.compileStmnt(string);
            Bytecode bytecode = javac.getBytecode();
            int n3 = bytecode.getMaxLocals();
            int n4 = bytecode.getMaxStack();
            codeAttribute.setMaxLocals(n3);
            if (n4 > codeAttribute.getMaxStack()) {
                codeAttribute.setMaxStack(n4);
            }
            n2 = codeIterator.insertAt(n2, bytecode.get());
            codeIterator.insert(bytecode.getExceptionTable(), n2);
            this.methodInfo.rebuildStackMapIf6(ctClass.getClassPool(), ctClass.getClassFile2());
            return n;
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode);
        }
    }
}

