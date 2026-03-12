package fr.xephi.authme.libs.com.icetar.tar;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.activation.FileTypeMap;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

public class TarArchive {
   protected boolean verbose;
   protected boolean debug;
   protected boolean keepOldFiles;
   protected boolean asciiTranslate;
   protected int userId;
   protected String userName;
   protected int groupId;
   protected String groupName;
   protected String rootPath;
   protected String tempPath;
   protected String pathPrefix;
   protected int recordSize;
   protected byte[] recordBuf;
   protected TarInputStream tarIn;
   protected TarOutputStream tarOut;
   protected TarTransFileTyper transTyper;
   protected TarProgressDisplay progressDisplay;

   public TarArchive(InputStream var1) {
      this((InputStream)var1, 10240);
   }

   public TarArchive(InputStream var1, int var2) {
      this((InputStream)var1, var2, 512);
   }

   public TarArchive(InputStream var1, int var2, int var3) {
      this.tarIn = new TarInputStream(var1, var2, var3);
      this.initialize(var3);
   }

   public TarArchive(OutputStream var1) {
      this((OutputStream)var1, 10240);
   }

   public TarArchive(OutputStream var1, int var2) {
      this((OutputStream)var1, var2, 512);
   }

   public TarArchive(OutputStream var1, int var2, int var3) {
      this.tarOut = new TarOutputStream(var1, var2, var3);
      this.initialize(var3);
   }

   private void initialize(int var1) {
      this.rootPath = null;
      this.pathPrefix = null;
      this.tempPath = System.getProperty("user.dir");
      this.userId = 0;
      this.userName = "";
      this.groupId = 0;
      this.groupName = "";
      this.debug = false;
      this.verbose = false;
      this.keepOldFiles = false;
      this.progressDisplay = null;
      this.recordBuf = new byte[this.getRecordSize()];
   }

   public void setDebug(boolean var1) {
      this.debug = var1;
      if (this.tarIn != null) {
         this.tarIn.setDebug(var1);
      } else if (this.tarOut != null) {
         this.tarOut.setDebug(var1);
      }

   }

