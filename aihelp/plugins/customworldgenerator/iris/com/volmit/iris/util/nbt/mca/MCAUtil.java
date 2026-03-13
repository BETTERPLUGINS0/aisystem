package com.volmit.iris.util.nbt.mca;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.Position2;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MCAUtil {
   private static final Pattern mcaFilePattern = Pattern.compile("^.*r\\.(?<regionX>-?\\d+)\\.(?<regionZ>-?\\d+)\\.mca$");

   private MCAUtil() {
   }

   public static MCAFile read(String file) {
      return read(new File(var0), -1L);
   }

   public static MCAFile read(File file) {
      return read(var0, -1L);
   }

   public static MCAFile read(String file, long loadFlags) {
      return read(new File(var0), var1);
   }

   public static MCAFile read(File file, long loadFlags) {
      MCAFile var3 = newMCAFile(var0);
      RandomAccessFile var4 = new RandomAccessFile(var0, "r");

      MCAFile var5;
      try {
         var3.deserialize(var4, var1);
         var5 = var3;
      } catch (Throwable var8) {
         try {
            var4.close();
         } catch (Throwable var7) {
            var8.addSuppressed(var7);
         }

         throw var8;
      }

      var4.close();
      return var5;
   }

   public static KList<Position2> sampleChunkPositions(File file) {
      MCAFile var1 = newMCAFile(var0);
      RandomAccessFile var2 = new RandomAccessFile(var0, "r");

      KList var3;
      try {
         var3 = var1.samplePositions(var2);
      } catch (Throwable var6) {
         try {
            var2.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      var2.close();
      return var3;
   }

   public static int write(MCAFile mcaFile, String file) {
      return write(var0, new File(var1), false);
   }

   public static int write(MCAFile mcaFile, File file) {
      return write(var0, var1, false);
   }

   public static int write(MCAFile mcaFile, String file, boolean changeLastUpdate) {
      return write(var0, new File(var1), var2);
   }

   public static int write(MCAFile mcaFile, File file, boolean changeLastUpdate) {
      if (var0 == null) {
         return 0;
      } else {
         File var3 = var1;
         if (var1.exists()) {
            var3 = File.createTempFile(var1.getName(), (String)null);
         }

         RandomAccessFile var5 = new RandomAccessFile(var3, "rw");

         int var4;
         try {
            var4 = var0.serialize(var5, var2);
         } catch (Throwable var9) {
            try {
               var5.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         var5.close();
         if (var4 > 0 && var3 != var1) {
            Files.move(var3.toPath(), var1.toPath(), StandardCopyOption.REPLACE_EXISTING);
         }

         return var4;
      }
   }

   public static String createNameFromChunkLocation(int chunkX, int chunkZ) {
      return createNameFromRegionLocation(chunkToRegion(var0), chunkToRegion(var1));
   }

   public static String createNameFromBlockLocation(int blockX, int blockZ) {
      return createNameFromRegionLocation(blockToRegion(var0), blockToRegion(var1));
   }

   public static String createNameFromRegionLocation(int regionX, int regionZ) {
      return "r." + var0 + "." + var1 + ".mca";
   }

   public static int blockToChunk(int block) {
      return var0 >> 4;
   }

   public static int blockToRegion(int block) {
      return var0 >> 9;
   }

   public static int chunkToRegion(int chunk) {
      return var0 >> 5;
   }

   public static int regionToChunk(int region) {
      return var0 << 5;
   }

   public static int regionToBlock(int region) {
      return var0 << 9;
   }

   public static int chunkToBlock(int chunk) {
      return var0 << 4;
   }

   public static MCAFile newMCAFile(File file) {
      Matcher var1 = mcaFilePattern.matcher(var0.getName());
      if (var1.find()) {
         return new MCAFile(Integer.parseInt(var1.group("regionX")), Integer.parseInt(var1.group("regionZ")));
      } else {
         throw new IllegalArgumentException("invalid mca file name: " + var0.getName());
      }
   }
}
