package me.PM2.infinitevehicles.xseries.profiles.objects.cache;

import me.PM2.infinitevehicles.xseries.profiles.exceptions.MojangAPIRetryException;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.MojangGameProfile;
import me.PM2.infinitevehicles.xseries.profiles.objects.Profileable;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public abstract class CacheableProfileable implements Profileable {
   protected MojangGameProfile cache;
   protected Throwable lastError;

   public final synchronized MojangGameProfile getProfile() {
      if (this.hasExpired(true)) {
         this.lastError = null;
         this.cache = null;
      }

      if (this.lastError != null && !(this.lastError instanceof MojangAPIRetryException)) {
         throw XReflection.throwCheckedException(this.lastError);
      } else {
         if (this.cache == null) {
            try {
               this.cache = this.cacheProfile();
               this.lastError = null;
            } catch (Throwable var2) {
               this.lastError = var2;
               throw var2;
            }
         }

         return this.cache;
      }
   }

   public final boolean hasExpired() {
      return this.hasExpired(false);
   }

   public final boolean isReady() {
      return !this.hasExpired(false);
   }

   protected boolean hasExpired(boolean var1) {
      return this.lastError instanceof MojangAPIRetryException;
   }

   @NotNull
   protected abstract MojangGameProfile cacheProfile();

   public final String toString() {
      return this.getClass().getSimpleName() + "[cache=" + this.cache + ", lastError=" + this.lastError + ']';
   }
}
