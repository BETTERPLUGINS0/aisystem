package me.PM2.infinitevehicles.xseries.profiles.mojang;

import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class PlayerProfile {
   public final UUID realUUID;
   public final GameProfile requestedGameProfile;
   public final GameProfile fetchedGameProfile;
   public final List<String> profileActions;

   PlayerProfile(UUID var1, GameProfile var2, GameProfile var3, List<String> var4) {
      this.realUUID = var1;
      this.requestedGameProfile = var2;
      this.fetchedGameProfile = var3;
      this.profileActions = var4;
   }

   boolean exists() {
      return this.fetchedGameProfile != null;
   }
}
