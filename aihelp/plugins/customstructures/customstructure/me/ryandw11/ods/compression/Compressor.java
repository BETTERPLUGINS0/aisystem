package me.ryandw11.ods.compression;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Compressor {
   InputStream getInputStream(InputStream var1) throws IOException;

   OutputStream getOutputStream(OutputStream var1) throws IOException;
}
