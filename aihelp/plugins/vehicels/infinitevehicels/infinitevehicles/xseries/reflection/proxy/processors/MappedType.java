package me.PM2.infinitevehicles.xseries.reflection.proxy.processors;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import me.PM2.infinitevehicles.xseries.reflection.proxy.ReflectiveProxyObject;

public final class MappedType {
   public static final Map<Class<? extends ReflectiveProxyObject>, Class<?>> LOOK_AHEAD = new IdentityHashMap();
   public final Class<?> synthetic;
   public final Class<?> real;

   public MappedType(Class<?> var1, Class<?> var2) {
      this.synthetic = var1;
      this.real = var2;
   }

   public boolean isDifferent() {
      return this.synthetic != this.real;
   }

   public static Class<?>[] getRealTypes(MappedType[] var0) {
      return (Class[])Arrays.stream(var0).map((var0x) -> {
         return var0x.real;
      }).toArray((var0x) -> {
         return new Class[var0x];
      });
   }

   public static Class<?> getMappedTypeOrCreate(Class<? extends ReflectiveProxyObject> var0) {
      Class var1 = (Class)LOOK_AHEAD.get(var0);
      if (var1 == null) {
         ReflectiveAnnotationProcessor var2 = new ReflectiveAnnotationProcessor(var0);
         var2.processTargetClass();
         var1 = var2.getTargetClass();
         if (var1 == null) {
            throw new IllegalStateException("Look ahead type " + var0 + " could not be processed.");
         }
      }

      return var1;
   }

   public String toString() {
      return "MappedType[synthetic: " + this.synthetic + ", real: " + this.real + ']';
   }
}
