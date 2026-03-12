package fr.xephi.authme.libs.org.jboss.security.factories;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

class SecurityActions {
   static ClassLoader getContextClassLoader() throws PrivilegedActionException {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedExceptionAction() {
         public Object run() throws Exception {
            return Thread.currentThread().getContextClassLoader();
         }
      });
   }

   static Class<Object> loadClass(final String fqn) throws PrivilegedActionException {
      return (Class)AccessController.doPrivileged(new PrivilegedExceptionAction() {
         public Object run() throws Exception {
            ClassLoader tcl = Thread.currentThread().getContextClassLoader();
            return tcl.loadClass(fqn);
         }
      });
   }
}
