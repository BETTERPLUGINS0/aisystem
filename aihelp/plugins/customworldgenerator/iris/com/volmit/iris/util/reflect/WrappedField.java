package com.volmit.iris.util.reflect;

import com.volmit.iris.Iris;
import java.lang.reflect.Field;

public class WrappedField<C, T> {
   private final Field field;

   public WrappedField(Class<C> origin, String methodName) {
      Field var3 = null;

      try {
         var3 = var1.getDeclaredField(var2);
         var3.setAccessible(true);
      } catch (NoSuchFieldException var5) {
         Iris.error("Failed to created WrappedField %s#%s: %s%s", var1.getSimpleName(), var2, var5.getClass().getSimpleName(), var5.getMessage().equals("") ? "" : " | " + var5.getMessage());
      }

      this.field = var3;
   }

   public T get() {
      return this.get((Object)null);
   }

   public T get(C instance) {
      if (this.field == null) {
         return null;
      } else {
         try {
            return this.field.get(var1);
         } catch (IllegalAccessException var3) {
            Iris.error("Failed to get WrappedField %s#%s: %s%s", this.field.getDeclaringClass().getSimpleName(), this.field.getName(), var3.getClass().getSimpleName(), var3.getMessage().equals("") ? "" : " | " + var3.getMessage());
            return null;
         }
      }
   }

   public void set(T value) {
      this.set((Object)null, var1);
   }

   public void set(C instance, T value) {
      if (this.field != null) {
         this.field.set(var1, var2);
      }
   }

   public boolean hasFailed() {
      return this.field == null;
   }
}
