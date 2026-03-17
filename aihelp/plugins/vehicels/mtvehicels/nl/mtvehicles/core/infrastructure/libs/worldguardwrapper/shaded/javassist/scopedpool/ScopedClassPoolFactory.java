/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.scopedpool;

import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.ClassPool;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.scopedpool.ScopedClassPool;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.scopedpool.ScopedClassPoolRepository;

public interface ScopedClassPoolFactory {
    public ScopedClassPool create(ClassLoader var1, ClassPool var2, ScopedClassPoolRepository var3);

    public ScopedClassPool create(ClassPool var1, ScopedClassPoolRepository var2);
}

