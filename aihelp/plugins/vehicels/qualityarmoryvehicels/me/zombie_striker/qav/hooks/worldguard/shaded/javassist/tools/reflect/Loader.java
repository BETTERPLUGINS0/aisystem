/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.Reflection;

public class Loader
extends me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Loader {
    protected Reflection reflection;

    public static void main(String[] stringArray) {
        Loader loader = new Loader();
        loader.run(stringArray);
    }

    public Loader() {
        this.delegateLoadingOf("me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.Loader");
        this.reflection = new Reflection();
        ClassPool classPool = ClassPool.getDefault();
        this.addTranslator(classPool, this.reflection);
    }

    public boolean makeReflective(String string, String string2, String string3) {
        return this.reflection.makeReflective(string, string2, string3);
    }
}

