package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.mooshroom;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class MooshroomVariants {
   private static final VersionedRegistry<MooshroomVariant> REGISTRY = new VersionedRegistry("mooshroom_variant");
   public static final MooshroomVariant RED = define("red");
   public static final MooshroomVariant BROWN = define("brown");

   private MooshroomVariants() {
   }

   @ApiStatus.Internal
   public static MooshroomVariant define(String name) {
      return (MooshroomVariant)REGISTRY.define(name, StaticMooshroomVariant::new);
   }

   public static VersionedRegistry<MooshroomVariant> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
