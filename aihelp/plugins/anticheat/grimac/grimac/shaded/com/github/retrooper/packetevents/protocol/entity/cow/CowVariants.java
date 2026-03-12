package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cow;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CowVariants {
   private static final VersionedRegistry<CowVariant> REGISTRY = new VersionedRegistry("cow_variant");
   public static final CowVariant COLD;
   public static final CowVariant TEMPERATE;
   public static final CowVariant WARM;

   private CowVariants() {
   }

   @ApiStatus.Internal
   public static CowVariant define(String name, CowVariant.ModelType modelType, String texture) {
      ResourceLocation assetId = new ResourceLocation("entity/cow/" + texture);
      return (CowVariant)REGISTRY.define(name, (data) -> {
         return new StaticCowVariant(data, modelType, assetId);
      });
   }

   public static VersionedRegistry<CowVariant> getRegistry() {
      return REGISTRY;
   }

   static {
      COLD = define("cold", CowVariant.ModelType.COLD, "cold_cow");
      TEMPERATE = define("temperate", CowVariant.ModelType.NORMAL, "temperate_cow");
      WARM = define("warm", CowVariant.ModelType.WARM, "warm_cow");
      REGISTRY.unloadMappings();
   }
}
