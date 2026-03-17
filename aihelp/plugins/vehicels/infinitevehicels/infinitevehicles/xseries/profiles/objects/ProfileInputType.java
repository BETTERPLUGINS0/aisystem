package me.PM2.infinitevehicles.xseries.profiles.objects;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.PM2.infinitevehicles.xseries.profiles.PlayerProfiles;
import me.PM2.infinitevehicles.xseries.profiles.exceptions.InvalidProfileException;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.MojangGameProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public enum ProfileInputType {
   TEXTURE_HASH(Pattern.compile("[0-9a-z]{55,70}")) {
      public MojangGameProfile getProfile(String var1) {
         String var2 = PlayerProfiles.encodeBase64("{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + var1 + "\"}}}");
         return PlayerProfiles.profileFromHashAndBase64(var1, var2);
      }
   },
   TEXTURE_URL(Pattern.compile("(?:https?://)?(?:textures\\.)?minecraft\\.net/texture/(?<hash>" + TEXTURE_HASH.pattern + ')', 2)) {
      public MojangGameProfile getProfile(String var1) {
         String var2 = ProfileInputType.extractTextureHash(var1);
         return TEXTURE_HASH.getProfile(var2);
      }
   },
   BASE64(Pattern.compile("[-A-Za-z0-9+/]{100,}={0,3}")) {
      public MojangGameProfile getProfile(String var1) {
         String var2 = PlayerProfiles.decodeBase64(var1);
         if (var2 == null) {
            throw new InvalidProfileException(var1, "Not a base64 string: " + var1);
         } else {
            String var3 = ProfileInputType.extractTextureHash(var2);
            if (var3 == null) {
               throw new InvalidProfileException(var2, "Can't extract texture hash from base64: " + var2);
            } else {
               return PlayerProfiles.profileFromHashAndBase64(var3, var1);
            }
         }
      }
   },
   UUID(Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
      public MojangGameProfile getProfile(String var1) {
         UUID var2;
         try {
            var2 = java.util.UUID.fromString(var1);
         } catch (IllegalArgumentException var4) {
            throw new InvalidProfileException(var1, "Invalid UUID string: " + var1, var4);
         }

         return Profileable.of(var2).getProfile();
      }
   },
   USERNAME(Pattern.compile("[A-Za-z0-9_]{1,16}")) {
      public MojangGameProfile getProfile(String var1) {
         return Profileable.username(var1).getProfile();
      }
   };

   @Internal
   public final Pattern pattern;
   private static final ProfileInputType[] VALUES = values();

   private ProfileInputType(Pattern param3) {
      this.pattern = var3;
   }

   public abstract MojangGameProfile getProfile(String var1);

   @Nullable
   public static ProfileInputType typeOf(@NotNull String var0) {
      Objects.requireNonNull(var0, "Identifier cannot be null");
      return (ProfileInputType)Arrays.stream(VALUES).filter((var1) -> {
         return var1.pattern.matcher(var0).matches();
      }).findFirst().orElse((Object)null);
   }

   @Nullable
   private static String extractTextureHash(String var0) {
      Matcher var1 = TEXTURE_HASH.pattern.matcher(var0);
      return var1.find() ? var1.group() : null;
   }

   // $FF: synthetic method
   private static ProfileInputType[] $values() {
      return new ProfileInputType[]{TEXTURE_HASH, TEXTURE_URL, BASE64, UUID, USERNAME};
   }

   // $FF: synthetic method
   ProfileInputType(Pattern var3, Object var4) {
      this(var3);
   }
}
