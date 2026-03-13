package com.volmit.iris.util.slimjar.relocation.helper;

import com.volmit.iris.util.slimjar.downloader.strategy.FilePathStrategy;
import com.volmit.iris.util.slimjar.relocation.Relocator;
import com.volmit.iris.util.slimjar.relocation.meta.MetaMediator;
import com.volmit.iris.util.slimjar.relocation.meta.MetaMediatorFactory;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import java.io.File;
import org.jetbrains.annotations.NotNull;

public final class VerifyingRelocationHelper implements RelocationHelper {
   @NotNull
   private final FilePathStrategy outputFilePathStrategy;
   @NotNull
   private final Relocator relocator;
   @NotNull
   private final String selfHash;
   @NotNull
   private final MetaMediatorFactory mediatorFactory;

   public VerifyingRelocationHelper(@NotNull String var1, @NotNull FilePathStrategy var2, @NotNull Relocator var3, @NotNull MetaMediatorFactory var4) {
      this.mediatorFactory = var4;
      this.outputFilePathStrategy = var2;
      this.relocator = var3;
      this.selfHash = var1;
   }

   @NotNull
   public File relocate(@NotNull Dependency var1, @NotNull File var2) {
      File var3 = this.outputFilePathStrategy.selectFileFor(var1);
      MetaMediator var4 = this.mediatorFactory.create(var3.toPath());
      if (var3.exists()) {
         String var5 = var4.readAttribute("slimjar.owner");
         if (var5 != null && this.selfHash.trim().equals(var5.trim())) {
            return var3;
         }
      }

      this.relocator.relocate(var2, var3);
      var4.writeAttribute("slimjar.owner", this.selfHash);
      return var3;
   }
}
