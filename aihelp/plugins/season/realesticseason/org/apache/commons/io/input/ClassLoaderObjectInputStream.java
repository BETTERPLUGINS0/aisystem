package org.apache.commons.io.input;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Proxy;

public class ClassLoaderObjectInputStream extends ObjectInputStream {
   private final ClassLoader classLoader;

   public ClassLoaderObjectInputStream(ClassLoader var1, InputStream var2) {
      super(var2);
      this.classLoader = var1;
   }

   protected Class<?> resolveClass(ObjectStreamClass var1) {
      try {
         return Class.forName(var1.getName(), false, this.classLoader);
      } catch (ClassNotFoundException var3) {
         return super.resolveClass(var1);
      }
   }

   protected Class<?> resolveProxyClass(String[] var1) {
      Class[] var2 = new Class[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = Class.forName(var1[var3], false, this.classLoader);
      }

      try {
         return Proxy.getProxyClass(this.classLoader, var2);
      } catch (IllegalArgumentException var4) {
         return super.resolveProxyClass(var1);
      }
   }
}
