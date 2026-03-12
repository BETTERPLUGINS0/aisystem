package com.lenis0012.bukkit.loginsecurity.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class ReflectionBuilder {
   private final Class<?> clazz;
   private final Object instance;

   public ReflectionBuilder(String className) {
      try {
         this.clazz = Class.forName(className);
         this.instance = this.clazz.newInstance();
      } catch (Throwable var3) {
         throw var3;
      }
   }

   public ReflectionBuilder call(String methodName, Object... args) {
      try {
         Optional<Method> matchingMethod = Arrays.stream(this.clazz.getMethods()).filter((method) -> {
            return method.getName().equals(methodName);
         }).filter((method) -> {
            return method.getParameterCount() == args.length;
         }).filter((method) -> {
            return methodSignatureMatches(method, args);
         }).findFirst();
         ((Method)matchingMethod.get()).invoke(this.instance, args);
         return this;
      } catch (Throwable var4) {
         throw var4;
      }
   }

   public Object build() {
      return this.instance;
   }

   public <T> T build(Class<T> type) {
      return type.cast(this.instance);
   }

   private static boolean methodSignatureMatches(Method method, Object[] args) {
      Class<?>[] parameterTypes = method.getParameterTypes();

      for(int i = 0; i < parameterTypes.length; ++i) {
         if (!parameterTypes[i].isAssignableFrom(args[i].getClass())) {
            return false;
         }
      }

      return true;
   }

   public static boolean classExists(String className) {
      try {
         Class.forName(className);
         return true;
      } catch (ClassNotFoundException var2) {
         return false;
      }
   }
}
