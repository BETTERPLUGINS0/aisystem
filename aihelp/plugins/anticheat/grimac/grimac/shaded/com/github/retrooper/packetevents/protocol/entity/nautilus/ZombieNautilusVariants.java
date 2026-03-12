package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.nautilus;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ZombieNautilusVariants {
   private static final VersionedRegistry<ZombieNautilusVariant> REGISTRY = new VersionedRegistry("zombie_nautilus_variant");
   public static final ZombieNautilusVariant TEMPERATE;
   public static final ZombieNautilusVariant WARM;

   private ZombieNautilusVariants() {
   }

   @ApiStatus.Internal
   public static ZombieNautilusVariant define(String name, ZombieNautilusVariant.ModelType modelType, String texture) {
      ResourceLocation assetId = new ResourceLocation("entity/nautilus/" + texture);
      return (ZombieNautilusVariant)REGISTRY.define(name, (data) -> {
         return new StaticZombieNautilusVariant(data, modelType, assetId);
      });
   }

   public static VersionedRegistry<ZombieNautilusVariant> getRegistry() {
      return REGISTRY;
   }

   static {
      TEMPERATE = define("temperate", ZombieNautilusVariant.ModelType.NORMAL, "zombie_nautilus");
      WARM = define("warm", ZombieNautilusVariant.ModelType.WARM, "zombie_nautilus_coral");
      REGISTRY.unloadMappings();
   }
}
