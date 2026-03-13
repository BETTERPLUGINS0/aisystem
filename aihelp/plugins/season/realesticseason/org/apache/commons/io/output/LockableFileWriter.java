package org.apache.commons.io.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

public class LockableFileWriter extends Writer {
   private static final String LCK = ".lck";
   private final Writer out;
   private final File lockFile;

   public LockableFileWriter(String var1) {
      this((String)var1, false, (String)null);
   }

   public LockableFileWriter(String var1, boolean var2) {
      this((String)var1, var2, (String)null);
   }

   public LockableFileWriter(String var1, boolean var2, String var3) {
      this(new File(var1), var2, var3);
   }

   public LockableFileWriter(File var1) {
      this((File)var1, false, (String)null);
   }

   public LockableFileWriter(File var1, boolean var2) {
      this((File)var1, var2, (String)null);
   }

   /** @deprecated */
   @Deprecated
   public LockableFileWriter(File var1, boolean var2, String var3) {
      this(var1, Charset.defaultCharset(), var2, var3);
   }

   public LockableFileWriter(File var1, Charset var2) {
      this(var1, (Charset)var2, false, (String)null);
   }

   public LockableFileWriter(File var1, String var2) {
      this(var1, (String)var2, false, (String)null);
   }

   public LockableFileWriter(File var1, Charset var2, boolean var3, String var4) {
      var1 = var1.getAbsoluteFile();
      if (var1.getParentFile() != null) {
         FileUtils.forceMkdir(var1.getParentFile());
      }

      if (var1.isDirectory()) {
         throw new IOException("File specified is a directory");
      } else {
         if (var4 == null) {
            var4 = System.getProperty("java.io.tmpdir");
         }

         File var5 = new File(var4);
         FileUtils.forceMkdir(var5);
         this.testLockDir(var5);
         this.lockFile = new File(var5, var1.getName() + ".lck");
         this.createLock();
         this.out = this.initWriter(var1, var2, var3);
      }
   }

   public LockableFileWriter(File var1, String var2, boolean var3, String var4) {
      this(var1, Charsets.toCharset(var2), var3, var4);
   }

   private void testLockDir(File var1) {
      if (!var1.exists()) {
         throw new IOException("Could not find lockDir: " + var1.getAbsolutePath());
      } else if (!var1.canWrite()) {
         throw new IOException("Could not write to lockDir: " + var1.getAbsolutePath());
      }
   }

   private void createLock() {
      Class var1 = LockableFileWriter.class;
      synchronized(LockableFileWriter.class) {
         if (!this.lockFile.createNewFile()) {
            throw new IOException("Can't write file, lock " + this.lockFile.getAbsolutePath() + " exists");
         } else {
            this.lockFile.deleteOnExit();
         }
      }
   }

   private Writer initWriter(File var1, Charset var2, boolean var3) {
      boolean var4 = var1.exists();

      try {
         return new OutputStreamWriter(new FileOutputStream(var1.getAbsolutePath(), var3), Charsets.toCharset(var2));
      } catch (RuntimeException | IOException var6) {
         FileUtils.deleteQuietly(this.lockFile);
         if (!var4) {
            FileUtils.deleteQuietly(var1);
         }

         throw var6;
      }
   }

   public void close() {
      try {
         this.out.close();
      } finally {
         FileUtils.delete(this.lockFile);
      }

   }

   public void write(int var1) {
      this.out.write(var1);
   }

   public void write(char[] var1) {
      this.out.write(var1);
   }

   public void write(char[] var1, int var2, int var3) {
      this.out.write(var1, var2, var3);
   }

   public void write(String var1) {
      this.out.write(var1);
   }

   public void write(String var1, int var2, int var3) {
      this.out.write(var1, var2, var3);
   }

   public void flush() {
      this.out.flush();
   }
}
