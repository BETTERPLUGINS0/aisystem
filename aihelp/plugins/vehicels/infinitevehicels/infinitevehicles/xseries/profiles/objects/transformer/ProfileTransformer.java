package me.PM2.infinitevehicles.xseries.profiles.objects.transformer;

import com.mojang.authlib.properties.Property;
import java.util.concurrent.atomic.AtomicLong;
import me.PM2.infinitevehicles.xseries.profiles.PlayerProfiles;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.MojangGameProfile;
import me.PM2.infinitevehicles.xseries.profiles.objects.Profileable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface ProfileTransformer {
   @NotNull
   MojangGameProfile transform(@NotNull Profileable var1, @NotNull MojangGameProfile var2);

   @Internal
   boolean canBeCached();

   @NotNull
   static ProfileTransformer stackable() {
      return ProfileTransformer.RemoveMetadata.INSTANCE;
   }

   @NotNull
   static ProfileTransformer nonStackable() {
      return ProfileTransformer.MakeNotStackable.INSTANCE;
   }

   @NotNull
   static ProfileTransformer removeMetadata() {
      return ProfileTransformer.RemoveMetadata.INSTANCE;
   }

   @NotNull
   static ProfileTransformer includeOriginalValue() {
      return ProfileTransformer.IncludeOriginalValue.INSTANCE;
   }

   public static final class RemoveMetadata implements ProfileTransformer {
      private static final ProfileTransformer.RemoveMetadata INSTANCE = new ProfileTransformer.RemoveMetadata();

      public MojangGameProfile transform(Profileable var1, MojangGameProfile var2) {
         PlayerProfiles.removeTimestamp(var2);
         return var2.copy((var0) -> {
            var0.removeProperty("XSeries");
            var0.removeProperty("OriginalValue");
         });
      }

      public boolean canBeCached() {
         return true;
      }
   }

   public static final class MakeNotStackable implements ProfileTransformer {
      private static final ProfileTransformer.MakeNotStackable INSTANCE = new ProfileTransformer.MakeNotStackable();
      private static final String PROPERTY_NAME = "XSeriesSeed";
      private static final AtomicLong NEXT_ID = new AtomicLong();

      public MojangGameProfile transform(Profileable var1, MojangGameProfile var2) {
         String var3 = System.currentTimeMillis() + "-" + NEXT_ID.getAndIncrement();
         return var2.copy((var1x) -> {
            var1x.setProperty("XSeriesSeed", var3);
         });
      }

      public boolean canBeCached() {
         return false;
      }
   }

   public static final class IncludeOriginalValue implements ProfileTransformer {
      private static final ProfileTransformer.IncludeOriginalValue INSTANCE = new ProfileTransformer.IncludeOriginalValue();
      public static final String PROPERTY_NAME = "OriginalValue";

      @Nullable
      public static String getOriginalValue(@NotNull MojangGameProfile var0) {
         Property var1 = var0.getProperty("OriginalValue");
         return var1 == null ? null : PlayerProfiles.getPropertyValue(var1);
      }

      public MojangGameProfile transform(Profileable var1, MojangGameProfile var2) {
         String var3 = var1.getProfileValue();
         return var2.copy((var1x) -> {
            var1x.setProperty("OriginalValue", var3);
         });
      }

      public boolean canBeCached() {
         return true;
      }
   }
}
