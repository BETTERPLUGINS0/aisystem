package fr.xephi.authme.libs.org.jboss.security.auth.spi.otp;

import java.security.AccessController;
import java.security.PrivilegedAction;

class SecurityActions {
   static ClassLoader getContextClassLoader() {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
         public ClassLoader run() {
            return Thread.currentThread().getContextClassLoader();
         }
      });
   }
}
