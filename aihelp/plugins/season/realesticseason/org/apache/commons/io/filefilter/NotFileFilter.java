package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class NotFileFilter extends AbstractFileFilter implements Serializable {
   private static final long serialVersionUID = 6131563330944994230L;
   private final IOFileFilter filter;

   public NotFileFilter(IOFileFilter var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("The filter must not be null");
      } else {
         this.filter = var1;
      }
   }

   public boolean accept(File var1) {
      return !this.filter.accept(var1);
   }

   public boolean accept(File var1, String var2) {
      return !this.filter.accept(var1, var2);
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      return this.not(this.filter.accept(var1, var2));
   }

   private FileVisitResult not(FileVisitResult var1) {
      return var1 == FileVisitResult.CONTINUE ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
   }

   public String toString() {
      return "NOT (" + this.filter.toString() + ")";
   }
}
