package fr.xephi.authme.libs.org.jboss.security.authorization.resources;

import java.security.AccessController;
import java.security.PrivilegedAction;

class SecurityActions {
   static String getSystemProperty(final String key, final String defaultValue) {
      return (String)AccessController.doPrivileged(new PrivilegedAction<String>() {
         public String run() {
            return System.getProperty(key, defaultValue);
         }
      });
   }
}
