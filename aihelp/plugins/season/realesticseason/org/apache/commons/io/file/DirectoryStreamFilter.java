package org.apache.commons.io.file;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.DirectoryStream.Filter;
import java.util.Objects;

public class DirectoryStreamFilter implements Filter<Path> {
   private final PathFilter pathFilter;

   public DirectoryStreamFilter(PathFilter var1) {
      this.pathFilter = (PathFilter)Objects.requireNonNull(var1, "pathFilter");
   }

   public boolean accept(Path var1) {
      return this.pathFilter.accept(var1, PathUtils.readBasicFileAttributes(var1)) == FileVisitResult.CONTINUE;
   }

   public PathFilter getPathFilter() {
      return this.pathFilter;
   }
}
