package org.apache.commons.lang3.compare;

import java.io.Serializable;
import java.util.Comparator;

public final class ObjectToStringComparator implements Comparator<Object>, Serializable {
   public static final ObjectToStringComparator INSTANCE = new ObjectToStringComparator();
   private static final long serialVersionUID = 1L;

   public int compare(Object var1, Object var2) {
      if (var1 == null && var2 == null) {
         return 0;
      } else if (var1 == null) {
         return 1;
      } else if (var2 == null) {
         return -1;
      } else {
         String var3 = var1.toString();
         String var4 = var2.toString();
         if (var3 == null && var4 == null) {
            return 0;
         } else if (var3 == null) {
            return 1;
         } else {
            return var4 == null ? -1 : var3.compareTo(var4);
         }
      }
   }
}
