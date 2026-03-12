package fr.xephi.authme.listener;

import fr.xephi.authme.message.MessageKey;

public class FailedVerificationException extends Exception {
   private static final long serialVersionUID = 3903242223297960699L;
   private final MessageKey reason;
   private final String[] args;

   public FailedVerificationException(MessageKey reason, String... args) {
      this.reason = reason;
      this.args = args;
   }

   public MessageKey getReason() {
      return this.reason;
   }

   public String[] getArgs() {
      return this.args;
   }

   public String toString() {
      return this.getClass().getSimpleName() + ": reason=" + this.reason + ";args=" + (this.args == null ? "null" : String.join(", ", this.args));
   }
}
