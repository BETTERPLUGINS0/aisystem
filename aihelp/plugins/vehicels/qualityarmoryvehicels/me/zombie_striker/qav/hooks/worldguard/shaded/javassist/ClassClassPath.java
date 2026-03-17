/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import java.io.InputStream;
import java.net.URL;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPath;

public class ClassClassPath
implements ClassPath {
    private Class<?> thisClass;

    public ClassClassPath(Class<?> clazz) {
        this.thisClass = clazz;
    }

    ClassClassPath() {
        this(Object.class);
    }

    @Override
    public InputStream openClassfile(String string) {
        String string2 = '/' + string.replace('.', '/') + ".class";
        return this.thisClass.getResourceAsStream(string2);
    }

    @Override
    public URL find(String string) {
        String string2 = '/' + string.replace('.', '/') + ".class";
        return this.thisClass.getResource(string2);
    }

    public String toString() {
        return this.thisClass.getName() + ".class";
    }
}

