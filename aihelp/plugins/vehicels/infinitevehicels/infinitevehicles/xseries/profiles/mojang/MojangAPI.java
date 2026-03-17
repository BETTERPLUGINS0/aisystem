package me.PM2.infinitevehicles.xseries.profiles.mojang;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import me.PM2.infinitevehicles.xseries.profiles.PlayerProfiles;
import me.PM2.infinitevehicles.xseries.profiles.PlayerUUIDs;
import me.PM2.infinitevehicles.xseries.profiles.ProfileLogger;
import me.PM2.infinitevehicles.xseries.profiles.ProfilesCore;
import me.PM2.infinitevehicles.xseries.profiles.exceptions.UnknownPlayerException;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.MojangGameProfile;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.XGameProfile;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.property.XProperty;
import me.PM2.infinitevehicles.xseries.profiles.lock.KeyedLock;
import me.PM2.infinitevehicles.xseries.profiles.lock.KeyedLockMap;
import me.PM2.infinitevehicles.xseries.profiles.lock.MojangRequestQueue;
import me.PM2.infinitevehicles.xseries.profiles.objects.ProfileInputType;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.ApiStatus.Obsolete;

@Internal
public final class MojangAPI {
   private static final MojangProfileCache MOJANG_PROFILE_CACHE;
   private static final Cache<UUID, Optional<GameProfile>> INSECURE_PROFILES;
   private static final boolean REQUIRE_SECURE_PROFILES = false;
   private static final MinecraftClient USERNAME_TO_UUID;
   private static final MinecraftClient USERNAMES_TO_UUIDS;
   private static final MinecraftClient UUID_TO_PROFILE;

   @Nullable
   public static UUID requestUsernameToUUID(@NotNull String var0) {
      JsonElement var1 = USERNAME_TO_UUID.session((ProfileRequestConfiguration)null).append(var0).request();
      if (var1 == null) {
         return null;
      } else {
         JsonObject var2 = var1.getAsJsonObject();
         JsonElement var3 = var2.get("id");
         if (var3 == null) {
            throw new IllegalArgumentException("No 'id' field for UUID request for '" + var0 + "': " + var2);
         } else {
            return PlayerUUIDs.UUIDFromDashlessString(var3.getAsString());
         }
      }
   }

   @Obsolete
   private static GameProfile getCachedProfileByUsername(String var0) {
      try {
         Object var1 = ProfilesCore.GameProfileCache_get$profileByName$.invoke(ProfilesCore.USER_CACHE, var0);
         if (var1 instanceof Optional) {
            var1 = ((Optional)var1).orElse((Object)null);
         }

         GameProfile var2 = var1 == null ? PlayerProfiles.createGameProfile(PlayerUUIDs.IDENTITY_UUID, var0).object() : PlayerProfiles.sanitizeProfile((GameProfile)var1);
         ProfileLogger.debug("The cached profile for {} -> {}", var0, var1);
         return var2;
      } catch (Throwable var3) {
         ProfileLogger.LOGGER.error("Unable to get cached profile by username: {}", var0, var3);
         return null;
      }
   }

   public static Optional<GameProfile> getMojangCachedProfileFromUsername(String var0) {
      try {
         return getMojangCachedProfileFromUsername0(var0);
      } catch (Throwable var2) {
         throw XReflection.throwCheckedException(var2);
      }
   }

   private static Optional<GameProfile> getMojangCachedProfileFromUsername0(String var0) {
      String var1 = var0.toLowerCase(Locale.ENGLISH);
      Object var2 = ProfilesCore.UserCache_profilesByName.get(var1);
      Optional var3;
      if (var2 != null) {
         if (ProfilesCore.GameProfileInfo_setLastAccess != null && ProfilesCore.UserCache_getNextOperation != null) {
            long var4 = ProfilesCore.UserCache_getNextOperation.invoke(ProfilesCore.USER_CACHE);
            ProfilesCore.GameProfileInfo_setLastAccess.invoke(var2, var4);
         }

         if (ProfilesCore.GameProfileInfo_getProfile != null) {
            var3 = Optional.of(ProfilesCore.GameProfileInfo_getProfile.invoke(var2));
         } else {
            Object var6 = ProfilesCore.GameProfileInfo_nameAndId.invoke(var2);
            var3 = Optional.of(XGameProfile.create(ProfilesCore.NameAndId_id.invoke(var6), ProfilesCore.NameAndId_name.invoke(var6)).object());
         }
      } else {
         UUID var7 = PlayerUUIDs.getRealUUIDOfPlayer(var0);
         if (var7 == null) {
            return Optional.empty();
         }

         GameProfile var5 = PlayerProfiles.createGameProfile(PlayerUUIDs.isOnlineMode() ? var7 : PlayerUUIDs.getOfflineUUID(var0), var0).object();
         var3 = Optional.of(var5);
         cacheProfile(var5);
      }

      return var3;
   }

