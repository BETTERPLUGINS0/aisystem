package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class TrueFileFilter implements IOFileFilter, Serializable {
   private static final String TO_STRING;
   private static final long serialVersionUID = 8782512160909720199L;
   public static final IOFileFilter TRUE;
   public static final IOFileFilter INSTANCE;

   protected TrueFileFilter() {
   }

   public boolean accept(File var1) {
      return true;
   }

   public boolean accept(File var1, String var2) {
      return true;
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      return FileVisitResult.CONTINUE;
   }

   public IOFileFilter negate() {
      return FalseFileFilter.INSTANCE;
   }

   public IOFileFilter or(IOFileFilter var1) {
      return INSTANCE;
   }

   public IOFileFilter and(IOFileFilter var1) {
      return var1;
   }

   public String toString() {
      return TO_STRING;
   }

   static {
      TO_STRING = Boolean.TRUE.toString();
      TRUE = new TrueFileFilter();
      INSTANCE = TRUE;
   }
}
