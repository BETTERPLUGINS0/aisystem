package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class FalseFileFilter implements IOFileFilter, Serializable {
   private static final String TO_STRING;
   public static final IOFileFilter FALSE;
   public static final IOFileFilter INSTANCE;
   private static final long serialVersionUID = 6210271677940926200L;

   protected FalseFileFilter() {
   }

   public boolean accept(File var1) {
      return false;
   }

   public boolean accept(File var1, String var2) {
      return false;
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      return FileVisitResult.TERMINATE;
   }

   public IOFileFilter negate() {
      return TrueFileFilter.INSTANCE;
   }

   public String toString() {
      return TO_STRING;
   }

   public IOFileFilter and(IOFileFilter var1) {
      return INSTANCE;
   }

   public IOFileFilter or(IOFileFilter var1) {
      return var1;
   }

   static {
      TO_STRING = Boolean.FALSE.toString();
      FALSE = new FalseFileFilter();
      INSTANCE = FALSE;
   }
}
