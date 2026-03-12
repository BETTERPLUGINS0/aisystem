package fr.xephi.authme.libs.org.jboss.security.vault;

import java.security.GeneralSecurityException;

public class SecurityVaultException extends GeneralSecurityException {
   private static final long serialVersionUID = -463686701228652165L;

   public SecurityVaultException() {
   }

   public SecurityVaultException(String message, Throwable cause) {
      super(message, cause);
   }

   public SecurityVaultException(String msg) {
      super(msg);
   }

   public SecurityVaultException(Throwable cause) {
      super(cause);
   }
}
