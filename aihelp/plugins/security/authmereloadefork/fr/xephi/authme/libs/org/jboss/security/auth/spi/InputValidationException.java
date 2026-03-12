package fr.xephi.authme.libs.org.jboss.security.auth.spi;

public class InputValidationException extends Exception {
   public InputValidationException() {
   }

   public InputValidationException(String message) {
      super(message);
   }

   public InputValidationException(String message, Throwable cause) {
      super(message, cause);
   }
}
