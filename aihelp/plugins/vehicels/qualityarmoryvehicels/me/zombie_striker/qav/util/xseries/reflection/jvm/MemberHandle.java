/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.intellij.lang.annotations.Language
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.EnumSet;
import java.util.Set;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.XAccessFlag;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.ClassHandle;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus;

public abstract class MemberHandle
implements ReflectiveHandle<MethodHandle> {
    protected Set<XAccessFlag> accessFlags = EnumSet.noneOf(XAccessFlag.class);
    protected final ClassHandle clazz;

    protected MemberHandle(ClassHandle classHandle) {
        this.clazz = classHandle;
    }

    @ApiStatus.Internal
    public Set<XAccessFlag> getAccessFlags() {
        return this.accessFlags;
    }

    public ClassHandle getClassHandle() {
        return this.clazz;
    }

    public MemberHandle makeAccessible() {
        this.accessFlags.add(XAccessFlag.PRIVATE);
        return this;
    }

    public abstract MemberHandle signature(@Language(value="Java") String var1);

    @Override
    public abstract MethodHandle reflect();

    public abstract <T extends AccessibleObject> T reflectJvm();

    protected <T extends AccessibleObject> T handleAccessible(T t) {
        if (this.accessFlags.contains((Object)XAccessFlag.PRIVATE) || Modifier.isPrivate(((Member)((Object)t)).getDeclaringClass().getModifiers())) {
            t.setAccessible(true);
        }
        return t;
    }

    public abstract MemberHandle copy();
}

