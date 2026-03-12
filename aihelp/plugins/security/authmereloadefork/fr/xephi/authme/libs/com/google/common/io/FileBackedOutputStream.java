package fr.xephi.authme.libs.com.google.common.io;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.GuardedBy;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class FileBackedOutputStream extends OutputStream {
   private final int fileThreshold;
   private final boolean resetOnFinalize;
   private final ByteSource source;
   @CheckForNull
   private final File parentDirectory;
   @GuardedBy("this")
   private OutputStream out;
   @CheckForNull
   @GuardedBy("this")
   private FileBackedOutputStream.MemoryOutput memory;
   @CheckForNull
   @GuardedBy("this")
   private File file;

   @CheckForNull
   @VisibleForTesting
   synchronized File getFile() {
      return this.file;
   }

   public FileBackedOutputStream(int fileThreshold) {
      this(fileThreshold, false);
   }

   public FileBackedOutputStream(int fileThreshold, boolean resetOnFinalize) {
      this(fileThreshold, resetOnFinalize, (File)null);
   }

   private FileBackedOutputStream(int fileThreshold, boolean resetOnFinalize, @CheckForNull File parentDirectory) {
      this.fileThreshold = fileThreshold;
      this.resetOnFinalize = resetOnFinalize;
      this.parentDirectory = parentDirectory;
      this.memory = new FileBackedOutputStream.MemoryOutput();
      this.out = this.memory;
      if (resetOnFinalize) {
         this.source = new ByteSource() {
            public InputStream openStream() throws IOException {
               return FileBackedOutputStream.this.openInputStream();
            }

            protected void finalize() {
               try {
                  FileBackedOutputStream.this.reset();
               } catch (Throwable var2) {
                  var2.printStackTrace(System.err);
               }

            }
         };
      } else {
         this.source = new ByteSource() {
            public InputStream openStream() throws IOException {
               return FileBackedOutputStream.this.openInputStream();
            }
         };
      }

   }

   public ByteSource asByteSource() {
      return this.source;
   }

   private synchronized InputStream openInputStream() throws IOException {
      if (this.file != null) {
         return new FileInputStream(this.file);
      } else {
         Objects.requireNonNull(this.memory);
         return new ByteArrayInputStream(this.memory.getBuffer(), 0, this.memory.getCount());
      }
   }

   public synchronized void reset() throws IOException {
      boolean var7 = false;

      try {
         var7 = true;
         this.close();
         var7 = false;
      } finally {
         if (var7) {
            if (this.memory == null) {
               this.memory = new FileBackedOutputStream.MemoryOutput();
            } else {
               this.memory.reset();
            }

            this.out = this.memory;
            if (this.file != null) {
               File deleteMe = this.file;
               this.file = null;
               if (!deleteMe.delete()) {
                  String var5 = String.valueOf(deleteMe);
                  throw new IOException((new StringBuilder(18 + String.valueOf(var5).length())).append("Could not delete: ").append(var5).toString());
               }
            }

         }
      }

      if (this.memory == null) {
         this.memory = new FileBackedOutputStream.MemoryOutput();
      } else {
         this.memory.reset();
      }

      this.out = this.memory;
      if (this.file != null) {
         File deleteMe = this.file;
         this.file = null;
         if (!deleteMe.delete()) {
            String var2 = String.valueOf(deleteMe);
            throw new IOException((new StringBuilder(18 + String.valueOf(var2).length())).append("Could not delete: ").append(var2).toString());
         }
      }

   }

   public synchronized void write(int b) throws IOException {
      this.update(1);
      this.out.write(b);
   }

   public synchronized void write(byte[] b) throws IOException {
      this.write(b, 0, b.length);
   }

   public synchronized void write(byte[] b, int off, int len) throws IOException {
      this.update(len);
      this.out.write(b, off, len);
   }

   public synchronized void close() throws IOException {
      this.out.close();
   }

   public synchronized void flush() throws IOException {
      this.out.flush();
   }

   @GuardedBy("this")
   private void update(int len) throws IOException {
      if (this.memory != null && this.memory.getCount() + len > this.fileThreshold) {
         File temp = File.createTempFile("FileBackedOutputStream", (String)null, this.parentDirectory);
         if (this.resetOnFinalize) {
            temp.deleteOnExit();
         }

         try {
            FileOutputStream transfer = new FileOutputStream(temp);
            transfer.write(this.memory.getBuffer(), 0, this.memory.getCount());
            transfer.flush();
            this.out = transfer;
         } catch (IOException var4) {
            temp.delete();
            throw var4;
         }

         this.file = temp;
         this.memory = null;
      }

   }

   private static class MemoryOutput extends ByteArrayOutputStream {
      private MemoryOutput() {
      }

      byte[] getBuffer() {
         return this.buf;
      }

      int getCount() {
         return this.count;
      }

      // $FF: synthetic method
      MemoryOutput(Object x0) {
         this();
      }
   }
}
