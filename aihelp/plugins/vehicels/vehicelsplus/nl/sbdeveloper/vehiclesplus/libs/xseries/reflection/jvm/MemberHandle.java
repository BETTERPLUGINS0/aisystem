/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.ReflectiveHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes.ClassHandle;
import org.intellij.lang.annotations.Language;

public abstract class MemberHandle
implements ReflectiveHandle<MethodHandle> {
    protected boolean makeAccessible;
    protected boolean isFinal;
    protected final ClassHandle clazz;

    protected MemberHandle(ClassHandle classHandle) {
        this.clazz = classHandle;
    }

    public ClassHandle getClassHandle() {
        return this.clazz;
    }

    public MemberHandle makeAccessible() {
        this.makeAccessible = true;
        return this;
    }

    public abstract MemberHandle signature(@Language(value="Java") String var1);

    @Override
    public abstract MethodHandle reflect();

    public abstract <T extends AccessibleObject> T reflectJvm();

    protected <T extends AccessibleObject> T handleAccessible(T t) {
        if (this.makeAccessible || Modifier.isPrivate(((Member)((Object)t)).getDeclaringClass().getModifiers())) {
            t.setAccessible(true);
        }
        return t;
    }

    public abstract MemberHandle clone();
}

