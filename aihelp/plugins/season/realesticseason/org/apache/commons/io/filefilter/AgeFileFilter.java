package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.file.PathUtils;

public class AgeFileFilter extends AbstractFileFilter implements Serializable {
   private static final long serialVersionUID = -2132740084016138541L;
   private final boolean acceptOlder;
   private final long cutoffMillis;

   public AgeFileFilter(Date var1) {
      this(var1, true);
   }

   public AgeFileFilter(Date var1, boolean var2) {
      this(var1.getTime(), var2);
   }

   public AgeFileFilter(File var1) {
      this(var1, true);
   }

   public AgeFileFilter(File var1, boolean var2) {
      this(FileUtils.lastModifiedUnchecked(var1), var2);
   }

   public AgeFileFilter(long var1) {
      this(var1, true);
   }

   public AgeFileFilter(long var1, boolean var3) {
      this.acceptOlder = var3;
      this.cutoffMillis = var1;
   }

   public boolean accept(File var1) {
      boolean var2 = FileUtils.isFileNewer(var1, this.cutoffMillis);
      return this.acceptOlder != var2;
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      boolean var3;
      try {
         var3 = PathUtils.isNewer(var1, this.cutoffMillis);
      } catch (IOException var5) {
         return this.handle(var5);
      }

      return toFileVisitResult(this.acceptOlder != var3, var1);
   }

   public String toString() {
      String var1 = this.acceptOlder ? "<=" : ">";
      return super.toString() + "(" + var1 + this.cutoffMillis + ")";
   }
}
