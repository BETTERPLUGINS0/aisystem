package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;

public class WildcardFileFilter extends AbstractFileFilter implements Serializable {
   private static final long serialVersionUID = -7426486598995782105L;
   private final String[] wildcards;
   private final IOCase caseSensitivity;

   public WildcardFileFilter(List<String> var1) {
      this(var1, IOCase.SENSITIVE);
   }

   public WildcardFileFilter(List<String> var1, IOCase var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The wildcard list must not be null");
      } else {
         this.wildcards = (String[])var1.toArray(EMPTY_STRING_ARRAY);
         this.caseSensitivity = var2 == null ? IOCase.SENSITIVE : var2;
      }
   }

   public WildcardFileFilter(String var1) {
      this(var1, IOCase.SENSITIVE);
   }

   public WildcardFileFilter(String... var1) {
      this(var1, IOCase.SENSITIVE);
   }

   public WildcardFileFilter(String var1, IOCase var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The wildcard must not be null");
      } else {
         this.wildcards = new String[]{var1};
         this.caseSensitivity = var2 == null ? IOCase.SENSITIVE : var2;
      }
   }

   public WildcardFileFilter(String[] var1, IOCase var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The wildcard array must not be null");
      } else {
         this.wildcards = new String[var1.length];
         System.arraycopy(var1, 0, this.wildcards, 0, var1.length);
         this.caseSensitivity = var2 == null ? IOCase.SENSITIVE : var2;
      }
   }

   public boolean accept(File var1) {
      return this.accept(var1.getName());
   }

   public boolean accept(File var1, String var2) {
      return this.accept(var2);
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      return toFileVisitResult(this.accept(Objects.toString(var1.getFileName(), (String)null)), var1);
   }

   private boolean accept(String var1) {
      String[] var2 = this.wildcards;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (FilenameUtils.wildcardMatch(var1, var5, this.caseSensitivity)) {
            return true;
         }
      }

      return false;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("(");

      for(int var2 = 0; var2 < this.wildcards.length; ++var2) {
         if (var2 > 0) {
            var1.append(",");
         }

         var1.append(this.wildcards[var2]);
      }

      var1.append(")");
      return var1.toString();
   }
}
