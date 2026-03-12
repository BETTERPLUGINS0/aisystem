package fr.xephi.authme.libs.com.icetar.tar;

import java.io.File;
import java.util.Date;

public class TarEntry implements Cloneable {
   protected File file;
   protected TarHeader header;
   protected boolean unixFormat;
   protected boolean ustarFormat;
   protected boolean gnuFormat;

   protected TarEntry() {
   }

   public TarEntry(String var1) {
      this.initialize();
      this.nameTarHeader(this.header, var1);
   }

   public TarEntry(File var1) throws InvalidHeaderException {
      this.initialize();
      this.getFileTarHeader(this.header, var1);
   }

   public TarEntry(byte[] var1) throws InvalidHeaderException {
      this.initialize();
      this.parseTarHeader(this.header, var1);
   }

   private void initialize() {
      this.file = null;
      this.header = new TarHeader();
      this.gnuFormat = false;
      this.ustarFormat = true;
      this.unixFormat = false;
   }

   public Object clone() {
      TarEntry var1 = null;

      try {
         var1 = (TarEntry)super.clone();
         if (this.header != null) {
            var1.header = (TarHeader)this.header.clone();
         }

         if (this.file != null) {
            var1.file = new File(this.file.getAbsolutePath());
         }
      } catch (CloneNotSupportedException var3) {
         var3.printStackTrace(System.err);
      }

      return var1;
   }

   public boolean isUSTarFormat() {
      return this.ustarFormat;
   }

   public void setUSTarFormat() {
      this.ustarFormat = true;
      this.gnuFormat = false;
      this.unixFormat = false;
   }

   public boolean isGNUTarFormat() {
      return this.gnuFormat;
   }

   public void setGNUTarFormat() {
      this.gnuFormat = true;
      this.ustarFormat = false;
      this.unixFormat = false;
   }

   public boolean isUnixTarFormat() {
      return this.unixFormat;
   }

   public void setUnixTarFormat() {
      this.unixFormat = true;
      this.ustarFormat = false;
      this.gnuFormat = false;
   }

   public boolean equals(TarEntry var1) {
      return this.header.name.toString().equals(var1.header.name.toString());
   }

   public boolean isDescendent(TarEntry var1) {
      return var1.header.name.toString().startsWith(this.header.name.toString());
   }

   public TarHeader getHeader() {
      return this.header;
   }

   public String getName() {
      return this.header.name.toString();
   }

   public void setName(String var1) {
      this.header.name = new StringBuffer(var1);
   }

   public int getUserId() {
      return this.header.userId;
   }

   public void setUserId(int var1) {
      this.header.userId = var1;
   }

   public int getGroupId() {
      return this.header.groupId;
   }

   public void setGroupId(int var1) {
      this.header.groupId = var1;
   }

   public String getUserName() {
      return this.header.userName.toString();
   }

   public void setUserName(String var1) {
      this.header.userName = new StringBuffer(var1);
   }

   public String getGroupName() {
      return this.header.groupName.toString();
   }

   public void setGroupName(String var1) {
      this.header.groupName = new StringBuffer(var1);
   }

   public void setIds(int var1, int var2) {
      this.setUserId(var1);
      this.setGroupId(var2);
   }

   public void setNames(String var1, String var2) {
      this.setUserName(var1);
      this.setGroupName(var2);
   }

   public void setModTime(long var1) {
      this.header.modTime = var1 / 1000L;
   }

   public void setModTime(Date var1) {
      this.header.modTime = var1.getTime() / 1000L;
   }

   public Date getModTime() {
      return new Date(this.header.modTime * 1000L);
   }

   public File getFile() {
      return this.file;
   }

   public long getSize() {
      return this.header.size;
   }

   public void setSize(long var1) {
      this.header.size = var1;
   }

   public boolean isDirectory() {
      if (this.file != null) {
         return this.file.isDirectory();
      } else {
         if (this.header != null) {
            if (this.header.linkFlag == 53) {
               return true;
            }

            if (this.header.name.toString().endsWith("/")) {
               return true;
            }
         }

         return false;
      }
   }

