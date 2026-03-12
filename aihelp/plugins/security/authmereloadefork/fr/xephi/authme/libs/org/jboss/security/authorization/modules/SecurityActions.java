package fr.xephi.authme.libs.org.jboss.security.authorization.modules;

import java.security.AccessController;
import java.security.PrivilegedAction;

class SecurityActions {
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
}
