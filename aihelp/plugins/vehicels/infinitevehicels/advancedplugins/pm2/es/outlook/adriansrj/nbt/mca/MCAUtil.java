package es.outlook.adriansrj.nbt.mca;

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

   public static MCAFile read(String var0) {
      return read(new File(var0), -1L);
   }

   public static MCAFile read(File var0) {
      return read(var0, -1L);
   }

   public static MCAFile read(String var0, long var1) {
      return read(new File(var0), var1);
   }

   public static MCAFile read(File var0, long var1) {
      MCAFile var3 = newMCAFile(var0);
      RandomAccessFile var4 = new RandomAccessFile(var0, "r");
      Throwable var5 = null;

      MCAFile var6;
      try {
         var3.deserialize(var4, var1);
         var6 = var3;
      } catch (Throwable var15) {
         var5 = var15;
         throw var15;
      } finally {
         if (var4 != null) {
            if (var5 != null) {
               try {
                  var4.close();
               } catch (Throwable var14) {
                  var5.addSuppressed(var14);
               }
            } else {
               var4.close();
            }
         }

      }

      return var6;
   }

   public static int write(MCAFile var0, String var1) {
      return write(var0, new File(var1), false);
   }

   public static int write(MCAFile var0, File var1) {
      return write(var0, var1, false);
   }

   public static int write(MCAFile var0, String var1, boolean var2) {
      return write(var0, new File(var1), var2);
   }

   public static int write(MCAFile var0, File var1, boolean var2) {
      File var3 = var1;
      if (var1.exists()) {
         var3 = File.createTempFile(var1.getName(), (String)null);
      }

      RandomAccessFile var5 = new RandomAccessFile(var3, "rw");
      Throwable var6 = null;

      int var4;
      try {
         var4 = var0.serialize(var5, var2);
      } catch (Throwable var15) {
         var6 = var15;
         throw var15;
      } finally {
         if (var5 != null) {
            if (var6 != null) {
               try {
                  var5.close();
               } catch (Throwable var14) {
                  var6.addSuppressed(var14);
               }
            } else {
               var5.close();
            }
         }

      }

      if (var4 > 0 && var3 != var1) {
         Files.move(var3.toPath(), var1.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }

      return var4;
   }

   public static String createNameFromChunkLocation(int var0, int var1) {
      return createNameFromRegionLocation(chunkToRegion(var0), chunkToRegion(var1));
   }

   public static String createNameFromBlockLocation(int var0, int var1) {
      return createNameFromRegionLocation(blockToRegion(var0), blockToRegion(var1));
   }

   public static String createNameFromRegionLocation(int var0, int var1) {
      return "r." + var0 + "." + var1 + ".mca";
   }

   public static int blockToChunk(int var0) {
      return var0 >> 4;
   }

   public static int blockToRegion(int var0) {
      return var0 >> 9;
   }

   public static int chunkToRegion(int var0) {
      return var0 >> 5;
   }

   public static int regionToChunk(int var0) {
      return var0 << 5;
   }

   public static int regionToBlock(int var0) {
      return var0 << 9;
   }

   public static int chunkToBlock(int var0) {
      return var0 << 4;
   }

   public static MCAFile newMCAFile(File var0) {
      Matcher var1 = mcaFilePattern.matcher(var0.getName());
      if (var1.find()) {
         return new MCAFile(Integer.parseInt(var1.group("regionX")), Integer.parseInt(var1.group("regionZ")));
      } else {
         throw new IllegalArgumentException("invalid mca file name: " + var0.getName());
      }
   }
}
