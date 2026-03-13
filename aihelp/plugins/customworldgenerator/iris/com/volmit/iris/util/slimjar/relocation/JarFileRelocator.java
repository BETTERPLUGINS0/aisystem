package com.volmit.iris.util.slimjar.relocation;

import com.volmit.iris.util.slimjar.exceptions.RelocatorException;
import com.volmit.iris.util.slimjar.relocation.facade.JarRelocatorFacadeFactory;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public final class JarFileRelocator implements Relocator {
   @NotNull
   private final Collection<RelocationRule> relocations;
   @NotNull
   private final JarRelocatorFacadeFactory relocatorFacadeFactory;

   public JarFileRelocator(@NotNull Collection<RelocationRule> var1, @NotNull JarRelocatorFacadeFactory var2) {
      this.relocations = var1;
      this.relocatorFacadeFactory = var2;
   }

   public void relocate(@NotNull File var1, @NotNull File var2) {
      var2.getParentFile().mkdirs();

      try {
         var2.createNewFile();
         this.relocatorFacadeFactory.createFacade(var1, var2, this.relocations).run();
      } catch (IOException var4) {
         throw new RelocatorException("Unable to create new file during relocation.", var4);
      }
   }
}
