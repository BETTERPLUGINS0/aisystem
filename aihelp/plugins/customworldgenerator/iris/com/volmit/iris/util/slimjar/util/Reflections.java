package com.volmit.iris.util.slimjar.util;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Reflections {
   private Reflections() {
   }

   @NotNull
   public static Constructor<?> findConstructor(@NotNull Class<?> var0, @Nullable @NotNull Object... var1) {
      try {
         return var0.getConstructor(typesFrom(var1));
      } catch (NoSuchMethodException var3) {
         return (Constructor)Arrays.stream(var0.getConstructors()).filter((var1x) -> {
            return isConstructorApplicable(var1x, var1);
         }).findFirst().orElseThrow(() -> {
            return var3;
         });
      }
   }

   @Nullable
   @NotNull
   public static Class<?>[] typesFrom(@Nullable @NotNull Object... var0) {
      Class[] var1 = new Class[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         Object var3 = var0[var2];
         var1[var2] = var3 != null ? var3.getClass() : Object.class;
      }

      return var1;
   }

   private static boolean isConstructorApplicable(@NotNull Constructor<?> var0, @Nullable @NotNull Object[] var1) {
      Class[] var2 = var0.getParameterTypes();
      if (var2.length == var1.length || var0.isVarArgs() && var1.length >= var2.length - 1) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var0.isVarArgs() && var3 == var2.length - 1) {
               Class var7 = var2[var3].getComponentType();

               for(int var5 = var3; var5 < var1.length; ++var5) {
                  Object var6 = var1[var5];
                  if (var6 != null && !var7.isInstance(var6)) {
                     return false;
                  }
               }

               return true;
            }

            if (var3 < var1.length) {
               Object var4 = var1[var3];
               if (var4 != null && !var2[var3].isInstance(var4)) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
