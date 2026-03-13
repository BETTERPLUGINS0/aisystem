package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import org.apache.commons.io.file.PathVisitor;

public abstract class AbstractFileFilter implements IOFileFilter, PathVisitor {
   static FileVisitResult toFileVisitResult(boolean var0, Path var1) {
      return var0 ? FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
   }

   public boolean accept(File var1) {
      Objects.requireNonNull(var1, "file");
      return this.accept(var1.getParentFile(), var1.getName());
   }

   public boolean accept(File var1, String var2) {
      Objects.requireNonNull(var2, "name");
      return this.accept(new File(var1, var2));
   }

   protected FileVisitResult handle(Throwable var1) {
      return FileVisitResult.TERMINATE;
   }

   public FileVisitResult postVisitDirectory(Path var1, IOException var2) {
      return FileVisitResult.CONTINUE;
   }

   public FileVisitResult preVisitDirectory(Path var1, BasicFileAttributes var2) {
      return this.accept(var1, var2);
   }

   public String toString() {
      return this.getClass().getSimpleName();
   }

   public FileVisitResult visitFile(Path var1, BasicFileAttributes var2) {
      return this.accept(var1, var2);
   }

   public FileVisitResult visitFileFailed(Path var1, IOException var2) {
      return FileVisitResult.CONTINUE;
   }
}
