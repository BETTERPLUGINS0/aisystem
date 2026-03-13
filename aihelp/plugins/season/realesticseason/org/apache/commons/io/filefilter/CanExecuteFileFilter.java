package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class CanExecuteFileFilter extends AbstractFileFilter implements Serializable {
   public static final IOFileFilter CAN_EXECUTE = new CanExecuteFileFilter();
   public static final IOFileFilter CANNOT_EXECUTE;
   private static final long serialVersionUID = 3179904805251622989L;

   protected CanExecuteFileFilter() {
   }

   public boolean accept(File var1) {
      return var1.canExecute();
   }

   public FileVisitResult accept(Path var1, BasicFileAttributes var2) {
      return toFileVisitResult(Files.isExecutable(var1), var1);
   }

   static {
      CANNOT_EXECUTE = CAN_EXECUTE.negate();
   }
}
