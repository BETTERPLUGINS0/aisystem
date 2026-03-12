package fr.xephi.authme.libs.org.jboss.security.callbacks;

import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import javax.security.auth.callback.Callback;

public class SecurityContextCallback implements Callback {
   private SecurityContext securityContext = null;

   public SecurityContext getSecurityContext() {
      return this.securityContext;
   }

   public void setSecurityContext(SecurityContext securityContext) {
      this.securityContext = securityContext;
   }
}
