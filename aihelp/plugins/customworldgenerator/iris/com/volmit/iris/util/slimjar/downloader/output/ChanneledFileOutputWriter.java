package com.volmit.iris.util.slimjar.downloader.output;

import com.volmit.iris.util.slimjar.exceptions.OutputWriterException;
import com.volmit.iris.util.slimjar.logging.LocationAwareProcessLogger;
import com.volmit.iris.util.slimjar.logging.ProcessLogger;
import java.io.File;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ChanneledFileOutputWriter implements OutputWriter {
   @NotNull
   private static final ProcessLogger LOGGER = LocationAwareProcessLogger.generic();
   @NotNull
   private final File outputFile;

   @Contract(
      pure = true
   )
   public ChanneledFileOutputWriter(@NotNull File var1) {
      this.outputFile = var1;
   }

   @Contract(
      mutates = "param1"
   )
   @NotNull
   public File writeFrom(@NotNull InputStream var1, long var2) {
      LOGGER.debug("Attempting to write from inputStream...");

      try {
         InputStream var4 = var1;

         File var5;
         label68: {
            try {
               if (this.outputFile.exists()) {
                  var5 = this.outputFile;
                  break label68;
               }

               LOGGER.debug("Writing %s bytes...", var2 == -1L ? "unknown" : var2);
               Files.copy(var1, this.outputFile.toPath(), new CopyOption[0]);
            } catch (Throwable var8) {
               if (var1 != null) {
                  try {
                     var4.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }
               }

               throw var8;
            }

            if (var1 != null) {
               var1.close();
            }

            return this.outputFile;
         }

         if (var1 != null) {
            var1.close();
         }

         return var5;
      } catch (Exception var9) {
         throw new OutputWriterException("Unable to copy from input stream to %s.".formatted(new Object[]{this.outputFile}), var9);
      }
   }
}
