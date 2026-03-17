package me.PM2.infinitevehicles.xseries.profiles;

import com.google.common.base.Strings;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.PM2.infinitevehicles.xseries.profiles.lock.KeyedLock;
import me.PM2.infinitevehicles.xseries.profiles.lock.KeyedLockMap;
import me.PM2.infinitevehicles.xseries.profiles.lock.MojangRequestQueue;
import me.PM2.infinitevehicles.xseries.profiles.mojang.MojangAPI;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class PlayerUUIDs {
   public static final UUID IDENTITY_UUID = new UUID(0L, 0L);
   private static final Pattern UUID_NO_DASHES = Pattern.compile("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{12})");
   public static final Map<UUID, UUID> OFFLINE_TO_ONLINE = new HashMap();
   public static final Map<UUID, UUID> ONLINE_TO_OFFLINE = new HashMap();
   public static final Map<String, UUID> USERNAME_TO_ONLINE = new HashMap();

   public static UUID UUIDFromDashlessString(String var0) {
      Matcher var1 = UUID_NO_DASHES.matcher(var0);

      try {
         return UUID.fromString(var1.replaceFirst("$1-$2-$3-$4-$5"));
      } catch (IllegalArgumentException var3) {
         throw new IllegalArgumentException("Cannot convert from dashless UUID: " + var0, var3);
      }
   }

   public static String toUndashedUUID(UUID var0) {
      return var0.toString().replace("-", "");
   }

   @NotNull
   public static UUID getOfflineUUID(@NotNull String var0) {
      return UUID.nameUUIDFromBytes(("OfflinePlayer:" + var0).getBytes(StandardCharsets.UTF_8));
   }

   public static boolean isOnlineMode() {
      return Bukkit.getOnlineMode();
   }

   @Nullable
   public static UUID getRealUUIDOfPlayer(@NotNull String var0) {
      if (Strings.isNullOrEmpty(var0)) {
         throw new IllegalArgumentException("Username is null or empty: " + var0);
      } else {
         UUID var1 = getOfflineUUID(var0);

         UUID var2;
         boolean var3;
         try {
            KeyedLockMap var10000 = MojangRequestQueue.USERNAME_REQUESTS;
            Map var10002 = USERNAME_TO_ONLINE;
            Objects.requireNonNull(var10002);
            KeyedLock var4 = var10000.lock(var0, (Function)(var10002::get));

            try {
               var2 = (UUID)var4.getOrRetryValue();
               var3 = var2 != null;
               if (var2 == null) {
                  var2 = MojangAPI.requestUsernameToUUID(var0);
                  if (var2 == null) {
                     ProfileLogger.debug("Caching null for {} ({}) because it doesn't exist.", var0, var1);
                     var2 = IDENTITY_UUID;
                  } else {
                     ONLINE_TO_OFFLINE.put(var2, var1);
                  }

                  OFFLINE_TO_ONLINE.put(var1, var2);
                  USERNAME_TO_ONLINE.put(var0, var2);
               }
            } catch (Throwable var8) {
               if (var4 != null) {
                  try {
                     var4.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }
               }

               throw var8;
            }

            if (var4 != null) {
               var4.close();
            }
         } catch (Exception var9) {
            throw new IllegalStateException("Error while getting real UUID of player: " + var0, var9);
         }

         if (var2 == IDENTITY_UUID) {
            ProfileLogger.debug("Providing null UUID for {} because it doesn't exist.", var0);
            var2 = null;
         } else {
            ProfileLogger.debug((var3 ? "Cached " : "") + "Real UUID for {} ({}) is {}", var0, var1, var2);
         }

         return var2;
      }
   }

   @Nullable
   public static UUID getRealUUIDOfPlayer(@NotNull String var0, @NotNull UUID var1) {
      Objects.requireNonNull(var1);
      if (Strings.isNullOrEmpty(var0)) {
         throw new IllegalArgumentException("Username is null or empty: " + var0);
      } else if (isOnlineMode()) {
         return var1;
      } else {
         UUID var2;
         boolean var3;
         try {
            KeyedLock var4 = MojangRequestQueue.USERNAME_REQUESTS.lock(var0, (Supplier)(() -> {
               return (UUID)OFFLINE_TO_ONLINE.get(var1);
            }));

            try {
               var2 = (UUID)var4.getOrRetryValue();
               var3 = var2 != null;
               if (var2 == null) {
                  var2 = MojangAPI.requestUsernameToUUID(var0);
                  if (var2 == null) {
                     ProfileLogger.debug("Caching null for {} ({}) because it doesn't exist.", var0, var1);
                     var2 = IDENTITY_UUID;
                  } else {
                     ONLINE_TO_OFFLINE.put(var2, var1);
                  }

                  OFFLINE_TO_ONLINE.put(var1, var2);
                  USERNAME_TO_ONLINE.put(var0, var2);
               }
            } catch (Throwable var8) {
               if (var4 != null) {
                  try {
                     var4.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }
               }

               throw var8;
            }

            if (var4 != null) {
               var4.close();
            }
         } catch (IOException var9) {
            throw new IllegalStateException("Error while getting real UUID of player: " + var0 + " (" + var1 + ')', var9);
         }

         if (var2 == IDENTITY_UUID) {
            ProfileLogger.debug("Providing null UUID for {} ({}) because it doesn't exist.", var0, var1);
            var2 = null;
         } else {
            ProfileLogger.debug((var3 ? "Cached " : "") + "Real UUID for {} ({}) is {}", var0, var1, var2);
         }

         UUID var10 = getOfflineUUID(var0);
         if (!var1.equals(var10) && !var1.equals(var2)) {
            throw new IllegalArgumentException("The provided UUID (" + var1 + ") for '" + var0 + "' doesn't match the offline UUID (" + var10 + ") or the real UUID (" + var2 + ')');
         } else {
            return var2;
         }
      }
   }
}
