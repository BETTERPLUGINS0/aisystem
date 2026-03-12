package fr.xephi.authme.libs.com.icetar.tar;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TarInputStream extends FilterInputStream {
   protected boolean debug;
   protected boolean hasHitEOF;
   protected long entrySize;
   protected long entryOffset;
   protected byte[] oneBuf;
   protected byte[] readBuf;
   protected TarBuffer buffer;
   protected TarEntry currEntry;
   protected TarInputStream.EntryFactory eFactory;

   public TarInputStream(InputStream var1) {
      this(var1, 10240, 512);
   }

   public TarInputStream(InputStream var1, int var2) {
      this(var1, var2, 512);
   }

   public TarInputStream(InputStream var1, int var2, int var3) {
      super(var1);
      this.buffer = new TarBuffer(var1, var2, var3);
      this.readBuf = null;
      this.oneBuf = new byte[1];
      this.debug = false;
      this.hasHitEOF = false;
      this.eFactory = null;
   }

   public void setDebug(boolean var1) {
      this.debug = var1;
   }

   public void setEntryFactory(TarInputStream.EntryFactory var1) {
      this.eFactory = var1;
   }

   public void setBufferDebug(boolean var1) {
      this.buffer.setDebug(var1);
   }

   public void close() throws IOException {
      this.buffer.close();
   }

   public int getRecordSize() {
      return this.buffer.getRecordSize();
   }

   public int available() throws IOException {
      return (int)(this.entrySize - this.entryOffset);
   }

   public long skip(long var1) throws IOException {
      byte[] var3 = new byte[8192];

      long var4;
      int var6;
      for(var4 = var1; var4 > 0L; var4 -= (long)var6) {
         var6 = this.read(var3, 0, var4 > (long)var3.length ? var3.length : (int)var4);
         if (var6 == -1) {
            break;
         }
      }

      return var1 - var4;
   }

   public boolean markSupported() {
      return false;
   }

   public void mark(int var1) {
   }

   public void reset() {
   }

   public long getEntryPosition() {
      return this.entryOffset;
   }

   public long getStreamPosition() {
      return (long)(this.buffer.getBlockSize() * this.buffer.getCurrentBlockNum() + this.buffer.getCurrentRecordNum());
   }

   public TarEntry getNextEntry() throws IOException {
      if (this.hasHitEOF) {
         return null;
      } else {
         if (this.currEntry != null) {
            long var1 = this.entrySize - this.entryOffset;
            if (this.debug) {
               System.err.println("TarInputStream: SKIP currENTRY '" + this.currEntry.getName() + "' SZ " + this.entrySize + " OFF " + this.entryOffset + "  skipping " + var1 + " bytes");
            }

            if (var1 > 0L) {
               this.skip(var1);
            }

            this.readBuf = null;
         }

         byte[] var4 = this.buffer.readRecord();
         if (var4 == null) {
            if (this.debug) {
               System.err.println("READ NULL RECORD");
            }

            this.hasHitEOF = true;
         } else if (this.buffer.isEOFRecord(var4)) {
            if (this.debug) {
               System.err.println("READ EOF RECORD");
            }

            this.hasHitEOF = true;
         }

         if (this.hasHitEOF) {
            this.currEntry = null;
         } else {
            try {
               if (this.eFactory == null) {
                  this.currEntry = new TarEntry(var4);
               } else {
                  this.currEntry = this.eFactory.createEntry(var4);
               }

               if (this.debug) {
                  System.err.println("TarInputStream: SET CURRENTRY '" + this.currEntry.getName() + "' size = " + this.currEntry.getSize());
               }

               this.entryOffset = 0L;
               this.entrySize = this.currEntry.getSize();
            } catch (InvalidHeaderException var3) {
               this.entrySize = 0L;
               this.entryOffset = 0L;
               this.currEntry = null;
               throw new InvalidHeaderException("bad header in block " + this.buffer.getCurrentBlockNum() + " record " + this.buffer.getCurrentRecordNum() + ", " + var3.getMessage());
            }
         }

         return this.currEntry;
      }
   }

   public int read() throws IOException {
      int var1 = this.read(this.oneBuf, 0, 1);
      return var1 == -1 ? var1 : this.oneBuf[0];
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = 0;
      if (this.entryOffset >= this.entrySize) {
         return -1;
      } else {
         if ((long)var3 + this.entryOffset > this.entrySize) {
            var3 = (int)(this.entrySize - this.entryOffset);
         }

         int var6;
         if (this.readBuf != null) {
            int var5 = var3 > this.readBuf.length ? this.readBuf.length : var3;
            System.arraycopy(this.readBuf, 0, var1, var2, var5);
            if (var5 >= this.readBuf.length) {
               this.readBuf = null;
            } else {
               var6 = this.readBuf.length - var5;
               byte[] var7 = new byte[var6];
               System.arraycopy(this.readBuf, var5, var7, 0, var6);
               this.readBuf = var7;
            }

            var4 += var5;
            var3 -= var5;
            var2 += var5;
         }

         while(var3 > 0) {
            byte[] var8 = this.buffer.readRecord();
            if (var8 == null) {
               throw new IOException("unexpected EOF with " + var3 + " bytes unread");
            }

            var6 = var3;
            int var9 = var8.length;
            if (var9 > var3) {
               System.arraycopy(var8, 0, var1, var2, var3);
               this.readBuf = new byte[var9 - var3];
               System.arraycopy(var8, var3, this.readBuf, 0, var9 - var3);
            } else {
               var6 = var9;
               System.arraycopy(var8, 0, var1, var2, var9);
            }

            var4 += var6;
            var3 -= var6;
            var2 += var6;
         }

         this.entryOffset += (long)var4;
         return var4;
      }
   }

   public void copyEntryContents(OutputStream var1) throws IOException {
      byte[] var2 = new byte['耀'];

      while(true) {
         int var3 = this.read(var2, 0, var2.length);
         if (var3 == -1) {
            return;
         }

         var1.write(var2, 0, var3);
      }
   }

   public class EntryAdapter implements TarInputStream.EntryFactory {
      public TarEntry createEntry(String var1) {
         return new TarEntry(var1);
      }

      public TarEntry createEntry(File var1) throws InvalidHeaderException {
         return new TarEntry(var1);
      }

      public TarEntry createEntry(byte[] var1) throws InvalidHeaderException {
         return new TarEntry(var1);
      }
   }

   public interface EntryFactory {
      TarEntry createEntry(String var1);

      TarEntry createEntry(File var1) throws InvalidHeaderException;

      TarEntry createEntry(byte[] var1) throws InvalidHeaderException;
   }
}
