package fr.xephi.authme.libs.org.picketbox.exceptions;

public class ConfigurationParsingException extends IllegalStateException {
   private static final long serialVersionUID = 1L;

   public ConfigurationParsingException() {
   }

   public ConfigurationParsingException(String arg0, Throwable arg1) {
      super(arg0, arg1);
   }

   public ConfigurationParsingException(String arg0) {
      super(arg0);
   }

   public ConfigurationParsingException(Throwable arg0) {
      super(arg0);
   }
}
