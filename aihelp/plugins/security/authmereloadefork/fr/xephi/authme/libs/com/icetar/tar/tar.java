package fr.xephi.authme.libs.com.icetar.tar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;

public class tar implements TarProgressDisplay {
   private boolean debug = false;
   private boolean verbose = false;
   private boolean compressed = false;
   private boolean listingArchive = false;
   private boolean writingArchive = true;
   private boolean unixArchiveFormat = false;
   private boolean keepOldFiles = false;
   private boolean asciiTranslate = false;
   private boolean mimeFileLoaded;
   private String archiveName = null;
   private int blockSize = 10240;
   private int userId;
   private String userName;
   private int groupId;
   private String groupName;
   // $FF: synthetic field
   static Class class$com$ice$tar$tar;

   public static void main(String[] var0) {
      tar var1 = new tar();
      var1.instanceMain(var0);
   }

   public tar() {
      String var1 = System.getProperty("user.name");
      this.userId = 0;
      this.userName = var1 == null ? "" : var1;
      this.groupId = 0;
      this.groupName = "";
   }

   public void instanceMain(String[] var1) {
      TarArchive var2 = null;
      int var3 = this.processArguments(var1);
      Object var4;
      if (this.writingArchive) {
         var4 = System.out;
         if (this.archiveName != null && !this.archiveName.equals("-")) {
            try {
               var4 = new FileOutputStream(this.archiveName);
            } catch (IOException var17) {
               var4 = null;
               var17.printStackTrace(System.err);
            }
         }

         if (var4 != null) {
            if (this.compressed) {
               try {
                  var4 = new GZIPOutputStream((OutputStream)var4);
               } catch (IOException var16) {
                  var4 = null;
                  var16.printStackTrace(System.err);
               }
            }

            var2 = new TarArchive((OutputStream)var4, this.blockSize);
         }
      } else {
         var4 = System.in;
         if (this.archiveName != null && !this.archiveName.equals("-")) {
            try {
               var4 = new FileInputStream(this.archiveName);
            } catch (IOException var15) {
               var4 = null;
               var15.printStackTrace(System.err);
            }
         }

         if (var4 != null) {
            if (this.compressed) {
               try {
                  var4 = new GZIPInputStream((InputStream)var4);
               } catch (IOException var14) {
                  var4 = null;
                  var14.printStackTrace(System.err);
               }
            }

            var2 = new TarArchive((InputStream)var4, this.blockSize);
         }
      }

      if (var2 != null) {
         var2.setDebug(this.debug);
         var2.setVerbose(this.verbose);
         var2.setTarProgressDisplay(this);
         var2.setKeepOldFiles(this.keepOldFiles);
         var2.setAsciiTranslation(this.asciiTranslate);
         var2.setUserInfo(this.userId, this.userName, this.groupId, this.groupName);
      }

      if (var2 == null) {
         System.err.println("no processing due to errors");
      } else if (this.writingArchive) {
         for(; var3 < var1.length; ++var3) {
            try {
               File var20 = new File(var1[var3]);
               TarEntry var18 = new TarEntry(var20);
               if (this.unixArchiveFormat) {
                  var18.setUnixTarFormat();
               } else {
                  var18.setUSTarFormat();
               }

               var2.writeEntry(var18, true);
            } catch (IOException var13) {
               var13.printStackTrace(System.err);
            }
         }
      } else if (this.listingArchive) {
         try {
            var2.listContents();
         } catch (InvalidHeaderException var11) {
            var11.printStackTrace(System.err);
         } catch (IOException var12) {
            var12.printStackTrace(System.err);
         }
      } else {
         String var19 = System.getProperty("user.dir", (String)null);
         File var5 = new File(var19);
         if (!var5.exists() && !var5.mkdirs()) {
            var5 = null;
            Throwable var6 = new Throwable("ERROR, mkdirs() on '" + var5.getPath() + "' returned false.");
            var6.printStackTrace(System.err);
         }

         if (var5 != null) {
            try {
               var2.extractContents(var5);
            } catch (InvalidHeaderException var9) {
               var9.printStackTrace(System.err);
            } catch (IOException var10) {
               var10.printStackTrace(System.err);
            }
         }
      }

      if (var2 != null) {
         try {
            var2.closeArchive();
         } catch (IOException var8) {
            var8.printStackTrace(System.err);
         }
      }

   }

