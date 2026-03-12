package fr.xephi.authme.libs.org.jboss.security.authorization;

import java.security.GeneralSecurityException;

public class AuthorizationException extends GeneralSecurityException {
   private static final long serialVersionUID = -1345277261367748064L;

   public AuthorizationException() {
   }

   public AuthorizationException(String msg) {
      super(msg);
   }
}
