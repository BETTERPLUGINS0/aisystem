package me.ryandw11.ods.compression;

import java.io.InputStream;
import java.io.OutputStream;

public class NoCompression implements Compressor {
   public InputStream getInputStream(InputStream file) {
      return file;
   }

   public OutputStream getOutputStream(OutputStream file) {
      return file;
   }
}
