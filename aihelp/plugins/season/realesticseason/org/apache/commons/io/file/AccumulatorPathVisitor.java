package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AccumulatorPathVisitor extends CountingPathVisitor {
   private final List<Path> dirList = new ArrayList();
   private final List<Path> fileList = new ArrayList();

   public static AccumulatorPathVisitor withBigIntegerCounters() {
      return new AccumulatorPathVisitor(Counters.bigIntegerPathCounters());
   }

   public static AccumulatorPathVisitor withBigIntegerCounters(PathFilter var0, PathFilter var1) {
      return new AccumulatorPathVisitor(Counters.bigIntegerPathCounters(), var0, var1);
   }

   public static AccumulatorPathVisitor withLongCounters() {
      return new AccumulatorPathVisitor(Counters.longPathCounters());
   }

   public static AccumulatorPathVisitor withLongCounters(PathFilter var0, PathFilter var1) {
      return new AccumulatorPathVisitor(Counters.longPathCounters(), var0, var1);
   }

   public AccumulatorPathVisitor() {
      super(Counters.noopPathCounters());
   }

   public AccumulatorPathVisitor(Counters.PathCounters var1) {
      super(var1);
   }

   public AccumulatorPathVisitor(Counters.PathCounters var1, PathFilter var2, PathFilter var3) {
      super(var1, var2, var3);
   }

   private void add(List<Path> var1, Path var2) {
      var1.add(var2.normalize());
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!super.equals(var1)) {
         return false;
      } else if (!(var1 instanceof AccumulatorPathVisitor)) {
         return false;
      } else {
         AccumulatorPathVisitor var2 = (AccumulatorPathVisitor)var1;
         return Objects.equals(this.dirList, var2.dirList) && Objects.equals(this.fileList, var2.fileList);
      }
   }

   public List<Path> getDirList() {
      return this.dirList;
   }

   public List<Path> getFileList() {
      return this.fileList;
   }

   public int hashCode() {
      boolean var1 = true;
      int var2 = super.hashCode();
      var2 = 31 * var2 + Objects.hash(new Object[]{this.dirList, this.fileList});
      return var2;
   }

   public List<Path> relativizeDirectories(Path var1, boolean var2, Comparator<? super Path> var3) {
      return PathUtils.relativize(this.getDirList(), var1, var2, var3);
   }

   public List<Path> relativizeFiles(Path var1, boolean var2, Comparator<? super Path> var3) {
      return PathUtils.relativize(this.getFileList(), var1, var2, var3);
   }

   protected void updateDirCounter(Path var1, IOException var2) {
      super.updateDirCounter(var1, var2);
      this.add(this.dirList, var1);
   }

   protected void updateFileCounters(Path var1, BasicFileAttributes var2) {
      super.updateFileCounters(var1, var2);
      this.add(this.fileList, var1);
   }
}
