package fr.xephi.authme.libs.org.postgresql.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class ObjectFactory {
   public static <T> T instantiate(Class<T> expectedClass, String classname, Properties info, boolean tryString, String stringarg) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
      Object[] args = new Object[]{info};
      Constructor<? extends T> ctor = null;
      Class cls = Class.forName(classname).asSubclass(expectedClass);

      try {
         ctor = cls.getConstructor(Properties.class);
      } catch (NoSuchMethodException var10) {
      }

      if (tryString && ctor == null) {
         try {
            ctor = cls.getConstructor(String.class);
            args = new String[]{stringarg};
         } catch (NoSuchMethodException var9) {
         }
      }

      if (ctor == null) {
         ctor = cls.getConstructor();
         args = new Object[0];
      }

      return ctor.newInstance((Object[])args);
   }
}
