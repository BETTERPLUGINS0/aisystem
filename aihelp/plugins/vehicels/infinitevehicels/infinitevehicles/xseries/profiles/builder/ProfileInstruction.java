package me.PM2.infinitevehicles.xseries.profiles.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import me.PM2.infinitevehicles.xseries.profiles.ProfileLogger;
import me.PM2.infinitevehicles.xseries.profiles.exceptions.InvalidProfileException;
import me.PM2.infinitevehicles.xseries.profiles.exceptions.ProfileChangeException;
import me.PM2.infinitevehicles.xseries.profiles.exceptions.ProfileException;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.MojangGameProfile;
import me.PM2.infinitevehicles.xseries.profiles.mojang.PlayerProfileFetcherThread;
import me.PM2.infinitevehicles.xseries.profiles.mojang.ProfileRequestConfiguration;
import me.PM2.infinitevehicles.xseries.profiles.objects.DelegateProfileable;
import me.PM2.infinitevehicles.xseries.profiles.objects.ProfileContainer;
import me.PM2.infinitevehicles.xseries.profiles.objects.Profileable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class ProfileInstruction<T> implements DelegateProfileable {
   private final ProfileContainer<T> profileContainer;
   private Profileable profileable;
   private final List<Profileable> fallbacks = new ArrayList();
   private Consumer<ProfileFallback<T>> onFallback;
   private ProfileRequestConfiguration profileRequestConfiguration;
   private boolean lenient = false;

   protected ProfileInstruction(ProfileContainer<T> var1) {
      this.profileContainer = var1;
   }

   @NotNull
   @Contract(
      mutates = "this"
   )
   public T removeProfile() {
      this.profileContainer.setProfile((MojangGameProfile)null);
      return this.profileContainer.getObject();
   }

   @NotNull
   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @Experimental
   public ProfileInstruction<T> profileRequestConfiguration(ProfileRequestConfiguration var1) {
      this.profileRequestConfiguration = var1;
      return this;
   }

   @NotNull
   @Contract(
      value = "-> this",
      mutates = "this"
   )
   public ProfileInstruction<T> lenient() {
      this.lenient = true;
      return this;
   }

   @Nullable
   @Internal
   public MojangGameProfile getProfile() {
      return this.profileContainer.getProfile();
   }

   @Contract(
      pure = true
   )
   @Internal
   public Profileable getDelegateProfile() {
      return this.profileContainer;
   }

   @NotNull
   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   public ProfileInstruction<T> profile(@NotNull Profileable var1) {
      this.profileable = (Profileable)Objects.requireNonNull(var1, "Profileable is null");
      return this;
   }

   @NotNull
   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   public ProfileInstruction<T> fallback(@NotNull Profileable... var1) {
      Objects.requireNonNull(var1, "fallbacks array is null");
      this.fallbacks.addAll(Arrays.asList(var1));
      return this;
   }

   @NotNull
   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   public ProfileInstruction<T> onFallback(@Nullable Consumer<ProfileFallback<T>> var1) {
      this.onFallback = var1;
      return this;
   }

   @NotNull
   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   public ProfileInstruction<T> onFallback(@NotNull Runnable var1) {
      Objects.requireNonNull(var1, "onFallback runnable is null");
      this.onFallback = (var1x) -> {
         var1.run();
      };
      return this;
   }

   @NotNull
   public T apply() {
      Objects.requireNonNull(this.profileable, "No profile was set");
      ProfileChangeException var1 = null;
      ArrayList var2 = new ArrayList(2 + this.fallbacks.size());
      var2.add(this.profileable);
      var2.addAll(this.fallbacks);
      if (this.lenient) {
         var2.add(XSkull.getDefaultProfile());
      }

      boolean var3 = false;
      boolean var4 = false;
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         Profileable var6 = (Profileable)var5.next();

         try {
            MojangGameProfile var7 = var6.getDisposableProfile();
            if (var7 != null) {
               this.profileContainer.setProfile(var7);
               var3 = true;
               break;
            }

            if (var1 == null) {
               var1 = new ProfileChangeException("Could not set the profile for " + this.profileContainer);
            }

            var1.addSuppressed(new InvalidProfileException(var6.toString(), "Profile doesn't have a value: " + var6));
            var4 = true;
         } catch (ProfileException var8) {
            if (var1 == null) {
               var1 = new ProfileChangeException("Could not set the profile for " + this.profileContainer);
            }

            var1.addSuppressed(var8);
            var4 = true;
         }
      }

      if (var1 != null) {
         if (!var3 && !this.lenient) {
            throw var1;
         }

         ProfileLogger.debug("apply() silenced exception {}", var1);
      }

      Object var9 = this.profileContainer.getObject();
      if (var4 && this.onFallback != null) {
         ProfileFallback var10 = new ProfileFallback(this, var9, var1);
         this.onFallback.accept(var10);
         var9 = var10.getObject();
      }

      return var9;
   }

   @NotNull
   public CompletableFuture<T> applyAsync() {
      return CompletableFuture.supplyAsync(this::apply, PlayerProfileFetcherThread.EXECUTOR);
   }
}
