package advancedplugins.pm2.cv.models.api.utils.archive;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;

public abstract class Archive<T, R> {
   protected final Map<String, T> registry = this.mapSupplier();
   protected T defaultItem;

   public void register(String var1, T var2) {
      this.registry.put(var1, var2);
   }

   public void registerAndDefault(String var1, T var2) {
      this.registry.put(var1, var2);
      this.defaultItem = var2;
   }

   public R get(String var1) {
      return !this.registry.containsKey(var1) ? this.getDefault() : this.convert(this.registry.get(var1));
   }

   public R getDefault() {
      return this.convert(this.defaultItem);
   }

   public Set<String> getKeys() {
      return this.registry.keySet();
   }

   protected abstract R convert(T var1);

   protected Map<String, T> mapSupplier() {
      return Maps.newConcurrentMap();
   }

   public Map<String, T> readOnly() {
      return new ConcurrentHashMap(this.registry);
   }

   @Generated
   public void setDefaultItem(T var1) {
      this.defaultItem = var1;
   }
}
