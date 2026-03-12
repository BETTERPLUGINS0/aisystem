package emanondev.itemedit.aliases;

import java.util.Collection;
import java.util.stream.Collectors;
import org.bukkit.Keyed;
import org.bukkit.Registry;

public class RegistryAliasSet<T extends Keyed> extends AliasSet<T> {
   private final Registry<T> registry;

   public RegistryAliasSet(String path, Registry<T> registry) {
      super(path);
      this.registry = registry;
   }

   public String getName(T type) {
      return (type.getKey().getNamespace().equals("minecraft") ? type.getKey().getKey() : type.getKey().toString()).replace(".", "_");
   }

   public Collection<T> getValues() {
      return (Collection)this.registry.stream().collect(Collectors.toList());
   }
}
