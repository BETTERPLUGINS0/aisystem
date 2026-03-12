package fr.xephi.authme.libs.org.jboss.security.auth.message.config;

import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextAssociation;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

class SecurityActions {
   static ClassLoader getContextClassLoader() {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
         public ClassLoader run() {
            return Thread.currentThread().getContextClassLoader();
         }
      });
   }

   static SecurityContext getSecurityContext() {
      return (SecurityContext)AccessController.doPrivileged(new PrivilegedAction<SecurityContext>() {
         public SecurityContext run() {
            return SecurityContextAssociation.getSecurityContext();
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
}
