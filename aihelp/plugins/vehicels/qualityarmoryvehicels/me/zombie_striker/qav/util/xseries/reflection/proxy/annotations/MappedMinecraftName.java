/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.xseries.reflection.proxy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftMapping;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.MappedMinecraftNames;
import me.zombie_striker.qav.util.xseries.reflection.proxy.annotations.ReflectName;

@Target(value={ElementType.TYPE, ElementType.METHOD})
@Retention(value=RetentionPolicy.RUNTIME)
@Repeatable(value=MappedMinecraftNames.class)
public @interface MappedMinecraftName {
    public MinecraftMapping mapping();

    public ReflectName[] names();
}

