package me.PM2.infinitevehicles.xseries.reflection.minecraft;

import me.PM2.infinitevehicles.xseries.reflection.ReflectiveNamespace;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.DynamicClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.PackageHandle;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.Pattern;

public class MinecraftClassHandle extends DynamicClassHandle {
   public MinecraftClassHandle(ReflectiveNamespace var1) {
      super(var1);
   }

   public MinecraftClassHandle inPackage(MinecraftPackage var1) {
      super.inPackage((PackageHandle)var1);
      return this;
   }

   public MinecraftClassHandle inPackage(MinecraftPackage var1, @Pattern("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String var2) {
      super.inPackage(var1, var2);
      return this;
   }

   public MinecraftClassHandle inner(@Language(value = "Java",suffix = "{}") String var1) {
      return (MinecraftClassHandle)this.inner((DynamicClassHandle)this.namespace.ofMinecraft(var1));
   }

   public MinecraftClassHandle named(@Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String... var1) {
      super.named(var1);
      return this;
   }

   public MinecraftClassHandle map(MinecraftMapping var1, @Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") String var2) {
      this.classNames.add(var2);
      return this;
   }

   public MinecraftClassHandle copy() {
      MinecraftClassHandle var1 = new MinecraftClassHandle(this.namespace);
      var1.array = this.array;
      var1.parent = this.parent;
      var1.packageName = this.packageName;
      var1.classNames.addAll(this.classNames);
      return var1;
   }
}
