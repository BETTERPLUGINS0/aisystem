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

   public static AccumulatorPathVisitor withBigIntegerCounters(PathFilter fileFilter, PathFilter dirFilter) {
      return new AccumulatorPathVisitor(Counters.bigIntegerPathCounters(), fileFilter, dirFilter);
   }

   public static AccumulatorPathVisitor withLongCounters() {
      return new AccumulatorPathVisitor(Counters.longPathCounters());
   }

   public static AccumulatorPathVisitor withLongCounters(PathFilter fileFilter, PathFilter dirFilter) {
      return new AccumulatorPathVisitor(Counters.longPathCounters(), fileFilter, dirFilter);
   }

   public AccumulatorPathVisitor() {
      super(Counters.noopPathCounters());
   }

   public AccumulatorPathVisitor(Counters.PathCounters pathCounter) {
      super(pathCounter);
   }

   public AccumulatorPathVisitor(Counters.PathCounters pathCounter, PathFilter fileFilter, PathFilter dirFilter) {
      super(pathCounter, fileFilter, dirFilter);
   }

   private void add(List<Path> list, Path dir) {
      list.add(dir.normalize());
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!super.equals(obj)) {
         return false;
      } else if (!(obj instanceof AccumulatorPathVisitor)) {
         return false;
      } else {
         AccumulatorPathVisitor other = (AccumulatorPathVisitor)obj;
         return Objects.equals(this.dirList, other.dirList) && Objects.equals(this.fileList, other.fileList);
      }
   }

   public List<Path> getDirList() {
      return this.dirList;
   }

   public List<Path> getFileList() {
      return this.fileList;
   }

   public int hashCode() {
      int prime = true;
      int result = super.hashCode();
      result = 31 * result + Objects.hash(new Object[]{this.dirList, this.fileList});
      return result;
   }

   public List<Path> relativizeDirectories(Path parent, boolean sort, Comparator<? super Path> comparator) {
      return PathUtils.relativize(this.getDirList(), parent, sort, comparator);
   }

   public List<Path> relativizeFiles(Path parent, boolean sort, Comparator<? super Path> comparator) {
      return PathUtils.relativize(this.getFileList(), parent, sort, comparator);
   }

   protected void updateDirCounter(Path dir, IOException exc) {
      super.updateDirCounter(dir, exc);
      this.add(this.dirList, dir);
   }

   protected void updateFileCounters(Path file, BasicFileAttributes attributes) {
      super.updateFileCounters(file, attributes);
      this.add(this.fileList, file);
   }
}
