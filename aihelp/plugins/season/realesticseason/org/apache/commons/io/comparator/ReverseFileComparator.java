package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

class ReverseFileComparator extends AbstractFileComparator implements Serializable {
   private static final long serialVersionUID = -4808255005272229056L;
   private final Comparator<File> delegate;

   public ReverseFileComparator(Comparator<File> var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Delegate comparator is missing");
      } else {
         this.delegate = var1;
      }
   }

   public int compare(File var1, File var2) {
      return this.delegate.compare(var2, var1);
   }

   public String toString() {
      return super.toString() + "[" + this.delegate.toString() + "]";
   }
}
