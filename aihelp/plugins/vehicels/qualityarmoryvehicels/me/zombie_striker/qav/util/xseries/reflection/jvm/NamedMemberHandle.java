/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.intellij.lang.annotations.Pattern
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import me.zombie_striker.qav.util.xseries.reflection.jvm.MemberHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.NameableReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.ClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftMapping;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

public abstract class NamedMemberHandle
extends MemberHandle
implements NameableReflectiveHandle {
    protected final Set<String> names = new HashSet<String>(5);

    @Override
    @NotNull
    public Set<String> getPossibleNames() {
        return this.names;
    }

    protected NamedMemberHandle(ClassHandle classHandle) {
        super(classHandle);
    }

    @Override
    public NamedMemberHandle map(MinecraftMapping minecraftMapping, @Pattern(value="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String string) {
        this.names.add(string);
        return this;
    }

    @Override
    public NamedMemberHandle named(@Pattern(value="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String ... stringArray) {
        this.names.addAll(Arrays.asList(stringArray));
        return this;
    }

    @Override
    public abstract NamedMemberHandle copy();
}

