/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.stream.Collectors;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.XReflection;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.FlaggedNamedMemberHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.MemberHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes.ClassHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.parser.ReflectionParser;

public class ConstructorMemberHandle
extends MemberHandle {
    protected ClassHandle[] parameterTypes = new ClassHandle[0];

    public ConstructorMemberHandle(ClassHandle classHandle) {
        super(classHandle);
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
        if (this.isFinal) {
            throw new UnsupportedOperationException("Constructor cannot be final: " + this);
        }
        if (this.makeAccessible) {
            return this.clazz.getNamespace().getLookup().unreflectConstructor((Constructor<?>)this.reflectJvm());
        }
        Class<?>[] classArray = FlaggedNamedMemberHandle.getParameters(this, this.parameterTypes);
        return this.clazz.getNamespace().getLookup().findConstructor((Class)this.clazz.unreflect(), MethodType.methodType(Void.TYPE, classArray));
    }

    @Override
    public ConstructorMemberHandle signature(String string) {
        return new ReflectionParser(string).imports(this.clazz.getNamespace()).parseConstructor(this);
    }

    public Constructor<?> reflectJvm() {
        Class<?>[] classArray = FlaggedNamedMemberHandle.getParameters(this, this.parameterTypes);
        return this.handleAccessible(((Class)this.clazz.unreflect()).getDeclaredConstructor(classArray));
    }

    @Override
    public ConstructorMemberHandle clone() {
        ConstructorMemberHandle constructorMemberHandle = new ConstructorMemberHandle(this.clazz);
        constructorMemberHandle.parameterTypes = this.parameterTypes;
        constructorMemberHandle.isFinal = this.isFinal;
        constructorMemberHandle.makeAccessible = this.makeAccessible;
        return constructorMemberHandle;
    }

    public String toString() {
        String string = this.getClass().getSimpleName() + '{';
        if (this.makeAccessible) {
            string = string + "protected/private ";
        }
        string = string + this.clazz.toString() + ' ';
        string = string + '(' + Arrays.stream(this.parameterTypes).map(Object::toString).collect(Collectors.joining(", ")) + ')';
        return string + '}';
    }
}

