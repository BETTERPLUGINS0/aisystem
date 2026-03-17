/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.MemberHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.NamedReflectiveHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.jvm.classes.ClassHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.minecraft.MinecraftMapping;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

public abstract class NamedMemberHandle
extends MemberHandle
implements NamedReflectiveHandle {
    protected final Set<String> names = new HashSet<String>(5);

    @Override
    @NotNull
    public Set<String> getPossibleNames() {
        return this.names;
    }

    protected NamedMemberHandle(ClassHandle classHandle) {
        super(classHandle);
    }

    public NamedMemberHandle map(MinecraftMapping minecraftMapping, @Pattern(value="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String string) {
        this.names.add(string);
        return this;
    }

    public NamedMemberHandle named(@Pattern(value="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String ... stringArray) {
        this.names.addAll(Arrays.asList(stringArray));
        return this;
    }

    @Override
    public abstract NamedMemberHandle clone();
}

