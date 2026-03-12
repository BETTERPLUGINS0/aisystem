package emanondev.itemedit.aliases;

import emanondev.itemedit.utility.TagContainer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;

public class TagAliasSet<T extends Keyed> extends AliasSet<TagContainer<T>> {
   private final Class<T> clazz;
   private final String registry;

   public TagAliasSet(@NotNull String path, @NotNull Class<T> clazz, @NotNull String registry) {
      super(path);
      this.clazz = clazz;
      this.registry = registry;
   }

   public String getPathName(TagContainer<T> value) {
      return (value.getTag().getKey().getNamespace().equals("minecraft") ? value.getTag().getKey().getKey() : value.getTag().getKey().toString()).replace(".", "_");
   }

   public String getDefaultName(TagContainer<T> value) {
      return value.getTag().getKey().getKey();
   }

   /** @deprecated */
   @Deprecated
   public String getName(TagContainer<T> value) {
      return (value.getTag().getKey().getNamespace().equals("minecraft") ? value.getTag().getKey().getKey() : value.getTag().getKey().toString()).replace(".", "_");
   }

   public Collection<TagContainer<T>> getValues() {
      Set<TagContainer<T>> set = new HashSet();
      Iterator var2 = Bukkit.getTags(this.registry, this.clazz).iterator();

      while(var2.hasNext()) {
         Tag<T> tag = (Tag)var2.next();
         set.add(new TagContainer(tag));
      }

      return set;
   }

   public Class<T> getClazz() {
      return this.clazz;
   }

   public String getRegistry() {
      return this.registry;
   }
}
