package advancedplugins.pm2.cv.models.core.util.exception;

public class UnknownVersionException extends Exception {
   public UnknownVersionException(String var1) {
      super(var1);
   }

   public UnknownVersionException() {
      super("Sorry but your server version is not currently supported.");
   }
}
