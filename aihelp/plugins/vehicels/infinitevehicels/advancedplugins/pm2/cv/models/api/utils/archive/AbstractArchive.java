package advancedplugins.pm2.cv.models.api.utils.archive;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractArchive<T> extends Archive<T, T> {
   public T get(String var1) {
      return var1 != null && this.registry.containsKey(var1) ? this.registry.get(var1) : this.getDefault();
   }

   public T getDefault() {
      return this.defaultItem;
   }

   public void clear() {
      this.registry.clear();
   }

   public boolean contains(String var1) {
      return this.registry.containsKey(var1);
   }

   public boolean isEmpty() {
      return this.registry.isEmpty();
   }

   public T remove(String var1) {
      return this.registry.remove(var1);
   }

   public List<T> getValues() {
      return new ArrayList(this.registry.values());
   }

   protected T convert(T var1) {
      throw new UnsupportedOperationException("The convert method should not be called in a singleton archive.");
   }
}
