package me.PM2.infinitevehicles.xseries.reflection;

import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
class CachedReflectiveHandle<T> implements ReflectiveHandle<T> {
   private final ReflectiveHandle<T> delegate;
   private T cache;
   private CachedReflectiveHandle<ReflectedObject> jvm;

   CachedReflectiveHandle(ReflectiveHandle<T> var1) {
      this.delegate = var1;
   }

   public ReflectiveHandle<T> getDelegate() {
      return this.delegate;
   }

   public ReflectiveHandle<T> copy() {
      return this.delegate.copy();
   }

   @NotNull
   public T reflect() {
      if (this.cache == null) {
         this.cache = this.delegate.reflect();
         return this.cache;
      } else {
         return this.cache;
      }
   }

   @NotNull
   public ReflectiveHandle<ReflectedObject> jvm() {
      if (this.jvm == null) {
         this.jvm = new CachedReflectiveHandle(this.delegate.jvm());
         return this.jvm;
      } else {
         return this.jvm;
      }
   }
}
