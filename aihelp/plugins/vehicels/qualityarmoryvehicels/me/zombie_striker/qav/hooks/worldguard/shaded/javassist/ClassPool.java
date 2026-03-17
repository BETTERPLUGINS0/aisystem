/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPath;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPoolTail;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClassType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtNewClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.DefineClassHelper;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.DefinePackageHelper;

public class ClassPool {
    public boolean childFirstLookup = false;
    public static boolean doPruning = false;
    private int compressCount;
    private static final int COMPRESS_THRESHOLD = 100;
    public static boolean releaseUnmodifiedClassFile = true;
    public static boolean cacheOpenedJarFile = true;
    protected ClassPoolTail source;
    protected ClassPool parent;
    protected Hashtable classes = new Hashtable(191);
    private Hashtable cflow = null;
    private static final int INIT_HASH_SIZE = 191;
    private ArrayList importedPackages;
    private static ClassPool defaultPool = null;

    public ClassPool() {
        this(null);
    }

    public ClassPool(boolean bl) {
        this(null);
        if (bl) {
            this.appendSystemPath();
        }
    }

    public ClassPool(ClassPool classPool) {
        this.source = new ClassPoolTail();
        this.parent = classPool;
        if (classPool == null) {
            CtClass[] ctClassArray = CtClass.primitiveTypes;
            for (int i = 0; i < ctClassArray.length; ++i) {
                this.classes.put(ctClassArray[i].getName(), ctClassArray[i]);
            }
        }
        this.cflow = null;
        this.compressCount = 0;
        this.clearImportedPackages();
    }

    public static synchronized ClassPool getDefault() {
        if (defaultPool == null) {
            defaultPool = new ClassPool(null);
            defaultPool.appendSystemPath();
        }
        return defaultPool;
    }

    protected CtClass getCached(String string) {
        return (CtClass)this.classes.get(string);
    }

    protected void cacheCtClass(String string, CtClass ctClass, boolean bl) {
        this.classes.put(string, ctClass);
    }

    protected CtClass removeCached(String string) {
        return (CtClass)this.classes.remove(string);
    }

    public String toString() {
        return this.source.toString();
    }

    void compress() {
        if (this.compressCount++ > 100) {
            this.compressCount = 0;
            Enumeration enumeration = this.classes.elements();
            while (enumeration.hasMoreElements()) {
                ((CtClass)enumeration.nextElement()).compress();
            }
        }
    }

    public void importPackage(String string) {
        this.importedPackages.add(string);
    }

    public void clearImportedPackages() {
        this.importedPackages = new ArrayList();
        this.importedPackages.add("java.lang");
    }

    public Iterator<String> getImportedPackages() {
        return this.importedPackages.iterator();
    }

    public void recordInvalidClassName(String string) {
    }

    void recordCflow(String string, String string2, String string3) {
        if (this.cflow == null) {
            this.cflow = new Hashtable();
        }
        this.cflow.put(string, new Object[]{string2, string3});
    }

    public Object[] lookupCflow(String string) {
        if (this.cflow == null) {
            this.cflow = new Hashtable();
        }
        return (Object[])this.cflow.get(string);
    }

    public CtClass getAndRename(String string, String string2) {
        CtClass ctClass = this.get0(string, false);
        if (ctClass == null) {
            throw new NotFoundException(string);
        }
        if (ctClass instanceof CtClassType) {
            ((CtClassType)ctClass).setClassPool(this);
        }
        ctClass.setName(string2);
        return ctClass;
    }

    synchronized void classNameChanged(String string, CtClass ctClass) {
        CtClass ctClass2 = this.getCached(string);
        if (ctClass2 == ctClass) {
            this.removeCached(string);
        }
        String string2 = ctClass.getName();
        this.checkNotFrozen(string2);
        this.cacheCtClass(string2, ctClass, false);
    }

    public CtClass get(String string) {
        CtClass ctClass = string == null ? null : this.get0(string, true);
        if (ctClass == null) {
            throw new NotFoundException(string);
        }
        ctClass.incGetCounter();
        return ctClass;
    }

    public CtClass getOrNull(String string) {
        CtClass ctClass = null;
        if (string == null) {
            ctClass = null;
        } else {
            try {
                ctClass = this.get0(string, true);
            } catch (NotFoundException notFoundException) {
                // empty catch block
            }
        }
        if (ctClass != null) {
            ctClass.incGetCounter();
        }
        return ctClass;
    }

