package me.ryandw11.ods.compression;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class ZLIBCompression implements Compressor {
   public InputStream getInputStream(InputStream file) {
      return new InflaterInputStream(file);
   }

   public OutputStream getOutputStream(OutputStream file) {
      return new DeflaterOutputStream(file);
   }
}
