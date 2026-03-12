package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.parrot;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class ParrotVariants {
   private static final VersionedRegistry<ParrotVariant> REGISTRY = new VersionedRegistry("parrot_variant");
   public static final ParrotVariant RED_BLUE = define("red_blue");
   public static final ParrotVariant BLUE = define("blue");
   public static final ParrotVariant GREEN = define("green");
   public static final ParrotVariant YELLOW_BLUE = define("yellow_blue");
   public static final ParrotVariant GRAY = define("gray");

   private ParrotVariants() {
   }

   @ApiStatus.Internal
   public static ParrotVariant define(String name) {
      return (ParrotVariant)REGISTRY.define(name, StaticParrotVariant::new);
   }

   public static VersionedRegistry<ParrotVariant> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
