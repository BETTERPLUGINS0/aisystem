package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;

public class DeletingPathVisitor extends CountingPathVisitor {
   private final String[] skip;
   private final boolean overrideReadOnly;
   private final LinkOption[] linkOptions;

   public static DeletingPathVisitor withBigIntegerCounters() {
      return new DeletingPathVisitor(Counters.bigIntegerPathCounters(), new String[0]);
   }

   public static DeletingPathVisitor withLongCounters() {
      return new DeletingPathVisitor(Counters.longPathCounters(), new String[0]);
   }

   public DeletingPathVisitor(Counters.PathCounters var1, DeleteOption[] var2, String... var3) {
      this(var1, PathUtils.NOFOLLOW_LINK_OPTION_ARRAY, var2, var3);
   }

   public DeletingPathVisitor(Counters.PathCounters var1, LinkOption[] var2, DeleteOption[] var3, String... var4) {
      super(var1);
      String[] var5 = var4 != null ? (String[])var4.clone() : EMPTY_STRING_ARRAY;
      Arrays.sort(var5);
      this.skip = var5;
      this.overrideReadOnly = StandardDeleteOption.overrideReadOnly(var3);
      this.linkOptions = var2 == null ? PathUtils.NOFOLLOW_LINK_OPTION_ARRAY : (LinkOption[])var2.clone();
   }

   public DeletingPathVisitor(Counters.PathCounters var1, String... var2) {
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
         DeletingPathVisitor var2 = (DeletingPathVisitor)var1;
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

   public FileVisitResult postVisitDirectory(Path var1, IOException var2) {
      if (PathUtils.isEmptyDirectory(var1)) {
         Files.deleteIfExists(var1);
      }

      return super.postVisitDirectory(var1, var2);
   }

   public FileVisitResult preVisitDirectory(Path var1, BasicFileAttributes var2) {
      super.preVisitDirectory(var1, var2);
      return this.accept(var1) ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
   }

   public FileVisitResult visitFile(Path var1, BasicFileAttributes var2) {
      if (this.accept(var1)) {
         if (Files.exists(var1, this.linkOptions)) {
            if (this.overrideReadOnly) {
               PathUtils.setReadOnly(var1, false, this.linkOptions);
            }

            Files.deleteIfExists(var1);
         }

         if (Files.isSymbolicLink(var1)) {
            try {
               Files.delete(var1);
            } catch (NoSuchFileException var4) {
            }
         }
      }

      this.updateFileCounters(var1, var2);
      return FileVisitResult.CONTINUE;
   }
}
