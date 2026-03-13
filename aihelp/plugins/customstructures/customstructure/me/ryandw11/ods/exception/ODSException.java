package me.ryandw11.ods.exception;

import java.io.IOException;

public class ODSException extends RuntimeException {
   private IOException ex;

   public ODSException(String s, IOException ex) {
      super(s);
      this.ex = ex;
   }

   public ODSException(String s) {
      super(s);
   }

   public IOException getIOException() {
      return this.ex;
   }

   public void printStackTrace() {
      super.printStackTrace();
      this.ex.printStackTrace();
   }
}
