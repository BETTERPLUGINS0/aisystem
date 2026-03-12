package fr.xephi.authme.events;

import fr.xephi.authme.security.crypts.EncryptionMethod;
import org.bukkit.event.HandlerList;

public class PasswordEncryptionEvent extends CustomEvent {
   private static final HandlerList handlers = new HandlerList();
   private EncryptionMethod method;

   public PasswordEncryptionEvent(EncryptionMethod method) {
      super(false);
      this.method = method;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public EncryptionMethod getMethod() {
      return this.method;
   }

   public void setMethod(EncryptionMethod method) {
      this.method = method;
   }
}
