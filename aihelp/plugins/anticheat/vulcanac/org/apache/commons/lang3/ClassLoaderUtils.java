package org.apache.commons.lang3;

import java.net.URLClassLoader;
import java.util.Arrays;

public class ClassLoaderUtils {
   public static String toString(ClassLoader var0) {
      return var0 instanceof URLClassLoader ? toString((URLClassLoader)var0) : var0.toString();
   }

   public static String toString(URLClassLoader var0) {
      return var0 + Arrays.toString(var0.getURLs());
   }
}