   public void getFileTarHeader(TarHeader var1, File var2) throws InvalidHeaderException {
      this.file = var2;
      String var3 = var2.getPath();
      String var4 = System.getProperty("os.name");
      if (var4 != null) {
         String var5 = "windows";
         if (var4.toLowerCase().startsWith(var5) && var3.length() > 2) {
            char var6 = var3.charAt(0);
            char var7 = var3.charAt(1);
            if (var7 == ':' && (var6 >= 'a' && var6 <= 'z' || var6 >= 'A' && var6 <= 'Z')) {
               var3 = var3.substring(2);
            }
         }
      }

      for(var3 = var3.replace(File.separatorChar, '/'); var3.startsWith("/"); var3 = var3.substring(1)) {
      }

      var1.linkName = new StringBuffer("");
      var1.name = new StringBuffer(var3);
      if (var2.isDirectory()) {
         var1.size = 0L;
         var1.mode = 16877;
         var1.linkFlag = 53;
         if (var1.name.charAt(var1.name.length() - 1) != '/') {
            var1.name.append("/");
         }
      } else {
         var1.size = var2.length();
         var1.mode = 33188;
         var1.linkFlag = 48;
      }

      var1.modTime = var2.lastModified() / 1000L;
      var1.checkSum = 0;
      var1.devMajor = 0;
      var1.devMinor = 0;
   }

   public TarEntry[] getDirectoryEntries() throws InvalidHeaderException {
      if (this.file != null && this.file.isDirectory()) {
         String[] var1 = this.file.list();
         TarEntry[] var2 = new TarEntry[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = new TarEntry(new File(this.file, var1[var3]));
         }

         return var2;
      } else {
         return new TarEntry[0];
      }
   }

   public long computeCheckSum(byte[] var1) {
      long var2 = 0L;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var2 += (long)(255 & var1[var4]);
      }

