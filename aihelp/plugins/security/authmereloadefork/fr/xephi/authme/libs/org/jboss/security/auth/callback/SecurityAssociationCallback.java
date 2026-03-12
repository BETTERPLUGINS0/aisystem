package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import java.security.Principal;
import javax.security.auth.callback.Callback;

public class SecurityAssociationCallback implements Callback {
   private transient Principal principal;
   private transient Object credential;

   public Principal getPrincipal() {
      return this.principal;
   }

   public void setPrincipal(Principal principal) {
      this.principal = principal;
   }

   public Object getCredential() {
      return this.credential;
   }

   public void setCredential(Object credential) {
      this.credential = credential;
   }

   public void clearCredential() {
      this.credential = null;
   }
}