   public static Map<UUID, String> usernamesToUUIDs(@NotNull Collection<String> var0, @Nullable ProfileRequestConfiguration var1) {
      if (var0 != null && !var0.isEmpty()) {
         Iterator var2 = var0.iterator();

         String var3;
         do {
            if (!var2.hasNext()) {
               HashMap var24 = new HashMap(var0.size());
               HashMap var25 = new HashMap(var0.size());
               boolean var21 = false;

               KeyedLock var6;
               HashMap var27;
               label247: {
                  Iterator var4;
                  try {
                     var21 = true;
                     var4 = var0.iterator();

                     while(var4.hasNext()) {
                        String var5 = (String)var4.next();
                        if (!var25.containsKey(var5)) {
                           KeyedLockMap var10000 = MojangRequestQueue.USERNAME_REQUESTS;
                           Map var10002 = PlayerUUIDs.USERNAME_TO_ONLINE;
                           Objects.requireNonNull(var10002);
                           var6 = var10000.lock(var5, (Function)(var10002::get));
                           UUID var7 = (UUID)var6.getOrRetryValue();
                           if (var7 != null) {
                              var24.put(var7, var5);
                              var6.unlock();
                           } else {
                              var25.put(var5, var6);
                           }
                        }
                     }

                     if (var25.isEmpty()) {
                        var27 = var24;
                        var21 = false;
                        break label247;
                     }

                     boolean var26 = PlayerUUIDs.isOnlineMode();
                     Iterable var28 = Iterables.partition(var25.keySet(), 10);
                     Iterator var31 = var28.iterator();

                     while(var31.hasNext()) {
                        List var32 = (List)var31.next();

                        JsonArray var8;
                        try {
                           var8 = USERNAMES_TO_UUIDS.session(var1).body(var32).request().getAsJsonArray();
                        } catch (IOException var22) {
                           throw new IllegalStateException("Failed to request UUIDs for username batch: " + var32, var22);
                        }

                        Iterator var9 = var8.iterator();

                        while(var9.hasNext()) {
                           JsonElement var10 = (JsonElement)var9.next();
                           JsonObject var11 = var10.getAsJsonObject();
                           String var12 = var11.get("name").getAsString();
                           UUID var13 = PlayerUUIDs.UUIDFromDashlessString(var11.get("id").getAsString());
                           UUID var14 = PlayerUUIDs.getOfflineUUID(var12);
                           PlayerUUIDs.USERNAME_TO_ONLINE.put(var12, var13);
                           PlayerUUIDs.ONLINE_TO_OFFLINE.put(var13, var14);
                           PlayerUUIDs.OFFLINE_TO_ONLINE.put(var14, var13);
                           if (!ProfilesCore.UserCache_profilesByName.containsKey(var12)) {
                              cacheProfile(PlayerProfiles.createGameProfile(var26 ? var13 : var14, var12).object());
                           }

                           String var15 = (String)var24.put(var13, var12);
                           if (var15 != null) {
                              throw new IllegalArgumentException("Got duplicate usernames for UUID: " + var13 + " (" + var15 + " -> " + var12 + ')');
                           }
                        }
                     }

                     var21 = false;
                  } finally {
                     if (var21) {
                        Iterator var17 = var25.values().iterator();

                        while(var17.hasNext()) {
                           KeyedLock var18 = (KeyedLock)var17.next();
                           var18.unlock();
                        }

                     }
                  }

                  var4 = var25.values().iterator();

                  while(var4.hasNext()) {
                     KeyedLock var29 = (KeyedLock)var4.next();
                     var29.unlock();
                  }

                  return var24;
               }

               Iterator var30 = var25.values().iterator();

               while(var30.hasNext()) {
                  var6 = (KeyedLock)var30.next();
                  var6.unlock();
               }

               return var27;
            }

            var3 = (String)var2.next();
         } while(var3 != null && ProfileInputType.USERNAME.pattern.matcher(var3).matches());

         throw new IllegalArgumentException("One of the requested usernames is invalid: " + var3 + " in " + var0);
      } else {
         throw new IllegalArgumentException("Usernames are null or empty");
      }
   }