      return var2;
   }

   public void writeEntryHeader(byte[] var1) throws InvalidHeaderException {
      boolean var2 = false;
      if (this.isUnixTarFormat() && this.header.name.length() > 100) {
         throw new InvalidHeaderException("file path is greater than 100 characters, " + this.header.name);
      } else {
         int var9 = TarHeader.getFileNameBytes(this.header.name.toString(), var1);
         var9 = TarHeader.getOctalBytes((long)this.header.mode, var1, var9, 8);
         var9 = TarHeader.getOctalBytes((long)this.header.userId, var1, var9, 8);
         var9 = TarHeader.getOctalBytes((long)this.header.groupId, var1, var9, 8);
         long var3 = this.header.size;
         var9 = TarHeader.getLongOctalBytes(var3, var1, var9, 12);
         var9 = TarHeader.getLongOctalBytes(this.header.modTime, var1, var9, 12);
         int var5 = var9;

         for(int var6 = 0; var6 < 8; ++var6) {
            var1[var9++] = 32;
         }

         var1[var9++] = this.header.linkFlag;
         var9 = TarHeader.getNameBytes(this.header.linkName, var1, var9, 100);
         if (this.unixFormat) {
            for(int var7 = 0; var7 < 8; ++var7) {
               var1[var9++] = 0;
            }
         } else {
            var9 = TarHeader.getNameBytes(this.header.magic, var1, var9, 8);
         }

         var9 = TarHeader.getNameBytes(this.header.userName, var1, var9, 32);
         var9 = TarHeader.getNameBytes(this.header.groupName, var1, var9, 32);
         var9 = TarHeader.getOctalBytes((long)this.header.devMajor, var1, var9, 8);

         for(var9 = TarHeader.getOctalBytes((long)this.header.devMinor, var1, var9, 8); var9 < var1.length; var1[var9++] = 0) {
         }

         long var10 = this.computeCheckSum(var1);
         TarHeader.getCheckSumOctalBytes(var10, var1, var5, 8);
      }
   }

   public void parseTarHeader(TarHeader var1, byte[] var2) throws InvalidHeaderException {
      boolean var3 = false;
      if (var2[257] == 0 && var2[258] == 0 && var2[259] == 0 && var2[260] == 0 && var2[261] == 0) {
         this.unixFormat = true;
         this.ustarFormat = false;
         this.gnuFormat = false;
      } else if (var2[257] == 117 && var2[258] == 115 && var2[259] == 116 && var2[260] == 97 && var2[261] == 114 && var2[262] == 0) {
         this.ustarFormat = true;
         this.gnuFormat = false;
         this.unixFormat = false;
      } else {
         if (var2[257] != 117 || var2[258] != 115 || var2[259] != 116 || var2[260] != 97 || var2[261] != 114 || var2[262] == 0 || var2[263] == 0) {
            StringBuffer var4 = new StringBuffer(128);
            var4.append("header magic is not 'ustar' or unix-style zeros, it is '");
            var4.append(var2[257]);
            var4.append(var2[258]);
            var4.append(var2[259]);
            var4.append(var2[260]);
            var4.append(var2[261]);
            var4.append(var2[262]);
            var4.append(var2[263]);
            var4.append("', or (dec) ");
            var4.append(var2[257]);
            var4.append(", ");
            var4.append(var2[258]);
            var4.append(", ");
            var4.append(var2[259]);
            var4.append(", ");
            var4.append(var2[260]);
            var4.append(", ");
            var4.append(var2[261]);
            var4.append(", ");
            var4.append(var2[262]);
            var4.append(", ");
            var4.append(var2[263]);
            throw new InvalidHeaderException(var4.toString());
         }

         this.gnuFormat = true;
         this.unixFormat = false;
         this.ustarFormat = false;
      }

      var1.name = TarHeader.parseFileName(var2);
      byte var5 = 100;
      var1.mode = (int)TarHeader.parseOctal(var2, var5, 8);
      int var6 = var5 + 8;
      var1.userId = (int)TarHeader.parseOctal(var2, var6, 8);
      var6 += 8;
      var1.groupId = (int)TarHeader.parseOctal(var2, var6, 8);
      var6 += 8;
      var1.size = TarHeader.parseOctal(var2, var6, 12);
      var6 += 12;
      var1.modTime = TarHeader.parseOctal(var2, var6, 12);
      var6 += 12;
      var1.checkSum = (int)TarHeader.parseOctal(var2, var6, 8);
      var6 += 8;
      var1.linkFlag = var2[var6++];
      var1.linkName = TarHeader.parseName(var2, var6, 100);
      var6 += 100;
      if (this.ustarFormat) {
         var1.magic = TarHeader.parseName(var2, var6, 8);
         var6 += 8;
         var1.userName = TarHeader.parseName(var2, var6, 32);
         var6 += 32;
         var1.groupName = TarHeader.parseName(var2, var6, 32);
         var6 += 32;
         var1.devMajor = (int)TarHeader.parseOctal(var2, var6, 8);
         var6 += 8;
         var1.devMinor = (int)TarHeader.parseOctal(var2, var6, 8);
      } else {
         var1.devMajor = 0;
         var1.devMinor = 0;
         var1.magic = new StringBuffer("");
         var1.userName = new StringBuffer("");
         var1.groupName = new StringBuffer("");
      }

   }

   public void nameTarHeader(TarHeader var1, String var2) {
      boolean var3 = var2.endsWith("/");
      this.gnuFormat = false;
      this.ustarFormat = true;
      this.unixFormat = false;
      var1.checkSum = 0;
      var1.devMajor = 0;
      var1.devMinor = 0;
      var1.name = new StringBuffer(var2);
      var1.mode = var3 ? 16877 : '膤';
      var1.userId = 0;
      var1.groupId = 0;
      var1.size = 0L;
      var1.checkSum = 0;
      var1.modTime = (new Date()).getTime() / 1000L;
      var1.linkFlag = (byte)(var3 ? 53 : 48);
      var1.linkName = new StringBuffer("");
      var1.userName = new StringBuffer("");
      var1.groupName = new StringBuffer("");
      var1.devMajor = 0;
      var1.devMinor = 0;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(128);
      return var1.append("[TarEntry name=").append(this.getName()).append(", isDir=").append(this.isDirectory()).append(", size=").append(this.getSize()).append(", userId=").append(this.getUserId()).append(", user=").append(this.getUserName()).append(", groupId=").append(this.getGroupId()).append(", group=").append(this.getGroupName()).append("]").toString();
   }
}
