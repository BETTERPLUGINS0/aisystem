package fr.xephi.authme.libs.org.jboss.security.javaee.exceptions;

import java.security.GeneralSecurityException;

public class WrongEEResourceException extends GeneralSecurityException {
   private static final long serialVersionUID = 1L;

   public WrongEEResourceException() {
   }

   public WrongEEResourceException(String message, Throwable cause) {
      super(message, cause);
   }

   public WrongEEResourceException(String msg) {
      super(msg);
   }

   public WrongEEResourceException(Throwable cause) {
      super(cause);
   }
}
