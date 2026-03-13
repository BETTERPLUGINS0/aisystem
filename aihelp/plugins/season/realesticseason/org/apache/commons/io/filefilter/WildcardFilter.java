package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.FilenameUtils;

/** @deprecated */
@Deprecated
public class WildcardFilter extends AbstractFileFilter implements Serializable {
   private static final long serialVersionUID = -5037645902506953517L;
   private final String[] wildcards;

   public WildcardFilter(List<String> var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("The wildcard list must not be null");
      } else {
         this.wildcards = (String[])var1.toArray(EMPTY_STRING_ARRAY);
      }
   }

   public WildcardFilter(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("The wildcard must not be null");
      } else {
         this.wildcards = new String[]{var1};
      }
   }

   public WildcardFilter(String... var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("The wildcard array must not be null");
      } else {
         this.wildcards = new String[var1.length];
         System.arraycopy(var1, 0, this.wildcards, 0, var1.length);
      }
   }

   public boolean accept(File var1) {
      if (var1.isDirectory()) {
         return false;
      } else {
         String[] var2 = this.wildcards;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (FilenameUtils.wildcardMatch(var1.getName(), var5)) {
               return true;
            }
         }

         return false;
      }
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      if (Files.isDirectory(var1, new LinkOption[0])) {
         return FileVisitResult.TERMINATE;
      } else {
         String[] var3 = this.wildcards;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (FilenameUtils.wildcardMatch(Objects.toString(var1.getFileName(), (String)null), var6)) {
               return FileVisitResult.CONTINUE;
            }
         }

         return FileVisitResult.TERMINATE;
      }
   }

   public boolean accept(File var1, String var2) {
      if (var1 != null && (new File(var1, var2)).isDirectory()) {
         return false;
      } else {
         String[] var3 = this.wildcards;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (FilenameUtils.wildcardMatch(var2, var6)) {
               return true;
            }
         }

         return false;
      }
   }
}