   public boolean isVerbose() {
      return this.verbose;
   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public void setTarProgressDisplay(TarProgressDisplay var1) {
      this.progressDisplay = var1;
   }

   public void setKeepOldFiles(boolean var1) {
      this.keepOldFiles = var1;
   }

   public void setAsciiTranslation(boolean var1) {
      this.asciiTranslate = var1;
   }

   public void setTransFileTyper(TarTransFileTyper var1) {
      this.transTyper = var1;
   }

   public void setUserInfo(int var1, String var2, int var3, String var4) {
      this.userId = var1;
      this.userName = var2;
      this.groupId = var3;
      this.groupName = var4;
   }

   public int getUserId() {
      return this.userId;
   }

   public String getUserName() {
      return this.userName;
   }

   public int getGroupId() {
      return this.groupId;
   }

   public String getGroupName() {
      return this.groupName;
   }

   public String getTempDirectory() {
      return this.tempPath;
   }

   public void setTempDirectory(String var1) {
      this.tempPath = var1;
   }

   public int getRecordSize() {
      if (this.tarIn != null) {
         return this.tarIn.getRecordSize();
      } else {
         return this.tarOut != null ? this.tarOut.getRecordSize() : 512;
      }
   }

   private String getTempFilePath(File var1) {
      String var2 = this.tempPath + File.separator + var1.getName() + ".tmp";

      for(int var3 = 1; var3 < 5; ++var3) {
         File var4 = new File(var2);
         if (!var4.exists()) {
            break;
         }

         var2 = this.tempPath + File.separator + var1.getName() + "-" + var3 + ".tmp";
      }

      return var2;
   }

   public void closeArchive() throws IOException {
      if (this.tarIn != null) {
         this.tarIn.close();
      } else if (this.tarOut != null) {
         this.tarOut.close();
      }

   }

   public void listContents() throws IOException, InvalidHeaderException {
      while(true) {
         TarEntry var1 = this.tarIn.getNextEntry();
         if (var1 == null) {
            if (this.debug) {
               System.err.println("READ EOF RECORD");
            }

            return;
         }

         if (this.progressDisplay != null) {
            this.progressDisplay.showTarProgressMessage(var1.getName());
         }
      }
   }

   public void extractContents(File var1) throws IOException, InvalidHeaderException {
      while(true) {
         TarEntry var2 = this.tarIn.getNextEntry();
         if (var2 == null) {
            if (this.debug) {
               System.err.println("READ EOF RECORD");
            }

            return;
         }

         this.extractEntry(var1, var2);
      }
   }

   private void extractEntry(File var1, TarEntry var2) throws IOException {
      if (this.verbose && this.progressDisplay != null) {
         this.progressDisplay.showTarProgressMessage(var2.getName());
      }

      String var3 = var2.getName();
      var3 = var3.replace('/', File.separatorChar);
      File var4 = new File(var1, var3);
      if (var2.isDirectory()) {
         if (!var4.exists() && !var4.mkdirs()) {
            throw new IOException("error making directory path '" + var4.getPath() + "'");
         }
      } else {
         File var5 = new File(var4.getParent());
         if (!var5.exists() && !var5.mkdirs()) {
            throw new IOException("error making directory path '" + var5.getPath() + "'");
         }

         if (this.keepOldFiles && var4.exists()) {
            if (this.verbose && this.progressDisplay != null) {
               this.progressDisplay.showTarProgressMessage("not overwriting " + var2.getName());
            }
         } else {
            boolean var6 = false;
            FileOutputStream var7 = new FileOutputStream(var4);
            if (this.asciiTranslate) {
               MimeType var8 = null;
               String var9 = null;

               try {
                  var9 = FileTypeMap.getDefaultFileTypeMap().getContentType(var4);
                  var8 = new MimeType(var9);
                  if (var8.getPrimaryType().equalsIgnoreCase("text")) {
                     var6 = true;
                  } else if (this.transTyper != null && this.transTyper.isAsciiFile(var2.getName())) {
                     var6 = true;
                  }
               } catch (MimeTypeParseException var14) {
               }

               if (this.debug) {
                  System.err.println("EXTRACT TRANS? '" + var6 + "'  ContentType='" + var9 + "'  PrimaryType='" + var8.getPrimaryType() + "'");
               }
            }

            PrintWriter var15 = null;
            if (var6) {
               var15 = new PrintWriter(var7);
            }

            byte[] var16 = new byte['耀'];

            while(true) {
               while(true) {
                  int var10 = this.tarIn.read(var16);
                  if (var10 == -1) {
                     if (var6) {
                        var15.close();
                     } else {
                        var7.close();
                     }

                     return;
                  }

                  if (var6) {
                     int var11 = 0;

                     for(int var12 = 0; var12 < var10; ++var12) {
                        if (var16[var12] == 10) {
                           String var13 = new String(var16, var11, var12 - var11);
                           var15.println(var13);
                           var11 = var12 + 1;
                        }
                     }
                  } else {
                     var7.write(var16, 0, var10);
                  }
               }
            }
         }
      }

   }

   public void writeEntry(TarEntry var1, boolean var2) throws IOException {
      boolean var3 = false;
      boolean var4 = var1.isUnixTarFormat();
      File var5 = null;
      File var6 = var1.getFile();
      TarEntry var7 = (TarEntry)var1.clone();
      if (this.verbose && this.progressDisplay != null) {
         this.progressDisplay.showTarProgressMessage(var7.getName());
      }

      if (this.asciiTranslate && !var7.isDirectory()) {
         MimeType var8 = null;
         String var9 = null;

         try {
            var9 = FileTypeMap.getDefaultFileTypeMap().getContentType(var6);
            var8 = new MimeType(var9);
            if (var8.getPrimaryType().equalsIgnoreCase("text")) {
               var3 = true;
            } else if (this.transTyper != null && this.transTyper.isAsciiFile(var6)) {
               var3 = true;
            }
         } catch (MimeTypeParseException var14) {
         }

         if (this.debug) {
            System.err.println("CREATE TRANS? '" + var3 + "'  ContentType='" + var9 + "'  PrimaryType='" + var8.getPrimaryType() + "'");
         }

         if (var3) {
            String var10 = this.getTempFilePath(var6);
            var5 = new File(var10);
            BufferedReader var11 = new BufferedReader(new InputStreamReader(new FileInputStream(var6)));
            BufferedOutputStream var12 = new BufferedOutputStream(new FileOutputStream(var5));

            while(true) {
               String var13 = var11.readLine();
               if (var13 == null) {
                  var11.close();
                  var12.flush();
                  var12.close();
                  var7.setSize(var5.length());
                  var6 = var5;
                  break;
               }

               var12.write(var13.getBytes());
               var12.write(10);
            }
         }
      }

      String var15 = null;
      if (this.rootPath != null && var7.getName().startsWith(this.rootPath)) {
         var15 = var7.getName().substring(this.rootPath.length() + 1);
      }

      if (this.pathPrefix != null) {
         var15 = var15 == null ? this.pathPrefix + "/" + var7.getName() : this.pathPrefix + "/" + var15;
      }

      if (var15 != null) {
         var7.setName(var15);
      }

      this.tarOut.putNextEntry(var7);
      if (var7.isDirectory()) {
         if (var2) {
            TarEntry[] var16 = var7.getDirectoryEntries();

            for(int var18 = 0; var18 < var16.length; ++var18) {
               TarEntry var20 = var16[var18];
               if (var4) {
                  var20.setUnixTarFormat();
               }

               this.writeEntry(var20, var2);
            }
         }
      } else {
         FileInputStream var17 = new FileInputStream(var6);
         byte[] var19 = new byte['耀'];

         while(true) {
            int var21 = var17.read(var19, 0, var19.length);
            if (var21 == -1) {
               var17.close();
               if (var5 != null) {
                  var5.delete();
               }

               this.tarOut.closeEntry();
               break;
            }

            this.tarOut.write(var19, 0, var21);
         }
      }

   }
}
