package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class CountingPathVisitor extends SimplePathVisitor {
   static final String[] EMPTY_STRING_ARRAY = new String[0];
   private final Counters.PathCounters pathCounters;
   private final PathFilter fileFilter;
   private final PathFilter dirFilter;

   public static CountingPathVisitor withBigIntegerCounters() {
      return new CountingPathVisitor(Counters.bigIntegerPathCounters());
   }

   public static CountingPathVisitor withLongCounters() {
      return new CountingPathVisitor(Counters.longPathCounters());
   }

   public CountingPathVisitor(Counters.PathCounters var1) {
      this(var1, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
   }

   public CountingPathVisitor(Counters.PathCounters var1, PathFilter var2, PathFilter var3) {
      this.pathCounters = (Counters.PathCounters)Objects.requireNonNull(var1, "pathCounter");
      this.fileFilter = (PathFilter)Objects.requireNonNull(var2, "fileFilter");
      this.dirFilter = (PathFilter)Objects.requireNonNull(var3, "dirFilter");
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof CountingPathVisitor)) {
         return false;
      } else {
         CountingPathVisitor var2 = (CountingPathVisitor)var1;
         return Objects.equals(this.pathCounters, var2.pathCounters);
      }
   }

   public Counters.PathCounters getPathCounters() {
      return this.pathCounters;
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.pathCounters});
   }

   public FileVisitResult postVisitDirectory(Path var1, IOException var2) {
      this.updateDirCounter(var1, var2);
      return FileVisitResult.CONTINUE;
   }

   public FileVisitResult preVisitDirectory(Path var1, BasicFileAttributes var2) {
      FileVisitResult var3 = this.dirFilter.accept(var1, var2);
      return var3 != FileVisitResult.CONTINUE ? FileVisitResult.SKIP_SUBTREE : FileVisitResult.CONTINUE;
   }

   public String toString() {
      return this.pathCounters.toString();
   }

   protected void updateDirCounter(Path var1, IOException var2) {
      this.pathCounters.getDirectoryCounter().increment();
   }

   protected void updateFileCounters(Path var1, BasicFileAttributes var2) {
      this.pathCounters.getFileCounter().increment();
      this.pathCounters.getByteCounter().add(var2.size());
   }

   public FileVisitResult visitFile(Path var1, BasicFileAttributes var2) {
      if (Files.exists(var1, new LinkOption[0]) && this.fileFilter.accept(var1, var2) == FileVisitResult.CONTINUE) {
         this.updateFileCounters(var1, var2);
      }

      return FileVisitResult.CONTINUE;
   }
}
