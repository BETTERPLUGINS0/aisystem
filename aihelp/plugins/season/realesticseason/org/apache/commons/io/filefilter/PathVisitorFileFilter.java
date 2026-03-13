package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.commons.io.file.NoopPathVisitor;
import org.apache.commons.io.file.PathUtils;
import org.apache.commons.io.file.PathVisitor;

public class PathVisitorFileFilter extends AbstractFileFilter {
   private final PathVisitor pathVisitor;

   public PathVisitorFileFilter(PathVisitor var1) {
      this.pathVisitor = (PathVisitor)(var1 == null ? NoopPathVisitor.INSTANCE : var1);
   }

   public boolean accept(File var1) {
      try {
         Path var2 = var1.toPath();
         return this.visitFile(var2, var1.exists() ? PathUtils.readBasicFileAttributes(var2) : null) == FileVisitResult.CONTINUE;
      } catch (IOException var3) {
         return this.handle(var3) == FileVisitResult.CONTINUE;
      }
   }

   public boolean accept(File var1, String var2) {
      try {
         Path var3 = var1.toPath().resolve(var2);
         return this.accept(var3, PathUtils.readBasicFileAttributes(var3)) == FileVisitResult.CONTINUE;
      } catch (IOException var4) {
         return this.handle(var4) == FileVisitResult.CONTINUE;
      }
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      try {
         return Files.isDirectory(var1, new LinkOption[0]) ? this.pathVisitor.postVisitDirectory(var1, (IOException)null) : this.visitFile(var1, var2);
      } catch (IOException var4) {
         return this.handle(var4);
      }
   }

   public FileVisitResult visitFile(Path var1, BasicFileAttributes var2) {
      return this.pathVisitor.visitFile(var1, var2);
   }
}
