package fr.xephi.authme.libs.org.jboss.security.plugins.javaee;

import fr.xephi.authme.libs.org.jboss.security.RunAs;
import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import java.security.AccessController;
import java.security.PrivilegedAction;

class SecurityActions {
   static RunAs getIncomingRunAs(final SecurityContext sc) {
      return (RunAs)AccessController.doPrivileged(new PrivilegedAction<RunAs>() {
         public RunAs run() {
            return sc.getIncomingRunAs();
         }
      });
   }
}
