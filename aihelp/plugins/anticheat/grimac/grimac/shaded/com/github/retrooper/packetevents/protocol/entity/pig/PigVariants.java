package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.pig;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class PigVariants {
   private static final VersionedRegistry<PigVariant> REGISTRY = new VersionedRegistry("pig_variant");
   public static final PigVariant COLD;
   public static final PigVariant TEMPERATE;
   public static final PigVariant WARM;

   private PigVariants() {
   }

   @ApiStatus.Internal
   public static PigVariant define(String name, PigVariant.ModelType modelType, String texture) {
      ResourceLocation assetId = new ResourceLocation("entity/pig/" + texture);
      return (PigVariant)REGISTRY.define(name, (data) -> {
         return new StaticPigVariant(data, modelType, assetId);
      });
   }

   public static VersionedRegistry<PigVariant> getRegistry() {
      return REGISTRY;
   }

   static {
      COLD = define("cold", PigVariant.ModelType.COLD, "cold_pig");
      TEMPERATE = define("temperate", PigVariant.ModelType.NORMAL, "temperate_pig");
      WARM = define("warm", PigVariant.ModelType.NORMAL, "warm_pig");
      REGISTRY.unloadMappings();
   }
}
