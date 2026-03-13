package com.volmit.iris.util.reflect;

import com.volmit.iris.Iris;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class WrappedReturningMethod<C, R> {
   private final Method method;

   public WrappedReturningMethod(Class<C> origin, String methodName, Class<?>... paramTypes) {
      Method var4 = null;

      try {
         var4 = var1.getDeclaredMethod(var2, var3);
         var4.setAccessible(true);
      } catch (NoSuchMethodException var6) {
         Iris.error("Failed to created WrappedMethod %s#%s: %s%s", var1.getSimpleName(), var2, var6.getClass().getSimpleName(), var6.getMessage().equals("") ? "" : " | " + var6.getMessage());
      }

      this.method = var4;
   }

   public R invoke(Object... args) {
      return this.invoke((Object)null, var1);
   }

   public R invoke(C instance, Object... args) {
      if (this.method == null) {
         return null;
      } else {
         try {
            return this.method.invoke(var1, var2);
         } catch (IllegalAccessException | InvocationTargetException var4) {
            Iris.error("Failed to invoke WrappedMethod %s#%s: %s%s", this.method.getDeclaringClass().getSimpleName(), this.method.getName(), var4.getClass().getSimpleName(), var4.getMessage().equals("") ? "" : " | " + var4.getMessage());
            return null;
         }
      }
   }
}
