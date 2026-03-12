package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.fox;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class FoxVariants {
   private static final VersionedRegistry<FoxVariant> REGISTRY = new VersionedRegistry("fox_variant");
   public static final FoxVariant RED = define("red");
   public static final FoxVariant SNOW = define("snow");

   private FoxVariants() {
   }

   @ApiStatus.Internal
   public static FoxVariant define(String name) {
      return (FoxVariant)REGISTRY.define(name, StaticFoxVariant::new);
   }

   public static VersionedRegistry<FoxVariant> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
