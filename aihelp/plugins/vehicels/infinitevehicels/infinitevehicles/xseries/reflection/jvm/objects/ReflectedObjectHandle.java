package me.PM2.infinitevehicles.xseries.reflection.jvm.objects;

import me.PM2.infinitevehicles.xseries.reflection.ReflectiveHandle;
import org.jetbrains.annotations.NotNull;

public final class ReflectedObjectHandle implements ReflectiveHandle<ReflectedObject> {
   private final ReflectedObjectHandle.ReflectiveOperation jvmGetter;

   public ReflectedObjectHandle(ReflectedObjectHandle.ReflectiveOperation var1) {
      this.jvmGetter = var1;
   }

   public ReflectiveHandle<ReflectedObject> copy() {
      return new ReflectedObjectHandle(this.jvmGetter);
   }

   @NotNull
   public ReflectedObject reflect() {
      return this.jvmGetter.get();
   }

   @NotNull
   public ReflectiveHandle<ReflectedObject> jvm() {
      return this;
   }

   @FunctionalInterface
   public interface ReflectiveOperation {
      ReflectedObject get() throws ReflectiveOperationException;
   }
}
