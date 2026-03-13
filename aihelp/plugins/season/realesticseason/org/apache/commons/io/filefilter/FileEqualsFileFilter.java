package org.apache.commons.io.filefilter;

import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class FileEqualsFileFilter extends AbstractFileFilter {
   private final File file;
   private final Path path;

   public FileEqualsFileFilter(File var1) {
      this.file = (File)Objects.requireNonNull(var1, "file");
      this.path = var1.toPath();
   }

   public boolean accept(File var1) {
      return Objects.equals(this.file, var1);
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      return toFileVisitResult(Objects.equals(this.path, var1), var1);
   }
}