    public CtClass getCtClass(String string) {
        if (string.charAt(0) == '[') {
            return Descriptor.toCtClass(string, this);
        }
        return this.get(string);
    }

    protected synchronized CtClass get0(String string, boolean bl) {
        CtClass ctClass = null;
        if (bl && (ctClass = this.getCached(string)) != null) {
            return ctClass;
        }
        if (!this.childFirstLookup && this.parent != null && (ctClass = this.parent.get0(string, bl)) != null) {
            return ctClass;
        }
        ctClass = this.createCtClass(string, bl);
        if (ctClass != null) {
            if (bl) {
                this.cacheCtClass(ctClass.getName(), ctClass, false);
            }
            return ctClass;
        }
        if (this.childFirstLookup && this.parent != null) {
            ctClass = this.parent.get0(string, bl);
        }
        return ctClass;
    }

    protected CtClass createCtClass(String string, boolean bl) {
        if (string.charAt(0) == '[') {
            string = Descriptor.toClassName(string);
        }
        if (string.endsWith("[]")) {
            String string2 = string.substring(0, string.indexOf(91));
            if (!(bl && this.getCached(string2) != null || this.find(string2) != null)) {
                return null;
            }
            return new CtArray(string, this);
        }
        if (this.find(string) == null) {
            return null;
        }
        return new CtClassType(string, this);
    }

    public URL find(String string) {
        return this.source.find(string);
    }

    void checkNotFrozen(String string) {
        CtClass ctClass = this.getCached(string);
        if (ctClass == null) {
            if (!this.childFirstLookup && this.parent != null) {
                try {
                    ctClass = this.parent.get0(string, true);
                } catch (NotFoundException notFoundException) {
                    // empty catch block
                }
                if (ctClass != null) {
                    throw new RuntimeException(string + " is in a parent ClassPool.  Use the parent.");
                }
            }
        } else if (ctClass.isFrozen()) {
            throw new RuntimeException(string + ": frozen class (cannot edit)");
        }
    }

    CtClass checkNotExists(String string) {
        CtClass ctClass = this.getCached(string);
        if (ctClass == null && !this.childFirstLookup && this.parent != null) {
            try {
                ctClass = this.parent.get0(string, true);
            } catch (NotFoundException notFoundException) {
                // empty catch block
            }
        }
        return ctClass;
    }

    InputStream openClassfile(String string) {
        return this.source.openClassfile(string);
    }

    void writeClassfile(String string, OutputStream outputStream) {
        this.source.writeClassfile(string, outputStream);
    }

    public CtClass[] get(String[] stringArray) {
        if (stringArray == null) {
            return new CtClass[0];
        }
        int n = stringArray.length;
        CtClass[] ctClassArray = new CtClass[n];
        for (int i = 0; i < n; ++i) {
            ctClassArray[i] = this.get(stringArray[i]);
        }
        return ctClassArray;
    }

    public CtMethod getMethod(String string, String string2) {
        CtClass ctClass = this.get(string);
        return ctClass.getDeclaredMethod(string2);
    }

    public CtClass makeClass(InputStream inputStream) {
        return this.makeClass(inputStream, true);
    }

    public CtClass makeClass(InputStream inputStream, boolean bl) {
        this.compress();
        inputStream = new BufferedInputStream(inputStream);
        CtClassType ctClassType = new CtClassType(inputStream, this);
        ((CtClass)ctClassType).checkModify();
        String string = ctClassType.getName();
        if (bl) {
            this.checkNotFrozen(string);
        }
        this.cacheCtClass(string, ctClassType, true);
        return ctClassType;
    }

    public CtClass makeClass(ClassFile classFile) {
        return this.makeClass(classFile, true);
    }

    public CtClass makeClass(ClassFile classFile, boolean bl) {
        this.compress();
        CtClassType ctClassType = new CtClassType(classFile, this);
        ((CtClass)ctClassType).checkModify();
        String string = ctClassType.getName();
        if (bl) {
            this.checkNotFrozen(string);
        }
        this.cacheCtClass(string, ctClassType, true);
        return ctClassType;
    }

