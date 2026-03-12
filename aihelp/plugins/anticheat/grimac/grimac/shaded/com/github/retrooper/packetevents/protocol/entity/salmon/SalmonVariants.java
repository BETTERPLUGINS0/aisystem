package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.salmon;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class SalmonVariants {
   private static final VersionedRegistry<SalmonVariant> REGISTRY = new VersionedRegistry("salmon_variant");
   public static final SalmonVariant SMALL = define("small");
   public static final SalmonVariant MEDIUM = define("medium");
   public static final SalmonVariant LARGE = define("large");

   private SalmonVariants() {
   }

   @ApiStatus.Internal
   public static SalmonVariant define(String name) {
      return (SalmonVariant)REGISTRY.define(name, StaticSalmonVariant::new);
   }

   public static VersionedRegistry<SalmonVariant> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
