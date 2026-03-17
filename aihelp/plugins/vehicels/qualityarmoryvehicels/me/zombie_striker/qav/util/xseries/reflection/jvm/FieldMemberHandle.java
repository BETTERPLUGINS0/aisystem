/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.intellij.lang.annotations.Language
 *  org.intellij.lang.annotations.Pattern
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.XAccessFlag;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import me.zombie_striker.qav.util.xseries.reflection.jvm.FlaggedNamedMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.MethodMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.ClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.DynamicClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObjectHandle;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftMapping;
import me.zombie_striker.qav.util.xseries.reflection.parser.ReflectionParser;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FieldMemberHandle
extends FlaggedNamedMemberHandle {
    public static final MethodHandle MODIFIERS_FIELD;
    public static final DynamicClassHandle VarHandle;
    public static final MethodHandle VAR_HANDLE_SET;
    private static final Object MODIFIERS_VAR_HANDLE;
    protected Boolean getter;

    public FieldMemberHandle(ClassHandle classHandle) {
        super(classHandle);
    }

    public boolean isGetter() {
        if (this.getter == null) {
            throw new IllegalStateException("Not specified whether the field handle is a getter or setter");
        }
        return this.getter;
    }

    public FieldMemberHandle getter() {
        this.getter = true;
        return this;
    }

    @Override
    public FieldMemberHandle asStatic() {
        super.asStatic();
        return this;
    }

    public FieldMemberHandle asFinal() {
        this.accessFlags.add(XAccessFlag.FINAL);
        return this;
    }

    @Override
    public FieldMemberHandle makeAccessible() {
        super.makeAccessible();
        return this;
    }

    public FieldMemberHandle setter() {
        this.getter = false;
        return this;
    }

    @Override
    public FieldMemberHandle returns(Class<?> clazz) {
        super.returns(clazz);
        return this;
    }

    @Override
    public FieldMemberHandle returns(ClassHandle classHandle) {
        super.returns(classHandle);
        return this;
    }

    @Override
    public FieldMemberHandle copy() {
        FieldMemberHandle fieldMemberHandle = new FieldMemberHandle(this.clazz);
        fieldMemberHandle.returnType = this.returnType;
        fieldMemberHandle.getter = this.getter;
        fieldMemberHandle.accessFlags.addAll(this.accessFlags);
        fieldMemberHandle.names.addAll(this.names);
        return fieldMemberHandle;
    }

    @Override
    public MethodHandle reflect() {
        Objects.requireNonNull(this.getter, "Not specified whether the field handle is a getter or setter");
        Field field = this.reflectJvm();
        if (this.getter.booleanValue()) {
            return this.clazz.getNamespace().getLookup().unreflectGetter(field);
        }
        return this.clazz.getNamespace().getLookup().unreflectSetter(field);
    }

    @Override
    public boolean exists() {
        try {
            this.reflectJvm();
            return true;
        } catch (ReflectiveOperationException reflectiveOperationException) {
            return false;
        }
    }

    @Override
    public FieldMemberHandle signature(@Language(value="Java", suffix=";") String string) {
        return new ReflectionParser(string).imports(this.clazz.getNamespace()).parseField(this);
    }

    @Override
    public FieldMemberHandle map(MinecraftMapping minecraftMapping, @Pattern(value="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String string) {
        super.map(minecraftMapping, string);
        return this;
    }

    @Override
    public FieldMemberHandle named(@Pattern(value="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String ... stringArray) {
        super.named(stringArray);
        return this;
    }

    @Override
    protected <T extends AccessibleObject> T handleAccessible(T t) {
        block6: {
            if ((t = super.handleAccessible(t)) == null) {
                return null;
            }
            if (this.getter != null && !this.getter.booleanValue() && this.accessFlags.contains((Object)XAccessFlag.FINAL) && this.accessFlags.contains((Object)XAccessFlag.STATIC)) {
                try {
                    int n = XAccessFlag.FINAL.remove(((Member)((Object)t)).getModifiers());
                    if (MODIFIERS_VAR_HANDLE != null) {
                        VAR_HANDLE_SET.invoke(MODIFIERS_VAR_HANDLE, t, n);
                        break block6;
                    }
                    if (MODIFIERS_FIELD != null) {
                        MODIFIERS_FIELD.invoke(t, n);
                        break block6;
                    }
                    throw new IllegalAccessException("Current Java version doesn't support modifying final fields. " + this);
                } catch (Throwable throwable) {
                    throw new ReflectiveOperationException("Cannot unfinal field " + this, throwable);
                }
            }
        }
        return t;
    }

    @Nullable
    public Object get(Object object) {
        try {
            return this.getter().reflectJvm().get(object);
        } catch (ReflectiveOperationException reflectiveOperationException) {
            throw XReflection.throwCheckedException(reflectiveOperationException);
        }
    }

    @Nullable
    public Object getStatic() {
        try {
            return this.asStatic().getter().reflectJvm().get(null);
        } catch (ReflectiveOperationException reflectiveOperationException) {
            throw XReflection.throwCheckedException(reflectiveOperationException);
        }
    }

    @Override
    @NotNull
    public ReflectiveHandle<ReflectedObject> jvm() {
        return new ReflectedObjectHandle(() -> ReflectedObject.of(this.reflectJvm()));
    }

    public Field reflectJvm() {
        Objects.requireNonNull(this.returnType, "Return type not specified");
        if (this.names.isEmpty()) {
            throw new IllegalStateException("No names specified");
        }
        Throwable throwable = null;
        Field field = null;
        Class clazz = (Class)this.clazz.reflect();
        Class<?> clazz2 = this.getReturnType();
        for (String string : this.names) {
            if (field != null) break;
            try {
                field = clazz.getDeclaredField(string);
                if (field.getType() == clazz2) continue;
                throw new NoSuchFieldException("Field named '" + string + "' was found but the types don't match: " + field + " != " + this);
            } catch (NoSuchFieldException noSuchFieldException) {
                field = null;
                if (throwable == null) {
                    throwable = new NoSuchFieldException("None of the fields were found for " + this);
                }
                throwable.addSuppressed(noSuchFieldException);
            }
        }
        if (field == null) {
            throw (NoSuchFieldException)XReflection.relativizeSuppressedExceptions(throwable);
        }
        return this.handleAccessible(field);
    }

    public String toString() {
        String string = this.getClass().getSimpleName() + '{';
        string = string + this.accessFlags.stream().map(xAccessFlag -> xAccessFlag.name().toLowerCase(Locale.ENGLISH)).collect(Collectors.joining(" "));
        if (this.returnType != null) {
            string = string + this.returnType + " ";
        }
        string = string + String.join((CharSequence)"/", this.names);
        return string + '}';
    }

    static {
        VarHandle = XReflection.classHandle().inPackage("java.lang.invoke").named("VarHandle");
        VAR_HANDLE_SET = (MethodHandle)((MethodMemberHandle)VarHandle.method().named("set").returns((Class)Void.TYPE)).parameters(Object[].class).reflectOrNull();
        Object var0 = null;
        MethodHandle methodHandle = null;
        try {
            methodHandle = (MethodHandle)XReflection.of(Field.class).field().setter().named("modifiers").returns((Class)Integer.TYPE).unreflect();
        } catch (Exception exception) {
            // empty catch block
        }
        try {
            VarHandle.reflect();
            MethodHandle methodHandle2 = ((MethodMemberHandle)XReflection.of(MethodHandles.class).method().named("privateLookupIn").returns((Class)MethodHandles.Lookup.class)).parameters(Class.class, MethodHandles.Lookup.class).reflect();
            MethodHandle methodHandle3 = ((MethodMemberHandle)VarHandle.method().named("findVarHandle").returns((Class)MethodHandles.Lookup.class)).parameters(Class.class, String.class, Class.class).reflect();
            MethodHandles.Lookup lookup = methodHandle2.invoke(Field.class, MethodHandles.lookup());
            methodHandle3.invoke(lookup, Field.class, "modifiers", Integer.TYPE);
        } catch (Throwable throwable) {
            // empty catch block
        }
        MODIFIERS_VAR_HANDLE = var0;
        MODIFIERS_FIELD = methodHandle;
    }
}

