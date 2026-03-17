/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.intellij.lang.annotations.Pattern
 */
package me.zombie_striker.qav.util.xseries.reflection.jvm;

import me.zombie_striker.qav.util.xseries.reflection.jvm.NamedReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftMapping;
import org.intellij.lang.annotations.Pattern;

public interface NameableReflectiveHandle
extends NamedReflectiveHandle {
    public NameableReflectiveHandle map(MinecraftMapping var1, @Pattern(value="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String var2);

    public NameableReflectiveHandle named(@Pattern(value="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String ... var1);
}

