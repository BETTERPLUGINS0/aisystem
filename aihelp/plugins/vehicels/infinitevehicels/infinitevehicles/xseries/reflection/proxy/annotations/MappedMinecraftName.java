package me.PM2.infinitevehicles.xseries.reflection.proxy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftMapping;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MappedMinecraftNames.class)
public @interface MappedMinecraftName {
   MinecraftMapping mapping();

   ReflectName[] names();
}
