package ac.grim.grimac.shaded.incendo.cloud.bukkit;

import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(
   status = Status.STABLE,
   since = "2.0.0"
)
public final class BukkitCommandMeta {
   public static final CloudKey<String> BUKKIT_DESCRIPTION = CloudKey.of("bukkit_description", String.class);

   private BukkitCommandMeta() {
   }
}
