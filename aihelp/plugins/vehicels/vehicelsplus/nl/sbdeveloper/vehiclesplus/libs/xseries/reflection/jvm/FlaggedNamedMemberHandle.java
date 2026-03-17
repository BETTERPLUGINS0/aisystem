/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm;

import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.XReflection;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.NamedMemberHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes.ClassHandle;

public abstract class FlaggedNamedMemberHandle
extends NamedMemberHandle {
    protected ClassHandle returnType;
    protected boolean isStatic;

    protected FlaggedNamedMemberHandle(ClassHandle classHandle) {
        super(classHandle);
    }

    public FlaggedNamedMemberHandle asStatic() {
        this.isStatic = true;
        return this;
    }

    public FlaggedNamedMemberHandle returns(Class<?> clazz) {
        this.returnType = XReflection.of(clazz);
        return this;
    }

    public FlaggedNamedMemberHandle returns(ClassHandle classHandle) {
        this.returnType = classHandle;
        return this;
    }

    public static Class<?>[] getParameters(Object object, ClassHandle[] classHandleArray) {
        Class[] classArray = new Class[classHandleArray.length];
        int n = 0;
        for (ClassHandle classHandle : classHandleArray) {
            try {
                classArray[n++] = (Class)classHandle.unreflect();
            } catch (Throwable throwable) {
                throw XReflection.throwCheckedException(new ReflectiveOperationException("Unknown parameter " + classHandle + " for " + object, throwable));
            }
        }
        return classArray;
    }

    protected Class<?> getReturnType() {
        try {
            return (Class)this.returnType.unreflect();
        } catch (Throwable throwable) {
            throw XReflection.throwCheckedException(new ReflectiveOperationException("Unknown return type " + this.returnType + " for " + this));
        }
    }
}

