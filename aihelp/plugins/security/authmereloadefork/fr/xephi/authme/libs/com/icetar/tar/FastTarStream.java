package fr.xephi.authme.libs.com.icetar.tar;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class FastTarStream {
   private boolean debug;
   private boolean hasHitEOF;
   private TarEntry currEntry;
   private InputStream inStream;
   private int recordSize;

   public FastTarStream(InputStream var1) {
      this(var1, 512);
   }

   public FastTarStream(InputStream var1, int var2) {
      this.debug = false;
      this.hasHitEOF = false;
      this.currEntry = null;
      this.inStream = null;
      this.recordSize = 512;
      this.inStream = var1;
      this.hasHitEOF = false;
      this.currEntry = null;
      this.recordSize = var2;
   }

   public void setDebug(boolean var1) {
      this.debug = var1;
   }

   public TarEntry getNextEntry() throws IOException {
      if (this.hasHitEOF) {
         return null;
      } else {
         if (this.currEntry != null && this.currEntry.getSize() > 0L) {
            int var1 = ((int)this.currEntry.getSize() + (this.recordSize - 1)) / this.recordSize;
            if (var1 > 0) {
               this.inStream.skip((long)(var1 * this.recordSize));
            }
         }

         byte[] var8 = new byte[this.recordSize];
         int var2 = 0;

         int var4;
         for(int var3 = this.recordSize; var3 > 0; var3 -= var4) {
            var4 = this.inStream.read(var8, var2, var3);
            if (var4 == -1) {
               this.hasHitEOF = true;
               break;
            }

            var2 += var4;
         }

         if (!this.hasHitEOF) {
            this.hasHitEOF = true;

            for(var4 = 0; var4 < var8.length; ++var4) {
               if (var8[var4] != 0) {
                  this.hasHitEOF = false;
                  break;
               }
            }
         }

         if (this.hasHitEOF) {
            this.currEntry = null;
         } else {
            try {
               this.currEntry = new TarEntry(var8);
               if (this.debug) {
                  byte[] var9 = new byte[var8.length];
                  int var5 = 0;

                  while(true) {
                     if (var5 >= var8.length) {
                        String var6 = new String(var9);
                        System.out.println("\n" + var6);
                        break;
                     }

                     var9[var5] = var8[var5] == 0 ? 20 : var8[var5];
                     ++var5;
                  }
               }

               if (var8[257] != 117 || var8[258] != 115 || var8[259] != 116 || var8[260] != 97 || var8[261] != 114) {
                  throw new InvalidHeaderException("header magic is not'ustar', but '" + var8[257] + var8[258] + var8[259] + var8[260] + var8[261] + "', or (dec) " + var8[257] + ", " + var8[258] + ", " + var8[259] + ", " + var8[260] + ", " + var8[261]);
               }
            } catch (InvalidHeaderException var7) {
               this.currEntry = null;
               throw var7;
            }
         }

         return this.currEntry;
      }
   }

   public static void main(String[] var0) {
      boolean var1 = false;
      Object var2 = null;
      String var3 = var0[0];

      try {
         int var4 = 0;
         if (var0.length > 0) {
            if (var0[var4].equals("-d")) {
               var1 = true;
               ++var4;
            }

            if (!var0[var4].endsWith(".gz") && !var0[var4].endsWith(".tgz")) {
               var2 = new FileInputStream(var0[var4]);
            } else {
               var2 = new GZIPInputStream(new FileInputStream(var0[var4]));
            }
         } else {
            var2 = System.in;
         }

         FastTarStream var5 = new FastTarStream((InputStream)var2);
         var5.setDebug(var1);
         byte var6 = 56;
         byte var7 = 9;
         byte var8 = 8;
         StringBuffer var9 = new StringBuffer(128);

         while(true) {
            TarEntry var10 = var5.getNextEntry();
            if (var10 == null) {
               break;
            }

            if (var10.isDirectory()) {
               System.out.print("D ");
               var9.setLength(0);
               var9.append(var10.getName());
               var9.setLength(var9.length() - 1);
               if (var9.length() > var6) {
                  var9.setLength(var6);
               }

               while(var9.length() < var6) {
                  var9.append('_');
               }

               var9.append('_');
               System.out.print(var9.toString());
               var9.setLength(0);

               while(var9.length() < var7) {
                  var9.insert(0, '_');
               }

               var9.append(' ');
               System.out.print(var9.toString());
               var9.setLength(0);
               var9.append(var10.getUserName());
               if (var9.length() > var8) {
                  var9.setLength(var8);
               }

               while(var9.length() < var8) {
                  var9.append(' ');
               }

               System.out.print(var9.toString());
            } else {
               System.out.print("F ");
               var9.setLength(0);
               var9.append(var10.getName());
               if (var9.length() > var6) {
                  var9.setLength(var6);
               }

               while(var9.length() < var6) {
                  var9.append(' ');
               }

               var9.append(' ');
               System.out.print(var9.toString());
               var9.setLength(0);
               var9.append(var10.getSize());
               if (var9.length() > var7) {
                  var9.setLength(var7);
               }

               while(var9.length() < var7) {
                  var9.insert(0, ' ');
               }

               var9.append(' ');
               System.out.print(var9.toString());
               var9.setLength(0);
               var9.append(var10.getUserName());
               if (var9.length() > var8) {
                  var9.setLength(var8);
               }

               while(var9.length() < var8) {
                  var9.append(' ');
               }

               System.out.print(var9.toString());
            }

            System.out.println("");
         }
      } catch (IOException var11) {
         var11.printStackTrace(System.err);
      }

   }
}
