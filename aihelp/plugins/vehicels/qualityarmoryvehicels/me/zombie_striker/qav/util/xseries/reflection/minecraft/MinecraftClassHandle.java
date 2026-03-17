/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.intellij.lang.annotations.Language
 *  org.intellij.lang.annotations.Pattern
 */
package me.zombie_striker.qav.util.xseries.reflection.minecraft;

import me.zombie_striker.qav.util.xseries.reflection.ReflectiveNamespace;
import me.zombie_striker.qav.util.xseries.reflection.jvm.classes.DynamicClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftMapping;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftPackage;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.Pattern;

public class MinecraftClassHandle
extends DynamicClassHandle {
    public MinecraftClassHandle(ReflectiveNamespace reflectiveNamespace) {
        super(reflectiveNamespace);
    }

    public MinecraftClassHandle inPackage(MinecraftPackage minecraftPackage) {
        super.inPackage(minecraftPackage);
        return this;
    }

    public MinecraftClassHandle inPackage(MinecraftPackage minecraftPackage, @Pattern(value="(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String string) {
        super.inPackage(minecraftPackage, string);
        return this;
    }

    @Override
    public MinecraftClassHandle inner(@Language(value="Java", suffix="{}") String string) {
        return this.inner(this.namespace.ofMinecraft(string));
    }

    @Override
    public MinecraftClassHandle named(@Pattern(value="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String ... stringArray) {
        super.named(stringArray);
        return this;
    }

    @Override
    public MinecraftClassHandle map(MinecraftMapping minecraftMapping, @Pattern(value="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String string) {
        this.classNames.add(string);
        return this;
    }

    @Override
    public MinecraftClassHandle copy() {
        MinecraftClassHandle minecraftClassHandle = new MinecraftClassHandle(this.namespace);
        minecraftClassHandle.array = this.array;
        minecraftClassHandle.parent = this.parent;
        minecraftClassHandle.packageName = this.packageName;
        minecraftClassHandle.classNames.addAll(this.classNames);
        return minecraftClassHandle;
    }
}

