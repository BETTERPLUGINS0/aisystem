package me.PM2.infinitevehicles.xseries.reflection.minecraft;

import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.PackageHandle;
import org.intellij.lang.annotations.Pattern;

public enum MinecraftPackage implements PackageHandle {
   NMS(XReflection.NMS_PACKAGE),
   CB(XReflection.CRAFTBUKKIT_PACKAGE),
   BUKKIT("org.bukkit"),
   SPIGOT("org.spigotmc");

   private final String packageId;

   private MinecraftPackage(String param3) {
      this.packageId = var3;
   }

   public String packageId() {
      return this.name();
   }

   public String getBasePackageName() {
      return this.packageId;
   }

   public String getPackage(@Pattern("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String var1) {
      if (!var1.startsWith(".") && !var1.endsWith(".")) {
         return var1.isEmpty() || this == NMS && !XReflection.supports(17) ? this.packageId : this.packageId + '.' + var1;
      } else {
         throw new IllegalArgumentException("Package name must not start or end with a dot: " + var1 + " (" + this + ')');
      }
   }

   // $FF: synthetic method
   private static MinecraftPackage[] $values() {
      return new MinecraftPackage[]{NMS, CB, BUKKIT, SPIGOT};
   }
}
