package fr.xephi.authme.libs.org.jboss.security;

import java.security.AccessController;
import java.security.PrivilegedAction;

class SecurityActions {
   static String getProperty(String name, String defaultValue) {
      SecurityManager sm = System.getSecurityManager();
      String prop;
      if (sm != null) {
         prop = SecurityActions.SystemPropertyAction.PRIVILEGED.getProperty(name, defaultValue);
      } else {
         prop = SecurityActions.SystemPropertyAction.NON_PRIVILEGED.getProperty(name, defaultValue);
      }

      return prop;
   }

   static ClassLoader getContextClassLoader() {
      ClassLoader loader = (ClassLoader)AccessController.doPrivileged(SecurityActions.GetTCLAction.ACTION);
      return loader;
   }

   private static class GetTCLAction implements PrivilegedAction<ClassLoader> {
      static PrivilegedAction<ClassLoader> ACTION = new SecurityActions.GetTCLAction();

      public ClassLoader run() {
         ClassLoader loader = Thread.currentThread().getContextClassLoader();
         return loader;
      }
   }

   interface SystemPropertyAction {
      SecurityActions.SystemPropertyAction PRIVILEGED = new SecurityActions.SystemPropertyAction() {
         public String getProperty(final String name, final String defaultValue) {
            String prop = (String)AccessController.doPrivileged(new PrivilegedAction<String>() {
               public String run() {
                  String p = System.getProperty(name, defaultValue);
                  return p;
               }
            });
            return prop;
         }
      };
      SecurityActions.SystemPropertyAction NON_PRIVILEGED = new SecurityActions.SystemPropertyAction() {
         public String getProperty(String name, String defaultValue) {
            String prop = System.getProperty(name, defaultValue);
            return prop;
         }
      };

      String getProperty(String var1, String var2);
   }
}
