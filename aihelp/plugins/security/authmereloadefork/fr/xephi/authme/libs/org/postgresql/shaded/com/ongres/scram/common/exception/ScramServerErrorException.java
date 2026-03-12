package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.exception;

import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.message.ServerFinalMessage;

public class ScramServerErrorException extends ScramException {
   private final ServerFinalMessage.Error error;

   private static String toString(ServerFinalMessage.Error error) {
      return "Server-final-message is an error message. Error: " + error.getErrorMessage();
   }

   public ScramServerErrorException(ServerFinalMessage.Error error) {
      super(toString(error));
      this.error = error;
   }

   public ScramServerErrorException(ServerFinalMessage.Error error, Throwable ex) {
      super(toString(error), ex);
      this.error = error;
   }

   public ServerFinalMessage.Error getError() {
      return this.error;
   }
}
