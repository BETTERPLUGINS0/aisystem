package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;

public class MagicNumberFileFilter extends AbstractFileFilter implements Serializable {
   private static final long serialVersionUID = -547733176983104172L;
   private final byte[] magicNumbers;
   private final long byteOffset;

   public MagicNumberFileFilter(byte[] var1) {
      this(var1, 0L);
   }

   public MagicNumberFileFilter(byte[] var1, long var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The magic number cannot be null");
      } else if (var1.length == 0) {
         throw new IllegalArgumentException("The magic number must contain at least one byte");
      } else if (var2 < 0L) {
         throw new IllegalArgumentException("The offset cannot be negative");
      } else {
         this.magicNumbers = IOUtils.byteArray(var1.length);
         System.arraycopy(var1, 0, this.magicNumbers, 0, var1.length);
         this.byteOffset = var2;
      }
   }

   public MagicNumberFileFilter(String var1) {
      this(var1, 0L);
   }

   public MagicNumberFileFilter(String var1, long var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The magic number cannot be null");
      } else if (var1.isEmpty()) {
         throw new IllegalArgumentException("The magic number must contain at least one byte");
      } else if (var2 < 0L) {
         throw new IllegalArgumentException("The offset cannot be negative");
      } else {
         this.magicNumbers = var1.getBytes(Charset.defaultCharset());
         this.byteOffset = var2;
      }
   }

   public boolean accept(File var1) {
      if (var1 != null && var1.isFile() && var1.canRead()) {
         try {
            RandomAccessFile var2 = new RandomAccessFile(var1, "r");

            boolean var5;
            label39: {
               try {
                  byte[] var3 = IOUtils.byteArray(this.magicNumbers.length);
                  var2.seek(this.byteOffset);
                  int var4 = var2.read(var3);
                  if (var4 != this.magicNumbers.length) {
                     var5 = false;
                     break label39;
                  }

                  var5 = Arrays.equals(this.magicNumbers, var3);
               } catch (Throwable var7) {
                  try {
                     var2.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }

                  throw var7;
               }

               var2.close();
               return var5;
            }

            var2.close();
            return var5;
         } catch (IOException var8) {
         }
      }

      return false;
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      if (var1 != null && Files.isRegularFile(var1, new LinkOption[0]) && Files.isReadable(var1)) {
         try {
            FileChannel var3 = FileChannel.open(var1);

            FileVisitResult var6;
            label56: {
               try {
                  ByteBuffer var4 = ByteBuffer.allocate(this.magicNumbers.length);
                  int var5 = var3.read(var4);
                  if (var5 == this.magicNumbers.length) {
                     var6 = toFileVisitResult(Arrays.equals(this.magicNumbers, var4.array()), var1);
                     break label56;
                  }

                  var6 = FileVisitResult.TERMINATE;
               } catch (Throwable var8) {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                     }
                  }

                  throw var8;
               }

               if (var3 != null) {
                  var3.close();
               }

               return var6;
            }

            if (var3 != null) {
               var3.close();
            }

            return var6;
         } catch (IOException var9) {
         }
      }

      return FileVisitResult.TERMINATE;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(super.toString());
      var1.append("(");
      var1.append(new String(this.magicNumbers, Charset.defaultCharset()));
      var1.append(",");
      var1.append(this.byteOffset);
      var1.append(")");
      return var1.toString();
   }
}
