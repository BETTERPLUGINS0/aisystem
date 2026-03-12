package com.lenis0012.bukkit.loginsecurity.util;

import com.lenis0012.bukkit.loginsecurity.storage.PlayerProfile;
import java.util.UUID;

public enum UserIdMode {
   UNKNOWN("U"),
   MOJANG("M"),
   OFFLINE("O");

   private final String id;

   public static UserIdMode fromId(String id) {
      UserIdMode[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         UserIdMode mode = var1[var3];
         if (mode.id.equalsIgnoreCase(id)) {
            return mode;
         }
      }

      return null;
   }

   public String getUserId(PlayerProfile profile) {
      if (profile.getUniqueIdMode() == this) {
         return profile.getUniqueUserId();
      } else {
         switch(this) {
         case OFFLINE:
            return profile.getLastName() == null ? profile.getUniqueUserId() : UUID.nameUUIDFromBytes(("OfflinePlayer:" + profile.getLastName().toLowerCase()).getBytes()).toString();
         case MOJANG:
            return profile.getUniqueUserId();
         default:
            throw new IllegalStateException("Invalid uuid mode: " + this.toString());
         }
      }
   }

   private UserIdMode(String id) {
      this.id = id;
   }

   public String getId() {
      return this.id;
   }

   // $FF: synthetic method
   private static UserIdMode[] $values() {
      return new UserIdMode[]{UNKNOWN, MOJANG, OFFLINE};
   }
}
