/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes.ClassHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes.DynamicClassHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes.StaticClassHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.minecraft.MinecraftClassHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.parser.ReflectionParser;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class ReflectiveNamespace {
    private final Map<String, Class<?>> imports = new HashMap();
    private final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private final Set<ClassHandle> handles = Collections.newSetFromMap(new IdentityHashMap());

    protected ReflectiveNamespace() {
    }

    public ReflectiveNamespace imports(@NotNull Class<?> ... classArray) {
        for (Class<?> clazz : classArray) {
            this.imports(clazz.getSimpleName(), clazz);
        }
        return this;
    }

    public ReflectiveNamespace imports(@NotNull String string, @NotNull Class<?> clazz) {
        Objects.requireNonNull(string);
        Objects.requireNonNull(clazz);
        this.imports.put(string, clazz);
        return this;
    }

    @NotNull
    @ApiStatus.Internal
    public Map<String, Class<?>> getImports() {
        for (ClassHandle classHandle : this.handles) {
            Class clazz = (Class)classHandle.reflectOrNull();
            if (clazz == null) continue;
            for (String string : classHandle.getPossibleNames()) {
                this.imports.put(string, clazz);
            }
        }
        return this.imports;
    }

    @ApiStatus.Internal
    public void link(ClassHandle classHandle) {
        if (classHandle.getNamespace() != this) {
            throw new IllegalArgumentException("Not the same namespace");
        }
        this.handles.add(classHandle);
    }

    @NotNull
    @ApiStatus.Internal
    public MethodHandles.Lookup getLookup() {
        return this.lookup;
    }

    public StaticClassHandle of(Class<?> clazz) {
        this.imports(clazz);
        return new StaticClassHandle(this, clazz);
    }

    public DynamicClassHandle classHandle(@Language(value="Java") String string) {
        DynamicClassHandle dynamicClassHandle = new DynamicClassHandle(this);
        return new ReflectionParser(string).imports(this).parseClass(dynamicClassHandle);
    }

    public MinecraftClassHandle ofMinecraft(@Language(value="Java") String string) {
        MinecraftClassHandle minecraftClassHandle = new MinecraftClassHandle(this);
        return new ReflectionParser(string).imports(this).parseClass(minecraftClassHandle);
    }
}

