package com.volmit.iris.util.mantle.io;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.io.CountingDataInputStream;
import com.volmit.iris.util.mantle.TectonicPlate;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Set;
import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;

public class IOWorker {
   private static final Set<OpenOption> OPTIONS;
   private static final int MAX_CACHE_SIZE = 128;
   private final Path root;
   private final File tmp;
   private final int worldHeight;
   private final Object2ObjectLinkedOpenHashMap<String, Holder> cache = new Object2ObjectLinkedOpenHashMap();

   public IOWorker(File root, int worldHeight) {
      this.root = var1.toPath();
      this.tmp = new File(var1, ".tmp");
      this.worldHeight = var2;
   }

   public TectonicPlate read(final String name) {
      PrecisionStopwatch var2 = PrecisionStopwatch.start();
      SynchronizedChannel var3 = this.getChannel(var1);

      TectonicPlate var8;
      try {
         InputStream var4 = var3.read();
         LZ4BlockInputStream var5 = new LZ4BlockInputStream(var4);
         BufferedInputStream var6 = new BufferedInputStream(var5);
         boolean var17 = false;

         String var10000;
         try {
            var17 = true;
            CountingDataInputStream var7 = CountingDataInputStream.wrap(var6);

            try {
               var8 = new TectonicPlate(this.worldHeight, var7, var1.startsWith("pv."));
            } catch (Throwable var20) {
               if (var7 != null) {
                  try {
                     var7.close();
                  } catch (Throwable var19) {
                     var20.addSuppressed(var19);
                  }
               }

               throw var20;
            }

            if (var7 != null) {
               var7.close();
               var17 = false;
            } else {
               var17 = false;
            }
         } finally {
            if (var17) {
               if (TectonicPlate.hasError() && IrisSettings.get().getGeneral().isDumpMantleOnError()) {
                  File var11 = Iris.instance.getDataFolder(new String[]{"dump", var1 + ".bin"});
                  Files.copy(new LZ4BlockInputStream(var3.read()), var11.toPath(), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
               } else {
                  var10000 = String.valueOf(C.DARK_GREEN);
                  Iris.debug("Read Tectonic Plate " + var10000 + var1 + String.valueOf(C.RED) + " in " + Form.duration(var2.getMilliseconds(), 2));
               }

            }
         }

         if (TectonicPlate.hasError() && IrisSettings.get().getGeneral().isDumpMantleOnError()) {
            File var9 = Iris.instance.getDataFolder(new String[]{"dump", var1 + ".bin"});
            Files.copy(new LZ4BlockInputStream(var3.read()), var9.toPath(), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
         } else {
            var10000 = String.valueOf(C.DARK_GREEN);
            Iris.debug("Read Tectonic Plate " + var10000 + var1 + String.valueOf(C.RED) + " in " + Form.duration(var2.getMilliseconds(), 2));
         }
      } catch (Throwable var22) {
         if (var3 != null) {
            try {
               var3.close();
            } catch (Throwable var18) {
               var22.addSuppressed(var18);
            }
         }

         throw var22;
      }

      if (var3 != null) {
         var3.close();
      }

      return var8;
   }

   public void write(final String name, final TectonicPlate plate) {
      PrecisionStopwatch var3 = PrecisionStopwatch.start();
      SynchronizedChannel var4 = this.getChannel(var1);

      try {
         this.tmp.mkdirs();
         File var5 = File.createTempFile("iris", ".bin", this.tmp);

         try {
            DataOutputStream var6 = new DataOutputStream(new LZ4BlockOutputStream(new FileOutputStream(var5)));

            try {
               var2.write(var6);
            } catch (Throwable var20) {
               try {
                  var6.close();
               } catch (Throwable var19) {
                  var20.addSuppressed(var19);
               }

               throw var20;
            }

            var6.close();
            OutputStream var24 = var4.write();

            try {
               Files.copy(var5.toPath(), var24);
               var24.flush();
            } catch (Throwable var21) {
               if (var24 != null) {
                  try {
                     var24.close();
                  } catch (Throwable var18) {
                     var21.addSuppressed(var18);
                  }
               }

               throw var21;
            }

            if (var24 != null) {
               var24.close();
            }
         } finally {
            var5.delete();
         }
      } catch (Throwable var23) {
         if (var4 != null) {
            try {
               var4.close();
            } catch (Throwable var17) {
               var23.addSuppressed(var17);
            }
         }

         throw var23;
      }

      if (var4 != null) {
         var4.close();
      }

      String var10000 = String.valueOf(C.DARK_GREEN);
      Iris.debug("Saved Tectonic Plate " + var10000 + var1 + String.valueOf(C.RED) + " in " + Form.duration(var3.getMilliseconds(), 2));
   }

   public void close() {
      synchronized(this.cache) {
         ObjectIterator var2 = this.cache.values().iterator();

         while(var2.hasNext()) {
            Holder var3 = (Holder)var2.next();
            var3.close();
         }

         this.cache.clear();
      }
   }

   private SynchronizedChannel getChannel(final String name) {
      PrecisionStopwatch var2 = PrecisionStopwatch.start();

      try {
         synchronized(this.cache) {
            Holder var4 = (Holder)this.cache.getAndMoveToFirst(var1);
            SynchronizedChannel var5;
            if (var4 != null) {
               var5 = var4.acquire();
               if (var5 != null) {
                  SynchronizedChannel var6 = var5;
                  return var6;
               }
            }

            if (this.cache.size() >= 128) {
               Holder var13 = (Holder)this.cache.removeLast();
               var13.close();
            }

            var4 = new Holder(FileChannel.open(this.root.resolve(var1), OPTIONS));
            this.cache.putAndMoveToFirst(var1, var4);
            var5 = (SynchronizedChannel)Objects.requireNonNull(var4.acquire());
            return var5;
         }
      } finally {
         String var10000 = String.valueOf(C.DARK_GREEN);
         Iris.debug("Acquired Channel for " + var10000 + var1 + String.valueOf(C.RED) + " in " + Form.duration(var2.getMilliseconds(), 2));
      }
   }

   static {
      OPTIONS = Set.of(StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.SYNC);
   }
}
