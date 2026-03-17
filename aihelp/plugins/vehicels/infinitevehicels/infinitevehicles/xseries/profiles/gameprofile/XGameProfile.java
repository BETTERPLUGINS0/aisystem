package me.PM2.infinitevehicles.xseries.profiles.gameprofile;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import java.util.UUID;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import org.checkerframework.checker.nullness.qual.Nullable;

public class XGameProfile {
   private static final boolean USE_RECORDS = XReflection.isRecord(GameProfile.class);

   private XGameProfile() {
   }

   @Nullable
   public static MojangGameProfile of(GameProfile var0) {
      if (var0 == null) {
         return null;
      } else {
         return (MojangGameProfile)(USE_RECORDS ? new NewGameProfile(var0) : new OldGameProfile(var0));
      }
   }

   public static MojangGameProfile create(UUID var0, String var1, PropertyMap var2) {
      if (USE_RECORDS) {
         return of(new GameProfile(var0, var1, var2));
      } else {
         GameProfile var3 = new GameProfile(var0, var1);
         MojangGameProfile var4 = of(var3);
         var4.properties().putAll(var2);
         return var4;
      }
   }

   public static MojangGameProfile create(UUID var0, String var1) {
      return of(new GameProfile(var0, var1));
   }
}
