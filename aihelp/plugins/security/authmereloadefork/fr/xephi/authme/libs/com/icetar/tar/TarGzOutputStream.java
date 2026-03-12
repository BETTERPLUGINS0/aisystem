package fr.xephi.authme.libs.com.icetar.tar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class TarGzOutputStream extends TarOutputStream {
   private TarOutputStream tos = null;
   private GZIPOutputStream gzip = null;
   private ByteArrayOutputStream bos = null;
   private TarEntry currentEntry = null;

   public TarGzOutputStream(OutputStream var1) throws IOException {
      super((OutputStream)null);
      this.gzip = new GZIPOutputStream(var1);
      this.tos = new TarOutputStream(this.gzip);
      this.bos = new ByteArrayOutputStream();
   }

   public void setDebug(boolean var1) {
      this.tos.setDebug(var1);
   }

   public void setBufferDebug(boolean var1) {
      this.tos.setBufferDebug(var1);
   }

   public void finish() throws IOException {
      if (this.currentEntry != null) {
         this.closeEntry();
      }

      this.tos.finish();
   }

   public void close() throws IOException {
      this.tos.close();
      this.gzip.finish();
   }

   public int getRecordSize() {
      return this.tos.getRecordSize();
   }

   public void putNextEntry(TarEntry var1) throws IOException {
      if (var1.getSize() != 0L) {
         this.tos.putNextEntry(var1);
      } else {
         this.currentEntry = var1;
      }

   }

   public void closeEntry() throws IOException {
      if (this.currentEntry == null) {
         this.tos.closeEntry();
      } else {
         this.currentEntry.setSize((long)this.bos.size());
         this.tos.putNextEntry(this.currentEntry);
         this.bos.writeTo(this.tos);
         this.tos.closeEntry();
         this.currentEntry = null;
         this.bos = new ByteArrayOutputStream();
      }

   }

   public void write(int var1) throws IOException {
      if (this.currentEntry == null) {
         this.tos.write(var1);
      } else {
         this.bos.write(var1);
      }

   }

   public void write(byte[] var1) throws IOException {
      if (this.currentEntry == null) {
         this.tos.write(var1);
      } else {
         this.bos.write(var1);
      }

   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (this.currentEntry == null) {
         this.tos.write(var1, var2, var3);
      } else {
         this.bos.write(var1, var2, var3);
      }

   }
}
