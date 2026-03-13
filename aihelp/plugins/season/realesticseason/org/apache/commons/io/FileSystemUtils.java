package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/** @deprecated */
@Deprecated
public class FileSystemUtils {
   private static final FileSystemUtils INSTANCE = new FileSystemUtils();
   private static final int INIT_PROBLEM = -1;
   private static final int OTHER = 0;
   private static final int WINDOWS = 1;
   private static final int UNIX = 2;
   private static final int POSIX_UNIX = 3;
   private static final int OS;
   private static final String DF;

   /** @deprecated */
   @Deprecated
   public static long freeSpace(String var0) {
      return INSTANCE.freeSpaceOS(var0, OS, false, Duration.ofMillis(-1L));
   }

   /** @deprecated */
   @Deprecated
   public static long freeSpaceKb(String var0) {
      return freeSpaceKb(var0, -1L);
   }

   /** @deprecated */
   @Deprecated
   public static long freeSpaceKb(String var0, long var1) {
      return INSTANCE.freeSpaceOS(var0, OS, true, Duration.ofMillis(var1));
   }

   /** @deprecated */
   @Deprecated
   public static long freeSpaceKb() {
      return freeSpaceKb(-1L);
   }

   /** @deprecated */
   @Deprecated
   public static long freeSpaceKb(long var0) {
      return freeSpaceKb((new File(".")).getAbsolutePath(), var0);
   }

   long freeSpaceOS(String var1, int var2, boolean var3, Duration var4) {
      if (var1 == null) {
         throw new IllegalArgumentException("Path must not be null");
      } else {
         switch(var2) {
         case 0:
            throw new IllegalStateException("Unsupported operating system");
         case 1:
            return var3 ? this.freeSpaceWindows(var1, var4) / 1024L : this.freeSpaceWindows(var1, var4);
         case 2:
            return this.freeSpaceUnix(var1, var3, false, var4);
         case 3:
            return this.freeSpaceUnix(var1, var3, true, var4);
         default:
            throw new IllegalStateException("Exception caught when determining operating system");
         }
      }
   }

   long freeSpaceWindows(String var1, Duration var2) {
      String var3 = FilenameUtils.normalize(var1, false);
      if (var3 == null) {
         throw new IllegalArgumentException(var1);
      } else {
         if (!var3.isEmpty() && var3.charAt(0) != '"') {
            var3 = "\"" + var3 + "\"";
         }

         String[] var4 = new String[]{"cmd.exe", "/C", "dir /a /-c " + var3};
         List var5 = this.performCommand(var4, Integer.MAX_VALUE, var2);

         for(int var6 = var5.size() - 1; var6 >= 0; --var6) {
            String var7 = (String)var5.get(var6);
            if (!var7.isEmpty()) {
               return this.parseDir(var7, var3);
            }
         }

         throw new IOException("Command line 'dir /-c' did not return any info for path '" + var3 + "'");
      }
   }

   long parseDir(String var1, String var2) {
      int var3 = 0;
      int var4 = 0;

      int var5;
      char var6;
      for(var5 = var1.length() - 1; var5 >= 0; --var5) {
         var6 = var1.charAt(var5);
         if (Character.isDigit(var6)) {
            var4 = var5 + 1;
            break;
         }
      }

      while(var5 >= 0) {
         var6 = var1.charAt(var5);
         if (!Character.isDigit(var6) && var6 != ',' && var6 != '.') {
            var3 = var5 + 1;
            break;
         }

         --var5;
      }

      if (var5 < 0) {
         throw new IOException("Command line 'dir /-c' did not return valid info for path '" + var2 + "'");
      } else {
         StringBuilder var8 = new StringBuilder(var1.substring(var3, var4));

         for(int var7 = 0; var7 < var8.length(); ++var7) {
            if (var8.charAt(var7) == ',' || var8.charAt(var7) == '.') {
               var8.deleteCharAt(var7--);
            }
         }

         return this.parseBytes(var8.toString(), var2);
      }
   }

