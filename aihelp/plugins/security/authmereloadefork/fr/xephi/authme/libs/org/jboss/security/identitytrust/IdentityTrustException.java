package fr.xephi.authme.libs.org.jboss.security.identitytrust;

import java.security.GeneralSecurityException;

public class IdentityTrustException extends GeneralSecurityException {
   private static final long serialVersionUID = 1L;

   public IdentityTrustException() {
   }

   public IdentityTrustException(String msg, Throwable t) {
      super(msg, t);
   }

   public IdentityTrustException(String msg) {
      super(msg);
   }

   public IdentityTrustException(Throwable t) {
      super(t);
   }
}
