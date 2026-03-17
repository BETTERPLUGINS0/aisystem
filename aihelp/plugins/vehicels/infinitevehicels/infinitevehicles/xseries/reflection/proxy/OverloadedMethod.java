package me.PM2.infinitevehicles.xseries.reflection.proxy;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public abstract class OverloadedMethod<T> {
   public abstract T get(Supplier<String> var1);

   public abstract Collection<T> getOverloads();

   static String getParameterDescriptor(Class<?>[] var0) {
      StringBuilder var1 = new StringBuilder(var0.length * 10);
      Class[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class var5 = var2[var4];

         Class var6;
         for(var6 = var5; var6.isArray(); var6 = var6.getComponentType()) {
            var1.append('[');
         }

         if (var6.isPrimitive()) {
            var1.append(getDescriptor(var6));
         } else if (var6 == String.class) {
            var1.append('@');
         } else {
            var1.append(var6.getName());
         }
      }

      return var1.toString();
   }

   static char getDescriptor(Class<?> var0) {
      if (var0 == Integer.TYPE) {
         return 'I';
      } else if (var0 == Void.TYPE) {
         return 'V';
      } else if (var0 == Boolean.TYPE) {
         return 'Z';
      } else if (var0 == Byte.TYPE) {
         return 'B';
      } else if (var0 == Character.TYPE) {
         return 'C';
      } else if (var0 == Short.TYPE) {
         return 'S';
      } else if (var0 == Double.TYPE) {
         return 'D';
      } else if (var0 == Float.TYPE) {
         return 'F';
      } else if (var0 == Long.TYPE) {
         return 'J';
      } else {
         throw new AssertionError("Unknown primitive: " + var0);
      }
   }

   private static final class Multi<T> extends OverloadedMethod<T> {
      private final Map<String, T> descriptorMap;

      public Multi(Map<String, T> var1) {
         this.descriptorMap = var1;
      }

      public T get(Supplier<String> var1) {
         return this.descriptorMap.get(var1.get());
      }

      public Collection<T> getOverloads() {
         return this.descriptorMap.values();
      }

      public String toString() {
         return "Multi(" + this.descriptorMap.keySet() + ')';
      }
   }

   private static final class Single<T> extends OverloadedMethod<T> {
      private final T object;

      public Single(T var1) {
         this.object = var1;
      }

      public T get(Supplier<String> var1) {
         return this.object;
      }

      public Collection<T> getOverloads() {
         return Collections.singleton(this.object);
      }

      public String toString() {
         return "Single";
      }
   }

   public static final class Builder<T> {
      private final Map<String, Map<String, T>> descriptorMap = new HashMap(10);
      private final Function<T, String> descritporProcessor;

      public Builder(Function<T, String> var1) {
         this.descritporProcessor = var1;
      }

      public void add(T var1, String var2) {
         Map var3 = (Map)this.descriptorMap.computeIfAbsent(var2, (var0) -> {
            return new HashMap(3);
         });
         String var4 = (String)this.descritporProcessor.apply(var1);
         if (var3.put(var4, var1) != null) {
            throw new IllegalArgumentException("Method named '" + var2 + "' with descriptor '" + var4 + "' was already added: " + var3);
         }
      }

      public ClassOverloadedMethods<T> build() {
         HashMap var1 = new HashMap(this.descriptorMap.size());
         ClassOverloadedMethods var2 = new ClassOverloadedMethods(var1);
         this.build(var1);
         return var2;
      }

      public void build(Map<String, OverloadedMethod<T>> var1) {
         Entry var3;
         Object var4;
         for(Iterator var2 = this.descriptorMap.entrySet().iterator(); var2.hasNext(); var1.put((String)var3.getKey(), var4)) {
            var3 = (Entry)var2.next();
            if (((Map)var3.getValue()).size() == 1) {
               var4 = new OverloadedMethod.Single(((Map)var3.getValue()).values().iterator().next());
            } else {
               var4 = new OverloadedMethod.Multi((Map)var3.getValue());
            }
         }

      }
   }
}
