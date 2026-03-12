package fr.xephi.authme.libs.org.picketbox.config;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

class SecurityActions {
   static ClassLoader getContextClassLoader() throws PrivilegedActionException {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedExceptionAction<ClassLoader>() {
         public ClassLoader run() throws Exception {
            return Thread.currentThread().getContextClassLoader();
         }
      });
   }

   static Class<?> loadClass(final String fqn) throws PrivilegedActionException {
      return (Class)AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
         public Class<?> run() throws Exception {
            ClassLoader tcl = Thread.currentThread().getContextClassLoader();
            return tcl.loadClass(fqn);
         }
      });
   }

   static ClassLoader getClassLoader(final Class<?> clazz) {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
         public ClassLoader run() {
            return clazz.getClassLoader();
         }
      });
   }
}
