package me.gypopo.economyshopgui.util;

import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;

public enum NBTMaps {
   MAX_STACK_SIZE((String)null, "max_stack_size"),
   MAX_DAMAGE((String)null, "max_damage"),
   DAMAGE("Damage", "damage"),
   UNBREAKABLE("Unbreakable", "unbreakable"),
   NAME("display::Name", "item_name"),
   DISPLAYNAME("display::Name", "custom_name"),
   LORE("display::Lore", "lore"),
   RARITY((String)null, "rarity"),
   ENCHANTMENTS("Enchantments", "enchantments"),
   CAN_PLACE_ON("CanPlaceOn", "can_place_on"),
   CAN_BREAK("CanDestroy", "can_break"),
   ATTRIBUTE_MODIFIERS("AttributeModifiers", "attribute_modifiers"),
   CUSTOM_MODEL_DATA("CustomModelData", "custom_model_data"),
   HIDE_ADDITIONAL_TOOLTIP((String)null, "hide_additional_tooltip"),
   HIDE_FLAGS("HideFlags", "hide_tooltip"),
   REPAIR_COST("RepairCost", "repair_cost"),
   CREATIVE_SLOT_LOCK((String)null, "creative_slot_lock"),
   ENCHANTMENT_GLINT((String)null, "enchantment_glint_override"),
   INTANGIBLE_PROJECTILE((String)null, "intangible_projectile"),
   FOOD((String)null, "food"),
   FIRE_RESISTANCE((String)null, "fire_resistant"),
   TOOL((String)null, "tool"),
   STORED_ENCHANTMENTS("StoredEnchantments", "stored_enchantments"),
   ARMOR_COLOR("display::color", "dyed_color"),
   MAP_COLOR("display::MapColor", "map_color"),
   MAP_ID("map", "map_id"),
   MAP_DECORATIONS("Decorations", "map_decorations"),
   MAP_POST_PROCESSING("map_scale_direction", "map_post_processing"),
   CHARGED_PROJECTILES("ChargedProjectiles", "charged_projectiles"),
   BUNDLE_CONTENTS("Items", "bundle_contents"),
   BASE_POTION("Potion", "potion_contents::potion"),
   CUSTOM_POTION_EFFECTS("custom_potion_effects", "potion_contents::custom_effects"),
   CUSTOM_POTION_COLOR("CustomPotionColor", "potion_contents::custom_color"),
   SUSPICIOUS_STEW_EFFECTS("effects", "suspicious_stew_effects"),
   WRITEABLE_BOOK_CONTENT((String)null, "writable_book_content"),
   WRITTEN_BOOK_CONTENT("pages", "written_book_content::pages"),
   BOOK_AUTHOR("author", "written_book_content::author"),
   BOOK_TITLE("title", "written_book_content::title"),
   TRIM_MATERIAL("Trim::material", "trim::material"),
   TRIM_PATTERN("Trim::pattern", "trim::pattern"),
   DEBUG_STICK("DebugProperty", "debug_stick_state"),
   ENTITY_DATA("EntityTag", "entity_data"),
   BUCKET_ENTITY_DATA("BucketVariantTag", "bucket_entity_data"),
   BLOCK_ENTITY_DATA("BlockEntityTag", "block_entity_data"),
   INSTRUMENT("instrument", "instrument"),
   OMINOUS_BOTTLE_AMPLIFIER((String)null, "ominous_bottle_amplifier"),
   RECIPES("Recipes", "recipes"),
   LODESTONE_TRACKER("LodestoneTracked", "lodestone_tracker"),
   FIREWORK_EXPLOSION("Explosion", "firework_explosion"),
   FIREWORK_EXPLOSIONS("Fireworks::Explosions", "fireworks::explosions"),
   FIREWORK_FLIGHT_DURATION("Fireworks::Flight", "fireworks::flight_duration"),
   SKULLOWNER_ID("SkullOwner::Id", "profile::id"),
   SKULLOWNER_NAME("SkullOwner::Name", "profile::name"),
   SKULLOWNER_PROPERTIES("SkullOwner::Properties", "profile::properties"),
   BANNER_PATTERNS("BlockEntityTag::Patterns", "banner_patterns"),
   BASE_COLOR("BlockEntityTag::Base", "base_color"),
   CUSTOM_DATA("", "custom_data::");

   final String NBT;
   final String COMPOUND;

   private NBTMaps(String param3, String param4) {
      this.NBT = nbt;
      this.COMPOUND = compound;
   }

