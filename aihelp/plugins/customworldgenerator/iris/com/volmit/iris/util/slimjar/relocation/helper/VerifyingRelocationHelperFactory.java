package com.volmit.iris.util.slimjar.relocation.helper;

import com.volmit.iris.util.slimjar.downloader.strategy.FilePathStrategy;
import com.volmit.iris.util.slimjar.downloader.verify.FileChecksumCalculator;
import com.volmit.iris.util.slimjar.exceptions.RelocatorException;
import com.volmit.iris.util.slimjar.relocation.Relocator;
import com.volmit.iris.util.slimjar.relocation.meta.MetaMediatorFactory;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class VerifyingRelocationHelperFactory implements RelocationHelperFactory {
   @NotNull
   private static final URI JAR_URL;
   @NotNull
   private final FilePathStrategy relocationFilePathStrategy;
   @NotNull
   private final MetaMediatorFactory mediatorFactory;
   @NotNull
   private final String selfHash;

   public VerifyingRelocationHelperFactory(@NotNull String var1, @NotNull FilePathStrategy var2, @NotNull MetaMediatorFactory var3) {
      this.relocationFilePathStrategy = var2;
      this.mediatorFactory = var3;
      this.selfHash = var1;
   }

   public VerifyingRelocationHelperFactory(@NotNull FileChecksumCalculator var1, @NotNull FilePathStrategy var2, @NotNull MetaMediatorFactory var3) {
      this(var1.calculate(new File(JAR_URL)), var2, var3);
   }

   @Contract("_ -> new")
   @NotNull
   public RelocationHelper create(@NotNull Relocator var1) {
      return new VerifyingRelocationHelper(this.selfHash, this.relocationFilePathStrategy, var1, this.mediatorFactory);
   }

   static {
      try {
         JAR_URL = VerifyingRelocationHelperFactory.class.getProtectionDomain().getCodeSource().getLocation().toURI();
      } catch (URISyntaxException var1) {
         throw new RelocatorException("Failed to convert URL to URI.", var1);
      }
   }
}
