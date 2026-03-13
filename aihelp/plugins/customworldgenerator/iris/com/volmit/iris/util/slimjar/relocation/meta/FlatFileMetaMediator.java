package com.volmit.iris.util.slimjar.relocation.meta;

import com.volmit.iris.util.slimjar.exceptions.RelocatorException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FlatFileMetaMediator implements MetaMediator {
   @NotNull
   private final Path metaFolderPath;

   public FlatFileMetaMediator(@NotNull Path var1) {
      this.metaFolderPath = var1;
   }

   @Nullable
   public String readAttribute(@NotNull String var1) {
      Path var2 = this.metaFolderPath.resolve(var1);
      if (!Files.notExists(var2, new LinkOption[0]) && !Files.isDirectory(var2, new LinkOption[0])) {
         try {
            return Files.readString(var2);
         } catch (IOException var4) {
            throw new RelocatorException("Failed to read attribute " + var1, var4);
         }
      } else {
         return null;
      }
   }

   public void writeAttribute(@NotNull String var1, @NotNull String var2) {
      Path var3 = this.metaFolderPath.resolve(var1);

      try {
         Files.deleteIfExists(var3);
         Files.createFile(var3);
         Files.write(var3, var2.getBytes(), new OpenOption[0]);
      } catch (IOException var5) {
         throw new RelocatorException("Failed to write attribute " + var1, var5);
      }
   }
}
