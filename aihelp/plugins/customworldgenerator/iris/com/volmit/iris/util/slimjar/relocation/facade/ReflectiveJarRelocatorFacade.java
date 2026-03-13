package com.volmit.iris.util.slimjar.relocation.facade;

import com.volmit.iris.util.slimjar.exceptions.RelocatorException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.jetbrains.annotations.NotNull;

public final class ReflectiveJarRelocatorFacade implements JarRelocatorFacade {
   @NotNull
   private final Object relocator;
   @NotNull
   private final Method relocatorRunMethod;

   ReflectiveJarRelocatorFacade(@NotNull Object var1, @NotNull Method var2) {
      this.relocator = var1;
      this.relocatorRunMethod = var2;
   }

   public void run() {
      try {
         this.relocatorRunMethod.invoke(this.relocator);
      } catch (IllegalAccessException | InvocationTargetException var2) {
         throw new RelocatorException("Unable to invoke relocator.", var2);
      }
   }
}
