package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.IOCase;

public class NameFileFilter extends AbstractFileFilter implements Serializable {
   private static final long serialVersionUID = 176844364689077340L;
   private final String[] names;
   private final IOCase caseSensitivity;

   public NameFileFilter(List<String> var1) {
      this((List)var1, (IOCase)null);
   }

   public NameFileFilter(List<String> var1, IOCase var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The list of names must not be null");
      } else {
         this.names = (String[])var1.toArray(EMPTY_STRING_ARRAY);
         this.caseSensitivity = this.toIOCase(var2);
      }
   }

   public NameFileFilter(String var1) {
      this(var1, IOCase.SENSITIVE);
   }

   public NameFileFilter(String... var1) {
      this(var1, IOCase.SENSITIVE);
   }

   public NameFileFilter(String var1, IOCase var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The wildcard must not be null");
      } else {
         this.names = new String[]{var1};
         this.caseSensitivity = this.toIOCase(var2);
      }
   }

   public NameFileFilter(String[] var1, IOCase var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The array of names must not be null");
      } else {
         this.names = new String[var1.length];
         System.arraycopy(var1, 0, this.names, 0, var1.length);
         this.caseSensitivity = this.toIOCase(var2);
      }
   }

   public boolean accept(File var1) {
      return this.acceptBaseName(var1.getName());
   }

   public boolean accept(File var1, String var2) {
      return this.acceptBaseName(var2);
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      return toFileVisitResult(this.acceptBaseName(Objects.toString(var1.getFileName(), (String)null)), var1);
   }

   private boolean acceptBaseName(String var1) {
      String[] var2 = this.names;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (this.caseSensitivity.checkEquals(var1, var5)) {
            return true;
         }
      }

      return false;
   }

   private IOCase toIOCase(IOCase var1) {
      return var1 == null ? IOCase.SENSITIVE : var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("(");
      if (this.names != null) {
         for(int var2 = 0; var2 < this.names.length; ++var2) {
            if (var2 > 0) {
               var1.append(",");
            }

            var1.append(this.names[var2]);
         }
      }

      var1.append(")");
      return var1.toString();
   }
}
