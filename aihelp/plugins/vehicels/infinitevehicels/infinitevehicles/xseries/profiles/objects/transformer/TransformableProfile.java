package me.PM2.infinitevehicles.xseries.profiles.objects.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.MojangGameProfile;
import me.PM2.infinitevehicles.xseries.profiles.objects.Profileable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class TransformableProfile implements Profileable {
   private final Profileable profileable;
   private final TransformableProfile.TransformationSequence transformers;

   @Internal
   public TransformableProfile(Profileable var1, List<ProfileTransformer> var2) {
      this.profileable = var1;
      this.transformers = new TransformableProfile.TransformationSequence(var1, var2);
   }

   public boolean isReady() {
      return true;
   }

   public Profileable transform(ProfileTransformer... var1) {
      ArrayList var2 = new ArrayList(this.transformers.transformers.length + var1.length);
      var2.addAll((Collection)Arrays.stream(this.transformers.transformers).map((var0) -> {
         return var0.transformer;
      }).collect(Collectors.toList()));
      var2.addAll(Arrays.asList(var1));
      return new TransformableProfile(this.profileable, var2);
   }

   public MojangGameProfile getProfile() {
      this.transformers.profile = this.profileable.getProfile();
      if (this.transformers.profile == null) {
         return null;
      } else {
         TransformableProfile.TransformationSequence.TransformedProfileCache[] var1 = this.transformers.transformers;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            TransformableProfile.TransformationSequence.TransformedProfileCache var4 = var1[var3];
            var4.transform();
         }

         return this.transformers.profile;
      }
   }

   private static final class TransformationSequence {
      private final Profileable profileable;
      @Nullable
      private MojangGameProfile profile;
      private boolean expired;
      private final TransformableProfile.TransformationSequence.TransformedProfileCache[] transformers;

      private TransformationSequence(Profileable var1, List<ProfileTransformer> var2) {
         this.profileable = var1;
         this.transformers = (TransformableProfile.TransformationSequence.TransformedProfileCache[])var2.stream().map((var1x) -> {
            return new TransformableProfile.TransformationSequence.TransformedProfileCache(var1x);
         }).toArray((var0) -> {
            return new TransformableProfile.TransformationSequence.TransformedProfileCache[var0];
         });
      }

      // $FF: synthetic method
      TransformationSequence(Profileable var1, List var2, Object var3) {
         this(var1, var2);
      }

      private final class TransformedProfileCache {
         @Nullable
         private final ProfileTransformer transformer;
         @Nullable
         private MojangGameProfile cacheProfile;

         private TransformedProfileCache(@Nullable ProfileTransformer param2) {
            this.transformer = var2;
         }

         private void transform() {
            if (this.cacheProfile != null && this.transformer.canBeCached()) {
               if (!TransformationSequence.this.expired) {
                  TransformationSequence.this.profile = this.cacheProfile;
                  return;
               }
            } else {
               TransformationSequence.this.expired = true;
            }

            TransformationSequence.this.profile = this.cacheProfile = this.transformer.transform(TransformationSequence.this.profileable, TransformationSequence.this.profile);
         }

         // $FF: synthetic method
         TransformedProfileCache(ProfileTransformer var2, Object var3) {
            this(var2);
         }
      }
   }
}
