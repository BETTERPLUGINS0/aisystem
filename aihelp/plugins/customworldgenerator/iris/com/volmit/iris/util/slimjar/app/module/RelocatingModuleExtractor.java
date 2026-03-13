package com.volmit.iris.util.slimjar.app.module;

import com.volmit.iris.util.slimjar.downloader.verify.ChecksumCalculator;
import com.volmit.iris.util.slimjar.downloader.verify.FileChecksumCalculator;
import com.volmit.iris.util.slimjar.exceptions.ModuleExtractorException;
import com.volmit.iris.util.slimjar.relocation.Relocator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;

public final class RelocatingModuleExtractor implements ModuleExtractor {
   @NotNull
   private final Path dataDirectory;
   @NotNull
   private final Relocator relocator;
   @NotNull
   private final ChecksumCalculator calculator;

   public RelocatingModuleExtractor(@NotNull Path var1, @NotNull Relocator var2) {
      this.dataDirectory = var1;
      this.relocator = var2;
      this.calculator = new FileChecksumCalculator("sha256");
   }

   @NotNull
   public URL extractModule(@NotNull URL var1, @NotNull String var2) {
      File var3 = this.dataDirectory.resolve(var2 + ".jar").toFile();
      Path var4 = this.dataDirectory.resolve(var2 + ".checksum");
      File var5 = ModuleExtractor.extractFile(var1, var2);
      String var6 = this.calculator.calculate(var5);

      URL var8;
      try {
         String var7 = Files.exists(var4, new LinkOption[0]) ? (new String(Files.readAllBytes(var4))).trim() : null;
         if (!var6.equals(var7)) {
            var3.getParentFile().mkdirs();
            this.relocator.relocate(var5, var3);
            Files.write(var4, var6.getBytes(), new OpenOption[0]);
         }

         var8 = var3.toURI().toURL();
      } catch (IOException var12) {
         throw new ModuleExtractorException("Encountered IOException.", var12);
      } finally {
         var5.delete();
      }

      return var8;
   }
}
