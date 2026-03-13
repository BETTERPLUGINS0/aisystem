package org.apache.commons.io.output;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class DeferredFileOutputStream extends ThresholdingOutputStream {
   private ByteArrayOutputStream memoryOutputStream;
   private OutputStream currentOutputStream;
   private File outputFile;
   private final String prefix;
   private final String suffix;
   private final File directory;
   private boolean closed;

   public DeferredFileOutputStream(int var1, File var2) {
      this(var1, var2, (String)null, (String)null, (File)null, 1024);
   }

   private DeferredFileOutputStream(int var1, File var2, String var3, String var4, File var5, int var6) {
      super(var1);
      this.outputFile = var2;
      this.prefix = var3;
      this.suffix = var4;
      this.directory = var5;
      this.memoryOutputStream = new ByteArrayOutputStream(var6);
      this.currentOutputStream = this.memoryOutputStream;
   }

   public DeferredFileOutputStream(int var1, int var2, File var3) {
      this(var1, var3, (String)null, (String)null, (File)null, var2);
      if (var2 < 0) {
         throw new IllegalArgumentException("Initial buffer size must be atleast 0.");
      }
   }

   public DeferredFileOutputStream(int var1, int var2, String var3, String var4, File var5) {
      this(var1, (File)null, var3, var4, var5, var2);
      if (var3 == null) {
         throw new IllegalArgumentException("Temporary file prefix is missing");
      } else if (var2 < 0) {
         throw new IllegalArgumentException("Initial buffer size must be atleast 0.");
      }
   }

   public DeferredFileOutputStream(int var1, String var2, String var3, File var4) {
      this(var1, (File)null, var2, var3, var4, 1024);
      if (var2 == null) {
         throw new IllegalArgumentException("Temporary file prefix is missing");
      }
   }

   public void close() {
      super.close();
      this.closed = true;
   }

   public byte[] getData() {
      return this.memoryOutputStream != null ? this.memoryOutputStream.toByteArray() : null;
   }

   public File getFile() {
      return this.outputFile;
   }

   protected OutputStream getStream() {
      return this.currentOutputStream;
   }

   public boolean isInMemory() {
      return !this.isThresholdExceeded();
   }

   protected void thresholdReached() {
      if (this.prefix != null) {
         this.outputFile = File.createTempFile(this.prefix, this.suffix, this.directory);
      }

      FileUtils.forceMkdirParent(this.outputFile);
      OutputStream var1 = Files.newOutputStream(this.outputFile.toPath());

      try {
         this.memoryOutputStream.writeTo(var1);
      } catch (IOException var3) {
         var1.close();
         throw var3;
      }

      this.currentOutputStream = var1;
      this.memoryOutputStream = null;
   }

   public InputStream toInputStream() {
      if (!this.closed) {
         throw new IOException("Stream not closed");
      } else {
         return this.isInMemory() ? this.memoryOutputStream.toInputStream() : Files.newInputStream(this.outputFile.toPath());
      }
   }

   public void writeTo(OutputStream var1) {
      if (!this.closed) {
         throw new IOException("Stream not closed");
      } else {
         if (this.isInMemory()) {
            this.memoryOutputStream.writeTo(var1);
         } else {
            InputStream var2 = Files.newInputStream(this.outputFile.toPath());

            try {
               IOUtils.copy(var2, var1);
            } catch (Throwable var6) {
               if (var2 != null) {
                  try {
                     var2.close();
                  } catch (Throwable var5) {
                     var6.addSuppressed(var5);
                  }
               }

               throw var6;
            }

            if (var2 != null) {
               var2.close();
            }
         }

      }
   }
}
