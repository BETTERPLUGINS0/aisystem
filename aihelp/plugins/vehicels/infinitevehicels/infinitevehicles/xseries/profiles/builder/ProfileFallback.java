package me.PM2.infinitevehicles.xseries.profiles.builder;

import me.PM2.infinitevehicles.xseries.profiles.exceptions.ProfileChangeException;

public final class ProfileFallback<T> {
   private final ProfileInstruction<T> instruction;
   private T object;
   private final ProfileChangeException error;

   public ProfileFallback(ProfileInstruction<T> var1, T var2, ProfileChangeException var3) {
      this.instruction = var1;
      this.object = var2;
      this.error = var3;
   }

   public T getObject() {
      return this.object;
   }

   public ProfileInstruction<T> getInstruction() {
      return this.instruction;
   }

   public void setObject(T var1) {
      this.object = var1;
   }

   public ProfileChangeException getError() {
      return this.error;
   }
}
