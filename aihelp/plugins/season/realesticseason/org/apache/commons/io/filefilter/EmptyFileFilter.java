package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;

public class EmptyFileFilter extends AbstractFileFilter implements Serializable {
   public static final IOFileFilter EMPTY = new EmptyFileFilter();
   public static final IOFileFilter NOT_EMPTY;
   private static final long serialVersionUID = 3631422087512832211L;

   protected EmptyFileFilter() {
   }

   public boolean accept(File var1) {
      if (var1.isDirectory()) {
         File[] var2 = var1.listFiles();
         return IOUtils.length((Object[])var2) == 0;
      } else {
         return var1.length() == 0L;
      }
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      try {
         if (Files.isDirectory(var1, new LinkOption[0])) {
            Stream var3 = Files.list(var1);

            FileVisitResult var4;
            try {
               var4 = toFileVisitResult(!var3.findFirst().isPresent(), var1);
            } catch (Throwable var7) {
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (var3 != null) {
               var3.close();
            }

            return var4;
         } else {
            return toFileVisitResult(Files.size(var1) == 0L, var1);
         }
      } catch (IOException var8) {
         return this.handle(var8);
      }
   }

   static {
      NOT_EMPTY = EMPTY.negate();
   }
}
