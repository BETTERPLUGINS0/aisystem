package emanondev.itemedit.aliases;

import emanondev.itemedit.utility.TagContainer;
import emanondev.itemedit.utility.VersionUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Aliases {
   public static final EnchAliases ENCHANT = getEnchAliases();
   public static final AliasSet<PatternType> PATTERN_TYPE = getPatternAlias();
   public static final GenAliases BOOK_TYPE = getGenAliases();
   public static final AliasSet<PotionEffectType> POTION_EFFECT = new AliasSet<PotionEffectType>("potion_effect") {
      private final Collection<PotionEffectType> values = this.grabValues();

      public String getName(PotionEffectType type) {
         String name = type.getName().toLowerCase(Locale.ENGLISH);
         if (name.startsWith("minecraft:")) {
            name = name.substring(10);
         }

         return name;
      }

      private Collection<PotionEffectType> grabValues() {
         HashSet<PotionEffectType> set = new HashSet();
         PotionEffectType[] var2 = PotionEffectType.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            PotionEffectType val = var2[var4];
            if (val != null) {
               set.add(val);
            }
         }

         return set;
      }

      public Collection<PotionEffectType> getValues() {
         return this.values;
      }
   };
   public static final AliasSet<DyeColor> COLOR = new EnumAliasSet("color", DyeColor.class);
   public static final AliasSet<String> ANIMATION = VersionUtils.isVersionAfter(1, 20, 5) ? new AliasSet<String>("animations") {
      private final LinkedHashSet<String> values = new LinkedHashSet(this.craftValues());

      public String getName(String type) {
         return type.toLowerCase(Locale.ENGLISH);
      }

      private List<String> craftValues() {
         return Arrays.asList("drink", "eat", "crossbow", "none", "block", "bow", "spear", "spyglass", "toot_horn", "brush");
      }

      public Collection<String> getValues() {
         return this.values;
      }
   } : null;
   public static final EggTypeAliases EGG_TYPE = getEggTypeAliases();
   public static final AliasSet<ItemFlag> FLAG_TYPE = new EnumAliasSet<ItemFlag>("flag_type", ItemFlag.class) {
      public String getName(ItemFlag type) {
         String name = type.name().toLowerCase(Locale.ENGLISH);
         if (name.startsWith("hide_")) {
            name = name.substring("hide_".length());
         }

         return name;
      }
   };
   public static final AliasSet<Boolean> BOOLEAN = new AliasSet<Boolean>("boolean") {
      public String getName(Boolean value) {
         return value ? "true" : "false";
      }

      public Collection<Boolean> getValues() {
         return Arrays.asList(Boolean.FALSE, Boolean.TRUE);
      }
   };
   public static final AliasSet<EquipmentSlot> EQUIPMENT_SLOTS = new EnumAliasSet("equip_slot", EquipmentSlot.class);
   public static final AttributeAliases ATTRIBUTE = getAttributeAliases();
   public static final OperationAliases OPERATIONS = getAttributeOperationAliases();
   public static final RarityAliases RARITY = getRarityAliases();
   public static final TropicalFishPatternAliases TROPICALPATTERN = getTropicalPatternAliases();
   public static final TrimMaterialAliases TRIM_MATERIAL = getTrimMaterialAliases();
   public static final TrimPatternAliases TRIM_PATTERN = getTrimPatternAliases();
   public static final EnumAliasSet<Type> FIREWORK_TYPE = new EnumAliasSet("firework_type", Type.class);
   public static final AxolotlVariantAliases AXOLOTL_VARIANT = getAxolotlVariantAliases();
   public static final GoatHornSoundAliases GOAT_HORN_SOUND = getGoatHornSoundAliases();
   public static final EquipmentSlotGroupAliases EQUIPMENT_SLOTGROUPS = VersionUtils.isVersionAfter(1, 21) ? new EquipmentSlotGroupAliases() : null;
   public static final SoundAliases SOUND = getSoundAliases();
   public static final AliasSet<EntityType> ENTITY_TYPE = new EnumAliasSet(EntityType.class);
   public static final AliasSet<TagContainer<EntityType>> ENTITY_GROUPS = VersionUtils.isVersionAfter(1, 21) ? new TagAliasSet("entitygroups", EntityType.class, "entity_types") : null;
   private static final Map<String, IAliasSet<?>> types = new HashMap();
   private static boolean loaded = false;

   private static AliasSet<PatternType> getPatternAlias() {
      try {
         if (VersionUtils.isVersionAfter(1, 20, 6)) {
            return new BannerPatternAliasesNew();
         }
      } catch (Throwable var1) {
      }

      return new BannerPatternAliasesOld();
   }

   private static TrimMaterialAliases getTrimMaterialAliases() {
      if (VersionUtils.isVersionUpTo(1, 19, 4)) {
         return null;
      } else {
         try {
            return (TrimMaterialAliases)(VersionUtils.isVersionAfter(1, 20, 2) ? new TrimMaterialAliasesNew() : new TrimMaterialAliasesOld());
         } catch (Throwable var1) {
            var1.printStackTrace();
            return null;
         }
      }
   }

   private static TrimPatternAliases getTrimPatternAliases() {
      if (VersionUtils.isVersionUpTo(1, 19, 4)) {
         return null;
      } else {
         try {
            return (TrimPatternAliases)(VersionUtils.isVersionAfter(1, 20, 2) ? new TrimPatternAliasesNew() : new TrimPatternAliasesOld());
         } catch (Throwable var1) {
            var1.printStackTrace();
            return null;
         }
      }
   }

   private static void loadTypesMap() {
      registerAliasType(ENCHANT);
      registerAliasType(PATTERN_TYPE);
      registerAliasType(BOOK_TYPE);
      registerAliasType(POTION_EFFECT);
      registerAliasType(COLOR);
      registerAliasType(EGG_TYPE);
      registerAliasType(FLAG_TYPE);
      registerAliasType(BOOLEAN);
      registerAliasType(EQUIPMENT_SLOTS);
      registerAliasType(FIREWORK_TYPE);
      registerAliasType(ATTRIBUTE);
      registerAliasType(OPERATIONS);
      registerAliasType(TROPICALPATTERN);
      registerAliasType(AXOLOTL_VARIANT);
      registerAliasType(GOAT_HORN_SOUND);
      registerAliasType(TRIM_MATERIAL);
      registerAliasType(TRIM_PATTERN);
      registerAliasType(RARITY);
      registerAliasType(EQUIPMENT_SLOTGROUPS);
      registerAliasType(ANIMATION);
      registerAliasType(SOUND);
      registerAliasType(ENTITY_TYPE);
      registerAliasType(ENTITY_GROUPS);
   }

   public static <T> void registerAliasType(@Nullable IAliasSet<T> set) {
      registerAliasType(set, false);
   }

   public static <T> void registerAliasType(@Nullable IAliasSet<T> set, boolean forced) {
      if (set != null) {
         if (!forced && types.containsKey(set.getID())) {
            throw new IllegalArgumentException("Duplicate id");
         } else {
            types.put(set.getID(), set);
         }
      }
   }

   public static IAliasSet<?> getAliasType(@NotNull String id) {
      return (IAliasSet)types.get(id);
   }

   public static void reload() {
      if (!loaded) {
         loaded = true;
         loadTypesMap();
      }

      Iterator var0 = types.values().iterator();

      while(var0.hasNext()) {
         IAliasSet<?> set = (IAliasSet)var0.next();
         set.reload();
      }

   }

   public static Map<String, IAliasSet<?>> getTypes() {
      return Collections.unmodifiableMap(types);
   }

   private static EggTypeAliases getEggTypeAliases() {
      return VersionUtils.isVersionInRange(1, 11, 1, 12) ? new EggTypeAliases() : null;
   }

   private static TropicalFishPatternAliases getTropicalPatternAliases() {
      return VersionUtils.isVersionUpTo(1, 12) ? null : new TropicalFishPatternAliases();
   }

   private static GenAliases getGenAliases() {
      return VersionUtils.isVersionUpTo(1, 9) ? null : new GenAliases();
   }

   @NotNull
   private static EnchAliases getEnchAliases() {
      return (EnchAliases)(VersionUtils.isVersionUpTo(1, 12) ? new EnchAliasesOld() : new EnchAliases());
   }

   private static AttributeAliases getAttributeAliases() {
      if (VersionUtils.isVersionUpTo(1, 11)) {
         return null;
      } else {
         return (AttributeAliases)(VersionUtils.isVersionUpTo(1, 21, 2) ? new AttributeAliasesOld() : new AttributeAliasesNew());
      }
   }

   private static OperationAliases getAttributeOperationAliases() {
      return VersionUtils.isVersionUpTo(1, 11) ? null : new OperationAliases();
   }

   private static RarityAliases getRarityAliases() {
      if (VersionUtils.isVersionUpTo(1, 20, 4)) {
         return null;
      } else {
         try {
            return new RarityAliases();
         } catch (Throwable var1) {
            var1.printStackTrace();
            return null;
         }
      }
   }

   private static AxolotlVariantAliases getAxolotlVariantAliases() {
      return VersionUtils.isVersionUpTo(1, 17) ? null : new AxolotlVariantAliases();
   }

   private static GoatHornSoundAliases getGoatHornSoundAliases() {
      if (VersionUtils.isVersionUpTo(1, 19, 2)) {
         return null;
      } else {
         try {
            return new GoatHornSoundAliases();
         } catch (Throwable var1) {
            return null;
         }
      }
   }

   private static SoundAliases getSoundAliases() {
      return VersionUtils.isVersionAfter(1, 20, 5) ? new SoundAliases() : null;
   }
}
