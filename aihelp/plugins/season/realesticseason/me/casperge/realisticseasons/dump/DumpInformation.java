package me.casperge.realisticseasons.dump;

import java.util.HashMap;

public class DumpInformation {
   public HashMap<String, String> info = new HashMap();
   private String key;
   private String password;
   private boolean isRateLimit;

   public void addEntry(String var1, String var2) {
      this.info.put(var1, var2);
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String var1) {
      this.key = var1;
      this.info.put("key", var1);
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.password = var1;
      this.info.put("password", var1);
   }

   public boolean isRateLimit() {
      return this.isRateLimit;
   }

   public void setRateLimit(boolean var1) {
      this.isRateLimit = var1;
   }
}
