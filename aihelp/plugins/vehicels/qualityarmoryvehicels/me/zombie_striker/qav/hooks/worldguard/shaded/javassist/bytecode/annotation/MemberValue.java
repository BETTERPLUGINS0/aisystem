/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation;

import java.lang.reflect.Method;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.AnnotationsWriter;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.MemberValueVisitor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.annotation.NoSuchClassError;

public abstract class MemberValue {
    ConstPool cp;
    char tag;

    MemberValue(char c, ConstPool constPool) {
        this.cp = constPool;
        this.tag = c;
    }

    abstract Object getValue(ClassLoader var1, ClassPool var2, Method var3);

    abstract Class<?> getType(ClassLoader var1);

    static Class<?> loadClass(ClassLoader classLoader, String string) {
        try {
            return Class.forName(MemberValue.convertFromArray(string), true, classLoader);
        } catch (LinkageError linkageError) {
            throw new NoSuchClassError(string, linkageError);
        }
    }

    private static String convertFromArray(String string) {
        int n = string.indexOf("[]");
        if (n != -1) {
            String string2 = string.substring(0, n);
            StringBuilder stringBuilder = new StringBuilder(Descriptor.of(string2));
            while (n != -1) {
                stringBuilder.insert(0, '[');
                n = string.indexOf("[]", n + 1);
            }
            return stringBuilder.toString().replace('/', '.');
        }
        return string;
    }

    public void renameClass(String string, String string2) {
    }

    public void renameClass(Map<String, String> map) {
    }

    public abstract void accept(MemberValueVisitor var1);

    public abstract void write(AnnotationsWriter var1);
}

