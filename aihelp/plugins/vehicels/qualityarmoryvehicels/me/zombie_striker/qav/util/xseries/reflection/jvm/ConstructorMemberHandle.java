/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.intellij.lang.annotations.Language
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.XAccessFlag;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import me.zombie_striker.qav.util.xseries.reflection.jvm.FlaggedNamedMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.MemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.ClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObjectHandle;
import me.zombie_striker.qav.util.xseries.reflection.parser.ReflectionParser;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class ConstructorMemberHandle
extends MemberHandle {
    protected ClassHandle[] parameterTypes = new ClassHandle[0];

    public ConstructorMemberHandle(ClassHandle classHandle) {
        super(classHandle);
    }

    @ApiStatus.Internal
    public ClassHandle[] getParameterTypes() {
        return this.parameterTypes;
    }

    public ConstructorMemberHandle parameters(Class<?> ... classArray) {
        this.parameterTypes = (ClassHandle[])Arrays.stream(classArray).map(XReflection::of).toArray(ClassHandle[]::new);
        return this;
    }

    public ConstructorMemberHandle parameters(ClassHandle ... classHandleArray) {
        this.parameterTypes = classHandleArray;
        return this;
    }

    @Override
    public MethodHandle reflect() {
        if (this.accessFlags.contains((Object)XAccessFlag.FINAL)) {
            throw new UnsupportedOperationException("Constructor cannot be final: " + this);
        }
        if (this.accessFlags.contains((Object)XAccessFlag.PRIVATE)) {
            return this.clazz.getNamespace().getLookup().unreflectConstructor((Constructor<?>)this.reflectJvm());
        }
        Class<?>[] classArray = FlaggedNamedMemberHandle.getParameters(this, this.parameterTypes);
        return this.clazz.getNamespace().getLookup().findConstructor((Class)this.clazz.unreflect(), MethodType.methodType(Void.TYPE, classArray));
    }

    @Override
    public ConstructorMemberHandle signature(@Language(value="Java", suffix=";") String string) {
        return new ReflectionParser(string).imports(this.clazz.getNamespace()).parseConstructor(this);
    }

    @Override
    @NotNull
    public ReflectiveHandle<ReflectedObject> jvm() {
        return new ReflectedObjectHandle(() -> ReflectedObject.of(this.reflectJvm()));
    }

    public Constructor<?> reflectJvm() {
        Class<?>[] classArray = FlaggedNamedMemberHandle.getParameters(this, this.parameterTypes);
        return this.handleAccessible(((Class)this.clazz.unreflect()).getDeclaredConstructor(classArray));
    }

    @Override
    public ConstructorMemberHandle copy() {
        ConstructorMemberHandle constructorMemberHandle = new ConstructorMemberHandle(this.clazz);
        constructorMemberHandle.parameterTypes = this.parameterTypes;
        constructorMemberHandle.accessFlags.addAll(this.accessFlags);
        return constructorMemberHandle;
    }

    public String toString() {
        String string = this.getClass().getSimpleName() + '{';
        string = string + this.accessFlags.stream().map(xAccessFlag -> xAccessFlag.name().toLowerCase(Locale.ENGLISH)).collect(Collectors.joining(" "));
        string = string + this.clazz.toString() + ' ';
        string = string + '(' + Arrays.stream(this.parameterTypes).map(Object::toString).collect(Collectors.joining(", ")) + ')';
        return string + '}';
    }
}

