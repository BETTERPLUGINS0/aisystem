package fr.xephi.authme.libs.com.icetar.tar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TarBuffer {
   public static final int DEFAULT_RCDSIZE = 512;
   public static final int DEFAULT_BLKSIZE = 10240;
   private InputStream inStream;
   private OutputStream outStream;
   private byte[] blockBuffer;
   private int currBlkIdx;
   private int currRecIdx;
   private int blockSize;
   private int recordSize;
   private int recsPerBlock;
   private boolean debug;

   public TarBuffer(InputStream var1) {
      this((InputStream)var1, 10240);
   }

   public TarBuffer(InputStream var1, int var2) {
      this((InputStream)var1, var2, 512);
   }

   public TarBuffer(InputStream var1, int var2, int var3) {
      this.inStream = var1;
      this.outStream = null;
      this.initialize(var2, var3);
   }

   public TarBuffer(OutputStream var1) {
      this((OutputStream)var1, 10240);
   }

   public TarBuffer(OutputStream var1, int var2) {
      this((OutputStream)var1, var2, 512);
   }

   public TarBuffer(OutputStream var1, int var2, int var3) {
      this.inStream = null;
      this.outStream = var1;
      this.initialize(var2, var3);
   }

   private void initialize(int var1, int var2) {
      this.debug = false;
      this.blockSize = var1;
      this.recordSize = var2;
      this.recsPerBlock = this.blockSize / this.recordSize;
      this.blockBuffer = new byte[this.blockSize];
      if (this.inStream != null) {
         this.currBlkIdx = -1;
         this.currRecIdx = this.recsPerBlock;
      } else {
         this.currBlkIdx = 0;
         this.currRecIdx = 0;
      }

   }

   public int getBlockSize() {
      return this.blockSize;
   }

   public int getRecordSize() {
      return this.recordSize;
   }

   public void setDebug(boolean var1) {
      this.debug = var1;
   }

   public boolean isEOFRecord(byte[] var1) {
      int var2 = 0;

      for(int var3 = this.getRecordSize(); var2 < var3; ++var2) {
         if (var1[var2] != 0) {
            return false;
         }
      }

      return true;
   }

   public void skipRecord() throws IOException {
      if (this.debug) {
         System.err.println("SkipRecord: recIdx = " + this.currRecIdx + " blkIdx = " + this.currBlkIdx);
      }

      if (this.inStream == null) {
         throw new IOException("reading (via skip) from an output buffer");
      } else if (this.currRecIdx < this.recsPerBlock || this.readBlock()) {
         ++this.currRecIdx;
      }
   }

   public byte[] readRecord() throws IOException {
      if (this.debug) {
         System.err.println("ReadRecord: recIdx = " + this.currRecIdx + " blkIdx = " + this.currBlkIdx);
      }

      if (this.inStream == null) {
         throw new IOException("reading from an output buffer");
      } else if (this.currRecIdx >= this.recsPerBlock && !this.readBlock()) {
         return null;
      } else {
         byte[] var1 = new byte[this.recordSize];
         System.arraycopy(this.blockBuffer, this.currRecIdx * this.recordSize, var1, 0, this.recordSize);
         ++this.currRecIdx;
         return var1;
      }
   }

   private boolean readBlock() throws IOException {
      if (this.debug) {
         System.err.println("ReadBlock: blkIdx = " + this.currBlkIdx);
      }

      if (this.inStream == null) {
         throw new IOException("reading from an output buffer");
      } else {
         this.currRecIdx = 0;
         int var1 = 0;
         int var2 = this.blockSize;

         while(var2 > 0) {
            long var3 = (long)this.inStream.read(this.blockBuffer, var1, var2);
            if (var3 == -1L) {
               break;
            }

            var1 = (int)((long)var1 + var3);
            var2 = (int)((long)var2 - var3);
            if (var3 != (long)this.blockSize && this.debug) {
               System.err.println("ReadBlock: INCOMPLETE READ " + var3 + " of " + this.blockSize + " bytes read.");
            }
         }

         ++this.currBlkIdx;
         return true;
      }
   }

   public int getCurrentBlockNum() {
      return this.currBlkIdx;
   }

   public int getCurrentRecordNum() {
      return this.currRecIdx - 1;
   }

   public void writeRecord(byte[] var1) throws IOException {
      if (this.debug) {
         System.err.println("WriteRecord: recIdx = " + this.currRecIdx + " blkIdx = " + this.currBlkIdx);
      }

      if (this.outStream == null) {
         throw new IOException("writing to an input buffer");
      } else if (var1.length != this.recordSize) {
         throw new IOException("record to write has length '" + var1.length + "' which is not the record size of '" + this.recordSize + "'");
      } else {
         if (this.currRecIdx >= this.recsPerBlock) {
            this.writeBlock();
         }

         System.arraycopy(var1, 0, this.blockBuffer, this.currRecIdx * this.recordSize, this.recordSize);
         ++this.currRecIdx;
      }
   }

   public void writeRecord(byte[] var1, int var2) throws IOException {
      if (this.debug) {
         System.err.println("WriteRecord: recIdx = " + this.currRecIdx + " blkIdx = " + this.currBlkIdx);
      }

      if (this.outStream == null) {
         throw new IOException("writing to an input buffer");
      } else if (var2 + this.recordSize > var1.length) {
         throw new IOException("record has length '" + var1.length + "' with offset '" + var2 + "' which is less than the record size of '" + this.recordSize + "'");
      } else {
         if (this.currRecIdx >= this.recsPerBlock) {
            this.writeBlock();
         }

         System.arraycopy(var1, var2, this.blockBuffer, this.currRecIdx * this.recordSize, this.recordSize);
         ++this.currRecIdx;
      }
   }

   private void writeBlock() throws IOException {
      if (this.debug) {
         System.err.println("WriteBlock: blkIdx = " + this.currBlkIdx);
      }

      if (this.outStream == null) {
         throw new IOException("writing to an input buffer");
      } else {
         this.outStream.write(this.blockBuffer, 0, this.blockSize);
         this.outStream.flush();
         this.currRecIdx = 0;
         ++this.currBlkIdx;
      }
   }

   private void flushBlock() throws IOException {
      if (this.debug) {
         System.err.println("TarBuffer.flushBlock() called.");
      }

      if (this.outStream == null) {
         throw new IOException("writing to an input buffer");
      } else {
         if (this.currRecIdx > 0) {
            int var1 = this.currRecIdx * this.recordSize;
            byte[] var2 = new byte[this.blockSize - var1];
            System.arraycopy(var2, 0, this.blockBuffer, var1, var2.length);
            this.writeBlock();
         }

      }
   }

   public void close() throws IOException {
      if (this.debug) {
         System.err.println("TarBuffer.closeBuffer().");
      }

      if (this.outStream != null) {
         this.flushBlock();
         if (this.outStream != System.out && this.outStream != System.err) {
            this.outStream.close();
            this.outStream = null;
         }
      } else if (this.inStream != null && this.inStream != System.in) {
         this.inStream.close();
         this.inStream = null;
      }

   }
}
