package me.PM2.infinitevehicles.xseries.reflection;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.ClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.DynamicClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.jvm.classes.StaticClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.minecraft.MinecraftClassHandle;
import me.PM2.infinitevehicles.xseries.reflection.parser.ReflectionParser;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ReflectiveNamespace {
   private final Map<String, Class<?>> imports = new HashMap();
   private final Lookup lookup = MethodHandles.lookup();
   private final Set<ClassHandle> handles = Collections.newSetFromMap(new IdentityHashMap());

   protected ReflectiveNamespace() {
   }

   public ReflectiveNamespace imports(@NotNull Class<?>... var1) {
      Class[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class var5 = var2[var4];
         this.imports(var5.getSimpleName(), var5);
      }

      return this;
   }

   public ReflectiveNamespace imports(@NotNull String var1, @NotNull Class<?> var2) {
      Objects.requireNonNull(var1);
      Objects.requireNonNull(var2);
      this.imports.put(var1, var2);
      return this;
   }

   @NotNull
   @Internal
   public Map<String, Class<?>> getImports() {
      Iterator var1 = this.handles.iterator();

      while(true) {
         ClassHandle var2;
         Class var3;
         do {
            if (!var1.hasNext()) {
               return this.imports;
            }

            var2 = (ClassHandle)var1.next();
            var3 = (Class)var2.reflectOrNull();
         } while(var3 == null);

         Iterator var4 = var2.getPossibleNames().iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            this.imports.put(var5, var3);
         }
      }
   }

   @Internal
   public void link(ClassHandle var1) {
      if (var1.getNamespace() != this) {
         throw new IllegalArgumentException("Not the same namespace");
      } else {
         this.handles.add(var1);
      }
   }

   @NotNull
   @Internal
   public Lookup getLookup() {
      return this.lookup;
   }

   public StaticClassHandle of(Class<?> var1) {
      this.imports(var1);
      return new StaticClassHandle(this, var1);
   }

   public DynamicClassHandle classHandle(@Language(value = "Java",suffix = "{}") String var1) {
      DynamicClassHandle var2 = new DynamicClassHandle(this);
      return (new ReflectionParser(var1)).imports(this).parseClass(var2);
   }

   public DynamicClassHandle classHandle() {
      return new DynamicClassHandle(this);
   }

   public MinecraftClassHandle ofMinecraft(@Language(value = "Java",suffix = "{}") String var1) {
      MinecraftClassHandle var2 = new MinecraftClassHandle(this);
      return (MinecraftClassHandle)(new ReflectionParser(var1)).imports(this).parseClass(var2);
   }

   public MinecraftClassHandle ofMinecraft() {
      return new MinecraftClassHandle(this);
   }
}
