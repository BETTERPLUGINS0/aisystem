package fr.xephi.authme.libs.com.icetar.tar;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarOutputStream extends FilterOutputStream {
   protected boolean debug;
   protected long currSize;
   protected long currBytes;
   protected byte[] oneBuf;
   protected byte[] recordBuf;
   protected int assemLen;
   protected byte[] assemBuf;
   protected TarBuffer buffer;

   public TarOutputStream(OutputStream var1) {
      this(var1, 10240, 512);
   }

   public TarOutputStream(OutputStream var1, int var2) {
      this(var1, var2, 512);
   }

   public TarOutputStream(OutputStream var1, int var2, int var3) {
      super(var1);
      this.buffer = new TarBuffer(var1, var2, var3);
      this.debug = false;
      this.assemLen = 0;
      this.assemBuf = new byte[var3];
      this.recordBuf = new byte[var3];
      this.oneBuf = new byte[1];
   }

   public void setDebug(boolean var1) {
      this.debug = var1;
   }

   public void setBufferDebug(boolean var1) {
      this.buffer.setDebug(var1);
   }

   public void finish() throws IOException {
      this.writeEOFRecord();
   }

   public void close() throws IOException {
      this.finish();
      this.buffer.close();
   }

   public int getRecordSize() {
      return this.buffer.getRecordSize();
   }

   public void putNextEntry(TarEntry var1) throws IOException {
      StringBuffer var2 = var1.getHeader().name;
      if ((!var1.isUnixTarFormat() || var2.length() <= 100) && (var1.isUnixTarFormat() || var2.length() <= 255)) {
         var1.writeEntryHeader(this.recordBuf);
         this.buffer.writeRecord(this.recordBuf);
         this.currBytes = 0L;
         if (var1.isDirectory()) {
            this.currSize = 0L;
         } else {
            this.currSize = var1.getSize();
         }

      } else {
         throw new InvalidHeaderException("file name '" + var2 + "' is too long ( " + var2.length() + " > " + (var1.isUnixTarFormat() ? 100 : 255) + " bytes )");
      }
   }

   public void closeEntry() throws IOException {
      if (this.assemLen > 0) {
         for(int var1 = this.assemLen; var1 < this.assemBuf.length; ++var1) {
            this.assemBuf[var1] = 0;
         }

         this.buffer.writeRecord(this.assemBuf);
         this.currBytes += (long)this.assemLen;
         this.assemLen = 0;
      }

      if (this.currBytes < this.currSize) {
         throw new IOException("entry closed at '" + this.currBytes + "' before the '" + this.currSize + "' bytes specified in the header were written");
      }
   }

   public void write(int var1) throws IOException {
      this.oneBuf[0] = (byte)var1;
      this.write(this.oneBuf, 0, 1);
   }

   public void write(byte[] var1) throws IOException {
      this.write(var1, 0, var1.length);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (this.currBytes + (long)var3 > this.currSize) {
         throw new IOException("request to write '" + var3 + "' bytes exceeds size in header of '" + this.currSize + "' bytes");
      } else {
         if (this.assemLen > 0) {
            if (this.assemLen + var3 >= this.recordBuf.length) {
               int var4 = this.recordBuf.length - this.assemLen;
               System.arraycopy(this.assemBuf, 0, this.recordBuf, 0, this.assemLen);
               System.arraycopy(var1, var2, this.recordBuf, this.assemLen, var4);
               this.buffer.writeRecord(this.recordBuf);
               this.currBytes += (long)this.recordBuf.length;
               var2 += var4;
               var3 -= var4;
               this.assemLen = 0;
            } else {
               System.arraycopy(var1, var2, this.assemBuf, this.assemLen, var3);
               var2 += var3;
               this.assemLen += var3;
               var3 -= var3;
            }
         }

         while(var3 > 0) {
            if (var3 < this.recordBuf.length) {
               System.arraycopy(var1, var2, this.assemBuf, this.assemLen, var3);
               this.assemLen += var3;
               break;
            }

            this.buffer.writeRecord(var1, var2);
            long var6 = (long)this.recordBuf.length;
            this.currBytes += var6;
            var3 = (int)((long)var3 - var6);
            var2 = (int)((long)var2 + var6);
         }

      }
   }

   private void writeEOFRecord() throws IOException {
      for(int var1 = 0; var1 < this.recordBuf.length; ++var1) {
         this.recordBuf[var1] = 0;
      }

      this.buffer.writeRecord(this.recordBuf);
   }
}
