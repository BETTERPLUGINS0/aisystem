package com.volmit.iris.util.slimjar.relocation.meta;

import com.volmit.iris.util.slimjar.exceptions.RelocatorException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class FlatFileMetaMediatorFactory implements MetaMediatorFactory {
   @Contract("_ -> new")
   @NotNull
   public MetaMediator create(@NotNull Path var1) {
      Path var2 = var1.getParent().resolve(var1.getFileName().toString() + ".slimjar_meta");
      if (Files.exists(var2, new LinkOption[0])) {
         return new FlatFileMetaMediator(var2);
      } else {
         try {
            Files.createDirectories(var2);
         } catch (IOException var4) {
            throw new RelocatorException("Failed to create new file.", var4);
         }

         return new FlatFileMetaMediator(var2);
      }
   }
}
