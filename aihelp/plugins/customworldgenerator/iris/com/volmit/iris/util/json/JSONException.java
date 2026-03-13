package com.volmit.iris.util.json;

public class JSONException extends RuntimeException {
   private static final long serialVersionUID = 0L;
   private Throwable cause;

   public JSONException(String message) {
      super(var1);
   }

   public JSONException(Throwable cause) {
      super(var1.getMessage());
      this.cause = var1;
   }

   public Throwable getCause() {
      return this.cause;
   }
}
