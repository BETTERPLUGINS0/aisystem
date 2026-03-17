/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.intellij.lang.annotations.Language
 *  org.intellij.lang.annotations.Pattern
 *  org.jetbrains.annotations.ApiStatus$Experimental
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.XAccessFlag;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import me.zombie_striker.qav.util.xseries.reflection.jvm.FlaggedNamedMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.ClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObjectHandle;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftMapping;
import me.zombie_striker.qav.util.xseries.reflection.parser.ReflectionParser;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class MethodMemberHandle
extends FlaggedNamedMemberHandle {
    protected ClassHandle[] parameterTypes = new ClassHandle[0];

    public MethodMemberHandle(ClassHandle classHandle) {
        super(classHandle);
    }

    public MethodMemberHandle parameters(ClassHandle ... classHandleArray) {
        this.parameterTypes = classHandleArray;
        return this;
    }

    @Override
    public MethodMemberHandle returns(Class<?> clazz) {
        super.returns(clazz);
        return this;
    }

    @Override
    public MethodMemberHandle returns(ClassHandle classHandle) {
        super.returns(classHandle);
        return this;
    }

    @Override
    public MethodMemberHandle asStatic() {
        super.asStatic();
        return this;
    }

    public MethodMemberHandle parameters(Class<?> ... classArray) {
        this.parameterTypes = (ClassHandle[])Arrays.stream(classArray).map(XReflection::of).toArray(ClassHandle[]::new);
        return this;
    }

    @Override
    public MethodMemberHandle signature(@Language(value="Java", suffix=";") String string) {
        return new ReflectionParser(string).imports(this.clazz.getNamespace()).parseMethod(this);
    }

    @Override
    public MethodMemberHandle map(MinecraftMapping minecraftMapping, @Pattern(value="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String string) {
        super.map(minecraftMapping, string);
        return this;
    }

    @Override
    public MethodMemberHandle named(@Pattern(value="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String ... stringArray) {
        super.named(stringArray);
        return this;
    }

    public MethodType getMethodType() {
        return MethodType.methodType(this.getReturnType(), FlaggedNamedMemberHandle.getParameters(this, this.parameterTypes));
    }

    @ApiStatus.Experimental
    public CallSite toLambda(Class<?> clazz, String string) {
        MethodType methodType = this.getMethodType();
        MethodType methodType2 = this.accessFlags.contains((Object)XAccessFlag.STATIC) ? MethodType.methodType(clazz) : MethodType.methodType(clazz, (Class)this.clazz.reflect());
        return LambdaMetafactory.metafactory(this.clazz.getNamespace().getLookup(), string, methodType2, methodType, this.reflect(), methodType);
    }

    @Override
    public MethodHandle reflect() {
        return this.clazz.getNamespace().getLookup().unreflect(this.reflectJvm());
    }

    @Override
    @NotNull
    public ReflectiveHandle<ReflectedObject> jvm() {
        return new ReflectedObjectHandle(() -> ReflectedObject.of(this.reflectJvm()));
    }

    public Method reflectJvm() {
        Objects.requireNonNull(this.returnType, "Return type not specified");
        if (this.names.isEmpty()) {
            throw new IllegalStateException("No names specified");
        }
        Throwable throwable = null;
        Method method = null;
        Class clazz = (Class)this.clazz.reflect();
        Class<?>[] classArray = FlaggedNamedMemberHandle.getParameters(this, this.parameterTypes);
        Class<?> clazz2 = this.getReturnType();
        for (String string : this.names) {
            if (method != null) break;
            try {
                method = clazz.getDeclaredMethod(string, classArray);
                if (method.getReturnType() == clazz2) continue;
                throw new NoSuchMethodException("Method named '" + string + "' was found but the return types don't match: " + this.returnType + " != " + method.getReturnType());
            } catch (NoSuchMethodException noSuchMethodException) {
                try {
                    method = clazz.getMethod(string, classArray);
                    if (method.getReturnType() == clazz2) continue;
                    throw new NoSuchMethodException("Method named '" + string + "' was found but the return types don't match: " + this.returnType + " != " + method.getReturnType());
                } catch (NoSuchMethodException noSuchMethodException2) {
                    NoSuchMethodException noSuchMethodException3 = noSuchMethodException2;
                    method = null;
                    if (throwable == null) {
                        throwable = new NoSuchMethodException("None of the methods were found for " + this);
                    }
                    throwable.addSuppressed(noSuchMethodException3);
                }
            }
        }
        if (method == null) {
            throw (NoSuchMethodException)XReflection.relativizeSuppressedExceptions(throwable);
        }
        return this.handleAccessible(method);
    }

    @Override
    public MethodMemberHandle copy() {
        MethodMemberHandle methodMemberHandle = new MethodMemberHandle(this.clazz);
        methodMemberHandle.returnType = this.returnType;
        methodMemberHandle.parameterTypes = this.parameterTypes;
        methodMemberHandle.accessFlags.addAll(this.accessFlags);
        methodMemberHandle.names.addAll(this.names);
        return methodMemberHandle;
    }

    public String toString() {
        String string = this.getClass().getSimpleName() + '{';
        string = string + this.accessFlags.stream().map(xAccessFlag -> xAccessFlag.name().toLowerCase(Locale.ENGLISH)).collect(Collectors.joining(" "));
        if (this.returnType != null) {
            string = string + this.returnType + " ";
        }
        string = string + String.join((CharSequence)"/", this.names);
        string = string + '(' + Arrays.stream(this.parameterTypes).map(Object::toString).collect(Collectors.joining(", ")) + ')';
        return string + '}';
    }
}

