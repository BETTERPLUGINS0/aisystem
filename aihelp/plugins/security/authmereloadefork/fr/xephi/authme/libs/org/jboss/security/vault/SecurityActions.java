package fr.xephi.authme.libs.org.jboss.security.vault;

import java.security.AccessController;
import java.security.PrivilegedAction;

class SecurityActions {
   static Class<?> loadClass(final Class<?> clazz, final String fqn) {
      return (Class)AccessController.doPrivileged(new PrivilegedAction<Class<?>>() {
         public Class<?> run() {
            ClassLoader cl = clazz.getClassLoader();
            Class loadedClass = null;

            try {
               loadedClass = cl.loadClass(fqn);
            } catch (ClassNotFoundException var5) {
            }

            if (loadedClass == null) {
               try {
                  loadedClass = Thread.currentThread().getContextClassLoader().loadClass(fqn);
               } catch (ClassNotFoundException var4) {
               }
            }

            return loadedClass;
         }
      });
   }

   static Class<?> loadClass(final ClassLoader classLoader, final String fqn) {
      return (Class)AccessController.doPrivileged(new PrivilegedAction<Class<?>>() {
         public Class<?> run() {
            try {
               return classLoader.loadClass(fqn);
            } catch (ClassNotFoundException var2) {
               return null;
            }
         }
      });
   }
}
