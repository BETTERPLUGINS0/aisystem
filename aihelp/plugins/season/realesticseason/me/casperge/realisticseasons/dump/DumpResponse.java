package me.casperge.realisticseasons.dump;

public class DumpResponse {
   private boolean isSucces;
   private String message;

   public DumpResponse(boolean var1, String var2) {
      this.isSucces = var1;
      this.message = var2;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String var1) {
      this.message = var1;
   }

   public boolean isSucces() {
      return this.isSucces;
   }

   public void setSucces(boolean var1) {
      this.isSucces = var1;
   }
}
