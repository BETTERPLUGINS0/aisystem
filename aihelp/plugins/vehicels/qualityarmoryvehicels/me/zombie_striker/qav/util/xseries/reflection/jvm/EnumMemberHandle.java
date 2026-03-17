/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.intellij.lang.annotations.Language
 *  org.intellij.lang.annotations.Pattern
 *  org.jetbrains.annotations.ApiStatus$Obsolete
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import me.zombie_striker.qav.util.xseries.reflection.jvm.MemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.NamedMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.ClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObjectHandle;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftMapping;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnumMemberHandle
extends NamedMemberHandle {
    public EnumMemberHandle(ClassHandle classHandle) {
        super(classHandle);
    }

    @Override
    public EnumMemberHandle map(MinecraftMapping minecraftMapping, @Pattern(value="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String string) {
        super.map(minecraftMapping, string);
        return this;
    }

    @Override
    public EnumMemberHandle named(@Pattern(value="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String ... stringArray) {
        super.named(stringArray);
        return this;
    }

    @Override
    @ApiStatus.Obsolete
    public MemberHandle signature(@Language(value="Java", suffix=";") String string) {
        throw new UnsupportedOperationException();
    }

    @Override
    @NotNull
    @ApiStatus.Obsolete
    public MethodHandle unreflect() {
        return (MethodHandle)super.unreflect();
    }

    @Override
    @ApiStatus.Obsolete
    @Nullable
    public MethodHandle reflectOrNull() {
        return (MethodHandle)super.reflectOrNull();
    }

    @Override
    @ApiStatus.Obsolete
    @NotNull
    public ReflectiveHandle<ReflectedObject> jvm() {
        return new ReflectedObjectHandle(() -> ReflectedObject.of(this.reflectJvm()));
    }

    @Override
    @ApiStatus.Obsolete
    public MethodHandle reflect() {
        Field field = this.reflectJvm();
        return this.clazz.getNamespace().getLookup().unreflectGetter(field);
    }

    @Nullable
    public Object getEnumConstant() {
        try {
            return this.reflectJvm().get(null);
        } catch (ReflectiveOperationException reflectiveOperationException) {
            throw XReflection.throwCheckedException(reflectiveOperationException);
        }
    }

    public Field reflectJvm() {
        if (this.names.isEmpty()) {
            throw new IllegalStateException("No enum names specified");
        }
        Throwable throwable = null;
        Field field = null;
        Class clazz = (Class)this.clazz.reflect();
        if (!clazz.isEnum()) {
            throw new IllegalStateException("Class is not an enum: " + this.clazz + " -> " + clazz);
        }
        for (String string : this.names) {
            if (field != null) break;
            try {
                field = clazz.getDeclaredField(string);
                if (field.isEnumConstant()) continue;
                throw new NoSuchFieldException("Field named '" + string + "' was found but it's not an enum constant " + this);
            } catch (NoSuchFieldException noSuchFieldException) {
                field = null;
                if (throwable == null) {
                    throwable = new NoSuchFieldException("None of the enums were found for " + this);
                }
                throwable.addSuppressed(noSuchFieldException);
            }
        }
        if (field == null) {
            throw (NoSuchFieldException)XReflection.relativizeSuppressedExceptions(throwable);
        }
        return this.handleAccessible(field);
    }

    @Override
    @ApiStatus.Obsolete
    public EnumMemberHandle copy() {
        throw new UnsupportedOperationException();
    }
}

