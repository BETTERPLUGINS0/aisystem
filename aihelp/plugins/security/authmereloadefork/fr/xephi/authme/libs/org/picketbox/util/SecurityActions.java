package fr.xephi.authme.libs.org.picketbox.util;

import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;

class SecurityActions {
   static Class<?> loadClass(final Class<?> theClass, final String fqn) {
      return (Class)AccessController.doPrivileged(new PrivilegedAction<Class<?>>() {
         public Class<?> run() {
            ClassLoader classLoader = theClass.getClassLoader();
            Class<?> clazz = SecurityActions.loadClass(classLoader, fqn);
            if (clazz == null) {
               classLoader = Thread.currentThread().getContextClassLoader();
               clazz = SecurityActions.loadClass(classLoader, fqn);
            }

            return clazz;
         }
      });
   }

   static Class<?> loadClass(final ClassLoader cl, final String fqn) {
      return (Class)AccessController.doPrivileged(new PrivilegedAction<Class<?>>() {
         public Class<?> run() {
            try {
               return cl.loadClass(fqn);
            } catch (ClassNotFoundException var2) {
               return null;
            }
         }
      });
   }

   static void setSystemProperty(final String key, final String value) {
      AccessController.doPrivileged(new PrivilegedAction<Object>() {
         public Object run() {
            System.setProperty(key, value);
            return null;
         }
      });
   }

   static String getSystemProperty(final String key, final String defaultValue) {
      return (String)AccessController.doPrivileged(new PrivilegedAction<String>() {
         public String run() {
            return System.getProperty(key, defaultValue);
         }
      });
   }

   static URL loadResource(final Class<?> clazz, final String resourceName) {
      return (URL)AccessController.doPrivileged(new PrivilegedAction<URL>() {
         public URL run() {
            URL url = null;
            ClassLoader clazzLoader = clazz.getClassLoader();
            url = clazzLoader.getResource(resourceName);
            if (url == null) {
               clazzLoader = Thread.currentThread().getContextClassLoader();
               url = clazzLoader.getResource(resourceName);
            }

            return url;
         }
      });
   }
}
