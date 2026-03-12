package org.apache.commons.lang3;

public class ClassPathUtils {
   public static String toFullyQualifiedName(Class<?> var0, String var1) {
      Validate.notNull(var0, "context");
      Validate.notNull(var1, "resourceName");
      return toFullyQualifiedName(var0.getPackage(), var1);
   }

   public static String toFullyQualifiedName(Package var0, String var1) {
      Validate.notNull(var0, "context");
      Validate.notNull(var1, "resourceName");
      return var0.getName() + "." + var1;
   }

   public static String toFullyQualifiedPath(Class<?> var0, String var1) {
      Validate.notNull(var0, "context");
      Validate.notNull(var1, "resourceName");
      return toFullyQualifiedPath(var0.getPackage(), var1);
   }

   public static String toFullyQualifiedPath(Package var0, String var1) {
      Validate.notNull(var0, "context");
      Validate.notNull(var1, "resourceName");
      return var0.getName().replace('.', '/') + "/" + var1;
   }
}
