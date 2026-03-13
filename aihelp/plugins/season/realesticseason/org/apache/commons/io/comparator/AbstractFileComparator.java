package org.apache.commons.io.comparator;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

abstract class AbstractFileComparator implements Comparator<File> {
   public File[] sort(File... var1) {
      if (var1 != null) {
         Arrays.sort(var1, this);
      }

      return var1;
   }

   public List<File> sort(List<File> var1) {
      if (var1 != null) {
         var1.sort(this);
      }

      return var1;
   }

   public String toString() {
      return this.getClass().getSimpleName();
   }
}