    public CtClass makeClassIfNew(InputStream inputStream) {
        this.compress();
        inputStream = new BufferedInputStream(inputStream);
        CtClassType ctClassType = new CtClassType(inputStream, this);
        ((CtClass)ctClassType).checkModify();
        String string = ctClassType.getName();
        CtClass ctClass = this.checkNotExists(string);
        if (ctClass != null) {
            return ctClass;
        }
        this.cacheCtClass(string, ctClassType, true);
        return ctClassType;
    }

    public CtClass makeClass(String string) {
        return this.makeClass(string, null);
    }

    public synchronized CtClass makeClass(String string, CtClass ctClass) {
        this.checkNotFrozen(string);
        CtNewClass ctNewClass = new CtNewClass(string, this, false, ctClass);
        this.cacheCtClass(string, ctNewClass, true);
        return ctNewClass;
    }

    synchronized CtClass makeNestedClass(String string) {
        this.checkNotFrozen(string);
        CtNewClass ctNewClass = new CtNewClass(string, this, false, null);
        this.cacheCtClass(string, ctNewClass, true);
        return ctNewClass;
    }

    public CtClass makeInterface(String string) {
        return this.makeInterface(string, null);
    }

    public synchronized CtClass makeInterface(String string, CtClass ctClass) {
        this.checkNotFrozen(string);
        CtNewClass ctNewClass = new CtNewClass(string, this, true, ctClass);
        this.cacheCtClass(string, ctNewClass, true);
        return ctNewClass;
    }

    public CtClass makeAnnotation(String string) {
        try {
            CtClass ctClass = this.makeInterface(string, this.get("java.lang.annotation.Annotation"));
            ctClass.setModifiers(ctClass.getModifiers() | 0x2000);
            return ctClass;
        } catch (NotFoundException notFoundException) {
            throw new RuntimeException(notFoundException.getMessage(), notFoundException);
        }
    }

    public ClassPath appendSystemPath() {
        return this.source.appendSystemPath();
    }

    public ClassPath insertClassPath(ClassPath classPath) {
        return this.source.insertClassPath(classPath);
    }

    public ClassPath appendClassPath(ClassPath classPath) {
        return this.source.appendClassPath(classPath);
    }

    public ClassPath insertClassPath(String string) {
        return this.source.insertClassPath(string);
    }

    public ClassPath appendClassPath(String string) {
        return this.source.appendClassPath(string);
    }

    public void removeClassPath(ClassPath classPath) {
        this.source.removeClassPath(classPath);
    }

    public void appendPathList(String string) {
        char c = File.pathSeparatorChar;
        int n = 0;
        while (true) {
            int n2;
            if ((n2 = string.indexOf(c, n)) < 0) break;
            this.appendClassPath(string.substring(n, n2));
            n = n2 + 1;
        }
        this.appendClassPath(string.substring(n));
    }

    public Class toClass(CtClass ctClass) {
        return this.toClass(ctClass, this.getClassLoader());
    }

    public ClassLoader getClassLoader() {
        return ClassPool.getContextClassLoader();
    }

    static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public Class toClass(CtClass ctClass, ClassLoader classLoader) {
        return this.toClass(ctClass, null, classLoader, null);
    }

    public Class toClass(CtClass ctClass, ClassLoader classLoader, ProtectionDomain protectionDomain) {
        return this.toClass(ctClass, null, classLoader, protectionDomain);
    }

    public Class<?> toClass(CtClass ctClass, Class<?> clazz) {
        try {
            return DefineClassHelper.toClass(clazz, ctClass.toBytecode());
        } catch (IOException iOException) {
            throw new CannotCompileException(iOException);
        }
    }

    public Class<?> toClass(CtClass ctClass, MethodHandles.Lookup lookup) {
        try {
            return DefineClassHelper.toClass(lookup, ctClass.toBytecode());
        } catch (IOException iOException) {
            throw new CannotCompileException(iOException);
        }
    }

    public Class toClass(CtClass ctClass, Class<?> clazz, ClassLoader classLoader, ProtectionDomain protectionDomain) {
        try {
            return DefineClassHelper.toClass(ctClass.getName(), clazz, classLoader, protectionDomain, ctClass.toBytecode());
        } catch (IOException iOException) {
            throw new CannotCompileException(iOException);
        }
    }

    public void makePackage(ClassLoader classLoader, String string) {
        DefinePackageHelper.definePackage(string, classLoader);
    }
}

