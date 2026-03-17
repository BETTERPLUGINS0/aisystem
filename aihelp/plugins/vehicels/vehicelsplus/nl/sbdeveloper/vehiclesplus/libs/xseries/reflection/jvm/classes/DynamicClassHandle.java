/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes;

import com.google.common.base.Strings;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.ReflectiveNamespace;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.XReflection;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes.ClassHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes.PackageHandle;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

public class DynamicClassHandle
extends ClassHandle {
    protected ClassHandle parent;
    protected String packageName;
    protected final Set<String> classNames = new HashSet<String>(5);
    protected int array;

    public DynamicClassHandle(ReflectiveNamespace reflectiveNamespace) {
        super(reflectiveNamespace);
    }

    public DynamicClassHandle inPackage(@Pattern(value="(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") @NotNull String string) {
        Objects.requireNonNull(string, "Null package name");
        this.packageName = string;
        return this;
    }

    public DynamicClassHandle inPackage(@NotNull PackageHandle packageHandle) {
        return this.inPackage(packageHandle, "");
    }

    public DynamicClassHandle inPackage(@NotNull PackageHandle packageHandle, @Pattern(value="(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") @NotNull String string) {
        Objects.requireNonNull(packageHandle, "Null package handle type");
        Objects.requireNonNull(string, "Null package handle name");
        if (this.parent != null) {
            throw new IllegalStateException("Cannot change package of an inner class: " + packageHandle + " -> " + string);
        }
        this.packageName = packageHandle.getPackage(string);
        return this;
    }

    public DynamicClassHandle named(@Pattern(value="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") @NotNull String ... stringArray) {
        Objects.requireNonNull(stringArray);
        for (String string : this.classNames) {
            Objects.requireNonNull(string, () -> "Cannot add null class name from: " + Arrays.toString(stringArray) + " to " + this);
        }
        this.classNames.addAll(Arrays.asList(stringArray));
        return this;
    }

    public String[] reflectClassNames() {
        if (this.parent == null) {
            Objects.requireNonNull(this.packageName, "Package name is null");
        }
        String[] stringArray = new String[this.classNames.size()];
        Class clazz = this.parent == null ? null : (Class)XReflection.of((Class)this.parent.unreflect()).asArray(0).unreflect();
        int n = 0;
        for (String string : this.classNames) {
            String string2 = clazz == null ? this.packageName + '.' + string : clazz.getName() + '$' + string;
            if (this.array != 0) {
                string2 = Strings.repeat("[", this.array) + 'L' + string2 + ';';
            }
            stringArray[n++] = string2;
        }
        return stringArray;
    }

    @Override
    public DynamicClassHandle clone() {
        DynamicClassHandle dynamicClassHandle = new DynamicClassHandle(this.namespace);
        dynamicClassHandle.array = this.array;
        dynamicClassHandle.parent = this.parent;
        dynamicClassHandle.packageName = this.packageName;
        dynamicClassHandle.classNames.addAll(this.classNames);
        return dynamicClassHandle;
    }

    @Override
    public Class<?> reflect() {
        String[] stringArray = this.reflectClassNames();
        if (stringArray.length == 0) {
            throw new IllegalStateException("No class name specified for " + this);
        }
        Throwable throwable = null;
        for (String string : stringArray) {
            try {
                return Class.forName(string);
            } catch (ClassNotFoundException classNotFoundException) {
                if (throwable == null) {
                    throwable = new ClassNotFoundException("None of the classes were found");
                }
                throwable.addSuppressed(classNotFoundException);
            }
        }
        throw (ClassNotFoundException)XReflection.relativizeSuppressedExceptions(throwable);
    }

    @Override
    public DynamicClassHandle asArray(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Array dimension cannot be negative: " + n);
        }
        this.array = n;
        return this;
    }

    @Override
    public boolean isArray() {
        return this.array > 0;
    }

    @Override
    public Set<String> getPossibleNames() {
        return this.classNames;
    }

    public String toString() {
        return this.getClass().getSimpleName() + '{' + (this.parent == null ? "" : this.parent + " -> ") + (this.parent == null ? this.packageName : (this.packageName == null ? "" : this.packageName)) + '(' + String.join((CharSequence)"|", this.classNames) + ')' + (this.array == 0 ? "" : "[" + this.array + ']') + " }";
    }
}

