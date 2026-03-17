package me.PM2.infinitevehicles.xseries.reflection;

import me.PM2.infinitevehicles.xseries.reflection.jvm.objects.ReflectedObject;
import org.jetbrains.annotations.NotNull;

public class StaticReflectiveHandle<T> implements ReflectiveHandle<T> {
   private final T reflected;
   private final ReflectiveHandle<ReflectedObject> jvm;

   public StaticReflectiveHandle(T var1, ReflectedObject var2) {
      this.reflected = var1;
      this.jvm = new StaticReflectiveHandle(var2);
   }

   private StaticReflectiveHandle(T var1) {
      this.reflected = var1;
      this.jvm = null;
   }

   public ReflectiveHandle<T> copy() {
      return this;
   }

   @NotNull
   public T reflect() {
      return this.reflected;
   }

   @NotNull
   public ReflectiveHandle<ReflectedObject> jvm() {
      return (ReflectiveHandle)(this.jvm == null ? this : this.jvm);
   }
}
