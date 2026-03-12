package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.armormaterial;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import java.util.HashMap;
import java.util.Map;

public final class ArmorMaterials {
   private static final Map<String, String> DFU = new HashMap();
   private static final VersionedRegistry<ArmorMaterial> REGISTRY;
   public static final ArmorMaterial LEATHER;
   public static final ArmorMaterial CHAINMAIL;
   public static final ArmorMaterial IRON;
   public static final ArmorMaterial GOLD;
   public static final ArmorMaterial DIAMOND;
   public static final ArmorMaterial TURTLE_SCUTE;
   /** @deprecated */
   @Deprecated
   public static final ArmorMaterial TURTLE;
   public static final ArmorMaterial NETHERITE;
   public static final ArmorMaterial ARMADILLO_SCUTE;
   /** @deprecated */
   @Deprecated
   public static final ArmorMaterial ARMADILLO;
   public static final ArmorMaterial ELYTRA;
   public static final ArmorMaterial WHITE_CARPET;
   public static final ArmorMaterial ORANGE_CARPET;
   public static final ArmorMaterial MAGENTA_CARPET;
   public static final ArmorMaterial LIGHT_BLUE_CARPET;
   public static final ArmorMaterial YELLOW_CARPET;
   public static final ArmorMaterial LIME_CARPET;
   public static final ArmorMaterial PINK_CARPET;
   public static final ArmorMaterial GRAY_CARPET;
   public static final ArmorMaterial LIGHT_GRAY_CARPET;
   public static final ArmorMaterial CYAN_CARPET;
   public static final ArmorMaterial PURPLE_CARPET;
   public static final ArmorMaterial BLUE_CARPET;
   public static final ArmorMaterial BROWN_CARPET;
   public static final ArmorMaterial GREEN_CARPET;
   public static final ArmorMaterial RED_CARPET;
   public static final ArmorMaterial BLACK_CARPET;
   public static final ArmorMaterial TRADER_LLAMA;

   private ArmorMaterials() {
   }

   private static ArmorMaterial define(String name) {
      return (ArmorMaterial)REGISTRY.define(name, StaticArmorMaterial::new);
   }

   public static VersionedRegistry<ArmorMaterial> getRegistry() {
      return REGISTRY;
   }

   public static ArmorMaterial getByName(String name) {
      return (ArmorMaterial)REGISTRY.getByName((String)DFU.getOrDefault(name, name));
   }

   public static ArmorMaterial getById(ClientVersion version, int id) {
      return (ArmorMaterial)REGISTRY.getById(version, id);
   }

   static {
      DFU.put("turtle", "minecraft:turtle_scute");
      DFU.put("minecraft:turtle", "minecraft:turtle_scute");
      DFU.put("armadillo", "minecraft:armadillo_scute");
      DFU.put("minecraft:armadillo", "minecraft:armadillo_scute");
      REGISTRY = new VersionedRegistry("equipment_asset");
      LEATHER = define("leather");
      CHAINMAIL = define("chainmail");
      IRON = define("iron");
      GOLD = define("gold");
      DIAMOND = define("diamond");
      TURTLE_SCUTE = define("turtle_scute");
      TURTLE = TURTLE_SCUTE;
      NETHERITE = define("netherite");
      ARMADILLO_SCUTE = define("armadillo_scute");
      ARMADILLO = ARMADILLO_SCUTE;
      ELYTRA = define("elytra");
      WHITE_CARPET = define("white_carpet");
      ORANGE_CARPET = define("orange_carpet");
      MAGENTA_CARPET = define("magenta_carpet");
      LIGHT_BLUE_CARPET = define("light_blue_carpet");
      YELLOW_CARPET = define("yellow_carpet");
      LIME_CARPET = define("lime_carpet");
      PINK_CARPET = define("pink_carpet");
      GRAY_CARPET = define("gray_carpet");
      LIGHT_GRAY_CARPET = define("light_gray_carpet");
      CYAN_CARPET = define("cyan_carpet");
      PURPLE_CARPET = define("purple_carpet");
      BLUE_CARPET = define("blue_carpet");
      BROWN_CARPET = define("brown_carpet");
      GREEN_CARPET = define("green_carpet");
      RED_CARPET = define("red_carpet");
      BLACK_CARPET = define("black_carpet");
      TRADER_LLAMA = define("trader_llama");
      REGISTRY.unloadMappings();
   }
}
