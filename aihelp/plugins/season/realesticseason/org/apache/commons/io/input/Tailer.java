package org.apache.commons.io.input;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class Tailer implements Runnable {
   private static final int DEFAULT_DELAY_MILLIS = 1000;
   private static final String RAF_MODE = "r";
   private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();
   private final byte[] inbuf;
   private final File file;
   private final Charset charset;
   private final long delayMillis;
   private final boolean end;
   private final TailerListener listener;
   private final boolean reOpen;
   private volatile boolean run;

   public Tailer(File var1, TailerListener var2) {
      this(var1, var2, 1000L);
   }

   public Tailer(File var1, TailerListener var2, long var3) {
      this(var1, var2, var3, false);
   }

   public Tailer(File var1, TailerListener var2, long var3, boolean var5) {
      this(var1, var2, var3, var5, 8192);
   }

   public Tailer(File var1, TailerListener var2, long var3, boolean var5, boolean var6) {
      this(var1, var2, var3, var5, var6, 8192);
   }

   public Tailer(File var1, TailerListener var2, long var3, boolean var5, int var6) {
      this(var1, var2, var3, var5, false, var6);
   }

   public Tailer(File var1, TailerListener var2, long var3, boolean var5, boolean var6, int var7) {
      this(var1, DEFAULT_CHARSET, var2, var3, var5, var6, var7);
   }

   public Tailer(File var1, Charset var2, TailerListener var3, long var4, boolean var6, boolean var7, int var8) {
      this.run = true;
      this.file = var1;
      this.delayMillis = var4;
      this.end = var6;
      this.inbuf = IOUtils.byteArray(var8);
      this.listener = var3;
      var3.init(this);
      this.reOpen = var7;
      this.charset = var2;
   }

   public static Tailer create(File var0, TailerListener var1, long var2, boolean var4, int var5) {
      return create(var0, var1, var2, var4, false, var5);
   }

   public static Tailer create(File var0, TailerListener var1, long var2, boolean var4, boolean var5, int var6) {
      return create(var0, DEFAULT_CHARSET, var1, var2, var4, var5, var6);
   }

   public static Tailer create(File var0, Charset var1, TailerListener var2, long var3, boolean var5, boolean var6, int var7) {
      Tailer var8 = new Tailer(var0, var1, var2, var3, var5, var6, var7);
      Thread var9 = new Thread(var8);
      var9.setDaemon(true);
      var9.start();
      return var8;
   }

   public static Tailer create(File var0, TailerListener var1, long var2, boolean var4) {
      return create(var0, var1, var2, var4, 8192);
   }

   public static Tailer create(File var0, TailerListener var1, long var2, boolean var4, boolean var5) {
      return create(var0, var1, var2, var4, var5, 8192);
   }

   public static Tailer create(File var0, TailerListener var1, long var2) {
      return create(var0, var1, var2, false);
   }

   public static Tailer create(File var0, TailerListener var1) {
      return create(var0, var1, 1000L, false);
   }

   public File getFile() {
      return this.file;
   }

   protected boolean getRun() {
      return this.run;
   }

   public long getDelay() {
      return this.delayMillis;
   }

   public void run() {
      RandomAccessFile var1 = null;

      try {
         long var2 = 0L;
         long var4 = 0L;

         while(this.getRun() && var1 == null) {
            try {
               var1 = new RandomAccessFile(this.file, "r");
            } catch (FileNotFoundException var29) {
               this.listener.fileNotFound();
            }

            if (var1 == null) {
               Thread.sleep(this.delayMillis);
            } else {
               var4 = this.end ? this.file.length() : 0L;
               var2 = FileUtils.lastModified(this.file);
               var1.seek(var4);
            }
         }

         while(this.getRun()) {
            boolean var6 = FileUtils.isFileNewer(this.file, var2);
            long var7 = this.file.length();
            if (var7 < var4) {
               this.listener.fileRotated();

               try {
                  RandomAccessFile var9 = var1;

                  try {
                     var1 = new RandomAccessFile(this.file, "r");

                     try {
                        this.readLines(var9);
                     } catch (IOException var28) {
                        this.listener.handle((Exception)var28);
                     }

                     var4 = 0L;
                  } catch (Throwable var30) {
                     if (var1 != null) {
                        try {
                           var9.close();
                        } catch (Throwable var27) {
                           var30.addSuppressed(var27);
                        }
                     }

                     throw var30;
                  }

                  if (var9 != null) {
                     var9.close();
                  }
               } catch (FileNotFoundException var31) {
                  this.listener.fileNotFound();
                  Thread.sleep(this.delayMillis);
               }
            } else {
               if (var7 > var4) {
                  var4 = this.readLines(var1);
                  var2 = FileUtils.lastModified(this.file);
               } else if (var6) {
                  var4 = 0L;
                  var1.seek(var4);
                  var4 = this.readLines(var1);
                  var2 = FileUtils.lastModified(this.file);
               }

               if (this.reOpen && var1 != null) {
                  var1.close();
               }

               Thread.sleep(this.delayMillis);
               if (this.getRun() && this.reOpen) {
                  var1 = new RandomAccessFile(this.file, "r");
                  var1.seek(var4);
               }
            }
         }
      } catch (InterruptedException var32) {
         Thread.currentThread().interrupt();
         this.listener.handle((Exception)var32);
      } catch (Exception var33) {
         this.listener.handle(var33);
      } finally {
         try {
            if (var1 != null) {
               var1.close();
            }
         } catch (IOException var26) {
            this.listener.handle((Exception)var26);
         }

         this.stop();
      }

   }

   public void stop() {
      this.run = false;
   }

   private long readLines(RandomAccessFile var1) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream(64);

      long var13;
      try {
         long var3 = var1.getFilePointer();
         long var5 = var3;
         boolean var8 = false;

         while(true) {
            int var7;
            if (!this.getRun() || (var7 = var1.read(this.inbuf)) == -1) {
               var1.seek(var5);
               if (this.listener instanceof TailerListenerAdapter) {
                  ((TailerListenerAdapter)this.listener).endOfFileReached();
               }

               var13 = var5;
               break;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               byte var10 = this.inbuf[var9];
               switch(var10) {
               case 10:
                  var8 = false;
                  this.listener.handle(new String(var2.toByteArray(), this.charset));
                  var2.reset();
                  var5 = var3 + (long)var9 + 1L;
                  break;
               case 13:
                  if (var8) {
                     var2.write(13);
                  }

                  var8 = true;
                  break;
               default:
                  if (var8) {
                     var8 = false;
                     this.listener.handle(new String(var2.toByteArray(), this.charset));
                     var2.reset();
                     var5 = var3 + (long)var9 + 1L;
                  }

                  var2.write(var10);
               }
            }

            var3 = var1.getFilePointer();
         }
      } catch (Throwable var12) {
         try {
            var2.close();
         } catch (Throwable var11) {
            var12.addSuppressed(var11);
         }

         throw var12;
      }

      var2.close();
      return var13;
   }
}
