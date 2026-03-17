/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.XReflection;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.FlaggedNamedMemberHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes.ClassHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.minecraft.MinecraftMapping;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.parser.ReflectionParser;
import org.intellij.lang.annotations.Pattern;

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
    public MethodHandle reflect() {
        return this.clazz.getNamespace().getLookup().unreflect(this.reflectJvm());
    }

    @Override
    public MethodMemberHandle signature(String string) {
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
    public MethodMemberHandle clone() {
        MethodMemberHandle methodMemberHandle = new MethodMemberHandle(this.clazz);
        methodMemberHandle.returnType = this.returnType;
        methodMemberHandle.parameterTypes = this.parameterTypes;
        methodMemberHandle.isFinal = this.isFinal;
        methodMemberHandle.makeAccessible = this.makeAccessible;
        methodMemberHandle.names.addAll(this.names);
        return methodMemberHandle;
    }

    public String toString() {
        String string = this.getClass().getSimpleName() + '{';
        if (this.makeAccessible) {
            string = string + "protected/private ";
        }
        if (this.isFinal) {
            string = string + "final ";
        }
        if (this.returnType != null) {
            string = string + this.returnType + " ";
        }
        string = string + String.join((CharSequence)"/", this.names);
        string = string + '(' + Arrays.stream(this.parameterTypes).map(Object::toString).collect(Collectors.joining(", ")) + ')';
        return string + '}';
    }
}

