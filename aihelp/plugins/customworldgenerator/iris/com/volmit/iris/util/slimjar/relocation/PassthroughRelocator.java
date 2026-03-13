package com.volmit.iris.util.slimjar.relocation;

import com.volmit.iris.util.slimjar.exceptions.RelocatorException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.jetbrains.annotations.NotNull;

public final class PassthroughRelocator implements Relocator {
   public void relocate(@NotNull File var1, @NotNull File var2) {
      if (!var1.equals(var2)) {
         if (!var2.exists()) {
            try {
               Files.copy(var1.toPath(), var2.toPath());
            } catch (IOException var4) {
               throw new RelocatorException("Failed to copy to %s".formatted(new Object[]{var2}), var4);
            }
         }
      }
   }
}
