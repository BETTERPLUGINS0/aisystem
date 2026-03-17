/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CodeConverter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtNewMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Translator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.CannotReflectException;

public class Reflection
implements Translator {
    static final String classobjectField = "_classobject";
    static final String classobjectAccessor = "_getClass";
    static final String metaobjectField = "_metaobject";
    static final String metaobjectGetter = "_getMetaobject";
    static final String metaobjectSetter = "_setMetaobject";
    static final String readPrefix = "_r_";
    static final String writePrefix = "_w_";
    static final String metaobjectClassName = "me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.Metaobject";
    static final String classMetaobjectClassName = "me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.ClassMetaobject";
    protected CtMethod trapMethod;
    protected CtMethod trapStaticMethod;
    protected CtMethod trapRead;
    protected CtMethod trapWrite;
    protected CtClass[] readParam;
    protected ClassPool classPool = null;
    protected CodeConverter converter = new CodeConverter();

    private boolean isExcluded(String string) {
        return string.startsWith("_m_") || string.equals(classobjectAccessor) || string.equals(metaobjectSetter) || string.equals(metaobjectGetter) || string.startsWith(readPrefix) || string.startsWith(writePrefix);
    }

    @Override
    public void start(ClassPool classPool) {
        this.classPool = classPool;
        String string = "me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.Sample is not found or broken.";
        try {
            CtClass ctClass = this.classPool.get("me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.Sample");
            this.rebuildClassFile(ctClass.getClassFile());
            this.trapMethod = ctClass.getDeclaredMethod("trap");
            this.trapStaticMethod = ctClass.getDeclaredMethod("trapStatic");
            this.trapRead = ctClass.getDeclaredMethod("trapRead");
            this.trapWrite = ctClass.getDeclaredMethod("trapWrite");
            this.readParam = new CtClass[]{this.classPool.get("java.lang.Object")};
        } catch (NotFoundException notFoundException) {
            throw new RuntimeException("me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.Sample is not found or broken.");
        } catch (BadBytecode badBytecode) {
            throw new RuntimeException("me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.Sample is not found or broken.");
        }
    }

    @Override
    public void onLoad(ClassPool classPool, String string) {
        CtClass ctClass = classPool.get(string);
        ctClass.instrument(this.converter);
    }

    public boolean makeReflective(String string, String string2, String string3) {
        return this.makeReflective(this.classPool.get(string), this.classPool.get(string2), this.classPool.get(string3));
    }

    public boolean makeReflective(Class<?> clazz, Class<?> clazz2, Class<?> clazz3) {
        return this.makeReflective(clazz.getName(), clazz2.getName(), clazz3.getName());
    }

    public boolean makeReflective(CtClass ctClass, CtClass ctClass2, CtClass ctClass3) {
        if (ctClass.isInterface()) {
            throw new CannotReflectException("Cannot reflect an interface: " + ctClass.getName());
        }
        if (ctClass.subclassOf(this.classPool.get(classMetaobjectClassName))) {
            throw new CannotReflectException("Cannot reflect a subclass of ClassMetaobject: " + ctClass.getName());
        }
        if (ctClass.subclassOf(this.classPool.get(metaobjectClassName))) {
            throw new CannotReflectException("Cannot reflect a subclass of Metaobject: " + ctClass.getName());
        }
        this.registerReflectiveClass(ctClass);
        return this.modifyClassfile(ctClass, ctClass2, ctClass3);
    }

    private void registerReflectiveClass(CtClass ctClass) {
        CtField[] ctFieldArray = ctClass.getDeclaredFields();
        for (int i = 0; i < ctFieldArray.length; ++i) {
            CtField ctField = ctFieldArray[i];
            int n = ctField.getModifiers();
            if ((n & 1) == 0 || (n & 0x10) != 0) continue;
            String string = ctField.getName();
            this.converter.replaceFieldRead(ctField, ctClass, readPrefix + string);
            this.converter.replaceFieldWrite(ctField, ctClass, writePrefix + string);
        }
    }

    private boolean modifyClassfile(CtClass ctClass, CtClass ctClass2, CtClass ctClass3) {
        CtField ctField;
        boolean bl;
        if (ctClass.getAttribute("Reflective") != null) {
            return false;
        }
        ctClass.setAttribute("Reflective", new byte[0]);
        CtClass ctClass4 = this.classPool.get("me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.Metalevel");
        boolean bl2 = bl = !ctClass.subtypeOf(ctClass4);
        if (bl) {
            ctClass.addInterface(ctClass4);
        }
        this.processMethods(ctClass, bl);
        this.processFields(ctClass);
        if (bl) {
            ctField = new CtField(this.classPool.get(metaobjectClassName), metaobjectField, ctClass);
            ctField.setModifiers(4);
            ctClass.addField(ctField, CtField.Initializer.byNewWithParams(ctClass2));
            ctClass.addMethod(CtNewMethod.getter(metaobjectGetter, ctField));
            ctClass.addMethod(CtNewMethod.setter(metaobjectSetter, ctField));
        }
        ctField = new CtField(this.classPool.get(classMetaobjectClassName), classobjectField, ctClass);
        ctField.setModifiers(10);
        ctClass.addField(ctField, CtField.Initializer.byNew(ctClass3, new String[]{ctClass.getName()}));
        ctClass.addMethod(CtNewMethod.getter(classobjectAccessor, ctField));
        return true;
    }

    private void processMethods(CtClass ctClass, boolean bl) {
        CtMethod[] ctMethodArray = ctClass.getMethods();
        for (int i = 0; i < ctMethodArray.length; ++i) {
            CtMethod ctMethod = ctMethodArray[i];
            int n = ctMethod.getModifiers();
            if (!Modifier.isPublic(n) || Modifier.isAbstract(n)) continue;
            this.processMethods0(n, ctClass, ctMethod, i, bl);
        }
    }

    private void processMethods0(int n, CtClass ctClass, CtMethod ctMethod, int n2, boolean bl) {
        CtMethod ctMethod2;
        String string = ctMethod.getName();
        if (this.isExcluded(string)) {
            return;
        }
        if (ctMethod.getDeclaringClass() == ctClass) {
            if (Modifier.isNative(n)) {
                return;
            }
            ctMethod2 = ctMethod;
            if (Modifier.isFinal(n)) {
                ctMethod2.setModifiers(n &= 0xFFFFFFEF);
            }
        } else {
            if (Modifier.isFinal(n)) {
                return;
            }
            ctMethod2 = CtNewMethod.delegator(this.findOriginal(ctMethod, bl), ctClass);
            ctMethod2.setModifiers(n &= 0xFFFFFEFF);
            ctClass.addMethod(ctMethod2);
        }
        ctMethod2.setName("_m_" + n2 + "_" + string);
        CtMethod ctMethod3 = Modifier.isStatic(n) ? this.trapStaticMethod : this.trapMethod;
        CtMethod ctMethod4 = CtNewMethod.wrapped(ctMethod.getReturnType(), string, ctMethod.getParameterTypes(), ctMethod.getExceptionTypes(), ctMethod3, CtMethod.ConstParameter.integer(n2), ctClass);
        ctMethod4.setModifiers(n);
        ctClass.addMethod(ctMethod4);
    }

    private CtMethod findOriginal(CtMethod ctMethod, boolean bl) {
        if (bl) {
            return ctMethod;
        }
        String string = ctMethod.getName();
        CtMethod[] ctMethodArray = ctMethod.getDeclaringClass().getDeclaredMethods();
        for (int i = 0; i < ctMethodArray.length; ++i) {
            String string2 = ctMethodArray[i].getName();
            if (!string2.endsWith(string) || !string2.startsWith("_m_") || !ctMethodArray[i].getSignature().equals(ctMethod.getSignature())) continue;
            return ctMethodArray[i];
        }
        return ctMethod;
    }

    private void processFields(CtClass ctClass) {
        CtField[] ctFieldArray = ctClass.getDeclaredFields();
        for (int i = 0; i < ctFieldArray.length; ++i) {
            CtField ctField = ctFieldArray[i];
            int n = ctField.getModifiers();
            if ((n & 1) == 0 || (n & 0x10) != 0) continue;
            String string = ctField.getName();
            CtClass ctClass2 = ctField.getType();
            CtMethod ctMethod = CtNewMethod.wrapped(ctClass2, readPrefix + string, this.readParam, null, this.trapRead, CtMethod.ConstParameter.string(string), ctClass);
            ctMethod.setModifiers(n |= 8);
            ctClass.addMethod(ctMethod);
            CtClass[] ctClassArray = new CtClass[]{this.classPool.get("java.lang.Object"), ctClass2};
            ctMethod = CtNewMethod.wrapped(CtClass.voidType, writePrefix + string, ctClassArray, null, this.trapWrite, CtMethod.ConstParameter.string(string), ctClass);
            ctMethod.setModifiers(n);
            ctClass.addMethod(ctMethod);
        }
    }

    public void rebuildClassFile(ClassFile classFile) {
        if (ClassFile.MAJOR_VERSION < 50) {
            return;
        }
        for (MethodInfo methodInfo : classFile.getMethods()) {
            methodInfo.rebuildStackMap(this.classPool);
        }
    }
}

