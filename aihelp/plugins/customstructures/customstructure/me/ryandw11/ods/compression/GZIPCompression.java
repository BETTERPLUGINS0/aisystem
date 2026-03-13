package me.ryandw11.ods.compression;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPCompression implements Compressor {
   public InputStream getInputStream(InputStream file) throws IOException {
      return new GZIPInputStream(file);
   }

   public OutputStream getOutputStream(OutputStream file) throws IOException {
      return new GZIPOutputStream(file);
   }
}
