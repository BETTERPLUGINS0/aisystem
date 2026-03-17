package me.PM2.infinitevehicles.xseries.reflection.proxy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftPackage;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReflectMinecraftPackage {
   MinecraftPackage type();

   String packageName() default "";

   boolean ignoreCurrentName() default false;
}
