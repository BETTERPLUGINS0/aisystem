/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtConstructor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtNewConstructor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtNewMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Translator;

public class StubGenerator
implements Translator {
    private static final String fieldImporter = "importer";
    private static final String fieldObjectId = "objectId";
    private static final String accessorObjectId = "_getObjectId";
    private static final String sampleClass = "me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi.Sample";
    private ClassPool classPool;
    private Map<String, CtClass> proxyClasses = new Hashtable<String, CtClass>();
    private CtMethod forwardMethod;
    private CtMethod forwardStaticMethod;
    private CtClass[] proxyConstructorParamTypes;
    private CtClass[] interfacesForProxy;
    private CtClass[] exceptionForProxy;

    @Override
    public void start(ClassPool classPool) {
        this.classPool = classPool;
        CtClass ctClass = classPool.get(sampleClass);
        this.forwardMethod = ctClass.getDeclaredMethod("forward");
        this.forwardStaticMethod = ctClass.getDeclaredMethod("forwardStatic");
        this.proxyConstructorParamTypes = classPool.get(new String[]{"me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi.ObjectImporter", "int"});
        this.interfacesForProxy = classPool.get(new String[]{"java.io.Serializable", "me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi.Proxy"});
        this.exceptionForProxy = new CtClass[]{classPool.get("me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi.RemoteException")};
    }

    @Override
    public void onLoad(ClassPool classPool, String string) {
    }

    public boolean isProxyClass(String string) {
        return this.proxyClasses.get(string) != null;
    }

    public synchronized boolean makeProxyClass(Class<?> clazz) {
        String string = clazz.getName();
        if (this.proxyClasses.get(string) != null) {
            return false;
        }
        CtClass ctClass = this.produceProxyClass(this.classPool.get(string), clazz);
        this.proxyClasses.put(string, ctClass);
        this.modifySuperclass(ctClass);
        return true;
    }

    private CtClass produceProxyClass(CtClass ctClass, Class<?> clazz) {
        int n = ctClass.getModifiers();
        if (Modifier.isAbstract(n) || Modifier.isNative(n) || !Modifier.isPublic(n)) {
            throw new CannotCompileException(ctClass.getName() + " must be public, non-native, and non-abstract.");
        }
        CtClass ctClass2 = this.classPool.makeClass(ctClass.getName(), ctClass.getSuperclass());
        ctClass2.setInterfaces(this.interfacesForProxy);
        CtField ctField = new CtField(this.classPool.get("me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.rmi.ObjectImporter"), fieldImporter, ctClass2);
        ctField.setModifiers(2);
        ctClass2.addField(ctField, CtField.Initializer.byParameter(0));
        ctField = new CtField(CtClass.intType, fieldObjectId, ctClass2);
        ctField.setModifiers(2);
        ctClass2.addField(ctField, CtField.Initializer.byParameter(1));
        ctClass2.addMethod(CtNewMethod.getter(accessorObjectId, ctField));
        ctClass2.addConstructor(CtNewConstructor.defaultConstructor(ctClass2));
        CtConstructor ctConstructor = CtNewConstructor.skeleton(this.proxyConstructorParamTypes, null, ctClass2);
        ctClass2.addConstructor(ctConstructor);
        try {
            this.addMethods(ctClass2, clazz.getMethods());
            return ctClass2;
        } catch (SecurityException securityException) {
            throw new CannotCompileException(securityException);
        }
    }

    private CtClass toCtClass(Class<?> clazz) {
        String string;
        if (!clazz.isArray()) {
            string = clazz.getName();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            do {
                stringBuilder.append("[]");
            } while ((clazz = clazz.getComponentType()).isArray());
            stringBuilder.insert(0, clazz.getName());
            string = stringBuilder.toString();
        }
        return this.classPool.get(string);
    }

    private CtClass[] toCtClass(Class<?>[] classArray) {
        int n = classArray.length;
        CtClass[] ctClassArray = new CtClass[n];
        for (int i = 0; i < n; ++i) {
            ctClassArray[i] = this.toCtClass(classArray[i]);
        }
        return ctClassArray;
    }

    private void addMethods(CtClass ctClass, Method[] methodArray) {
        for (int i = 0; i < methodArray.length; ++i) {
            Method method = methodArray[i];
            int n = method.getModifiers();
            if (method.getDeclaringClass() == Object.class || Modifier.isFinal(n)) continue;
            if (Modifier.isPublic(n)) {
                CtMethod ctMethod = Modifier.isStatic(n) ? this.forwardStaticMethod : this.forwardMethod;
                CtMethod ctMethod2 = CtNewMethod.wrapped(this.toCtClass(method.getReturnType()), method.getName(), this.toCtClass(method.getParameterTypes()), this.exceptionForProxy, ctMethod, CtMethod.ConstParameter.integer(i), ctClass);
                ctMethod2.setModifiers(n);
                ctClass.addMethod(ctMethod2);
                continue;
            }
            if (Modifier.isProtected(n) || Modifier.isPrivate(n)) continue;
            throw new CannotCompileException("the methods must be public, protected, or private.");
        }
    }

    private void modifySuperclass(CtClass ctClass) {
        CtClass ctClass2;
        while ((ctClass2 = ctClass.getSuperclass()) != null) {
            try {
                ctClass2.getDeclaredConstructor(null);
                break;
            } catch (NotFoundException notFoundException) {
                ctClass2.addConstructor(CtNewConstructor.defaultConstructor(ctClass2));
                ctClass = ctClass2;
            }
        }
    }
}

