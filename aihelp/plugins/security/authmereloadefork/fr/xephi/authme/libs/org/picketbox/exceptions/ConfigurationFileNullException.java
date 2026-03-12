package fr.xephi.authme.libs.org.picketbox.exceptions;

public class ConfigurationFileNullException extends IllegalArgumentException {
   private static final long serialVersionUID = 1L;

   public ConfigurationFileNullException() {
   }

   public ConfigurationFileNullException(String message, Throwable cause) {
      super(message, cause);
   }

   public ConfigurationFileNullException(String s) {
      super(s);
   }

   public ConfigurationFileNullException(Throwable cause) {
      super(cause);
   }
}
