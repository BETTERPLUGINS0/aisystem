package com.lenis0012.bukkit.loginsecurity.modules.language;

public class TranslatedMessage {
   private String message;

   public TranslatedMessage(String message) {
      this.message = message;
   }

   public TranslatedMessage param(String key, Object value) {
      if (this.message == null) {
         return this;
      } else {
         this.message = this.message.replace("%" + key + "%", value.toString());
         return this;
      }
   }

   public String toString() {
      return this.message;
   }
}
