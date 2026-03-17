package me.PM2.infinitevehicles.xseries.reflection.proxy;

import java.util.Map;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class ClassOverloadedMethods<T> {
   private final Map<String, OverloadedMethod<T>> mapped;

   public ClassOverloadedMethods(Map<String, OverloadedMethod<T>> var1) {
      this.mapped = var1;
   }

   public Map<String, OverloadedMethod<T>> mappings() {
      return this.mapped;
   }

   public T get(String var1, Supplier<String> var2) {
      return this.get(var1, var2, false);
   }

   public T get(String var1, Supplier<String> var2, boolean var3) {
      OverloadedMethod var4 = (OverloadedMethod)this.mapped.get(var1);
      if (var4 == null) {
         if (var3) {
            return null;
         } else {
            throw new IllegalArgumentException("Failed to find any method named '" + var1 + "' with descriptor '" + (String)var2.get() + "' " + this);
         }
      } else {
         Object var5 = var4.get(var2);
         if (var5 == null) {
            if (var3) {
               return null;
            } else {
               throw new IllegalArgumentException("Failed to find overloaded method named '" + var1 + " with descriptor: '" + (String)var2.get() + "' " + this);
            }
         } else {
            return var5;
         }
      }
   }

   public String toString() {
      return this.getClass().getSimpleName() + '(' + this.mapped + ')';
   }
}
