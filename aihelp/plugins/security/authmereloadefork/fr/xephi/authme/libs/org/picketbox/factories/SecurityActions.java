package fr.xephi.authme.libs.org.picketbox.factories;

import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextAssociation;
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

   static String getSystemProperty(final String key, final String defaultValue) {
      return (String)AccessController.doPrivileged(new PrivilegedAction<String>() {
         public String run() {
            return System.getProperty(key, defaultValue);
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

   static void setSecurityContext(final SecurityContext sc) {
      AccessController.doPrivileged(new PrivilegedAction<Object>() {
         public Object run() {
            SecurityContextAssociation.setSecurityContext(sc);
            return null;
         }
      });
   }
}