   long freeSpaceUnix(String var1, boolean var2, boolean var3, Duration var4) {
      if (var1.isEmpty()) {
         throw new IllegalArgumentException("Path must not be empty");
      } else {
         String var5 = "-";
         if (var2) {
            var5 = var5 + "k";
         }

         if (var3) {
            var5 = var5 + "P";
         }

         String[] var6 = var5.length() > 1 ? new String[]{DF, var5, var1} : new String[]{DF, var1};
         List var7 = this.performCommand(var6, 3, var4);
         if (var7.size() < 2) {
            throw new IOException("Command line '" + DF + "' did not return info as expected for path '" + var1 + "'- response was " + var7);
         } else {
            String var8 = (String)var7.get(1);
            StringTokenizer var9 = new StringTokenizer(var8, " ");
            String var10;
            if (var9.countTokens() < 4) {
               if (var9.countTokens() != 1 || var7.size() < 3) {
                  throw new IOException("Command line '" + DF + "' did not return data as expected for path '" + var1 + "'- check path is valid");
               }

               var10 = (String)var7.get(2);
               var9 = new StringTokenizer(var10, " ");
            } else {
               var9.nextToken();
            }

            var9.nextToken();
            var9.nextToken();
            var10 = var9.nextToken();
            return this.parseBytes(var10, var1);
         }
      }
   }

   long parseBytes(String var1, String var2) {
      try {
         long var3 = Long.parseLong(var1);
         if (var3 < 0L) {
            throw new IOException("Command line '" + DF + "' did not find free space in response for path '" + var2 + "'- check path is valid");
         } else {
            return var3;
         }
      } catch (NumberFormatException var5) {
         throw new IOException("Command line '" + DF + "' did not return numeric data as expected for path '" + var2 + "'- check path is valid", var5);
      }
   }

   List<String> performCommand(String[] var1, int var2, Duration var3) {
      ArrayList var4 = new ArrayList(20);
      Process var5 = null;
      InputStream var6 = null;
      OutputStream var7 = null;
      InputStream var8 = null;
      BufferedReader var9 = null;

      ArrayList var12;
      try {
         Thread var10 = ThreadMonitor.start(var3);
         var5 = this.openProcess(var1);
         var6 = var5.getInputStream();
         var7 = var5.getOutputStream();
         var8 = var5.getErrorStream();
         var9 = new BufferedReader(new InputStreamReader(var6, Charset.defaultCharset()));

         for(String var11 = var9.readLine(); var11 != null && var4.size() < var2; var11 = var9.readLine()) {
            var11 = var11.toLowerCase(Locale.ENGLISH).trim();
            var4.add(var11);
         }

         var5.waitFor();
         ThreadMonitor.stop(var10);
         if (var5.exitValue() != 0) {
            throw new IOException("Command line returned OS error code '" + var5.exitValue() + "' for command " + Arrays.asList(var1));
         }

         if (var4.isEmpty()) {
            throw new IOException("Command line did not return any info for command " + Arrays.asList(var1));
         }

         var9.close();
         var9 = null;
         var6.close();
         var6 = null;
         if (var7 != null) {
            var7.close();
            var7 = null;
         }

         if (var8 != null) {
            var8.close();
            var8 = null;
         }

         var12 = var4;
      } catch (InterruptedException var16) {
         throw new IOException("Command line threw an InterruptedException for command " + Arrays.asList(var1) + " timeout=" + var3, var16);
      } finally {
         IOUtils.closeQuietly(var6);
         IOUtils.closeQuietly(var7);
         IOUtils.closeQuietly(var8);
         IOUtils.closeQuietly((Reader)var9);
         if (var5 != null) {
            var5.destroy();
         }

      }

      return var12;
   }

   Process openProcess(String[] var1) {
      return Runtime.getRuntime().exec(var1);
   }

   static {
      byte var0 = 0;
      String var1 = "df";

      try {
         String var2 = System.getProperty("os.name");
         if (var2 == null) {
            throw new IOException("os.name not found");
         }

         var2 = var2.toLowerCase(Locale.ENGLISH);
         if (var2.contains("windows")) {
            var0 = 1;
         } else if (!var2.contains("linux") && !var2.contains("mpe/ix") && !var2.contains("freebsd") && !var2.contains("openbsd") && !var2.contains("irix") && !var2.contains("digital unix") && !var2.contains("unix") && !var2.contains("mac os x")) {
            if (!var2.contains("sun os") && !var2.contains("sunos") && !var2.contains("solaris")) {
               if (var2.contains("hp-ux") || var2.contains("aix")) {
                  var0 = 3;
               }
            } else {
               var0 = 3;
               var1 = "/usr/xpg4/bin/df";
            }
         } else {
            var0 = 2;
         }
      } catch (Exception var3) {
         var0 = -1;
      }

      OS = var0;
      DF = var1;
   }
}
