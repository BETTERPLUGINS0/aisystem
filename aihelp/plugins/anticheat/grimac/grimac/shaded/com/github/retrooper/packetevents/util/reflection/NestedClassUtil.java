package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.reflection;

import java.lang.annotation.Annotation;

public class NestedClassUtil {
   public static Class<?> getNestedClass(Class<?> cls, String name) {
      if (cls == null) {
         return null;
      } else {
         Class[] var2 = cls.getDeclaredClasses();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Class<?> subClass = var2[var4];
            if (subClass.getSimpleName().equals(name)) {
               return subClass;
            }
         }

         return null;
      }
   }

   public static Class<?> getNestedClass(Class<?> cls, int index) {
      if (cls == null) {
         return null;
      } else {
         int currentIndex = 0;
         Class[] var3 = cls.getDeclaredClasses();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Class<?> subClass = var3[var5];
            if (index == currentIndex++) {
               return subClass;
            }
         }

         return null;
      }
   }

   public static Class<?> getNestedClass(Class<?> cls, Annotation annotation, int index) {
      int currentIndex = 0;
      Class[] var4 = cls.getDeclaredClasses();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Class<?> subClass = var4[var6];
         if (subClass.isAnnotationPresent(annotation.getClass()) && index == currentIndex++) {
            return subClass;
         }
      }

      return null;
   }
}
