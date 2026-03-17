package me.PM2.infinitevehicles.xseries.reflection.aggregate;

import java.util.concurrent.Callable;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class VersionHandle<T> {
   private int version;
   private int patch;
   private T handle;

   @Internal
   public VersionHandle(int var1, T var2) {
      this(var1, 0, (Object)var2);
   }

   @Internal
   public VersionHandle(int var1, int var2, T var3) {
      if (XReflection.supports(var1, var2)) {
         this.version = var1;
         this.patch = var2;
         this.handle = var3;
      }

   }

   @Internal
   public VersionHandle(int var1, int var2, Callable<T> var3) {
      if (XReflection.supports(var1, var2)) {
         this.version = var1;
         this.patch = var2;

         try {
            this.handle = var3.call();
         } catch (Exception var5) {
         }
      }

   }

   @Internal
   public VersionHandle(int var1, Callable<T> var2) {
      this(var1, 0, (Callable)var2);
   }

   public VersionHandle<T> v(int var1, T var2) {
      return this.v(var1, 0, (Object)var2);
   }

   private boolean checkVersion(int var1, int var2) {
      if (var1 == this.version && var2 == this.patch) {
         throw new IllegalArgumentException("Cannot have duplicate version handles for version: " + var1 + '.' + var2);
      } else {
         return var1 > this.version && var2 >= this.patch && XReflection.supports(var1, var2);
      }
   }

   public VersionHandle<T> v(int var1, int var2, Callable<T> var3) {
      if (!this.checkVersion(var1, var2)) {
         return this;
      } else {
         try {
            this.handle = var3.call();
         } catch (Exception var5) {
         }

         this.version = var1;
         this.patch = var2;
         return this;
      }
   }

   public VersionHandle<T> v(int var1, int var2, T var3) {
      if (this.checkVersion(var1, var2)) {
         this.version = var1;
         this.patch = var2;
         this.handle = var3;
      }

      return this;
   }

   public T orElse(T var1) {
      return this.version == 0 ? var1 : this.handle;
   }

   public T orElse(Callable<T> var1) {
      if (this.version == 0) {
         try {
            return var1.call();
         } catch (Exception var3) {
            throw new IllegalArgumentException("The last handle also failed", var3);
         }
      } else {
         return this.handle;
      }
   }
}
