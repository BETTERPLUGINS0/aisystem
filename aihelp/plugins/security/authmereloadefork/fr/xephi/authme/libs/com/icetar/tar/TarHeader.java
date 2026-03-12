package fr.xephi.authme.libs.com.icetar.tar;

public class TarHeader implements Cloneable {
   public static final int NAMELEN = 100;
   public static final int NAMEOFFSET = 0;
   public static final int PREFIXLEN = 155;
   public static final int PREFIXOFFSET = 345;
   public static final int MODELEN = 8;
   public static final int UIDLEN = 8;
   public static final int GIDLEN = 8;
   public static final int CHKSUMLEN = 8;
   public static final int SIZELEN = 12;
   public static final int MAGICLEN = 8;
   public static final int MODTIMELEN = 12;
   public static final int UNAMELEN = 32;
   public static final int GNAMELEN = 32;
   public static final int DEVLEN = 8;
   public static final byte LF_OLDNORM = 0;
   public static final byte LF_NORMAL = 48;
   public static final byte LF_LINK = 49;
   public static final byte LF_SYMLINK = 50;
   public static final byte LF_CHR = 51;
   public static final byte LF_BLK = 52;
   public static final byte LF_DIR = 53;
   public static final byte LF_FIFO = 54;
   public static final byte LF_CONTIG = 55;
   public static final String TMAGIC = "ustar";
   public static final String GNU_TMAGIC = "ustar  ";
   public StringBuffer name = new StringBuffer();
   public int mode;
   public int userId;
   public int groupId;
   public long size;
   public long modTime;
   public int checkSum;
   public byte linkFlag;
   public StringBuffer linkName = new StringBuffer();
   public StringBuffer magic = new StringBuffer("ustar");
   public StringBuffer userName;
   public StringBuffer groupName;
   public int devMajor;
   public int devMinor;

   public TarHeader() {
      String var1 = System.getProperty("user.name", "");
      if (var1.length() > 31) {
         var1 = var1.substring(0, 31);
      }

      this.userId = 0;
      this.groupId = 0;
      this.userName = new StringBuffer(var1);
      this.groupName = new StringBuffer("");
   }

   public Object clone() {
      TarHeader var1 = null;

      try {
         var1 = (TarHeader)super.clone();
         var1.name = this.name == null ? null : new StringBuffer(this.name.toString());
         var1.mode = this.mode;
         var1.userId = this.userId;
         var1.groupId = this.groupId;
         var1.size = this.size;
         var1.modTime = this.modTime;
         var1.checkSum = this.checkSum;
         var1.linkFlag = this.linkFlag;
         var1.linkName = this.linkName == null ? null : new StringBuffer(this.linkName.toString());
         var1.magic = this.magic == null ? null : new StringBuffer(this.magic.toString());
         var1.userName = this.userName == null ? null : new StringBuffer(this.userName.toString());
         var1.groupName = this.groupName == null ? null : new StringBuffer(this.groupName.toString());
         var1.devMajor = this.devMajor;
         var1.devMinor = this.devMinor;
      } catch (CloneNotSupportedException var3) {
         var3.printStackTrace(System.err);
      }

      return var1;
   }

   public String getName() {
      return this.name.toString();
   }

   public static long parseOctal(byte[] var0, int var1, int var2) throws InvalidHeaderException {
      long var3 = 0L;
      boolean var5 = true;
      int var6 = var1 + var2;

      for(int var7 = var1; var7 < var6 && var0[var7] != 0; ++var7) {
         if (var0[var7] == 32 || var0[var7] == 48) {
            if (var5) {
               continue;
            }

            if (var0[var7] == 32) {
               break;
            }
         }

         var5 = false;
         var3 = (var3 << 3) + (long)(var0[var7] - 48);
      }

      return var3;
   }

   public static StringBuffer parseFileName(byte[] var0) {
      StringBuffer var1 = new StringBuffer(256);
      int var2;
      if (var0[345] != 0) {
         for(var2 = 345; var2 < 500 && var0[var2] != 0; ++var2) {
            var1.append((char)var0[var2]);
         }

         var1.append("/");
      }

      for(var2 = 0; var2 < 100 && var0[var2] != 0; ++var2) {
         var1.append((char)var0[var2]);
      }

      return var1;
   }

   public static StringBuffer parseName(byte[] var0, int var1, int var2) throws InvalidHeaderException {
      StringBuffer var3 = new StringBuffer(var2);
      int var4 = var1 + var2;

      for(int var5 = var1; var5 < var4 && var0[var5] != 0; ++var5) {
         var3.append((char)var0[var5]);
      }

      return var3;
   }

   public static int getFileNameBytes(String var0, byte[] var1) throws InvalidHeaderException {
      if (var0.length() > 100) {
         int var2 = var0.indexOf("/", var0.length() - 100);
         if (var2 == -1) {
            throw new InvalidHeaderException("file name is greater than 100 characters, " + var0);
         }

         String var3 = var0.substring(var2 + 1);
         String var4 = var0.substring(0, var2);
         if (var4.length() > 155) {
            throw new InvalidHeaderException("file prefix is greater than 155 characters");
         }

         getNameBytes(new StringBuffer(var3), var1, 0, 100);
         getNameBytes(new StringBuffer(var4), var1, 345, 155);
      } else {
         getNameBytes(new StringBuffer(var0), var1, 0, 100);
      }

      return 100;
   }

   public static int getNameBytes(StringBuffer var0, byte[] var1, int var2, int var3) {
      int var4;
      for(var4 = 0; var4 < var3 && var4 < var0.length(); ++var4) {
         var1[var2 + var4] = (byte)var0.charAt(var4);
      }

      while(var4 < var3) {
         var1[var2 + var4] = 0;
         ++var4;
      }

      return var2 + var3;
   }

   public static int getOctalBytes(long var0, byte[] var2, int var3, int var4) {
      byte[] var5 = new byte[var4];
      int var6 = var4 - 1;
      var2[var3 + var6] = 0;
      --var6;
      var2[var3 + var6] = 32;
      --var6;
      if (var0 == 0L) {
         var2[var3 + var6] = 48;
         --var6;
      } else {
         for(long var7 = var0; var6 >= 0 && var7 > 0L; --var6) {
            var2[var3 + var6] = (byte)(48 + (byte)((int)(var7 & 7L)));
            var7 >>= 3;
         }
      }

      while(var6 >= 0) {
         var2[var3 + var6] = 32;
         --var6;
      }

      return var3 + var4;
   }

   public static int getLongOctalBytes(long var0, byte[] var2, int var3, int var4) {
      byte[] var5 = new byte[var4 + 1];
      getOctalBytes(var0, var5, 0, var4 + 1);
      System.arraycopy(var5, 0, var2, var3, var4);
      return var3 + var4;
   }

   public static int getCheckSumOctalBytes(long var0, byte[] var2, int var3, int var4) {
      getOctalBytes(var0, var2, var3, var4);
      var2[var3 + var4 - 1] = 32;
      var2[var3 + var4 - 2] = 0;
      return var3 + var4;
   }
}
