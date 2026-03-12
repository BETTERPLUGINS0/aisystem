package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.banner;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Collection;

public final class BannerPatterns {
   private static final VersionedRegistry<BannerPattern> REGISTRY = new VersionedRegistry("banner_pattern");
   public static final BannerPattern SQUARE_BOTTOM_LEFT = define("square_bottom_left");
   public static final BannerPattern STRIPE_BOTTOM = define("stripe_bottom");
   public static final BannerPattern CREEPER = define("creeper");
   public static final BannerPattern HALF_HORIZONTAL = define("half_horizontal");
   public static final BannerPattern STRIPE_MIDDLE = define("stripe_middle");
   public static final BannerPattern BASE = define("base");
   public static final BannerPattern DIAGONAL_UP_RIGHT = define("diagonal_up_right");
   public static final BannerPattern HALF_HORIZONTAL_BOTTOM = define("half_horizontal_bottom");
   public static final BannerPattern SMALL_STRIPES = define("small_stripes");
   public static final BannerPattern GRADIENT_UP = define("gradient_up");
   public static final BannerPattern CIRCLE = define("circle");
   public static final BannerPattern STRIPE_DOWNLEFT = define("stripe_downleft");
   public static final BannerPattern RHOMBUS = define("rhombus");
   public static final BannerPattern TRIANGLES_BOTTOM = define("triangles_bottom");
   public static final BannerPattern STRIPE_CENTER = define("stripe_center");
   public static final BannerPattern SQUARE_BOTTOM_RIGHT = define("square_bottom_right");
   public static final BannerPattern DIAGONAL_RIGHT = define("diagonal_right");
   public static final BannerPattern MOJANG = define("mojang");
   public static final BannerPattern STRIPE_LEFT = define("stripe_left");
   public static final BannerPattern SQUARE_TOP_LEFT = define("square_top_left");
   public static final BannerPattern TRIANGLE_BOTTOM = define("triangle_bottom");
   public static final BannerPattern SKULL = define("skull");
   public static final BannerPattern SQUARE_TOP_RIGHT = define("square_top_right");
   public static final BannerPattern GLOBE = define("globe");
   public static final BannerPattern STRIPE_TOP = define("stripe_top");
   public static final BannerPattern CROSS = define("cross");
   public static final BannerPattern BRICKS = define("bricks");
   public static final BannerPattern HALF_VERTICAL = define("half_vertical");
   public static final BannerPattern STRIPE_DOWNRIGHT = define("stripe_downright");
   public static final BannerPattern TRIANGLES_TOP = define("triangles_top");
   public static final BannerPattern STRIPE_RIGHT = define("stripe_right");
   public static final BannerPattern DIAGONAL_UP_LEFT = define("diagonal_up_left");
   public static final BannerPattern HALF_VERTICAL_RIGHT = define("half_vertical_right");
   public static final BannerPattern TRIANGLE_TOP = define("triangle_top");
   public static final BannerPattern FLOWER = define("flower");
   public static final BannerPattern STRAIGHT_CROSS = define("straight_cross");
   public static final BannerPattern GRADIENT = define("gradient");
   public static final BannerPattern CURLY_BORDER = define("curly_border");
   public static final BannerPattern BORDER = define("border");
   public static final BannerPattern PIGLIN = define("piglin");
   public static final BannerPattern DIAGONAL_LEFT = define("diagonal_left");
   public static final BannerPattern FLOW = define("flow");
   public static final BannerPattern GUSTER = define("guster");

   private BannerPatterns() {
   }

   @ApiStatus.Internal
   public static BannerPattern define(String key) {
      ResourceLocation assetId = ResourceLocation.minecraft(key);
      String translationKey = "block.minecraft.banner." + key;
      return define(key, assetId, translationKey);
   }

   @ApiStatus.Internal
   public static BannerPattern define(String key, ResourceLocation assetId, String translationKey) {
      return (BannerPattern)REGISTRY.define(key, (data) -> {
         return new StaticBannerPattern(data, assetId, translationKey);
      });
   }

   public static VersionedRegistry<BannerPattern> getRegistry() {
      return REGISTRY;
   }

   public static BannerPattern getByName(String name) {
      return (BannerPattern)REGISTRY.getByName(name);
   }

   public static BannerPattern getById(ClientVersion version, int id) {
      return (BannerPattern)REGISTRY.getById(version, id);
   }

   public static Collection<BannerPattern> values() {
      return REGISTRY.getEntries();
   }

   static {
      REGISTRY.unloadMappings();
   }
}
