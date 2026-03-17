package es.outlook.adriansrj.nbt.mca;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

public enum CompressionType {
   NONE(0, (var0) -> {
      return var0;
   }, (var0) -> {
      return var0;
   }),
   GZIP(1, GZIPOutputStream::new, GZIPInputStream::new),
   ZLIB(2, DeflaterOutputStream::new, InflaterInputStream::new);

   private byte id;
   private ExceptionFunction<OutputStream, ? extends OutputStream, IOException> compressor;
   private ExceptionFunction<InputStream, ? extends InputStream, IOException> decompressor;

   private CompressionType(int var3, ExceptionFunction<OutputStream, ? extends OutputStream, IOException> var4, ExceptionFunction<InputStream, ? extends InputStream, IOException> var5) {
      this.id = (byte)var3;
      this.compressor = var4;
      this.decompressor = var5;
   }

   public byte getID() {
      return this.id;
   }

   public OutputStream compress(OutputStream var1) {
      return (OutputStream)this.compressor.accept(var1);
   }

   public InputStream decompress(InputStream var1) {
      return (InputStream)this.decompressor.accept(var1);
   }

   public static CompressionType getFromID(byte var0) {
      CompressionType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         CompressionType var4 = var1[var3];
         if (var4.id == var0) {
            return var4;
         }
      }

      return null;
   }
}
