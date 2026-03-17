/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.scopedpool;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.security.ProtectionDomain;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.LoaderClassPath;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.scopedpool.ScopedClassPoolRepository;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.scopedpool.SoftValueHashMap;

public class ScopedClassPool
extends ClassPool {
    protected ScopedClassPoolRepository repository;
    protected Reference<ClassLoader> classLoader;
    protected LoaderClassPath classPath;
    protected Map<String, CtClass> softcache = new SoftValueHashMap<String, CtClass>();
    boolean isBootstrapCl = true;

    protected ScopedClassPool(ClassLoader classLoader, ClassPool classPool, ScopedClassPoolRepository scopedClassPoolRepository) {
        this(classLoader, classPool, scopedClassPoolRepository, false);
    }

    protected ScopedClassPool(ClassLoader classLoader, ClassPool classPool, ScopedClassPoolRepository scopedClassPoolRepository, boolean bl) {
        super(classPool);
        this.repository = scopedClassPoolRepository;
        this.classLoader = new WeakReference<ClassLoader>(classLoader);
        if (classLoader != null) {
            this.classPath = new LoaderClassPath(classLoader);
            this.insertClassPath(this.classPath);
        }
        this.childFirstLookup = true;
        if (!bl && classLoader == null) {
            this.isBootstrapCl = true;
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        ClassLoader classLoader = this.getClassLoader0();
        if (classLoader == null && !this.isBootstrapCl) {
            throw new IllegalStateException("ClassLoader has been garbage collected");
        }
        return classLoader;
    }

    protected ClassLoader getClassLoader0() {
        return this.classLoader.get();
    }

    public void close() {
        this.removeClassPath(this.classPath);
        this.classes.clear();
        this.softcache.clear();
    }

    public synchronized void flushClass(String string) {
        this.classes.remove(string);
        this.softcache.remove(string);
    }

    public synchronized void soften(CtClass ctClass) {
        if (this.repository.isPrune()) {
            ctClass.prune();
        }
        this.classes.remove(ctClass.getName());
        this.softcache.put(ctClass.getName(), ctClass);
    }

    public boolean isUnloadedClassLoader() {
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected CtClass getCached(String string) {
        CtClass ctClass = this.getCachedLocally(string);
        if (ctClass == null) {
            Object object;
            boolean bl = false;
            ClassLoader classLoader = this.getClassLoader0();
            if (classLoader != null) {
                int n = string.lastIndexOf(36);
                object = null;
                object = n < 0 ? string.replaceAll("[\\.]", "/") + ".class" : string.substring(0, n).replaceAll("[\\.]", "/") + string.substring(n) + ".class";
                boolean bl2 = bl = classLoader.getResource((String)object) != null;
            }
            if (!bl) {
                Map<ClassLoader, ScopedClassPool> map = this.repository.getRegisteredCLs();
                object = map;
                synchronized (object) {
                    for (ScopedClassPool scopedClassPool : map.values()) {
                        if (scopedClassPool.isUnloadedClassLoader()) {
                            this.repository.unregisterClassLoader(scopedClassPool.getClassLoader());
                            continue;
                        }
                        ctClass = scopedClassPool.getCachedLocally(string);
                        if (ctClass == null) continue;
                        return ctClass;
                    }
                }
            }
        }
        return ctClass;
    }

    @Override
    protected void cacheCtClass(String string, CtClass ctClass, boolean bl) {
        if (bl) {
            super.cacheCtClass(string, ctClass, bl);
        } else {
            if (this.repository.isPrune()) {
                ctClass.prune();
            }
            this.softcache.put(string, ctClass);
        }
    }

    public void lockInCache(CtClass ctClass) {
        super.cacheCtClass(ctClass.getName(), ctClass, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected CtClass getCachedLocally(String string) {
        CtClass ctClass = (CtClass)this.classes.get(string);
        if (ctClass != null) {
            return ctClass;
        }
        Map<String, CtClass> map = this.softcache;
        synchronized (map) {
            return this.softcache.get(string);
        }
    }

    public synchronized CtClass getLocally(String string) {
        this.softcache.remove(string);
        CtClass ctClass = (CtClass)this.classes.get(string);
        if (ctClass == null) {
            ctClass = this.createCtClass(string, true);
            if (ctClass == null) {
                throw new NotFoundException(string);
            }
            super.cacheCtClass(string, ctClass, false);
        }
        return ctClass;
    }

    @Override
    public Class<?> toClass(CtClass ctClass, ClassLoader classLoader, ProtectionDomain protectionDomain) {
        this.lockInCache(ctClass);
        return super.toClass(ctClass, this.getClassLoader0(), protectionDomain);
    }

    static {
        ClassPool.doPruning = false;
        ClassPool.releaseUnmodifiedClassFile = false;
    }
}

