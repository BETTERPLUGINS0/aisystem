package org.apache.commons.io.filefilter;

import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class PathEqualsFileFilter extends AbstractFileFilter {
   private final Path path;

   public PathEqualsFileFilter(Path var1) {
      this.path = var1;
   }

   public boolean accept(File var1) {
      return Objects.equals(this.path, var1.toPath());
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      return toFileVisitResult(Objects.equals(this.path, var1), var1);
   }
}
