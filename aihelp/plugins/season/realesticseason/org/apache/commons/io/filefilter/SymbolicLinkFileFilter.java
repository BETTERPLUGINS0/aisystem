package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class SymbolicLinkFileFilter extends AbstractFileFilter implements Serializable {
   public static final SymbolicLinkFileFilter INSTANCE = new SymbolicLinkFileFilter();
   private static final long serialVersionUID = 1L;

   protected SymbolicLinkFileFilter() {
   }

   public boolean accept(File var1) {
      return var1.isFile();
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      return toFileVisitResult(Files.isSymbolicLink(var1), var1);
   }
}
