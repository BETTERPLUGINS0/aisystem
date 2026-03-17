package me.PM2.infinitevehicles.xseries.reflection.jvm.classes;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import me.PM2.infinitevehicles.xseries.reflection.ReflectiveNamespace;
import org.jetbrains.annotations.NotNull;

public class StaticClassHandle extends ClassHandle {
   @NotNull
   protected Class<?> clazz;

   public StaticClassHandle(ReflectiveNamespace var1, @NotNull Class<?> var2) {
      super(var1);
      this.clazz = (Class)Objects.requireNonNull(var2);
   }

   private Class<?> purifyClass() {
      Class var1 = this.clazz;

      while(true) {
         Class var2 = var1.getComponentType();
         if (var2 == null) {
            return (Class)Objects.requireNonNull(var1);
         }

         var1 = var2;
      }
   }

   public StaticClassHandle asArray(int var1) {
      Class var2 = this.purifyClass();
      if (var1 > 0) {
         for(int var3 = 0; var3 < var1; ++var3) {
            var2 = Array.newInstance(var2, 0).getClass();
         }
      }

      this.clazz = var2;
      return this;
   }

   public Class<?> reflect() {
      return this.checkConstraints(this.clazz);
   }

   public boolean isArray() {
      return this.clazz.isArray();
   }

   public Set<String> getPossibleNames() {
      return Collections.singleton(this.clazz.getSimpleName());
   }

   public StaticClassHandle copy() {
      return new StaticClassHandle(this.namespace, this.clazz);
   }

   public String toString() {
      return "StaticClassHandle(" + this.clazz + ')';
   }
}
