package fr.xephi.authme.libs.org.picketbox.plugins;

import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextAssociation;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextFactory;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.security.auth.Subject;

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

   static SecurityContext getSecurityContext() throws PrivilegedActionException {
      return (SecurityContext)AccessController.doPrivileged(new PrivilegedExceptionAction<SecurityContext>() {
         public SecurityContext run() throws Exception {
            return SecurityContextAssociation.getSecurityContext();
         }
      });
   }

   static SecurityContext createSecurityContext(final String name) throws PrivilegedActionException {
      return (SecurityContext)AccessController.doPrivileged(new PrivilegedExceptionAction<SecurityContext>() {
         public SecurityContext run() throws Exception {
            return SecurityContextFactory.createSecurityContext(name);
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

   static void register(final SecurityContext sc, final Principal principal, final Object credential, final Subject subject) {
      AccessController.doPrivileged(new PrivilegedAction<Object>() {
         public Object run() {
            sc.getUtil().createSubjectInfo(principal, credential, subject);
            return null;
         }
      });
   }
}
