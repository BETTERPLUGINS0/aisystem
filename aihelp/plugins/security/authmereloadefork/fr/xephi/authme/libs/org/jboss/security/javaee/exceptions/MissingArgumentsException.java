package fr.xephi.authme.libs.org.jboss.security.javaee.exceptions;

public class MissingArgumentsException extends IllegalArgumentException {
   private static final long serialVersionUID = 1L;

   public MissingArgumentsException() {
   }

   public MissingArgumentsException(String message, Throwable cause) {
      super(message, cause);
   }

   public MissingArgumentsException(String s) {
      super(s);
   }

   public MissingArgumentsException(Throwable cause) {
      super(cause);
   }
}
