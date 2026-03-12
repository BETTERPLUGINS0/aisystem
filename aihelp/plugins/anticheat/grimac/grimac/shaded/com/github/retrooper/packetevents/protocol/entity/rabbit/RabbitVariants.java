package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.rabbit;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class RabbitVariants {
   private static final VersionedRegistry<RabbitVariant> REGISTRY = new VersionedRegistry("rabbit_variant");
   public static final RabbitVariant BROWN = define("brown");
   public static final RabbitVariant WHITE = define("white");
   public static final RabbitVariant BLACK = define("black");
   public static final RabbitVariant WHITE_SPLOTCHED = define("white_splotched");
   public static final RabbitVariant GOLD = define("gold");
   public static final RabbitVariant SALT = define("salt");
   public static final RabbitVariant EVIL = define("evil");

   private RabbitVariants() {
   }

   @ApiStatus.Internal
   public static RabbitVariant define(String name) {
      return (RabbitVariant)REGISTRY.define(name, StaticRabbitVariant::new);
   }

   public static VersionedRegistry<RabbitVariant> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
