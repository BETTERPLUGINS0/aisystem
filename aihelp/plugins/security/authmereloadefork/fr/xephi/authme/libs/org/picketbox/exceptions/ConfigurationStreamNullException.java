package fr.xephi.authme.libs.org.picketbox.exceptions;

public class ConfigurationStreamNullException extends IllegalArgumentException {
   private static final long serialVersionUID = 1L;

   public ConfigurationStreamNullException() {
   }

   public ConfigurationStreamNullException(String arg0, Throwable arg1) {
      super(arg0, arg1);
   }

   public ConfigurationStreamNullException(String arg0) {
      super(arg0);
   }

   public ConfigurationStreamNullException(Throwable arg0) {
      super(arg0);
   }
}
