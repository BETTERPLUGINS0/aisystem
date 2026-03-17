package com.bergerkiller.bukkit.tc.dep.softdependency;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public interface SoftDetectableDependency {
   void detect();

   static void detectAll(Object fieldContainer) {
      Field[] fields = fieldContainer instanceof Class ? ((Class)fieldContainer).getDeclaredFields() : fieldContainer.getClass().getDeclaredFields();
      Field[] var2 = fields;
      int var3 = fields.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];
         if (SoftDependency.class.isAssignableFrom(field.getType())) {
            try {
               field.setAccessible(true);
               SoftDetectableDependency dep;
               if (Modifier.isStatic(field.getModifiers())) {
                  dep = (SoftDetectableDependency)field.get((Object)null);
               } else {
                  if (fieldContainer instanceof Class) {
                     continue;
                  }

                  dep = (SoftDetectableDependency)field.get(fieldContainer);
               }

               if (dep != null) {
                  dep.detect();
               }
            } catch (Throwable var7) {
               throw new UnsupportedOperationException("Can't detect dependency", var7);
            }
         }
      }

   }
}
