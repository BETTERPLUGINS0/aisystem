package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public final class Defaults {
   private static final Double DOUBLE_DEFAULT = 0.0D;
   private static final Float FLOAT_DEFAULT = 0.0F;

   private Defaults() {
   }

   @CheckForNull
   public static <T> T defaultValue(Class<T> type) {
      Preconditions.checkNotNull(type);
      if (type.isPrimitive()) {
         if (type == Boolean.TYPE) {
            return Boolean.FALSE;
         }

         if (type == Character.TYPE) {
            return '\u0000';
         }

         if (type == Byte.TYPE) {
            return 0;
         }

         if (type == Short.TYPE) {
            return Short.valueOf((short)0);
         }

         if (type == Integer.TYPE) {
            return 0;
         }

         if (type == Long.TYPE) {
            return 0L;
         }

         if (type == Float.TYPE) {
            return FLOAT_DEFAULT;
         }

         if (type == Double.TYPE) {
            return DOUBLE_DEFAULT;
         }
      }

      return null;
   }
}
