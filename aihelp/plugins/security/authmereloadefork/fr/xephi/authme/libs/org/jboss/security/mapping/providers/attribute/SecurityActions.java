package fr.xephi.authme.libs.org.jboss.security.mapping.providers.attribute;

import java.security.AccessController;
import java.security.PrivilegedAction;

class SecurityActions {
   static ClassLoader getContextClassLoader() {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
         public ClassLoader run() {
            return Thread.currentThread().getContextClassLoader();
         }
      });
   }

   static Void setContextClassLoader(final ClassLoader cl) {
      return (Void)AccessController.doPrivileged(new PrivilegedAction<Void>() {
         public Void run() {
            Thread.currentThread().setContextClassLoader(cl);
            return null;
         }
      });
   }
}
