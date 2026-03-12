package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.horse;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class HorseVariants {
   private static final VersionedRegistry<HorseVariant> REGISTRY = new VersionedRegistry("horse_variant");
   public static final HorseVariant WHITE = define("white");
   public static final HorseVariant CREAMY = define("creamy");
   public static final HorseVariant CHESTNUT = define("chestnut");
   public static final HorseVariant BROWN = define("brown");
   public static final HorseVariant BLACK = define("black");
   public static final HorseVariant GRAY = define("gray");
   public static final HorseVariant DARK_BROWN = define("dark_brown");

   private HorseVariants() {
   }

   @ApiStatus.Internal
   public static HorseVariant define(String name) {
      return (HorseVariant)REGISTRY.define(name, StaticHorseVariant::new);
   }

   public static VersionedRegistry<HorseVariant> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
