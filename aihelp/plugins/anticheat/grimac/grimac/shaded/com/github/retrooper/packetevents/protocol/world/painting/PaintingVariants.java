package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.painting;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public final class PaintingVariants {
   private static final VersionedRegistry<PaintingVariant> REGISTRY = new VersionedRegistry("painting_variant");
   public static final PaintingVariant POINTER = define("pointer", 4, 4);
   public static final PaintingVariant CREEBET = define("creebet", 2, 1);
   public static final PaintingVariant PRAIRIE_RIDE = define("prairie_ride", 1, 2);
   public static final PaintingVariant POOL = define("pool", 2, 1);
   public static final PaintingVariant EARTH = define("earth", 2, 2);
   public static final PaintingVariant SKELETON = define("skeleton", 4, 3);
   public static final PaintingVariant MATCH = define("match", 2, 2);
   public static final PaintingVariant POND = define("pond", 3, 4);
   public static final PaintingVariant HUMBLE = define("humble", 2, 2);
   public static final PaintingVariant PIGSCENE = define("pigscene", 4, 4);
   public static final PaintingVariant WATER = define("water", 2, 2);
   public static final PaintingVariant ALBAN = define("alban", 1, 1);
   public static final PaintingVariant FINDING = define("finding", 4, 2);
   public static final PaintingVariant AZTEC2 = define("aztec2", 1, 1);
   public static final PaintingVariant TIDES = define("tides", 3, 3);
   public static final PaintingVariant FIGHTERS = define("fighters", 4, 2);
   public static final PaintingVariant FIRE = define("fire", 2, 2);
   public static final PaintingVariant CHANGING = define("changing", 4, 2);
   public static final PaintingVariant BURNING_SKULL = define("burning_skull", 4, 4);
   public static final PaintingVariant COTAN = define("cotan", 3, 3);
   public static final PaintingVariant WANDERER = define("wanderer", 1, 2);
   public static final PaintingVariant UNPACKED = define("unpacked", 4, 4);
   public static final PaintingVariant SUNSET = define("sunset", 2, 1);
   public static final PaintingVariant FERN = define("fern", 3, 3);
   public static final PaintingVariant BUST = define("bust", 2, 2);
   public static final PaintingVariant WIND = define("wind", 2, 2);
   public static final PaintingVariant LOWMIST = define("lowmist", 4, 2);
   public static final PaintingVariant PASSAGE = define("passage", 4, 2);
   public static final PaintingVariant SUNFLOWERS = define("sunflowers", 3, 3);
   public static final PaintingVariant GRAHAM = define("graham", 1, 2);
   public static final PaintingVariant WASTELAND = define("wasteland", 1, 1);
   public static final PaintingVariant SKULL_AND_ROSES = define("skull_and_roses", 2, 2);
   public static final PaintingVariant BOUQUET = define("bouquet", 3, 3);
   public static final PaintingVariant ORB = define("orb", 4, 4);
   public static final PaintingVariant BOMB = define("bomb", 1, 1);
   public static final PaintingVariant WITHER = define("wither", 2, 2);
   public static final PaintingVariant BACKYARD = define("backyard", 3, 4);
   public static final PaintingVariant ENDBOSS = define("endboss", 3, 3);
   public static final PaintingVariant MEDITATIVE = define("meditative", 1, 1);
   public static final PaintingVariant VOID = define("void", 2, 2);
   public static final PaintingVariant KEBAB = define("kebab", 1, 1);
   public static final PaintingVariant SEA = define("sea", 2, 1);
   public static final PaintingVariant DONKEY_KONG = define("donkey_kong", 4, 3);
   public static final PaintingVariant BAROQUE = define("baroque", 2, 2);
   public static final PaintingVariant STAGE = define("stage", 2, 2);
   public static final PaintingVariant AZTEC = define("aztec", 1, 1);
   public static final PaintingVariant PLANT = define("plant", 1, 1);
   public static final PaintingVariant CAVEBIRD = define("cavebird", 3, 3);
   public static final PaintingVariant COURBET = define("courbet", 2, 1);
   public static final PaintingVariant OWLEMONS = define("owlemons", 3, 3);
   public static final PaintingVariant DENNIS = define("dennis", 3, 3);

   private PaintingVariants() {
   }

   @ApiStatus.Internal
   public static PaintingVariant define(String key, int width, int height) {
      ResourceLocation assetId = ResourceLocation.minecraft(key);
      return define(key, width, height, assetId);
   }

   @ApiStatus.Internal
   public static PaintingVariant define(String key, int width, int height, ResourceLocation assetId) {
      return (PaintingVariant)REGISTRY.define(key, (data) -> {
         return new StaticPaintingVariant(data, width, height, assetId);
      });
   }

   public static VersionedRegistry<PaintingVariant> getRegistry() {
      return REGISTRY;
   }

   @Nullable
   public static PaintingVariant getByName(String name) {
      return (PaintingVariant)REGISTRY.getByName(name);
   }

   @Nullable
   public static PaintingVariant getById(ClientVersion version, int id) {
      return (PaintingVariant)REGISTRY.getById(version, id);
   }

   static {
      REGISTRY.unloadMappings();
   }
}
