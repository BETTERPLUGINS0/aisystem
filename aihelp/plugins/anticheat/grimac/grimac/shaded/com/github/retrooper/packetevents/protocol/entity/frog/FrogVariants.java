package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.frog;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class FrogVariants {
   private static final VersionedRegistry<FrogVariant> REGISTRY = new VersionedRegistry("frog_variant");
   public static final FrogVariant COLD = define("cold", "cold_frog");
   public static final FrogVariant TEMPERATE = define("temperate", "temperate_frog");
   public static final FrogVariant WARM = define("warm", "warm_frog");

   private FrogVariants() {
   }

   @ApiStatus.Internal
   public static FrogVariant define(String name, String texture) {
      ResourceLocation assetId = new ResourceLocation("entity/frog/" + texture);
      return (FrogVariant)REGISTRY.define(name, (data) -> {
         return new StaticFrogVariant(data, assetId);
      });
   }

   public static VersionedRegistry<FrogVariant> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
