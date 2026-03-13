package com.volmit.iris.util.slimjar.app;

import java.util.HashMap;
import java.util.Map;

public abstract class Application {
   private final Map<Class<?>, Object> facadeMapping = new HashMap();

   protected final <T, U extends T> T registerFacade(Class<T> var1, U var2) {
      Object var3 = this.facadeMapping.put(var1, var2);
      if (var3 != null && !var1.isAssignableFrom(var3.getClass())) {
         throw new IllegalStateException("Previous facade value did not conform to type restriction!");
      } else {
         return var3;
      }
   }

   public final <T> T getFacade(Class<T> var1) {
      Object var2 = this.facadeMapping.get(var1);
      if (var2 == null) {
         return null;
      } else if (!var1.isAssignableFrom(var2.getClass())) {
         throw new IllegalStateException("Current facade value does not conform to type restriction!");
      } else {
         return var2;
      }
   }
}
