package fr.xephi.authme.libs.com.google.common.reflect;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

@ElementTypesAreNonnullByDefault
public final class Reflection {
   public static String getPackageName(Class<?> clazz) {
      return getPackageName(clazz.getName());
   }

   public static String getPackageName(String classFullName) {
      int lastDot = classFullName.lastIndexOf(46);
      return lastDot < 0 ? "" : classFullName.substring(0, lastDot);
   }

   public static void initialize(Class<?>... classes) {
      Class[] var1 = classes;
      int var2 = classes.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Class clazz = var1[var3];

         try {
            Class.forName(clazz.getName(), true, clazz.getClassLoader());
         } catch (ClassNotFoundException var6) {
            throw new AssertionError(var6);
         }
      }

   }

   public static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler) {
      Preconditions.checkNotNull(handler);
      Preconditions.checkArgument(interfaceType.isInterface(), "%s is not an interface", (Object)interfaceType);
      Object object = Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[]{interfaceType}, handler);
      return interfaceType.cast(object);
   }

   private Reflection() {
   }
}
