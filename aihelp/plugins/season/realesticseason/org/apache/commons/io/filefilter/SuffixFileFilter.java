package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.IOCase;

public class SuffixFileFilter extends AbstractFileFilter implements Serializable {
   private static final long serialVersionUID = -3389157631240246157L;
   private final String[] suffixes;
   private final IOCase caseSensitivity;

   public SuffixFileFilter(List<String> var1) {
      this(var1, IOCase.SENSITIVE);
   }

   public SuffixFileFilter(List<String> var1, IOCase var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The list of suffixes must not be null");
      } else {
         this.suffixes = (String[])var1.toArray(EMPTY_STRING_ARRAY);
         this.caseSensitivity = var2 == null ? IOCase.SENSITIVE : var2;
      }
   }

   public SuffixFileFilter(String var1) {
      this(var1, IOCase.SENSITIVE);
   }

   public SuffixFileFilter(String... var1) {
      this(var1, IOCase.SENSITIVE);
   }

   public SuffixFileFilter(String var1, IOCase var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The suffix must not be null");
      } else {
         this.suffixes = new String[]{var1};
         this.caseSensitivity = var2 == null ? IOCase.SENSITIVE : var2;
      }
   }

   public SuffixFileFilter(String[] var1, IOCase var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The array of suffixes must not be null");
      } else {
         this.suffixes = new String[var1.length];
         System.arraycopy(var1, 0, this.suffixes, 0, var1.length);
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
      String[] var2 = this.suffixes;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (this.caseSensitivity.checkEndsWith(var1, var5)) {
            return true;
         }
      }

      return false;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("(");
      if (this.suffixes != null) {
         for(int var2 = 0; var2 < this.suffixes.length; ++var2) {
            if (var2 > 0) {
               var1.append(",");
            }

            var1.append(this.suffixes[var2]);
         }
      }

      var1.append(")");
      return var1.toString();
   }
}
