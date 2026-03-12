package fr.xephi.authme.service.bungeecord;

import java.util.Optional;

public enum MessageType {
   LOGIN("login", true),
   LOGOUT("logout", true),
   PERFORM_LOGIN("perform.login", false);

   private final String id;
   private final boolean broadcast;

   private MessageType(String param3, boolean param4) {
      this.id = id;
      this.broadcast = broadcast;
   }

   public String getId() {
      return this.id;
   }

   public boolean isBroadcast() {
      return this.broadcast;
   }

   public static Optional<MessageType> fromId(String id) {
      MessageType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         MessageType current = var1[var3];
         if (current.getId().equals(id)) {
            return Optional.of(current);
         }
      }

      return Optional.empty();
   }

   // $FF: synthetic method
   private static MessageType[] $values() {
      return new MessageType[]{LOGIN, LOGOUT, PERFORM_LOGIN};
   }
}
