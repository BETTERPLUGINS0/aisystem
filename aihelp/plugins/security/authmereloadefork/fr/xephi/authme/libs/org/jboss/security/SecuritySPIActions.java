package fr.xephi.authme.libs.org.jboss.security;

import java.security.AccessController;
import java.security.PrivilegedAction;

class SecuritySPIActions {
   static final PrivilegedAction<ClassLoader> GET_CONTEXT_CLASSLOADER = new PrivilegedAction<ClassLoader>() {
      public ClassLoader run() {
         return Thread.currentThread().getContextClassLoader();
      }
   };

   static ClassLoader getContextClassLoader() {
      return System.getSecurityManager() != null ? (ClassLoader)AccessController.doPrivileged(GET_CONTEXT_CLASSLOADER) : Thread.currentThread().getContextClassLoader();
   }

   static ClassLoader getCurrentClassLoader(final Class clazz) {
      return System.getSecurityManager() != null ? (ClassLoader)AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
         public ClassLoader run() {
            return clazz.getClassLoader();
         }
      }) : clazz.getClassLoader();
   }
}
