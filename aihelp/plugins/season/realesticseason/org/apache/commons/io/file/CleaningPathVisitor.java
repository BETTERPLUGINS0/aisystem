package org.apache.commons.io.file;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;

public class CleaningPathVisitor extends CountingPathVisitor {
   private final String[] skip;
   private final boolean overrideReadOnly;

   public static CountingPathVisitor withBigIntegerCounters() {
      return new CleaningPathVisitor(Counters.bigIntegerPathCounters(), new String[0]);
   }

   public static CountingPathVisitor withLongCounters() {
      return new CleaningPathVisitor(Counters.longPathCounters(), new String[0]);
   }

   public CleaningPathVisitor(Counters.PathCounters var1, DeleteOption[] var2, String... var3) {
      super(var1);
      String[] var4 = var3 != null ? (String[])var3.clone() : EMPTY_STRING_ARRAY;
      Arrays.sort(var4);
      this.skip = var4;
      this.overrideReadOnly = StandardDeleteOption.overrideReadOnly(var2);
   }

   public CleaningPathVisitor(Counters.PathCounters var1, String... var2) {
      this(var1, PathUtils.EMPTY_DELETE_OPTION_ARRAY, var2);
   }

   private boolean accept(Path var1) {
      return Arrays.binarySearch(this.skip, Objects.toString(var1.getFileName(), (String)null)) < 0;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!super.equals(var1)) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         CleaningPathVisitor var2 = (CleaningPathVisitor)var1;
         return this.overrideReadOnly == var2.overrideReadOnly && Arrays.equals(this.skip, var2.skip);
      }
   }

   public int hashCode() {
      boolean var1 = true;
      int var2 = super.hashCode();
      var2 = 31 * var2 + Arrays.hashCode(this.skip);
      var2 = 31 * var2 + Objects.hash(new Object[]{this.overrideReadOnly});
      return var2;
   }

   public FileVisitResult preVisitDirectory(Path var1, BasicFileAttributes var2) {
      super.preVisitDirectory(var1, var2);
      return this.accept(var1) ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
   }

   public FileVisitResult visitFile(Path var1, BasicFileAttributes var2) {
      if (this.accept(var1) && Files.exists(var1, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
         if (this.overrideReadOnly) {
            PathUtils.setReadOnly(var1, false, LinkOption.NOFOLLOW_LINKS);
         }

         Files.deleteIfExists(var1);
      }

      this.updateFileCounters(var1, var2);
      return FileVisitResult.CONTINUE;
   }
}
