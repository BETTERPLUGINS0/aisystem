package advancedplugins.pm2.cv.api.registry;

import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import gnu.trove.map.hash.THashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class RegistryBase<T extends IDeyed> implements Registry<T> {
   protected final Map<String, T> entries = new THashMap();

   @Nullable
   public T get(@NotNull String var1) {
      return (IDeyed)this.entries.get(var1.toLowerCase());
   }

   @NotNull
   public Collection<T> getEntries() {
      return this.entries.values();
   }

   @NotNull
   public Collection<String> getIds() {
      return this.entries.keySet();
   }

   public void register(@NotNull T var1) {
      this.entries.put(var1.getId(), var1);
   }

   public void unregister(@NotNull String var1) {
      this.entries.remove(var1.toLowerCase());
   }

   @NotNull
   public Iterator<T> iterator() {
      return this.entries.values().iterator();
   }
}
