package fr.xephi.authme.libs.org.jboss.security.plugins.mapping;

import java.security.AccessController;
import java.security.PrivilegedAction;
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

   static ClassLoader getContextClassLoader() {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
         public ClassLoader run() {
            return Thread.currentThread().getContextClassLoader();
         }
      });
   }

   static Class<?> loadClass(final String name) throws PrivilegedActionException {
      return (Class)AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
         public Class<?> run() throws PrivilegedActionException {
            try {
               return this.getClass().getClassLoader().loadClass(name);
            } catch (Exception var4) {
               try {
                  return SecurityActions.getContextClassLoader().loadClass(name);
               } catch (Exception var3) {
                  throw new PrivilegedActionException(var3);
               }
            }
         }
      });
   }

   static Class<?> loadClass(final ClassLoader cl, final String name) throws PrivilegedActionException {
      return (Class)AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
         public Class<?> run() throws PrivilegedActionException {
            if (cl == null) {
               return SecurityActions.loadClass(name);
            } else {
               try {
                  return cl.loadClass(name);
               } catch (Exception var2) {
                  return SecurityActions.loadClass(name);
               }
            }
         }
      });
   }
}
