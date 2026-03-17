package me.PM2.infinitevehicles.xseries.profiles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.MojangGameProfile;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.XGameProfile;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.property.XProperty;
import me.PM2.infinitevehicles.xseries.profiles.objects.transformer.ProfileTransformer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class PlayerProfiles {
   public static final String XSERIES_SIG = "XSeries";
   private static final Property XSERIES_GAMEPROFILE_SIGNATURE = XProperty.create("XSeries", "13.5.0").object();
   private static final String TEXTURES_PROPERTY = "textures";
   public static final GameProfile NIL;
   private static final Gson GSON;
   public static final String TEXTURES_NBT_PROPERTY_PREFIX = "{\"textures\":{\"SKIN\":{\"url\":\"";
   public static final String TEXTURES_BASE_URL = "http://textures.minecraft.net/texture/";

   public static Optional<Property> getTextureProperty(MojangGameProfile var0) {
      return Optional.ofNullable(var0.getProperty("textures"));
   }

   @Nullable
   public static String getTextureValue(@NotNull MojangGameProfile var0) {
      Objects.requireNonNull(var0, "Game profile cannot be null");
      return (String)getTextureProperty(var0).map(PlayerProfiles::getPropertyValue).orElse((Object)null);
   }

   @Nullable
   public static String getOriginalValue(@Nullable MojangGameProfile var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = ProfileTransformer.IncludeOriginalValue.getOriginalValue(var0);
         return var1 != null ? var1 : getTextureValue(var0);
      }
   }

   @NotNull
   public static String getPropertyValue(@NotNull Property var0) {
      if (ProfilesCore.NULLABILITY_RECORD_UPDATE) {
         return var0.value();
      } else {
         try {
            return ProfilesCore.Property_getValue.invoke(var0);
         } catch (Throwable var2) {
            throw new IllegalArgumentException("Unable to get a property value: " + var0, var2);
         }
      }
   }

   public static boolean hasTextures(MojangGameProfile var0) {
      return getTextureProperty(var0).isPresent();
   }

   @NotNull
   public static MojangGameProfile profileFromHashAndBase64(String var0, String var1) {
      UUID var2 = UUID.nameUUIDFromBytes(var0.getBytes(StandardCharsets.UTF_8));
      MojangGameProfile var3 = createNamelessGameProfile(var2);
      return setTexturesProperty(var3, var1);
   }

   public static void removeTimestamp(MojangGameProfile var0) {
      JsonObject var1 = (JsonObject)Optional.ofNullable(getTextureValue(var0)).map(PlayerProfiles::decodeBase64).map((var0x) -> {
         return (new JsonParser()).parse(var0x).getAsJsonObject();
      }).orElse((Object)null);
      if (var1 != null && var1.has("timestamp")) {
         var1.remove("timestamp");
         setTexturesProperty(var0, encodeBase64(GSON.toJson(var1)));
      }
   }

   @Nullable
   public static GameProfile fromResolvableProfile(@Nullable Object var0) {
      if (var0 == null) {
         return null;
      } else {
         if (!(var0 instanceof GameProfile) && ProfilesCore.ResolvableProfile_gameProfile != null) {
            var0 = ProfilesCore.ResolvableProfile_gameProfile.invoke(var0);
         }

         return (GameProfile)var0;
      }
   }

   @Nullable
   public static Object toResolvableProfile(@Nullable MojangGameProfile var0) {
      if (var0 == null) {
         return null;
      } else {
         return ProfilesCore.ResolvableProfile$bukkitSupports ? ProfilesCore.ResolvableProfile$constructor.invoke(var0.object()) : var0;
      }
   }

   public static GameProfile sanitizeProfile(GameProfile var0) {
      if (PlayerUUIDs.isOnlineMode()) {
         return var0;
      } else {
         MojangGameProfile var1 = XGameProfile.of(var0);
         UUID var2 = PlayerUUIDs.getOfflineUUID(var1.name());
         PlayerUUIDs.ONLINE_TO_OFFLINE.put(var1.id(), var2);
         MojangGameProfile var3 = createGameProfile(var2, var1.name(), var1.properties());
         return var3.object();
      }
   }

   @Contract(
      pure = true
   )
   public static MojangGameProfile setTexturesProperty(MojangGameProfile var0, String var1) {
      return var0.copy((var1x) -> {
         var1x.setProperty("textures", var1);
      });
   }

   @Contract(
      pure = true
   )
   public static String encodeBase64(String var0) {
      return Base64.getEncoder().encodeToString(var0.getBytes(StandardCharsets.UTF_8));
   }

   @Nullable
   @Contract(
      pure = true
   )
   public static String decodeBase64(String var0) {
      Objects.requireNonNull(var0, "Cannot decode null string");

      try {
         byte[] var1 = Base64.getDecoder().decode(var0);
         return new String(var1, StandardCharsets.UTF_8);
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }

   @Contract(
      pure = true
   )
   public static MojangGameProfile createGameProfile(UUID var0, String var1) {
      return signXSeries(XGameProfile.create(var0, var1));
   }

   @Contract(
      pure = true
   )
   public static MojangGameProfile createGameProfile(UUID var0, String var1, PropertyMap var2) {
      return signXSeries(XGameProfile.create(var0, var1, var2));
   }

   @Contract(
      pure = true
   )
   public static MojangGameProfile signXSeries(MojangGameProfile var0) {
      return var0.copy((var0x) -> {
         var0x.setProperty("XSeries", XSERIES_GAMEPROFILE_SIGNATURE);
      });
   }

   @Contract(
      pure = true
   )
   public static MojangGameProfile createNamelessGameProfile(UUID var0) {
      return createGameProfile(var0, "XSeries");
   }

   static {
      NIL = createGameProfile(PlayerUUIDs.IDENTITY_UUID, "XSeries").object();
      GSON = (new GsonBuilder()).setPrettyPrinting().create();
   }
}
