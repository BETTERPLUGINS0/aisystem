package fr.xephi.authme.libs.org.jboss.security.identitytrust;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

class SecurityActions {
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
