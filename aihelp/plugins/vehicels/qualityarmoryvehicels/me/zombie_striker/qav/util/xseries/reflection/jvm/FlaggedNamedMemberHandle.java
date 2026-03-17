/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm;

import me.zombie_striker.qav.util.xseries.reflection.XAccessFlag;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import me.zombie_striker.qav.util.xseries.reflection.jvm.NamedMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.ClassHandle;

public abstract class FlaggedNamedMemberHandle
extends NamedMemberHandle {
    protected ClassHandle returnType;

    protected FlaggedNamedMemberHandle(ClassHandle classHandle) {
        super(classHandle);
    }

    public FlaggedNamedMemberHandle asStatic() {
        this.accessFlags.add(XAccessFlag.STATIC);
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

