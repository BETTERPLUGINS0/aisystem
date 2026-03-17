/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.scopedpool;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.scopedpool.ScopedClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.scopedpool.ScopedClassPoolFactory;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.scopedpool.ScopedClassPoolRepository;

public class ScopedClassPoolFactoryImpl
implements ScopedClassPoolFactory {
    @Override
    public ScopedClassPool create(ClassLoader classLoader, ClassPool classPool, ScopedClassPoolRepository scopedClassPoolRepository) {
        return new ScopedClassPool(classLoader, classPool, scopedClassPoolRepository, false);
    }

    @Override
    public ScopedClassPool create(ClassPool classPool, ScopedClassPoolRepository scopedClassPoolRepository) {
        return new ScopedClassPool(null, classPool, scopedClassPoolRepository, true);
    }
}

