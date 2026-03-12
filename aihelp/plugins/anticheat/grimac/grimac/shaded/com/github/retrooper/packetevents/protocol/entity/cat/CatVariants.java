package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class CatVariants {
   private static final VersionedRegistry<CatVariant> REGISTRY = new VersionedRegistry("cat_variant");
   public static final CatVariant ALL_BLACK = define("all_black");
   public static final CatVariant BLACK = define("black");
   public static final CatVariant BRITISH_SHORTHAIR = define("british_shorthair");
   public static final CatVariant CALICO = define("calico");
   public static final CatVariant JELLIE = define("jellie");
   public static final CatVariant PERSIAN = define("persian");
   public static final CatVariant RAGDOLL = define("ragdoll");
   public static final CatVariant RED = define("red");
   public static final CatVariant SIAMESE = define("siamese");
   public static final CatVariant TABBY = define("tabby");
   public static final CatVariant WHITE = define("white");

   private CatVariants() {
   }

   @ApiStatus.Internal
   public static CatVariant define(String name) {
      ResourceLocation assetId = new ResourceLocation("entity/cat/" + name);
      return (CatVariant)REGISTRY.define(name, (data) -> {
         return new StaticCatVariant(data, assetId);
      });
   }

   public static VersionedRegistry<CatVariant> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
