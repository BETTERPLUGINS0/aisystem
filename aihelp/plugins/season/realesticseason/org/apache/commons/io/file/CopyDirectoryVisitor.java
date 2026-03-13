package org.apache.commons.io.file;

import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;

public class CopyDirectoryVisitor extends CountingPathVisitor {
   private final CopyOption[] copyOptions;
   private final Path sourceDirectory;
   private final Path targetDirectory;

   public CopyDirectoryVisitor(Counters.PathCounters var1, Path var2, Path var3, CopyOption... var4) {
      super(var1);
      this.sourceDirectory = var2;
      this.targetDirectory = var3;
      this.copyOptions = var4 == null ? PathUtils.EMPTY_COPY_OPTIONS : (CopyOption[])var4.clone();
   }

   public CopyDirectoryVisitor(Counters.PathCounters var1, PathFilter var2, PathFilter var3, Path var4, Path var5, CopyOption... var6) {
      super(var1, var2, var3);
      this.sourceDirectory = var4;
      this.targetDirectory = var5;
      this.copyOptions = var6 == null ? PathUtils.EMPTY_COPY_OPTIONS : (CopyOption[])var6.clone();
   }

   protected void copy(Path var1, Path var2) {
      Files.copy(var1, var2, this.copyOptions);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!super.equals(var1)) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         CopyDirectoryVisitor var2 = (CopyDirectoryVisitor)var1;
         return Arrays.equals(this.copyOptions, var2.copyOptions) && Objects.equals(this.sourceDirectory, var2.sourceDirectory) && Objects.equals(this.targetDirectory, var2.targetDirectory);
      }
   }

   public CopyOption[] getCopyOptions() {
      return (CopyOption[])this.copyOptions.clone();
   }

   public Path getSourceDirectory() {
      return this.sourceDirectory;
   }

   public Path getTargetDirectory() {
      return this.targetDirectory;
   }

   public int hashCode() {
      boolean var1 = true;
      int var2 = super.hashCode();
      var2 = 31 * var2 + Arrays.hashCode(this.copyOptions);
      var2 = 31 * var2 + Objects.hash(new Object[]{this.sourceDirectory, this.targetDirectory});
      return var2;
   }

   public FileVisitResult preVisitDirectory(Path var1, BasicFileAttributes var2) {
      Path var3 = this.resolveRelativeAsString(var1);
      if (Files.notExists(var3, new LinkOption[0])) {
         Files.createDirectory(var3);
      }

      return super.preVisitDirectory(var1, var2);
   }

   private Path resolveRelativeAsString(Path var1) {
      return this.targetDirectory.resolve(this.sourceDirectory.relativize(var1).toString());
   }

   public FileVisitResult visitFile(Path var1, BasicFileAttributes var2) {
      Path var3 = this.resolveRelativeAsString(var1);
      this.copy(var1, var3);
      return super.visitFile(var3, var2);
   }
}
