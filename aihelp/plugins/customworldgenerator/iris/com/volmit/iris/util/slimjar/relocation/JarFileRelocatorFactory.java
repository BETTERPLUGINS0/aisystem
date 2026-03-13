package com.volmit.iris.util.slimjar.relocation;

import com.volmit.iris.util.slimjar.relocation.facade.JarRelocatorFacadeFactory;
import java.util.Collection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class JarFileRelocatorFactory implements RelocatorFactory {
   @NotNull
   private final JarRelocatorFacadeFactory relocatorFacadeFactory;

   public JarFileRelocatorFactory(@NotNull JarRelocatorFacadeFactory var1) {
      this.relocatorFacadeFactory = var1;
   }

   @Contract("_ -> new")
   @NotNull
   public Relocator create(@NotNull Collection<RelocationRule> var1) {
      return new JarFileRelocator(var1, this.relocatorFacadeFactory);
   }
}
