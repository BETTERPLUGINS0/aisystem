package fr.xephi.authme.libs.org.jboss.security.plugins.authorization;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

class SecurityActions {
   static void setContextClassLoader(final ClassLoader tccl) throws PrivilegedActionException {
      AccessController.doPrivileged(new PrivilegedExceptionAction<ClassLoader>() {
         public ClassLoader run() {
            Thread.currentThread().setContextClassLoader(tccl);
            return null;
         }
      });
   }

   static ClassLoader getContextClassLoader() throws PrivilegedActionException {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedExceptionAction<ClassLoader>() {
         public ClassLoader run() {
            return Thread.currentThread().getContextClassLoader();
         }
      });
   }

   static Class<?> loadClass(final String name) throws PrivilegedActionException {
      return (Class)AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
         public Class<?> run() throws PrivilegedActionException {
            try {
               return SecurityActions.getContextClassLoader().loadClass(name);
            } catch (Exception var2) {
               throw new PrivilegedActionException(var2);
            }
         }
      });
   }
}
