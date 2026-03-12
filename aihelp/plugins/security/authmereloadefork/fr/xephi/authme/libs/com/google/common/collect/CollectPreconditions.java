package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class CollectPreconditions {
   static void checkEntryNotNull(Object key, Object value) {
      String var2;
      if (key == null) {
         var2 = String.valueOf(value);
         throw new NullPointerException((new StringBuilder(24 + String.valueOf(var2).length())).append("null key in entry: null=").append(var2).toString());
      } else if (value == null) {
         var2 = String.valueOf(key);
         throw new NullPointerException((new StringBuilder(26 + String.valueOf(var2).length())).append("null value in entry: ").append(var2).append("=null").toString());
      }
   }

   @CanIgnoreReturnValue
   static int checkNonnegative(int value, String name) {
      if (value < 0) {
         throw new IllegalArgumentException((new StringBuilder(40 + String.valueOf(name).length())).append(name).append(" cannot be negative but was: ").append(value).toString());
      } else {
         return value;
      }
   }

   @CanIgnoreReturnValue
   static long checkNonnegative(long value, String name) {
      if (value < 0L) {
         throw new IllegalArgumentException((new StringBuilder(49 + String.valueOf(name).length())).append(name).append(" cannot be negative but was: ").append(value).toString());
      } else {
         return value;
      }
   }

   static void checkPositive(int value, String name) {
      if (value <= 0) {
         throw new IllegalArgumentException((new StringBuilder(38 + String.valueOf(name).length())).append(name).append(" must be positive but was: ").append(value).toString());
      }
   }

   static void checkRemove(boolean canRemove) {
      Preconditions.checkState(canRemove, "no calls to next() since the last call to remove()");
   }
}
