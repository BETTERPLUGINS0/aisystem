package fr.xephi.authme.libs.org.jboss.security.javaee;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

class SecurityActions {
   static Class<?> loadClass(final String fqn) throws PrivilegedActionException {
      return (Class)AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
         public Class<?> run() throws Exception {
            try {
               return this.getClass().getClassLoader().loadClass(fqn);
            } catch (Exception var3) {
               ClassLoader tcl = Thread.currentThread().getContextClassLoader();
               return tcl.loadClass(fqn);
            }
         }
      });
   }
}
