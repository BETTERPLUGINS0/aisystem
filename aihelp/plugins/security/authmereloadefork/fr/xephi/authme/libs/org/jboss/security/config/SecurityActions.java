package fr.xephi.authme.libs.org.jboss.security.config;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

class SecurityActions {
   static <T> void addModules(final BaseSecurityInfo<T> binfo, final List<T> moduleEntries) {
      AccessController.doPrivileged(new PrivilegedAction<T>() {
         public T run() {
            binfo.add(moduleEntries);
            return null;
         }
      });
   }
}
