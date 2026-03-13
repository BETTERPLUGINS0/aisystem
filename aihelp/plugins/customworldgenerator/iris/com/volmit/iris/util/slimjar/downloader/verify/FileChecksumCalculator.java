package com.volmit.iris.util.slimjar.downloader.verify;

import com.volmit.iris.util.slimjar.exceptions.VerificationException;
import com.volmit.iris.util.slimjar.logging.LocationAwareProcessLogger;
import com.volmit.iris.util.slimjar.logging.ProcessLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class FileChecksumCalculator implements ChecksumCalculator {
   @NotNull
   private static final ProcessLogger LOGGER = LocationAwareProcessLogger.generic();
   @NotNull
   private static final String DIRECTORY_HASH = "DIRECTORY";
   @NotNull
   private final ThreadLocal<MessageDigest> digestThreadLocal;

   @Contract(
      pure = true
   )
   public FileChecksumCalculator(@NotNull String var1) {
      MessageDigest var2;
      try {
         var2 = MessageDigest.getInstance(var1);
      } catch (NoSuchAlgorithmException var4) {
         throw new VerificationException("Failed to initialize checksum calculator", var4);
      }

      this.digestThreadLocal = ThreadLocal.withInitial(() -> {
         try {
            return (MessageDigest)var2.clone();
         } catch (CloneNotSupportedException var2x) {
            throw new VerificationException("Failed to clone digest template for thread local use.", var2x);
         }
      });
   }

   @Contract(
      pure = true
   )
   @NotNull
   public String calculate(@NotNull File var1) {
      LOGGER.debug("Calculating hash for %s", var1.getPath());
      if (var1.isDirectory()) {
         return "DIRECTORY";
      } else {
         MessageDigest var2 = (MessageDigest)this.digestThreadLocal.get();

         try {
            FileInputStream var3 = new FileInputStream(var1);

            try {
               byte[] var4 = new byte[1024];

               int var5;
               while((var5 = var3.read(var4)) != -1) {
                  var2.update(var4, 0, var5);
               }
            } catch (Throwable var10) {
               try {
                  var3.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }

               throw var10;
            }

            var3.close();
         } catch (IOException var11) {
            throw new VerificationException("Encountered error while reading checksum file %s.".formatted(new Object[]{var1.getPath()}), var11);
         }

         byte[] var12 = var2.digest();
         StringBuilder var13 = new StringBuilder();
         byte[] var14 = var12;
         int var6 = var12.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            byte var8 = var14[var7];
            var13.append(Integer.toString((var8 & 255) + 256, 16).substring(1));
         }

         var13.trimToSize();
         String var15 = var13.toString();
         LOGGER.debug("Hash for %s -> %s", var1.getPath(), var15);
         this.digestThreadLocal.remove();
         return var15;
      }
   }
}
