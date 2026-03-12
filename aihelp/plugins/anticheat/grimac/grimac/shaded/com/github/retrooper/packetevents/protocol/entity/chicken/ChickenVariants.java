package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.chicken;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class ChickenVariants {
   private static final VersionedRegistry<ChickenVariant> REGISTRY = new VersionedRegistry("chicken_variant");
   public static final ChickenVariant COLD;
   public static final ChickenVariant TEMPERATE;
   public static final ChickenVariant WARM;

   private ChickenVariants() {
   }

   @ApiStatus.Internal
   public static ChickenVariant define(String name, ChickenVariant.ModelType modelType, String texture) {
      ResourceLocation assetId = new ResourceLocation("entity/chicken/" + texture);
      return (ChickenVariant)REGISTRY.define(name, (data) -> {
         return new StaticChickenVariant(data, modelType, assetId);
      });
   }

   public static VersionedRegistry<ChickenVariant> getRegistry() {
      return REGISTRY;
   }

   static {
      COLD = define("cold", ChickenVariant.ModelType.COLD, "cold_chicken");
      TEMPERATE = define("temperate", ChickenVariant.ModelType.NORMAL, "temperate_chicken");
      WARM = define("warm", ChickenVariant.ModelType.NORMAL, "warm_chicken");
      REGISTRY.unloadMappings();
   }
}
