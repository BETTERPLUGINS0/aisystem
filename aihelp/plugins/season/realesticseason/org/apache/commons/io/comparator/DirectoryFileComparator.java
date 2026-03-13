package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

public class DirectoryFileComparator extends AbstractFileComparator implements Serializable {
   private static final int TYPE_FILE = 2;
   private static final int TYPE_DIRECTORY = 1;
   private static final long serialVersionUID = 296132640160964395L;
   public static final Comparator<File> DIRECTORY_COMPARATOR = new DirectoryFileComparator();
   public static final Comparator<File> DIRECTORY_REVERSE;

   public int compare(File var1, File var2) {
      return this.getType(var1) - this.getType(var2);
   }

   private int getType(File var1) {
      return var1.isDirectory() ? 1 : 2;
   }

   static {
      DIRECTORY_REVERSE = new ReverseFileComparator(DIRECTORY_COMPARATOR);
   }
}
