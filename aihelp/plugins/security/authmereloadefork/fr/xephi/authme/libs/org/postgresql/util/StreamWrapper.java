package fr.xephi.authme.libs.org.postgresql.util;

import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class StreamWrapper implements Closeable {
   private static final int MAX_MEMORY_BUFFER_BYTES = 51200;
   private static final String TEMP_FILE_PREFIX = "postgres-pgjdbc-stream";
   @Nullable
   private final InputStream stream;
   @Nullable
   private TempFileHolder tempFileHolder;
   private final Object leakHandle = new Object();
   @Nullable
   private LazyCleaner.Cleanable<IOException> cleaner;
   @Nullable
   private final byte[] rawData;
   private final int offset;
   private final int length;

   public StreamWrapper(byte[] data, int offset, int length) {
      this.stream = null;
      this.rawData = data;
      this.offset = offset;
      this.length = length;
   }

   public StreamWrapper(InputStream stream, int length) {
      this.stream = stream;
      this.rawData = null;
      this.offset = 0;
      this.length = length;
   }

   public StreamWrapper(InputStream stream) throws PSQLException {
      try {
         ByteArrayOutputStream memoryOutputStream = new ByteArrayOutputStream();
         int memoryLength = copyStream(stream, memoryOutputStream, 51200);
         byte[] rawData = memoryOutputStream.toByteArray();
         if (memoryLength == -1) {
            Path tempFile = Files.createTempFile("postgres-pgjdbc-stream", ".tmp");

            int diskLength;
            try {
               OutputStream diskOutputStream = Files.newOutputStream(tempFile);

               try {
                  diskOutputStream.write(rawData);
                  diskLength = copyStream(stream, diskOutputStream, Integer.MAX_VALUE - rawData.length);
                  if (diskLength == -1) {
                     throw new PSQLException(GT.tr("Object is too large to send over the protocol."), PSQLState.NUMERIC_CONSTANT_OUT_OF_RANGE);
                  }
               } catch (Throwable var12) {
                  if (diskOutputStream != null) {
                     try {
                        diskOutputStream.close();
                     } catch (Throwable var11) {
                        var12.addSuppressed(var11);
                     }
                  }

                  throw var12;
               }

               if (diskOutputStream != null) {
                  diskOutputStream.close();
               }
            } catch (Error | PSQLException | RuntimeException var13) {
               try {
                  tempFile.toFile().delete();
               } catch (Throwable var10) {
               }

               throw var13;
            }

            this.offset = 0;
            this.length = rawData.length + diskLength;
            this.rawData = null;
            this.stream = null;
            TempFileHolder tempFileHolder = new TempFileHolder(tempFile);
            this.tempFileHolder = tempFileHolder;
            this.cleaner = LazyCleaner.getInstance().register(this.leakHandle, tempFileHolder);
         } else {
            this.rawData = rawData;
            this.stream = null;
            this.offset = 0;
            this.length = rawData.length;
         }

      } catch (IOException var14) {
         throw new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.IO_ERROR, var14);
      }
   }

   public InputStream getStream() throws IOException {
      if (this.stream != null) {
         return this.stream;
      } else {
         TempFileHolder finalizeAction = this.tempFileHolder;
         return (InputStream)(finalizeAction != null ? finalizeAction.getStream() : new ByteArrayInputStream((byte[])Nullness.castNonNull(this.rawData), this.offset, this.length));
      }
   }

   public void close() throws IOException {
      if (this.cleaner != null) {
         this.cleaner.clean();
      }

   }

   public int getLength() {
      return this.length;
   }

   public int getOffset() {
      return this.offset;
   }

   @Nullable
   public byte[] getBytes() {
      return this.rawData;
   }

   public String toString() {
      return "<stream of " + this.length + " bytes>";
   }

   private static int copyStream(InputStream inputStream, OutputStream outputStream, int limit) throws IOException {
      int totalLength = 0;
      byte[] buffer = new byte[2048];

      for(int readLength = inputStream.read(buffer); readLength > 0; readLength = inputStream.read(buffer)) {
         totalLength += readLength;
         outputStream.write(buffer, 0, readLength);
         if (totalLength >= limit) {
            return -1;
         }
      }

      return totalLength;
   }
}
