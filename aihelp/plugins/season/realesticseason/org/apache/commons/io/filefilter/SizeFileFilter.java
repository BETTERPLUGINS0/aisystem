package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class SizeFileFilter extends AbstractFileFilter implements Serializable {
   private static final long serialVersionUID = 7388077430788600069L;
   private final boolean acceptLarger;
   private final long size;

   public SizeFileFilter(long var1) {
      this(var1, true);
   }

   public SizeFileFilter(long var1, boolean var3) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("The size must be non-negative");
      } else {
         this.size = var1;
         this.acceptLarger = var3;
      }
   }

   public boolean accept(File var1) {
      return this.accept(var1.length());
   }

   private boolean accept(long var1) {
      return this.acceptLarger != var1 < this.size;
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      try {
         return toFileVisitResult(this.accept(Files.size(var1)), var1);
      } catch (IOException var4) {
         return this.handle(var4);
      }
   }

   public String toString() {
      String var1 = this.acceptLarger ? ">=" : "<";
      return super.toString() + "(" + var1 + this.size + ")";
   }

   public FileVisitResult visitFile(Path var1, BasicFileAttributes var2) {
      return toFileVisitResult(this.accept(Files.size(var1)), var1);
   }
}
