package me.PM2.infinitevehicles.xseries.reflection.asm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class ASMPrivateLookup {
   private final Lookup lookup = MethodHandles.lookup();
   private final Class<?> targetClass;

   public ASMPrivateLookup(Class<?> var1) {
      this.targetClass = var1;
   }

   public MethodHandle findMethod(String var1, Class<?> var2, Class<?>[] var3) {
      Method var4 = (Method)(new ASMPrivateLookup.ReflectionIterator((var2x) -> {
         return var2x.getDeclaredMethod(var1, var3);
      })).find();
      if (var4 == null) {
         throw new IllegalArgumentException("Couldn't find method named '" + var1 + "' with type: " + var2 + " (" + Arrays.toString(var3) + ')');
      } else {
         var4.setAccessible(true);
         return this.lookup.unreflect(var4);
      }
   }

   public MethodHandle findConstructor(Class<?>[] var1) {
      try {
         Constructor var2 = this.targetClass.getDeclaredConstructor(var1);
         var2.setAccessible(true);
         return this.lookup.unreflectConstructor(var2);
      } catch (NoSuchMethodException var3) {
         throw new IllegalArgumentException("Couldn't find constructor with type:  (" + Arrays.toString(var1) + ')', var3);
      }
   }

   public MethodHandle findField(String var1, Class<?> var2, boolean var3) {
      Field var4 = (Field)(new ASMPrivateLookup.ReflectionIterator((var1x) -> {
         return var1x.getDeclaredField(var1);
      })).find();
      if (var4 == null) {
         throw new IllegalArgumentException("Couldn't find field named '" + var1 + "' with type: " + var2);
      } else {
         var4.setAccessible(true);
         return var3 ? this.lookup.unreflectGetter(var4) : this.lookup.unreflectSetter(var4);
      }
   }

   private final class ReflectionIterator<T> {
      private final ASMPrivateLookup.UnsafeFunction<Class<?>, T> finder;

      private ReflectionIterator(ASMPrivateLookup.UnsafeFunction<Class<?>, T> param2) {
         this.finder = var2;
      }

      private T find() {
         try {
            return this.iterate(ASMPrivateLookup.this.targetClass);
         } catch (Exception var2) {
            throw new IllegalStateException(var2);
         }
      }

      private T iterate(Class<?> var1) {
         if (var1 == Object.class) {
            return null;
         } else {
            try {
               return this.finder.apply(var1);
            } catch (NoSuchFieldException | NoSuchMethodException var3) {
               return this.iterate(var1.getSuperclass());
            }
         }
      }

      // $FF: synthetic method
      ReflectionIterator(ASMPrivateLookup.UnsafeFunction var2, Object var3) {
         this(var2);
      }
   }

   @FunctionalInterface
   private interface UnsafeFunction<I, O> {
      O apply(I var1) throws Exception;
   }
}
