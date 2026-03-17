/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import java.util.List;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClassType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMember;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtNewWrappedMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtPrimitiveType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AccessFlag;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AnnotationsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.FieldInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.SignatureAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.Javac;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.SymbolTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.DoubleConst;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.IntConst;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.StringL;

public class CtField
extends CtMember {
    static final String javaLangString = "java.lang.String";
    protected FieldInfo fieldInfo;

    public CtField(CtClass ctClass, String string, CtClass ctClass2) {
        this(Descriptor.of(ctClass), string, ctClass2);
    }

    public CtField(CtField ctField, CtClass ctClass) {
        this(ctField.fieldInfo.getDescriptor(), ctField.fieldInfo.getName(), ctClass);
        FieldInfo fieldInfo = this.fieldInfo;
        fieldInfo.setAccessFlags(ctField.fieldInfo.getAccessFlags());
        ConstPool constPool = fieldInfo.getConstPool();
        List<AttributeInfo> list = ctField.fieldInfo.getAttributes();
        for (AttributeInfo attributeInfo : list) {
            fieldInfo.addAttribute(attributeInfo.copy(constPool, null));
        }
    }

    private CtField(String string, String string2, CtClass ctClass) {
        super(ctClass);
        ClassFile classFile = ctClass.getClassFile2();
        if (classFile == null) {
            throw new CannotCompileException("bad declaring class: " + ctClass.getName());
        }
        this.fieldInfo = new FieldInfo(classFile.getConstPool(), string2, string);
    }

    CtField(FieldInfo fieldInfo, CtClass ctClass) {
        super(ctClass);
        this.fieldInfo = fieldInfo;
    }

    @Override
    public String toString() {
        return this.getDeclaringClass().getName() + "." + this.getName() + ":" + this.fieldInfo.getDescriptor();
    }

    @Override
    protected void extendToString(StringBuilder stringBuilder) {
        stringBuilder.append(' ');
        stringBuilder.append(this.getName());
        stringBuilder.append(' ');
        stringBuilder.append(this.fieldInfo.getDescriptor());
    }

    protected ASTree getInitAST() {
        return null;
    }

    Initializer getInit() {
        ASTree aSTree = this.getInitAST();
        if (aSTree == null) {
            return null;
        }
        return Initializer.byExpr(aSTree);
    }

    public static CtField make(String string, CtClass ctClass) {
        Javac javac = new Javac(ctClass);
        try {
            CtMember ctMember = javac.compile(string);
            if (ctMember instanceof CtField) {
                return (CtField)ctMember;
            }
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        }
        throw new CannotCompileException("not a field");
    }

    public FieldInfo getFieldInfo() {
        this.declaringClass.checkModify();
        return this.fieldInfo;
    }

    public FieldInfo getFieldInfo2() {
        return this.fieldInfo;
    }

    @Override
    public CtClass getDeclaringClass() {
        return super.getDeclaringClass();
    }

    @Override
    public String getName() {
        return this.fieldInfo.getName();
    }

    public void setName(String string) {
        this.declaringClass.checkModify();
        this.fieldInfo.setName(string);
    }

    @Override
    public int getModifiers() {
        return AccessFlag.toModifier(this.fieldInfo.getAccessFlags());
    }

    @Override
    public void setModifiers(int n) {
        this.declaringClass.checkModify();
        this.fieldInfo.setAccessFlags(AccessFlag.of(n));
    }

    @Override
    public boolean hasAnnotation(String string) {
        FieldInfo fieldInfo = this.getFieldInfo2();
        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute)fieldInfo.getAttribute("RuntimeInvisibleAnnotations");
        AnnotationsAttribute annotationsAttribute2 = (AnnotationsAttribute)fieldInfo.getAttribute("RuntimeVisibleAnnotations");
        return CtClassType.hasAnnotationType(string, this.getDeclaringClass().getClassPool(), annotationsAttribute, annotationsAttribute2);
    }

    @Override
    public Object getAnnotation(Class<?> clazz) {
        FieldInfo fieldInfo = this.getFieldInfo2();
        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute)fieldInfo.getAttribute("RuntimeInvisibleAnnotations");
        AnnotationsAttribute annotationsAttribute2 = (AnnotationsAttribute)fieldInfo.getAttribute("RuntimeVisibleAnnotations");
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
        FieldInfo fieldInfo = this.getFieldInfo2();
        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute)fieldInfo.getAttribute("RuntimeInvisibleAnnotations");
        AnnotationsAttribute annotationsAttribute2 = (AnnotationsAttribute)fieldInfo.getAttribute("RuntimeVisibleAnnotations");
        return CtClassType.toAnnotationType(bl, this.getDeclaringClass().getClassPool(), annotationsAttribute, annotationsAttribute2);
    }

    @Override
    public String getSignature() {
        return this.fieldInfo.getDescriptor();
    }

    @Override
    public String getGenericSignature() {
        SignatureAttribute signatureAttribute = (SignatureAttribute)this.fieldInfo.getAttribute("Signature");
        return signatureAttribute == null ? null : signatureAttribute.getSignature();
    }

    @Override
    public void setGenericSignature(String string) {
        this.declaringClass.checkModify();
        this.fieldInfo.addAttribute(new SignatureAttribute(this.fieldInfo.getConstPool(), string));
    }

    public CtClass getType() {
        return Descriptor.toCtClass(this.fieldInfo.getDescriptor(), this.declaringClass.getClassPool());
    }

    public void setType(CtClass ctClass) {
        this.declaringClass.checkModify();
        this.fieldInfo.setDescriptor(Descriptor.of(ctClass));
    }

    public Object getConstantValue() {
        int n = this.fieldInfo.getConstantValue();
        if (n == 0) {
            return null;
        }
        ConstPool constPool = this.fieldInfo.getConstPool();
        switch (constPool.getTag(n)) {
            case 5: {
                return constPool.getLongInfo(n);
            }
            case 4: {
                return Float.valueOf(constPool.getFloatInfo(n));
            }
            case 6: {
                return constPool.getDoubleInfo(n);
            }
            case 3: {
                int n2 = constPool.getIntegerInfo(n);
                if ("Z".equals(this.fieldInfo.getDescriptor())) {
                    return n2 != 0;
                }
                return n2;
            }
            case 8: {
                return constPool.getStringInfo(n);
            }
        }
        throw new RuntimeException("bad tag: " + constPool.getTag(n) + " at " + n);
    }

    @Override
    public byte[] getAttribute(String string) {
        AttributeInfo attributeInfo = this.fieldInfo.getAttribute(string);
        if (attributeInfo == null) {
            return null;
        }
        return attributeInfo.get();
    }

    @Override
    public void setAttribute(String string, byte[] byArray) {
        this.declaringClass.checkModify();
        this.fieldInfo.addAttribute(new AttributeInfo(this.fieldInfo.getConstPool(), string, byArray));
    }

    public static abstract class Initializer {
        public static Initializer constant(int n) {
            return new IntInitializer(n);
        }

        public static Initializer constant(boolean bl) {
            return new IntInitializer(bl ? 1 : 0);
        }

        public static Initializer constant(long l) {
            return new LongInitializer(l);
        }

        public static Initializer constant(float f) {
            return new FloatInitializer(f);
        }

        public static Initializer constant(double d) {
            return new DoubleInitializer(d);
        }

        public static Initializer constant(String string) {
            return new StringInitializer(string);
        }

        public static Initializer byParameter(int n) {
            ParamInitializer paramInitializer = new ParamInitializer();
            paramInitializer.nthParam = n;
            return paramInitializer;
        }

        public static Initializer byNew(CtClass ctClass) {
            NewInitializer newInitializer = new NewInitializer();
            newInitializer.objectType = ctClass;
            newInitializer.stringParams = null;
            newInitializer.withConstructorParams = false;
            return newInitializer;
        }

        public static Initializer byNew(CtClass ctClass, String[] stringArray) {
            NewInitializer newInitializer = new NewInitializer();
            newInitializer.objectType = ctClass;
            newInitializer.stringParams = stringArray;
            newInitializer.withConstructorParams = false;
            return newInitializer;
        }

        public static Initializer byNewWithParams(CtClass ctClass) {
            NewInitializer newInitializer = new NewInitializer();
            newInitializer.objectType = ctClass;
            newInitializer.stringParams = null;
            newInitializer.withConstructorParams = true;
            return newInitializer;
        }

        public static Initializer byNewWithParams(CtClass ctClass, String[] stringArray) {
            NewInitializer newInitializer = new NewInitializer();
            newInitializer.objectType = ctClass;
            newInitializer.stringParams = stringArray;
            newInitializer.withConstructorParams = true;
            return newInitializer;
        }

        public static Initializer byCall(CtClass ctClass, String string) {
            MethodInitializer methodInitializer = new MethodInitializer();
            methodInitializer.objectType = ctClass;
            methodInitializer.methodName = string;
            methodInitializer.stringParams = null;
            methodInitializer.withConstructorParams = false;
            return methodInitializer;
        }

        public static Initializer byCall(CtClass ctClass, String string, String[] stringArray) {
            MethodInitializer methodInitializer = new MethodInitializer();
            methodInitializer.objectType = ctClass;
            methodInitializer.methodName = string;
            methodInitializer.stringParams = stringArray;
            methodInitializer.withConstructorParams = false;
            return methodInitializer;
        }

        public static Initializer byCallWithParams(CtClass ctClass, String string) {
            MethodInitializer methodInitializer = new MethodInitializer();
            methodInitializer.objectType = ctClass;
            methodInitializer.methodName = string;
            methodInitializer.stringParams = null;
            methodInitializer.withConstructorParams = true;
            return methodInitializer;
        }

        public static Initializer byCallWithParams(CtClass ctClass, String string, String[] stringArray) {
            MethodInitializer methodInitializer = new MethodInitializer();
            methodInitializer.objectType = ctClass;
            methodInitializer.methodName = string;
            methodInitializer.stringParams = stringArray;
            methodInitializer.withConstructorParams = true;
            return methodInitializer;
        }

        public static Initializer byNewArray(CtClass ctClass, int n) {
            return new ArrayInitializer(ctClass.getComponentType(), n);
        }

        public static Initializer byNewArray(CtClass ctClass, int[] nArray) {
            return new MultiArrayInitializer(ctClass, nArray);
        }

        public static Initializer byExpr(String string) {
            return new CodeInitializer(string);
        }

        static Initializer byExpr(ASTree aSTree) {
            return new PtreeInitializer(aSTree);
        }

        void check(String string) {
        }

        abstract int compile(CtClass var1, String var2, Bytecode var3, CtClass[] var4, Javac var5);

        abstract int compileIfStatic(CtClass var1, String var2, Bytecode var3, Javac var4);

        int getConstantValue(ConstPool constPool, CtClass ctClass) {
            return 0;
        }
    }

    static class MultiArrayInitializer
    extends Initializer {
        CtClass type;
        int[] dim;

        MultiArrayInitializer(CtClass ctClass, int[] nArray) {
            this.type = ctClass;
            this.dim = nArray;
        }

        @Override
        void check(String string) {
            if (string.charAt(0) != '[') {
                throw new CannotCompileException("type mismatch");
            }
        }

        @Override
        int compile(CtClass ctClass, String string, Bytecode bytecode, CtClass[] ctClassArray, Javac javac) {
            bytecode.addAload(0);
            int n = bytecode.addMultiNewarray(ctClass, this.dim);
            bytecode.addPutfield(Bytecode.THIS, string, Descriptor.of(ctClass));
            return n + 1;
        }

        @Override
        int compileIfStatic(CtClass ctClass, String string, Bytecode bytecode, Javac javac) {
            int n = bytecode.addMultiNewarray(ctClass, this.dim);
            bytecode.addPutstatic(Bytecode.THIS, string, Descriptor.of(ctClass));
            return n;
        }
    }

    static class ArrayInitializer
    extends Initializer {
        CtClass type;
        int size;

        ArrayInitializer(CtClass ctClass, int n) {
            this.type = ctClass;
            this.size = n;
        }

        private void addNewarray(Bytecode bytecode) {
            if (this.type.isPrimitive()) {
                bytecode.addNewarray(((CtPrimitiveType)this.type).getArrayType(), this.size);
            } else {
                bytecode.addAnewarray(this.type, this.size);
            }
        }

        @Override
        int compile(CtClass ctClass, String string, Bytecode bytecode, CtClass[] ctClassArray, Javac javac) {
            bytecode.addAload(0);
            this.addNewarray(bytecode);
            bytecode.addPutfield(Bytecode.THIS, string, Descriptor.of(ctClass));
            return 2;
        }

        @Override
        int compileIfStatic(CtClass ctClass, String string, Bytecode bytecode, Javac javac) {
            this.addNewarray(bytecode);
            bytecode.addPutstatic(Bytecode.THIS, string, Descriptor.of(ctClass));
            return 1;
        }
    }

    static class StringInitializer
    extends Initializer {
        String value;

        StringInitializer(String string) {
            this.value = string;
        }

        @Override
        int compile(CtClass ctClass, String string, Bytecode bytecode, CtClass[] ctClassArray, Javac javac) {
            bytecode.addAload(0);
            bytecode.addLdc(this.value);
            bytecode.addPutfield(Bytecode.THIS, string, Descriptor.of(ctClass));
            return 2;
        }

        @Override
        int compileIfStatic(CtClass ctClass, String string, Bytecode bytecode, Javac javac) {
            bytecode.addLdc(this.value);
            bytecode.addPutstatic(Bytecode.THIS, string, Descriptor.of(ctClass));
            return 1;
        }

        @Override
        int getConstantValue(ConstPool constPool, CtClass ctClass) {
            if (ctClass.getName().equals(CtField.javaLangString)) {
                return constPool.addStringInfo(this.value);
            }
            return 0;
        }
    }

    static class DoubleInitializer
    extends Initializer {
        double value;

        DoubleInitializer(double d) {
            this.value = d;
        }

        @Override
        void check(String string) {
            if (!string.equals("D")) {
                throw new CannotCompileException("type mismatch");
            }
        }

        @Override
        int compile(CtClass ctClass, String string, Bytecode bytecode, CtClass[] ctClassArray, Javac javac) {
            bytecode.addAload(0);
            bytecode.addLdc2w(this.value);
            bytecode.addPutfield(Bytecode.THIS, string, Descriptor.of(ctClass));
            return 3;
        }

        @Override
        int compileIfStatic(CtClass ctClass, String string, Bytecode bytecode, Javac javac) {
            bytecode.addLdc2w(this.value);
            bytecode.addPutstatic(Bytecode.THIS, string, Descriptor.of(ctClass));
            return 2;
        }

        @Override
        int getConstantValue(ConstPool constPool, CtClass ctClass) {
            if (ctClass == CtClass.doubleType) {
                return constPool.addDoubleInfo(this.value);
            }
            return 0;
        }
    }

    static class FloatInitializer
    extends Initializer {
        float value;

        FloatInitializer(float f) {
            this.value = f;
        }

        @Override
        void check(String string) {
            if (!string.equals("F")) {
                throw new CannotCompileException("type mismatch");
            }
        }

        @Override
        int compile(CtClass ctClass, String string, Bytecode bytecode, CtClass[] ctClassArray, Javac javac) {
            bytecode.addAload(0);
            bytecode.addFconst(this.value);
            bytecode.addPutfield(Bytecode.THIS, string, Descriptor.of(ctClass));
            return 3;
        }

        @Override
        int compileIfStatic(CtClass ctClass, String string, Bytecode bytecode, Javac javac) {
            bytecode.addFconst(this.value);
            bytecode.addPutstatic(Bytecode.THIS, string, Descriptor.of(ctClass));
            return 2;
        }

        @Override
        int getConstantValue(ConstPool constPool, CtClass ctClass) {
            if (ctClass == CtClass.floatType) {
                return constPool.addFloatInfo(this.value);
            }
            return 0;
        }
    }

    static class LongInitializer
    extends Initializer {
        long value;

        LongInitializer(long l) {
            this.value = l;
        }

        @Override
        void check(String string) {
            if (!string.equals("J")) {
                throw new CannotCompileException("type mismatch");
            }
        }

        @Override
        int compile(CtClass ctClass, String string, Bytecode bytecode, CtClass[] ctClassArray, Javac javac) {
            bytecode.addAload(0);
            bytecode.addLdc2w(this.value);
            bytecode.addPutfield(Bytecode.THIS, string, Descriptor.of(ctClass));
            return 3;
        }

        @Override
        int compileIfStatic(CtClass ctClass, String string, Bytecode bytecode, Javac javac) {
            bytecode.addLdc2w(this.value);
            bytecode.addPutstatic(Bytecode.THIS, string, Descriptor.of(ctClass));
            return 2;
        }

        @Override
        int getConstantValue(ConstPool constPool, CtClass ctClass) {
            if (ctClass == CtClass.longType) {
                return constPool.addLongInfo(this.value);
            }
            return 0;
        }
    }

    static class IntInitializer
    extends Initializer {
        int value;

        IntInitializer(int n) {
            this.value = n;
        }

        @Override
        void check(String string) {
            char c = string.charAt(0);
            if (c != 'I' && c != 'S' && c != 'B' && c != 'C' && c != 'Z') {
                throw new CannotCompileException("type mismatch");
            }
        }

        @Override
        int compile(CtClass ctClass, String string, Bytecode bytecode, CtClass[] ctClassArray, Javac javac) {
            bytecode.addAload(0);
            bytecode.addIconst(this.value);
            bytecode.addPutfield(Bytecode.THIS, string, Descriptor.of(ctClass));
            return 2;
        }

        @Override
        int compileIfStatic(CtClass ctClass, String string, Bytecode bytecode, Javac javac) {
            bytecode.addIconst(this.value);
            bytecode.addPutstatic(Bytecode.THIS, string, Descriptor.of(ctClass));
            return 1;
        }

        @Override
        int getConstantValue(ConstPool constPool, CtClass ctClass) {
            return constPool.addIntegerInfo(this.value);
        }
    }

    static class MethodInitializer
    extends NewInitializer {
        String methodName;

        MethodInitializer() {
        }

        @Override
        int compile(CtClass ctClass, String string, Bytecode bytecode, CtClass[] ctClassArray, Javac javac) {
            bytecode.addAload(0);
            bytecode.addAload(0);
            int n = this.stringParams == null ? 2 : this.compileStringParameter(bytecode) + 2;
            if (this.withConstructorParams) {
                n += CtNewWrappedMethod.compileParameterList(bytecode, ctClassArray, 1);
            }
            String string2 = Descriptor.of(ctClass);
            String string3 = this.getDescriptor() + string2;
            bytecode.addInvokestatic(this.objectType, this.methodName, string3);
            bytecode.addPutfield(Bytecode.THIS, string, string2);
            return n;
        }

        private String getDescriptor() {
            String string = "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)";
            if (this.stringParams == null) {
                if (this.withConstructorParams) {
                    return "(Ljava/lang/Object;[Ljava/lang/Object;)";
                }
                return "(Ljava/lang/Object;)";
            }
            if (this.withConstructorParams) {
                return "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)";
            }
            return "(Ljava/lang/Object;[Ljava/lang/String;)";
        }

        @Override
        int compileIfStatic(CtClass ctClass, String string, Bytecode bytecode, Javac javac) {
            String string2;
            int n = 1;
            if (this.stringParams == null) {
                string2 = "()";
            } else {
                string2 = "([Ljava/lang/String;)";
                n += this.compileStringParameter(bytecode);
            }
            String string3 = Descriptor.of(ctClass);
            bytecode.addInvokestatic(this.objectType, this.methodName, string2 + string3);
            bytecode.addPutstatic(Bytecode.THIS, string, string3);
            return n;
        }
    }

    static class NewInitializer
    extends Initializer {
        CtClass objectType;
        String[] stringParams;
        boolean withConstructorParams;

        NewInitializer() {
        }

        @Override
        int compile(CtClass ctClass, String string, Bytecode bytecode, CtClass[] ctClassArray, Javac javac) {
            bytecode.addAload(0);
            bytecode.addNew(this.objectType);
            bytecode.add(89);
            bytecode.addAload(0);
            int n = this.stringParams == null ? 4 : this.compileStringParameter(bytecode) + 4;
            if (this.withConstructorParams) {
                n += CtNewWrappedMethod.compileParameterList(bytecode, ctClassArray, 1);
            }
            bytecode.addInvokespecial(this.objectType, "<init>", this.getDescriptor());
            bytecode.addPutfield(Bytecode.THIS, string, Descriptor.of(ctClass));
            return n;
        }

        private String getDescriptor() {
            String string = "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)V";
            if (this.stringParams == null) {
                if (this.withConstructorParams) {
                    return "(Ljava/lang/Object;[Ljava/lang/Object;)V";
                }
                return "(Ljava/lang/Object;)V";
            }
            if (this.withConstructorParams) {
                return "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)V";
            }
            return "(Ljava/lang/Object;[Ljava/lang/String;)V";
        }

        @Override
        int compileIfStatic(CtClass ctClass, String string, Bytecode bytecode, Javac javac) {
            String string2;
            bytecode.addNew(this.objectType);
            bytecode.add(89);
            int n = 2;
            if (this.stringParams == null) {
                string2 = "()V";
            } else {
                string2 = "([Ljava/lang/String;)V";
                n += this.compileStringParameter(bytecode);
            }
            bytecode.addInvokespecial(this.objectType, "<init>", string2);
            bytecode.addPutstatic(Bytecode.THIS, string, Descriptor.of(ctClass));
            return n;
        }

        protected final int compileStringParameter(Bytecode bytecode) {
            int n = this.stringParams.length;
            bytecode.addIconst(n);
            bytecode.addAnewarray(CtField.javaLangString);
            for (int i = 0; i < n; ++i) {
                bytecode.add(89);
                bytecode.addIconst(i);
                bytecode.addLdc(this.stringParams[i]);
                bytecode.add(83);
            }
            return 4;
        }
    }

    static class ParamInitializer
    extends Initializer {
        int nthParam;

        ParamInitializer() {
        }

        @Override
        int compile(CtClass ctClass, String string, Bytecode bytecode, CtClass[] ctClassArray, Javac javac) {
            if (ctClassArray != null && this.nthParam < ctClassArray.length) {
                bytecode.addAload(0);
                int n = ParamInitializer.nthParamToLocal(this.nthParam, ctClassArray, false);
                int n2 = bytecode.addLoad(n, ctClass) + 1;
                bytecode.addPutfield(Bytecode.THIS, string, Descriptor.of(ctClass));
                return n2;
            }
            return 0;
        }

        static int nthParamToLocal(int n, CtClass[] ctClassArray, boolean bl) {
            CtClass ctClass = CtClass.longType;
            CtClass ctClass2 = CtClass.doubleType;
            int n2 = bl ? 0 : 1;
            for (int i = 0; i < n; ++i) {
                CtClass ctClass3 = ctClassArray[i];
                if (ctClass3 == ctClass || ctClass3 == ctClass2) {
                    n2 += 2;
                    continue;
                }
                ++n2;
            }
            return n2;
        }

        @Override
        int compileIfStatic(CtClass ctClass, String string, Bytecode bytecode, Javac javac) {
            return 0;
        }
    }

    static class PtreeInitializer
    extends CodeInitializer0 {
        private ASTree expression;

        PtreeInitializer(ASTree aSTree) {
            this.expression = aSTree;
        }

        @Override
        void compileExpr(Javac javac) {
            javac.compileExpr(this.expression);
        }

        @Override
        int getConstantValue(ConstPool constPool, CtClass ctClass) {
            return this.getConstantValue2(constPool, ctClass, this.expression);
        }
    }

    static class CodeInitializer
    extends CodeInitializer0 {
        private String expression;

        CodeInitializer(String string) {
            this.expression = string;
        }

        @Override
        void compileExpr(Javac javac) {
            javac.compileExpr(this.expression);
        }

        @Override
        int getConstantValue(ConstPool constPool, CtClass ctClass) {
            try {
                ASTree aSTree = Javac.parseExpr(this.expression, new SymbolTable());
                return this.getConstantValue2(constPool, ctClass, aSTree);
            } catch (CompileError compileError) {
                return 0;
            }
        }
    }

    static abstract class CodeInitializer0
    extends Initializer {
        CodeInitializer0() {
        }

        abstract void compileExpr(Javac var1);

        @Override
        int compile(CtClass ctClass, String string, Bytecode bytecode, CtClass[] ctClassArray, Javac javac) {
            try {
                bytecode.addAload(0);
                this.compileExpr(javac);
                bytecode.addPutfield(Bytecode.THIS, string, Descriptor.of(ctClass));
                return bytecode.getMaxStack();
            } catch (CompileError compileError) {
                throw new CannotCompileException(compileError);
            }
        }

        @Override
        int compileIfStatic(CtClass ctClass, String string, Bytecode bytecode, Javac javac) {
            try {
                this.compileExpr(javac);
                bytecode.addPutstatic(Bytecode.THIS, string, Descriptor.of(ctClass));
                return bytecode.getMaxStack();
            } catch (CompileError compileError) {
                throw new CannotCompileException(compileError);
            }
        }

        int getConstantValue2(ConstPool constPool, CtClass ctClass, ASTree aSTree) {
            if (ctClass.isPrimitive()) {
                if (aSTree instanceof IntConst) {
                    long l = ((IntConst)aSTree).get();
                    if (ctClass == CtClass.doubleType) {
                        return constPool.addDoubleInfo(l);
                    }
                    if (ctClass == CtClass.floatType) {
                        return constPool.addFloatInfo(l);
                    }
                    if (ctClass == CtClass.longType) {
                        return constPool.addLongInfo(l);
                    }
                    if (ctClass != CtClass.voidType) {
                        return constPool.addIntegerInfo((int)l);
                    }
                } else if (aSTree instanceof DoubleConst) {
                    double d = ((DoubleConst)aSTree).get();
                    if (ctClass == CtClass.floatType) {
                        return constPool.addFloatInfo((float)d);
                    }
                    if (ctClass == CtClass.doubleType) {
                        return constPool.addDoubleInfo(d);
                    }
                }
            } else if (aSTree instanceof StringL && ctClass.getName().equals(CtField.javaLangString)) {
                return constPool.addStringInfo(((StringL)aSTree).get());
            }
            return 0;
        }
    }
}

