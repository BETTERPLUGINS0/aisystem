package me.PM2.infinitevehicles.xseries.profiles.objects;

import me.PM2.infinitevehicles.xseries.profiles.exceptions.ProfileException;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.MojangGameProfile;
import me.PM2.infinitevehicles.xseries.profiles.objects.transformer.ProfileTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface DelegateProfileable extends Profileable {
   @Internal
   @NotNull
   Profileable getDelegateProfile();

   @Nullable
   @Unmodifiable
   default MojangGameProfile getProfile() {
      return this.getDelegateProfile().getProfile();
   }

   @Nullable
   default ProfileException test() {
      return this.getDelegateProfile().test();
   }

   @Nullable
   default MojangGameProfile getDisposableProfile() {
      return this.getDelegateProfile().getDisposableProfile();
   }

   default boolean isReady() {
      return this.getDelegateProfile().isReady();
   }

   @NotNull
   default Profileable transform(@NotNull ProfileTransformer... transformers) {
      return this.getDelegateProfile().transform(transformers);
   }

   @Nullable
   default String getProfileValue() {
      return this.getDelegateProfile().getProfileValue();
   }
}
