package emanondev.itemtag.activity;

import emanondev.itemtag.ItemTag;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Manager<T extends Factory, S extends Factory.Element> {
   private final String name;
   private final HashMap<String, T> types = new HashMap();

   public Manager(String name) {
      this.name = name;
   }

   public void register(@NotNull T factory) {
      String id = factory.getId();
      if (this.types.containsKey(id)) {
         throw new IllegalArgumentException();
      } else {
         this.types.put(id, factory);
         ItemTag.get().log(this.name + "Manager registered " + this.name + " Type &e" + factory.getId());
      }
   }

   public void unregister(@NotNull T factory) {
      String id = factory.getId();
      this.types.remove(id);
      ItemTag.get().log(this.name + "Manager unregistered " + this.name + " Type &e" + factory.getId());
   }

   public abstract void load();

   @Nullable
   public T getType(@NotNull String id) {
      return (Factory)this.types.get(id.toLowerCase(Locale.ENGLISH));
   }

   @NotNull
   public Collection<String> getIds() {
      return Collections.unmodifiableSet(this.types.keySet());
   }
}
