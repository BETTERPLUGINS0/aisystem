/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPoolTail;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Translator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;

public class Loader
extends ClassLoader {
    private HashMap<String, ClassLoader> notDefinedHere;
    private Vector<String> notDefinedPackages;
    private ClassPool source;
    private Translator translator;
    private ProtectionDomain domain;
    public boolean doDelegation = true;

    public Loader() {
        this((ClassPool)null);
    }

    public Loader(ClassPool classPool) {
        this.init(classPool);
    }

    public Loader(ClassLoader classLoader, ClassPool classPool) {
        super(classLoader);
        this.init(classPool);
    }

    private void init(ClassPool classPool) {
        this.notDefinedHere = new HashMap();
        this.notDefinedPackages = new Vector();
        this.source = classPool;
        this.translator = null;
        this.domain = null;
        this.delegateLoadingOf("me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Loader");
    }

    public void delegateLoadingOf(String string) {
        if (string.endsWith(".")) {
            this.notDefinedPackages.addElement(string);
        } else {
            this.notDefinedHere.put(string, this);
        }
    }

    public void setDomain(ProtectionDomain protectionDomain) {
        this.domain = protectionDomain;
    }

    public void setClassPool(ClassPool classPool) {
        this.source = classPool;
    }

    public void addTranslator(ClassPool classPool, Translator translator) {
        this.source = classPool;
        this.translator = translator;
        translator.start(classPool);
    }

    public static void main(String[] stringArray) {
        Loader loader = new Loader();
        loader.run(stringArray);
    }

    public void run(String[] stringArray) {
        if (stringArray.length >= 1) {
            this.run(stringArray[0], Arrays.copyOfRange(stringArray, 1, stringArray.length));
        }
    }

    public void run(String string, String[] stringArray) {
        Class<?> clazz = this.loadClass(string);
        try {
            clazz.getDeclaredMethod("main", String[].class).invoke(null, new Object[]{stringArray});
        } catch (InvocationTargetException invocationTargetException) {
            throw invocationTargetException.getTargetException();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected Class<?> loadClass(String string, boolean bl) {
        String string2 = string = string.intern();
        synchronized (string2) {
            Class<?> clazz = this.findLoadedClass(string);
            if (clazz == null) {
                clazz = this.loadClassByDelegation(string);
            }
            if (clazz == null) {
                clazz = this.findClass(string);
            }
            if (clazz == null) {
                clazz = this.delegateToParent(string);
            }
            if (bl) {
                this.resolveClass(clazz);
            }
            return clazz;
        }
    }

    @Override
    protected Class<?> findClass(String string) {
        Object object;
        byte[] byArray;
        block11: {
            try {
                if (this.source != null) {
                    if (this.translator != null) {
                        this.translator.onLoad(this.source, string);
                    }
                    try {
                        byArray = this.source.get(string).toBytecode();
                        break block11;
                    } catch (NotFoundException notFoundException) {
                        return null;
                    }
                }
                String string2 = "/" + string.replace('.', '/') + ".class";
                object = this.getClass().getResourceAsStream(string2);
                if (object == null) {
                    return null;
                }
                byArray = ClassPoolTail.readStream((InputStream)object);
            } catch (Exception exception) {
                throw new ClassNotFoundException("caught an exception while obtaining a class file for " + string, exception);
            }
        }
        int n = string.lastIndexOf(46);
        if (n != -1 && this.isDefinedPackage((String)(object = string.substring(0, n)))) {
            try {
                this.definePackage((String)object, null, null, null, null, null, null, null);
            } catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
        }
        if (this.domain == null) {
            return this.defineClass(string, byArray, 0, byArray.length);
        }
        return this.defineClass(string, byArray, 0, byArray.length, this.domain);
    }

    private boolean isDefinedPackage(String string) {
        if (ClassFile.MAJOR_VERSION >= 53) {
            return this.getDefinedPackage(string) == null;
        }
        return this.getPackage(string) == null;
    }

    protected Class<?> loadClassByDelegation(String string) {
        Class<?> clazz = null;
        if (this.doDelegation && (string.startsWith("java.") || string.startsWith("javax.") || string.startsWith("jdk.internal.") || string.startsWith("sun.") || string.startsWith("com.sun.") || string.startsWith("org.w3c.") || string.startsWith("org.xml.") || this.notDelegated(string))) {
            clazz = this.delegateToParent(string);
        }
        return clazz;
    }

    private boolean notDelegated(String string) {
        if (this.notDefinedHere.containsKey(string)) {
            return true;
        }
        for (String string2 : this.notDefinedPackages) {
            if (!string.startsWith(string2)) continue;
            return true;
        }
        return false;
    }

    protected Class<?> delegateToParent(String string) {
        ClassLoader classLoader = this.getParent();
        if (classLoader != null) {
            return classLoader.loadClass(string);
        }
        return this.findSystemClass(string);
    }

    public static class Simple
    extends ClassLoader {
        public Simple() {
        }

        public Simple(ClassLoader classLoader) {
            super(classLoader);
        }

        public Class<?> invokeDefineClass(CtClass ctClass) {
            byte[] byArray = ctClass.toBytecode();
            return this.defineClass(ctClass.getName(), byArray, 0, byArray.length);
        }
    }
}