   @NotNull
   public static GameProfile getCachedProfileByUUID(UUID var0) {
      var0 = PlayerUUIDs.isOnlineMode() ? var0 : (UUID)PlayerUUIDs.ONLINE_TO_OFFLINE.getOrDefault(var0, var0);

      try {
         Object var1 = ProfilesCore.GameProfileCache_get$profileByUUID$.invoke(ProfilesCore.USER_CACHE, var0);
         if (var1 instanceof Optional) {
            var1 = ((Optional)var1).orElse((Object)null);
         }

         if (ProfilesCore.NameAndId_name != null && var1 != null) {
            String var2 = ProfilesCore.NameAndId_name.invoke(var1);
            UUID var3 = ProfilesCore.NameAndId_id.invoke(var1);
            var1 = XGameProfile.create(var3, var2).object();
         }

         ProfileLogger.debug("The cached profile for {} -> {}", var0, var1);
         return var1 == null ? PlayerProfiles.createNamelessGameProfile(var0).object() : PlayerProfiles.sanitizeProfile((GameProfile)var1);
      } catch (Throwable var4) {
         ProfileLogger.LOGGER.error("Unable to get cached profile by UUID: {}", var0, var4);
         return PlayerProfiles.createNamelessGameProfile(var0).object();
      }
   }

   private static void cacheProfile(GameProfile var0) {
      try {
         if (ProfilesCore.NameAndId$ctor_GameProfile == null) {
            ProfilesCore.CachedUserNameToIdResolver_add.invoke(ProfilesCore.USER_CACHE, var0);
         } else {
            Object var1 = ProfilesCore.NameAndId$ctor_GameProfile.invoke(var0);
            ProfilesCore.CachedUserNameToIdResolver_add.invoke(ProfilesCore.USER_CACHE, var1);
         }

         ProfileLogger.debug("Profile is now cached: {}", var0);
      } catch (Throwable var2) {
         ProfileLogger.LOGGER.error("Unable to cache profile {}", var0);
         var2.printStackTrace();
      }

   }

