package me.PM2.infinitevehicles.xseries.profiles.mojang;

import com.google.common.base.Strings;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileActionType;
import com.mojang.authlib.yggdrasil.ProfileResult;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import me.PM2.infinitevehicles.xseries.profiles.PlayerProfiles;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.MojangGameProfile;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.XGameProfile;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
abstract class MojangProfileCache {
   abstract void cache(PlayerProfile var1);

   @Nullable
   abstract Optional<GameProfile> get(UUID var1, GameProfile var2);

   protected static final class GameProfileCache extends MojangProfileCache {
      private final LoadingCache<GameProfile, GameProfile> insecureProfiles;

      GameProfileCache(LoadingCache<?, ?> var1) {
         this.insecureProfiles = var1;
      }

      void cache(PlayerProfile var1) {
         if (var1.exists()) {
            this.insecureProfiles.put(var1.requestedGameProfile, var1.fetchedGameProfile);
         } else {
            this.insecureProfiles.put(var1.requestedGameProfile, PlayerProfiles.NIL);
         }

      }

      @Nullable
      Optional<GameProfile> get(UUID var1, GameProfile var2) {
         MojangGameProfile var3 = XGameProfile.of(var2);
         String var4 = var3.name();
         if (!Strings.isNullOrEmpty(var4) && !var4.equals("XSeries")) {
            GameProfile var5 = (GameProfile)this.insecureProfiles.getIfPresent(XGameProfile.create(var1, var3.name()).object());
            if (var5 == PlayerProfiles.NIL) {
               return Optional.empty();
            } else {
               return var5 == null ? null : Optional.of(var5);
            }
         } else {
            return null;
         }
      }
   }

   protected static final class ProfileResultCache extends MojangProfileCache {
      private final LoadingCache<UUID, Optional<ProfileResult>> insecureProfiles;

      ProfileResultCache(LoadingCache<?, ?> var1) {
         this.insecureProfiles = var1;
      }

      void cache(PlayerProfile var1) {
         if (var1.exists()) {
            ProfileResult var2 = new ProfileResult(var1.fetchedGameProfile, (Set)var1.profileActions.stream().map((var0) -> {
               try {
                  return ProfileActionType.valueOf(var0);
               } catch (IllegalArgumentException var2) {
                  return null;
               }
            }).filter(Objects::nonNull).collect(Collectors.toSet()));
            this.insecureProfiles.put(var1.realUUID, Optional.of(var2));
         } else {
            this.insecureProfiles.put(var1.realUUID, Optional.empty());
         }

      }

      Optional<GameProfile> get(UUID var1, GameProfile var2) {
         Optional var3 = (Optional)this.insecureProfiles.getIfPresent(var1);
         return var3 == null ? null : var3.map(ProfileResult::profile);
      }
   }
}
