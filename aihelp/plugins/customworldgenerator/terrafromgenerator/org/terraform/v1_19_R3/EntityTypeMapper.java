package org.terraform.v1_19_R3;

import java.util.HashMap;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.main.TerraformGeneratorPlugin;

public class EntityTypeMapper {
   private static final HashMap<EntityType, String> obsNames = new HashMap<EntityType, String>() {
      {
         this.put(EntityType.ALLAY, "b");
         this.put(EntityType.AREA_EFFECT_CLOUD, "c");
         this.put(EntityType.ARMOR_STAND, "d");
         this.put(EntityType.ARROW, "e");
         this.put(EntityType.AXOLOTL, "f");
         this.put(EntityType.BAT, "g");
         this.put(EntityType.BEE, "h");
         this.put(EntityType.BLAZE, "i");
         this.put(EntityType.BLOCK_DISPLAY, "j");
         this.put(EntityType.BOAT, "k");
         this.put(EntityType.CAMEL, "l");
         this.put(EntityType.CAT, "m");
         this.put(EntityType.CAVE_SPIDER, "n");
         this.put(EntityType.CHEST_BOAT, "o");
         this.put(EntityType.MINECART_CHEST, "p");
         this.put(EntityType.CHICKEN, "q");
         this.put(EntityType.COD, "r");
         this.put(EntityType.MINECART_COMMAND, "s");
         this.put(EntityType.COW, "t");
         this.put(EntityType.CREEPER, "u");
         this.put(EntityType.DOLPHIN, "v");
         this.put(EntityType.DONKEY, "w");
         this.put(EntityType.DRAGON_FIREBALL, "x");
         this.put(EntityType.DROWNED, "y");
         this.put(EntityType.EGG, "z");
         this.put(EntityType.ELDER_GUARDIAN, "A");
         this.put(EntityType.ENDER_CRYSTAL, "B");
         this.put(EntityType.ENDER_DRAGON, "C");
         this.put(EntityType.ENDER_PEARL, "D");
         this.put(EntityType.ENDERMAN, "E");
         this.put(EntityType.ENDERMITE, "F");
         this.put(EntityType.EVOKER, "G");
         this.put(EntityType.EVOKER_FANGS, "H");
         this.put(EntityType.THROWN_EXP_BOTTLE, "I");
         this.put(EntityType.EXPERIENCE_ORB, "J");
         this.put(EntityType.FALLING_BLOCK, "L");
         this.put(EntityType.FIREWORK, "M");
         this.put(EntityType.FOX, "N");
         this.put(EntityType.FROG, "O");
         this.put(EntityType.MINECART_FURNACE, "P");
         this.put(EntityType.GHAST, "Q");
         this.put(EntityType.GIANT, "R");
         this.put(EntityType.GLOW_ITEM_FRAME, "S");
         this.put(EntityType.GLOW_SQUID, "T");
         this.put(EntityType.GOAT, "U");
         this.put(EntityType.GUARDIAN, "V");
         this.put(EntityType.HOGLIN, "W");
         this.put(EntityType.MINECART_HOPPER, "X");
         this.put(EntityType.HORSE, "Y");
         this.put(EntityType.HUSK, "Z");
         this.put(EntityType.ILLUSIONER, "aa");
         this.put(EntityType.INTERACTION, "ab");
         this.put(EntityType.IRON_GOLEM, "ac");
         this.put(EntityType.DROPPED_ITEM, "ad");
         this.put(EntityType.ITEM_DISPLAY, "ae");
         this.put(EntityType.ITEM_FRAME, "af");
         this.put(EntityType.FIREBALL, "ag");
         this.put(EntityType.LEASH_HITCH, "ah");
         this.put(EntityType.LIGHTNING, "ai");
         this.put(EntityType.LLAMA, "aj");
         this.put(EntityType.LLAMA_SPIT, "ak");
         this.put(EntityType.MAGMA_CUBE, "al");
         this.put(EntityType.MARKER, "am");
         this.put(EntityType.MINECART, "an");
         this.put(EntityType.MUSHROOM_COW, "ao");
         this.put(EntityType.MULE, "ap");
         this.put(EntityType.OCELOT, "aq");
         this.put(EntityType.PAINTING, "ar");
         this.put(EntityType.PANDA, "as");
         this.put(EntityType.PARROT, "at");
         this.put(EntityType.PHANTOM, "au");
         this.put(EntityType.PIG, "av");
         this.put(EntityType.PIGLIN, "aw");
         this.put(EntityType.PIGLIN_BRUTE, "ax");
         this.put(EntityType.PILLAGER, "ay");
         this.put(EntityType.POLAR_BEAR, "az");
         this.put(EntityType.SPLASH_POTION, "aA");
         this.put(EntityType.PUFFERFISH, "aB");
         this.put(EntityType.RABBIT, "aC");
         this.put(EntityType.RAVAGER, "aD");
         this.put(EntityType.SALMON, "aE");
         this.put(EntityType.SHEEP, "aF");
         this.put(EntityType.SHULKER, "aG");
         this.put(EntityType.SHULKER_BULLET, "aH");
         this.put(EntityType.SILVERFISH, "aI");
         this.put(EntityType.SKELETON, "aJ");
         this.put(EntityType.SKELETON_HORSE, "aK");
         this.put(EntityType.SLIME, "aL");
         this.put(EntityType.SMALL_FIREBALL, "aM");
         this.put(EntityType.SNIFFER, "aN");
         this.put(EntityType.SNOWMAN, "aO");
         this.put(EntityType.SNOWBALL, "aP");
         this.put(EntityType.MINECART_MOB_SPAWNER, "aQ");
         this.put(EntityType.SPECTRAL_ARROW, "aR");
         this.put(EntityType.SPIDER, "aS");
         this.put(EntityType.SQUID, "aT");
         this.put(EntityType.STRAY, "aU");
         this.put(EntityType.STRIDER, "aV");
         this.put(EntityType.TADPOLE, "aW");
         this.put(EntityType.TEXT_DISPLAY, "aX");
         this.put(EntityType.PRIMED_TNT, "aY");
         this.put(EntityType.MINECART_TNT, "aZ");
         this.put(EntityType.TRADER_LLAMA, "ba");
         this.put(EntityType.TRIDENT, "bb");
         this.put(EntityType.TROPICAL_FISH, "bc");
         this.put(EntityType.TURTLE, "bd");
         this.put(EntityType.VEX, "be");
         this.put(EntityType.VILLAGER, "bf");
         this.put(EntityType.VINDICATOR, "bg");
         this.put(EntityType.WANDERING_TRADER, "bh");
         this.put(EntityType.WARDEN, "bi");
         this.put(EntityType.WITCH, "bj");
         this.put(EntityType.WITHER, "bk");
         this.put(EntityType.WITHER_SKELETON, "bl");
         this.put(EntityType.WITHER_SKULL, "bm");
         this.put(EntityType.WOLF, "bn");
         this.put(EntityType.ZOGLIN, "bo");
         this.put(EntityType.ZOMBIE, "bp");
         this.put(EntityType.ZOMBIE_HORSE, "bq");
         this.put(EntityType.ZOMBIE_VILLAGER, "br");
         this.put(EntityType.ZOMBIFIED_PIGLIN, "bs");
         this.put(EntityType.PLAYER, "bt");
         this.put(EntityType.FISHING_HOOK, "bu");
      }
   };

   @NotNull
   public static String getObfsNameFromBukkitEntityType(@NotNull EntityType e) {
      String name = (String)obsNames.get(e);
      if (name == null) {
         TerraformGeneratorPlugin.logger.error("INVALID ENTITY REQUESTED: " + String.valueOf(e));
         return "";
      } else {
         return name;
      }
   }
}
