/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Experimental
 */
package me.zombie_striker.qav.util.xseries.reflection.constraint;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.XAccessFlag;
import me.zombie_striker.qav.util.xseries.reflection.constraint.ReflectiveConstraint;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public enum ClassTypeConstraint implements ReflectiveConstraint
{
    INTERFACE{

        @Override
        protected boolean test(Class<?> clazz) {
            return clazz.isInterface();
        }
    }
    ,
    ABSTRACT{

        @Override
        protected boolean test(Class<?> clazz) {
            return XAccessFlag.ABSTRACT.isSet(clazz.getModifiers());
        }
    }
    ,
    ENUM{

        @Override
        protected boolean test(Class<?> clazz) {
            return clazz.isEnum();
        }
    }
    ,
    RECORD{
        private final MethodHandle isRecord;
        {
            MethodHandle methodHandle;
            try {
                methodHandle = MethodHandles.lookup().findVirtual(Class.class, "isRecord", MethodType.methodType(Boolean.TYPE));
            } catch (IllegalAccessException | NoSuchMethodException reflectiveOperationException) {
                methodHandle = null;
            }
            this.isRecord = methodHandle;
        }

        @Override
        protected boolean test(Class<?> clazz) {
            try {
                return this.isRecord.invoke(clazz);
            } catch (Throwable throwable) {
                throw new IllegalStateException("Cannot use Class#isRecord", throwable);
            }
        }
    }
    ,
    ANNOTATION{

        @Override
        protected boolean test(Class<?> clazz) {
            return clazz.isAnnotation();
        }
    };


    protected abstract boolean test(Class<?> var1);

    @Override
    public ReflectiveConstraint.Result appliesTo(ReflectiveHandle<?> reflectiveHandle, Object object) {
        if (object instanceof Class) {
            return ReflectiveConstraint.Result.of(this.test((Class)object));
        }
        return ReflectiveConstraint.Result.INCOMPATIBLE;
    }

    @Override
    public String category() {
        return "ClassType";
    }

    public String toString() {
        return this.getClass().getSimpleName() + "::" + this.name();
    }
}

