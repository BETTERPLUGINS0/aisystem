package org.apache.commons.io.input;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;

public class BOMInputStream extends ProxyInputStream {
   private final boolean include;
   private final List<ByteOrderMark> boms;
   private ByteOrderMark byteOrderMark;
   private int[] firstBytes;
   private int fbLength;
   private int fbIndex;
   private int markFbIndex;
   private boolean markedAtStart;
   private static final Comparator<ByteOrderMark> ByteOrderMarkLengthComparator = (var0, var1) -> {
      int var2 = var0.length();
      int var3 = var1.length();
      return Integer.compare(var3, var2);
   };

   public BOMInputStream(InputStream var1) {
      this(var1, false, ByteOrderMark.UTF_8);
   }

   public BOMInputStream(InputStream var1, boolean var2) {
      this(var1, var2, ByteOrderMark.UTF_8);
   }

   public BOMInputStream(InputStream var1, ByteOrderMark... var2) {
      this(var1, false, var2);
   }

   public BOMInputStream(InputStream var1, boolean var2, ByteOrderMark... var3) {
      super(var1);
      if (IOUtils.length((Object[])var3) == 0) {
         throw new IllegalArgumentException("No BOMs specified");
      } else {
         this.include = var2;
         List var4 = Arrays.asList(var3);
         var4.sort(ByteOrderMarkLengthComparator);
         this.boms = var4;
      }
   }

   public boolean hasBOM() {
      return this.getBOM() != null;
   }

   public boolean hasBOM(ByteOrderMark var1) {
      if (!this.boms.contains(var1)) {
         throw new IllegalArgumentException("Stream not configure to detect " + var1);
      } else {
         this.getBOM();
         return this.byteOrderMark != null && this.byteOrderMark.equals(var1);
      }
   }

   public ByteOrderMark getBOM() {
      if (this.firstBytes == null) {
         this.fbLength = 0;
         int var1 = ((ByteOrderMark)this.boms.get(0)).length();
         this.firstBytes = new int[var1];

         for(int var2 = 0; var2 < this.firstBytes.length; ++var2) {
            this.firstBytes[var2] = this.in.read();
            ++this.fbLength;
            if (this.firstBytes[var2] < 0) {
               break;
            }
         }

         this.byteOrderMark = this.find();
         if (this.byteOrderMark != null && !this.include) {
            if (this.byteOrderMark.length() < this.firstBytes.length) {
               this.fbIndex = this.byteOrderMark.length();
            } else {
               this.fbLength = 0;
            }
         }
      }

      return this.byteOrderMark;
   }

   public String getBOMCharsetName() {
      this.getBOM();
      return this.byteOrderMark == null ? null : this.byteOrderMark.getCharsetName();
   }

   private int readFirstBytes() {
      this.getBOM();
      return this.fbIndex < this.fbLength ? this.firstBytes[this.fbIndex++] : -1;
   }

   private ByteOrderMark find() {
      Iterator var1 = this.boms.iterator();

      ByteOrderMark var2;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         var2 = (ByteOrderMark)var1.next();
      } while(!this.matches(var2));

      return var2;
   }

   private boolean matches(ByteOrderMark var1) {
      for(int var2 = 0; var2 < var1.length(); ++var2) {
         if (var1.get(var2) != this.firstBytes[var2]) {
            return false;
         }
      }

      return true;
   }

   public int read() {
      int var1 = this.readFirstBytes();
      return var1 >= 0 ? var1 : this.in.read();
   }

   public int read(byte[] var1, int var2, int var3) {
      int var4 = 0;
      int var5 = 0;

      while(var3 > 0 && var5 >= 0) {
         var5 = this.readFirstBytes();
         if (var5 >= 0) {
            var1[var2++] = (byte)(var5 & 255);
            --var3;
            ++var4;
         }
      }

      int var6 = this.in.read(var1, var2, var3);
      return var6 < 0 ? (var4 > 0 ? var4 : -1) : var4 + var6;
   }

   public int read(byte[] var1) {
      return this.read(var1, 0, var1.length);
   }

   public synchronized void mark(int var1) {
      this.markFbIndex = this.fbIndex;
      this.markedAtStart = this.firstBytes == null;
      this.in.mark(var1);
   }

   public synchronized void reset() {
      this.fbIndex = this.markFbIndex;
      if (this.markedAtStart) {
         this.firstBytes = null;
      }

      this.in.reset();
   }

   public long skip(long var1) {
      int var3;
      for(var3 = 0; var1 > (long)var3 && this.readFirstBytes() >= 0; ++var3) {
      }

      return this.in.skip(var1 - (long)var3) + (long)var3;
   }
}