   private int processArguments(String[] var1) {
      int var2 = 0;

      boolean var3;
      for(var3 = false; var2 < var1.length; ++var2) {
         String var4 = var1[var2];
         if (!var4.startsWith("-")) {
            break;
         }

         if (var4.startsWith("--")) {
            if (var4.equals("--usage")) {
               this.usage();
               System.exit(1);
            } else if (var4.equals("--version")) {
               this.version();
               System.exit(1);
            } else {
               String var5;
               if (var4.equals("--trans")) {
                  this.asciiTranslate = true;
                  var5 = "javax.activation.FileTypeMap";

                  try {
                     Class var6 = Class.forName(var5);
                     if (!this.mimeFileLoaded) {
                        URL var7 = (class$com$ice$tar$tar == null ? (class$com$ice$tar$tar = class$("fr.xephi.authme.libs.com.icetar.tar.tar")) : class$com$ice$tar$tar).getResource("/fr/xephi/authme/libs/com/icetar/tar/asciimime.txt");
                        URLConnection var8 = var7.openConnection();
                        InputStream var9 = var8.getInputStream();
                        FileTypeMap.setDefaultFileTypeMap(new MimetypesFileTypeMap(var9));
                     }
                  } catch (ClassNotFoundException var15) {
                     System.err.println("Could not load the class named '" + var5 + "'.\n" + "The Java Activation package must " + "be installed to use ascii translation.");
                     System.exit(1);
                  } catch (IOException var16) {
                     var16.printStackTrace(System.err);
                     System.exit(1);
                  }
               } else if (var4.equals("--mime")) {
                  this.mimeFileLoaded = true;
                  var5 = "javax.activation.FileTypeMap";
                  ++var2;
                  File var18 = new File(var1[var2]);

                  try {
                     Class var20 = Class.forName(var5);
                     FileTypeMap.setDefaultFileTypeMap(new MimetypesFileTypeMap(new FileInputStream(var18)));
                  } catch (ClassNotFoundException var13) {
                     System.err.println("Could not load the class named '" + var5 + "'.\n" + "The Java Activation package must " + "be installed to use ascii translation.");
                     System.exit(1);
                  } catch (FileNotFoundException var14) {
                     System.err.println("Could not open the mimetypes file '" + var18.getPath() + "', " + var14.getMessage());
                  }
               } else {
                  System.err.println("unknown option: " + var4);
                  this.usage();
                  System.exit(1);
               }
            }
         } else {
            for(int var17 = 1; var17 < var4.length(); ++var17) {
               char var19 = var4.charAt(var17);
               if (var19 == '?') {
                  this.usage();
                  System.exit(1);
               } else if (var19 == 'f') {
                  ++var2;
                  this.archiveName = var1[var2];
               } else if (var19 == 'z') {
                  this.compressed = true;
               } else if (var19 == 'c') {
                  var3 = true;
                  this.writingArchive = true;
                  this.listingArchive = false;
               } else if (var19 == 'x') {
                  var3 = true;
                  this.writingArchive = false;
                  this.listingArchive = false;
               } else if (var19 == 't') {
                  var3 = true;
                  this.writingArchive = false;
                  this.listingArchive = true;
               } else if (var19 == 'k') {
                  this.keepOldFiles = true;
               } else if (var19 == 'o') {
                  this.unixArchiveFormat = true;
               } else if (var19 == 'b') {
                  try {
                     ++var2;
                     int var21 = Integer.parseInt(var1[var2]);
                     this.blockSize = var21 * 512;
                  } catch (NumberFormatException var12) {
                     var12.printStackTrace(System.err);
                  }
               } else if (var19 == 'u') {
                  ++var2;
                  this.userName = var1[var2];
               } else {
                  String var22;
                  if (var19 == 'U') {
                     ++var2;
                     var22 = var1[var2];

                     try {
                        this.userId = Integer.parseInt(var22);
                     } catch (NumberFormatException var11) {
                        this.userId = 0;
                        var11.printStackTrace(System.err);
                     }
                  } else if (var19 == 'g') {
                     ++var2;
                     this.groupName = var1[var2];
                  } else if (var19 == 'G') {
                     ++var2;
                     var22 = var1[var2];

                     try {
                        this.groupId = Integer.parseInt(var22);
                     } catch (NumberFormatException var10) {
                        this.groupId = 0;
                        var10.printStackTrace(System.err);
                     }
                  } else if (var19 == 'v') {
                     this.verbose = true;
                  } else if (var19 == 'D') {
                     this.debug = true;
                  } else {
                     System.err.println("unknown option: " + var19);
                     this.usage();
                     System.exit(1);
                  }
               }
            }
         }
      }

      if (!var3) {
         System.err.println("you must specify an operation option (c, x, or t)");
         this.usage();
         System.exit(1);
      }

      return var2;
   }

   public void showTarProgressMessage(String var1) {
      System.out.println(var1);
   }

   private void version() {
      System.err.println("Release 2.4 - $Revision: 1.10 $ $Name:  $");
   }

   private void usage() {
      System.err.println("usage: com.ice.tar.tar has three basic modes:");
      System.err.println("  com.ice.tar -c [options] archive files...");
      System.err.println("    Create new archive containing files.");
      System.err.println("  com.ice.tar -t [options] archive");
      System.err.println("    List contents of tar archive");
      System.err.println("  com.ice.tar -x [options] archive");
      System.err.println("    Extract contents of tar archive.");
      System.err.println("");
      System.err.println("options:");
      System.err.println("   -f file, use 'file' as the tar archive");
      System.err.println("   -v, verbose mode");
      System.err.println("   -z, use GZIP compression");
      System.err.println("   -D, debug archive and buffer operation");
      System.err.println("   -b blks, set blocking size to (blks * 512) bytes");
      System.err.println("   -o, write a V7 format archive rather than ANSI");
      System.err.println("   -u name, set user name to 'name'");
      System.err.println("   -U id, set user id to 'id'");
      System.err.println("   -g name, set group name to 'name'");
      System.err.println("   -G id, set group id to 'id'");
      System.err.println("   -?, print usage information");
      System.err.println("   --trans, translate 'text/*' files");
      System.err.println("   --mime file, use this mime types file and translate");
      System.err.println("   --usage, print usage information");
      System.err.println("   --version, print version information");
      System.err.println("");
      System.err.println("The translation options will translate from local line");
      System.err.println("endings to UNIX line endings of '\\n' when writing tar");
      System.err.println("archives, and from UNIX line endings into local line endings");
      System.err.println("when extracting archives.");
      System.err.println("");
      System.err.println("Written by Tim Endres");
      System.err.println("");
      System.err.println("This software has been placed into the public domain.");
      System.err.println("");
      this.version();
      System.exit(1);
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