   public static String getTag(String tag) {
      return getTag(tag, (String)null, (String)null, true);
   }

   public static String getTag(String tag, String section, String path) {
      return getTag(tag, section, path, true);
   }

   public static String getTag(String tag, String section, String path, boolean adjust) {
      boolean compound = ServerInfo.supportsComponents();
      NBTMaps mapping = null;
      boolean full = false;
      if (tag.contains("::")) {
         try {
            mapping = getFromSimple(tag);
         } catch (IllegalArgumentException var12) {
         }
      } else {
         try {
            mapping = getFromSimple(tag);
         } catch (IllegalArgumentException var13) {
            NBTMaps[] var8 = values();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               NBTMaps map = var8[var10];
               if (map.NBT != null && map.NBT.contains("::") && map.NBT.split("::")[0].equals(tag)) {
                  mapping = map;
                  full = true;
                  break;
               }

               if (map.COMPOUND != null && map.COMPOUND.contains("::") && map.COMPOUND.split("::")[0].equals(tag)) {
                  mapping = map;
                  full = true;
                  break;
               }
            }
         }
      }

      if (mapping != null && !tag.startsWith(CUSTOM_DATA.COMPOUND)) {
         String s = !compound ? mapping.NBT : mapping.COMPOUND;
         if (compound && !tag.equals(mapping.COMPOUND)) {
            if (path != null) {
               SendMessage.logDebugMessage("Incompatible component tag for current minecraft version: Did you mean '" + mapping.COMPOUND + "' instead of '" + tag + "'?\nPlease change the value manually in config to get rid of this message");
               if (!path.startsWith("pages") && !path.startsWith("items")) {
                  if (section == null) {
                     SendMessage.logDebugMessage(Lang.ITEMS_PATH_IN_CONFIG.get().toString().replace("%location%", path));
                  }
               } else {
                  SendMessage.logDebugMessage(Lang.ITEMS_LOCATION_IN_SHOPS.get().toString().replace("%section%", section).replace("%location%", path));
               }
            }

            if (adjust) {
               return mapping.COMPOUND;
            }
         }

         return s != null ? (full ? s.split("::")[0] : s) : tag;
      } else {
         return tag;
      }
   }

   public static NBTMaps getFromSimple(String tag) {
      NBTMaps[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         NBTMaps mapping = var1[var3];
         if (mapping.NBT != null && mapping.NBT.equals(tag) || mapping.COMPOUND != null && mapping.COMPOUND.equals(tag)) {
            return mapping;
         }
      }

      throw new IllegalArgumentException();
   }

   // $FF: synthetic method
   private static NBTMaps[] $values() {
      return new NBTMaps[]{MAX_STACK_SIZE, MAX_DAMAGE, DAMAGE, UNBREAKABLE, NAME, DISPLAYNAME, LORE, RARITY, ENCHANTMENTS, CAN_PLACE_ON, CAN_BREAK, ATTRIBUTE_MODIFIERS, CUSTOM_MODEL_DATA, HIDE_ADDITIONAL_TOOLTIP, HIDE_FLAGS, REPAIR_COST, CREATIVE_SLOT_LOCK, ENCHANTMENT_GLINT, INTANGIBLE_PROJECTILE, FOOD, FIRE_RESISTANCE, TOOL, STORED_ENCHANTMENTS, ARMOR_COLOR, MAP_COLOR, MAP_ID, MAP_DECORATIONS, MAP_POST_PROCESSING, CHARGED_PROJECTILES, BUNDLE_CONTENTS, BASE_POTION, CUSTOM_POTION_EFFECTS, CUSTOM_POTION_COLOR, SUSPICIOUS_STEW_EFFECTS, WRITEABLE_BOOK_CONTENT, WRITTEN_BOOK_CONTENT, BOOK_AUTHOR, BOOK_TITLE, TRIM_MATERIAL, TRIM_PATTERN, DEBUG_STICK, ENTITY_DATA, BUCKET_ENTITY_DATA, BLOCK_ENTITY_DATA, INSTRUMENT, OMINOUS_BOTTLE_AMPLIFIER, RECIPES, LODESTONE_TRACKER, FIREWORK_EXPLOSION, FIREWORK_EXPLOSIONS, FIREWORK_FLIGHT_DURATION, SKULLOWNER_ID, SKULLOWNER_NAME, SKULLOWNER_PROPERTIES, BANNER_PATTERNS, BASE_COLOR, CUSTOM_DATA};
   }
}
