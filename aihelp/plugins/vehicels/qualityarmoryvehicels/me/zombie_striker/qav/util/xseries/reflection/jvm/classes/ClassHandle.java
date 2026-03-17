/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.intellij.lang.annotations.Language
 *  org.jetbrains.annotations.ApiStatus$Experimental
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Range
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm.classes;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveNamespace;
import me.zombie_striker.qav.util.xseries.reflection.constraint.ReflectiveConstraint;
import me.zombie_striker.qav.util.xseries.reflection.constraint.ReflectiveConstraintException;
import me.zombie_striker.qav.util.xseries.reflection.jvm.ConstructorMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.EnumMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.FieldMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.MethodMemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.NamedReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.DynamicClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObjectHandle;
import me.zombie_striker.qav.util.xseries.reflection.parser.ReflectionParser;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public abstract class ClassHandle
implements ReflectiveHandle<Class<?>>,
NamedReflectiveHandle {
    protected final ReflectiveNamespace namespace;
    private final Map<Class<ReflectiveConstraint>, ReflectiveConstraint> constraints = new IdentityHashMap<Class<ReflectiveConstraint>, ReflectiveConstraint>();

    protected ClassHandle(@NotNull ReflectiveNamespace reflectiveNamespace) {
        this.namespace = reflectiveNamespace;
        reflectiveNamespace.link(this);
    }

    @ApiStatus.Experimental
    @Contract(value="_ -> this", mutates="this")
    public ClassHandle constraint(@NotNull ReflectiveConstraint reflectiveConstraint) {
        this.constraints.put(reflectiveConstraint.getClass(), reflectiveConstraint);
        return this;
    }

    protected <T extends Class<?>> T checkConstraints(T t) {
        for (ReflectiveConstraint reflectiveConstraint : this.constraints.values()) {
            ReflectiveConstraint.Result result = reflectiveConstraint.appliesTo(this, t);
            if (result == ReflectiveConstraint.Result.MATCHED) continue;
            throw ReflectiveConstraintException.create(reflectiveConstraint, result, this, t);
        }
        return t;
    }

    @NotNull
    @Contract(value="_ -> new")
    public abstract ClassHandle asArray(@Range(from=1L, to=0x7FFFFFFFL) int var1);

    @NotNull
    @Contract(value="-> new")
    public final ClassHandle asArray() {
        return this.asArray(1);
    }

    @Contract(pure=true)
    public abstract boolean isArray();

    @NotNull
    @Contract(value="_ -> new")
    public DynamicClassHandle inner(@Language(value="Java", suffix="{}") String string) {
        return this.inner(this.namespace.classHandle(string));
    }

    @NotNull
    @Contract(value="_ -> param1")
    public <T extends DynamicClassHandle> T inner(@NotNull T t) {
        Objects.requireNonNull(t, "Inner handle is null");
        if (this == t) {
            throw new IllegalArgumentException("Same instance: " + this);
        }
        t.parent = this;
        this.namespace.link(this);
        return t;
    }

    public @Range(from=-1L, to=0x7FFFFFFFL) int getDimensionCount() {
        int n = -1;
        Class<?> clazz = (Class<?>)this.reflectOrNull();
        if (clazz == null) {
            return n;
        }
        do {
            clazz = clazz.getComponentType();
            ++n;
        } while (clazz != null);
        return n;
    }

    @Contract(pure=true)
    public ReflectiveNamespace getNamespace() {
        return this.namespace;
    }

    @Contract(value="-> new", pure=true)
    public MethodMemberHandle method() {
        return new MethodMemberHandle(this);
    }

    @Contract(value="_ -> new", pure=true)
    public MethodMemberHandle method(@Language(value="Java", suffix=";") String string) {
        return this.createParser(string).parseMethod(this.method());
    }

    @Contract(value="-> new", pure=true)
    public EnumMemberHandle enums() {
        return new EnumMemberHandle(this);
    }

    @Contract(value="-> new", pure=true)
    public FieldMemberHandle field() {
        return new FieldMemberHandle(this);
    }

    @Contract(value="_ -> new", pure=true)
    public FieldMemberHandle field(@Language(value="Java", suffix=";") String string) {
        return this.createParser(string).parseField(this.field());
    }

    @Contract(value="_ -> new", pure=true)
    public ConstructorMemberHandle constructor(@Language(value="Java", suffix=";") String string) {
        return this.createParser(string).parseConstructor(this.constructor());
    }

    @Contract(value="-> new", pure=true)
    public ConstructorMemberHandle constructor() {
        return new ConstructorMemberHandle(this);
    }

    @Contract(value="_ -> new", pure=true)
    public ConstructorMemberHandle constructor(Class<?> ... classArray) {
        return this.constructor().parameters(classArray);
    }

    @Contract(value="_ -> new", pure=true)
    public ConstructorMemberHandle constructor(ClassHandle ... classHandleArray) {
        return this.constructor().parameters(classHandleArray);
    }

    @Contract(value="_ -> new", pure=true)
    private ReflectionParser createParser(@Language(value="Java") String string) {
        return new ReflectionParser(string).imports(this.namespace);
    }

    public abstract ClassHandle copy();

    @Override
    @NotNull
    public ReflectiveHandle<ReflectedObject> jvm() {
        return new ReflectedObjectHandle(() -> ReflectedObject.of((Class)this.reflect()));
    }
}

