package advancedplugins.pm2.cv.models.api.utils.data;

import java.util.LinkedHashSet;

public class NullableHashSet<T> extends LinkedHashSet<T> {
   public boolean add(T var1) {
      return var1 == null ? false : super.add(var1);
   }
}
