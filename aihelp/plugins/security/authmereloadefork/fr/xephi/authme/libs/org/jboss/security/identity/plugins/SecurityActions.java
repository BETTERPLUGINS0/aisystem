package fr.xephi.authme.libs.org.jboss.security.identity.plugins;

import java.security.AccessController;
import java.security.PrivilegedAction;

class SecurityActions {
   public static Class<?> getClass(final String FQN) {
      return (Class)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            try {
               return this.getClass().getClassLoader().loadClass(FQN);
            } catch (Exception var4) {
               try {
                  ClassLoader tcl = Thread.currentThread().getContextClassLoader();
                  return tcl.loadClass(FQN);
               } catch (Exception var3) {
                  throw new RuntimeException(var3);
               }
            }
         }
      });
   }
}