   @NotNull
   public static GameProfile getOrFetchProfile(@NotNull MojangGameProfile var0) {
      UUID var1;
      if (var0.name().equals("XSeries")) {
         var1 = var0.id();
      } else {
         var1 = PlayerUUIDs.getRealUUIDOfPlayer(var0.name(), var0.id());
         if (var1 == null) {
            throw new UnknownPlayerException(var0.name(), "Player with the given properties not found: " + var0);
         }
      }

      try {
         KeyedLock var2 = MojangRequestQueue.UUID_REQUESTS.lock(var1, (Supplier)(() -> {
            return handleCache(var0.object(), var1);
         }));

         GameProfile var12;
         label54: {
            GameProfile var8;
            try {
               GameProfile var3 = (GameProfile)var2.getOrRetryValue();
               if (var3 != null) {
                  var12 = var3;
                  break label54;
               }

               JsonElement var4 = requestProfile(var0.object(), var1);
               JsonObject var5 = var4.getAsJsonObject();
               ArrayList var6 = new ArrayList();
               GameProfile var7 = createGameProfile(var5, var6);
               var7 = PlayerProfiles.sanitizeProfile(var7);
               cacheProfile(var7);
               INSECURE_PROFILES.put(var1, Optional.of(var7));
               MOJANG_PROFILE_CACHE.cache(new PlayerProfile(var1, var0.object(), var7, var6));
               var8 = var7;
            } catch (Throwable var10) {
               if (var2 != null) {
                  try {
                     var2.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (var2 != null) {
               var2.close();
            }

            return var8;
         }

         if (var2 != null) {
            var2.close();
         }

         return var12;
      } catch (Throwable var11) {
         throw new IllegalStateException("Failed to fetch profile for " + var0, var11);
      }
   }

   @Nullable
   private static GameProfile handleCache(@NotNull GameProfile var0, UUID var1) {
      Optional var2 = (Optional)INSECURE_PROFILES.getIfPresent(var1);
      if (var2 != null) {
         ProfileLogger.debug("Found cached profile from UUID ({}): {} -> {}", var1, var0, var2);
         if (var2.isPresent()) {
            return (GameProfile)var2.get();
         } else {
            throw new UnknownPlayerException(var1, "Player with the given properties not found: " + var0);
         }
      } else {
         Optional var3 = MOJANG_PROFILE_CACHE.get(var1, var0);
         if (var3 != null) {
            INSECURE_PROFILES.put(var1, var3);
            if (var3.isPresent()) {
               return (GameProfile)var3.get();
            } else {
               throw new UnknownPlayerException(var1, "Player with the given properties not found: " + var0);
            }
         } else {
            return null;
         }
      }
   }

   @NotNull
   private static JsonElement requestProfile(@NotNull GameProfile var0, UUID var1) {
      JsonElement var2;
      try {
         var2 = UUID_TO_PROFILE.session((ProfileRequestConfiguration)null).append(PlayerUUIDs.toUndashedUUID(var1) + "?unsigned=" + true).request();
      } catch (IOException var4) {
         throw new IllegalStateException("Failed to request profile: " + var0 + " with real UUID: " + var1, var4);
      }

      if (var2 == null) {
         INSECURE_PROFILES.put(var1, Optional.empty());
         MOJANG_PROFILE_CACHE.cache(new PlayerProfile(var1, var0, (GameProfile)null, (List)null));
         throw new UnknownPlayerException(var1, "Player with the given properties not found: " + var0);
      } else {
         return var2;
      }
   }

   @NotNull
   private static GameProfile createGameProfile(JsonObject var0, List<String> var1) {
      UUID var2 = PlayerUUIDs.UUIDFromDashlessString(var0.get("id").getAsString());
      String var3 = var0.get("name").getAsString();
      ListMultimap var4 = MultimapBuilder.hashKeys().arrayListValues().build();
      JsonElement var5 = var0.get("properties");
      Iterator var7;
      JsonElement var8;
      if (var5 != null) {
         JsonArray var6 = var5.getAsJsonArray();

         String var10;
         Property var13;
         for(var7 = var6.iterator(); var7.hasNext(); var4.put(var10, var13)) {
            var8 = (JsonElement)var7.next();
            JsonObject var9 = var8.getAsJsonObject();
            var10 = var9.get("name").getAsString();
            String var11 = var9.get("value").getAsString();
            JsonElement var12 = var9.get("signature");
            if (var12 != null) {
               var13 = new Property(var10, var11, var12.getAsString());
            } else {
               var13 = new Property(var10, var11);
            }
         }
      }

      JsonElement var14 = var0.get("profileActions");
      if (var14 != null) {
         var7 = var14.getAsJsonArray().iterator();

         while(var7.hasNext()) {
            var8 = (JsonElement)var7.next();
            var1.add(var8.getAsString());
         }
      }

      PropertyMap var15 = XProperty.createPropertyMap(var4);
      return PlayerProfiles.createGameProfile(var2, var3, var15).object();
   }

   static {
      MOJANG_PROFILE_CACHE = (MojangProfileCache)(!ProfilesCore.NULLABILITY_RECORD_UPDATE ? new MojangProfileCache.GameProfileCache(ProfilesCore.YggdrasilMinecraftSessionService_insecureProfiles) : new MojangProfileCache.ProfileResultCache(ProfilesCore.YggdrasilMinecraftSessionService_insecureProfiles));
      INSECURE_PROFILES = CacheBuilder.newBuilder().expireAfterWrite(6L, TimeUnit.HOURS).build();
      USERNAME_TO_UUID = new MinecraftClient("GET", "https://api.mojang.com/users/profiles/minecraft/", new RateLimiter(600, Duration.ofMinutes(10L)));
      USERNAMES_TO_UUIDS = new MinecraftClient("POST", "https://api.minecraftservices.com/minecraft/profile/lookup/bulk/byname", new RateLimiter(600, Duration.ofMinutes(10L)));
      UUID_TO_PROFILE = new MinecraftClient("GET", "https://sessionserver.mojang.com/session/minecraft/profile/", new RateLimiter(200, Duration.ofMinutes(1L)));
   }
}
