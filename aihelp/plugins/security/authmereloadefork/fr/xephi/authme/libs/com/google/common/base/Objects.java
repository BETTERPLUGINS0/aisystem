package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.util.Arrays;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Objects extends ExtraObjectsMethodsForWeb {
   private Objects() {
   }

   public static boolean equal(@CheckForNull Object a, @CheckForNull Object b) {
      return a == b || a != null && a.equals(b);
   }

   public static int hashCode(@CheckForNull Object... objects) {
      return Arrays.hashCode(objects);
   }
}
