package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64;

public class DecoderException extends IllegalStateException {
   private Throwable cause;

   DecoderException(String msg, Throwable cause) {
      super(msg);
      this.cause = cause;
   }

   public Throwable getCause() {
      return this.cause;
   }
}
