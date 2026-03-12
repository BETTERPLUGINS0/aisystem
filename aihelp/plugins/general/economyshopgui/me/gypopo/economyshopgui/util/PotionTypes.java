package me.gypopo.economyshopgui.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import me.gypopo.economyshopgui.EconomyShopGUI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public enum PotionTypes {
   WATER("WATER", 0, false, 0, false, new String[]{"EMPTY"}),
   MUNDANE("MUNDANE", 0, false, 0, false, new String[0]),
   THICK("THICK", 0, false, 0, false, new String[0]),
   AWKWARD("AWKWARD", 0, false, 0, false, new String[0]),
   FIRE_RESISTANCE("FIRE_RESISTANCE", 3600, false, 0, false, new String[]{"RESISTANCE_FIRE"}),
   LONG_FIRE_RESISTANCE("FIRE_RESISTANCE", 9600, true, 0, false, new String[]{"FIRE_RESISTANCE_LONG", "RESISTANCE_FIRE_LONG"}),
   HARMING("INSTANT_DAMAGE", 1, false, 0, false, new String[]{"INSTANT_DAMAGE", "HARM", "DAMAGE_INSTANT"}),
   STRONG_HARMING("INSTANT_DAMAGE", 1, false, 1, true, new String[]{"STRONG_INSTANT_DAMAGE", "STRONG_HARM", "STRONG_DAMAGE_INSTANT"}),
   HEALING("INSTANT_HEALTH", "INSTANT_HEAL", 1, false, 0, false, new String[]{"INSTANT_HEALTH", "HEAL"}),
   STRONG_HEALING("INSTANT_HEALTH", "INSTANT_HEAL", 1, false, 1, true, new String[]{"STRONG_INSTANT_HEALTH", "STRONG_HEAL"}),
   INVISIBILITY("INVISIBILITY", 3600, false, 0, false, new String[]{"INVIS"}),
   LONG_INVISIBILITY("INVISIBILITY", 9600, true, 0, false, new String[]{"LONG_INVIS"}),
   LEAPING("JUMP_BOOST", "JUMP", 3600, false, 0, false, new String[]{"JUMP_BOOST", "JUMP"}),
   LONG_LEAPING("JUMP_BOOST", "JUMP", 9600, true, 0, false, new String[]{"LONG_JUMP_BOOST", "LONG_JUMP"}),
   STRONG_LEAPING("JUMP_BOOST", "JUMP", 1800, false, 1, true, new String[]{"STRONG_JUMP_BOOST", "STRONG_JUMP"}),
   LUCK("LUCK", 6000, false, 0, false, new String[]{"LUCKY"}),
   NIGHT_VISION("NIGHT_VISION", 3600, false, 0, false, new String[0]),
   LONG_NIGHT_VISION("NIGHT_VISION", 9600, true, 0, false, new String[0]),
   POISON("POISON", 900, false, 0, false, new String[0]),
   LONG_POISON("POISON", 2400, true, 0, false, new String[0]),
   STRONG_POISON("POISON", 440, false, 0, true, new String[0]),
   REGENERATION("REGENERATION", "REGEN", 900, false, 0, false, new String[]{"REGEN"}),
   LONG_REGENERATION("REGENERATION", "REGEN", 2400, true, 0, false, new String[]{"LONG_REGEN"}),
   STRONG_REGENERATION("REGENERATION", "REGEN", 440, false, 1, true, new String[]{"STRONG_REGEN"}),
   SLOW_FALLING("SLOW_FALLING", 1800, false, 0, false, new String[0]),
   LONG_SLOW_FALLING("SLOW_FALLING", 4800, true, 0, false, new String[0]),
   SLOWNESS("SLOWNESS", 1800, false, 0, false, new String[]{"SLOW"}),
   LONG_SLOWNESS("SLOWNESS", 4800, true, 0, false, new String[]{"LONG_SLOW"}),
   STRONG_SLOWNESS("SLOWNESS", 200, false, 3, true, new String[]{"STRONG_SLOW"}),
   STRENGTH("STRENGTH", 3600, false, 0, false, new String[0]),
   LONG_STRENGTH("STRENGTH", 9600, true, 0, false, new String[0]),
   STRONG_STRENGTH("STRENGTH", 1800, false, 1, true, new String[0]),
   SWIFTNESS("SPEED", 3600, false, 0, false, new String[]{"SPEED"}),
   LONG_SWIFTNESS("SPEED", 9600, true, 0, false, new String[]{"LONG_SPEED"}),
   STRONG_SWIFTNESS("SPEED", 1800, false, 1, true, new String[]{"STRONG_SPEED"}),
   TURTLE_MASTER("TURTLE_MASTER", 400, false, 0, false, new String[0]),
   LONG_TURTLE_MASTER("TURTLE_MASTER", 800, true, 0, false, new String[0]),
   STRONG_TURTLE_MASTER("TURTLE_MASTER", 400, false, 1, true, new String[0]),
   WATER_BREATHING("WATER_BREATHING", 3600, false, 0, false, new String[0]),
   LONG_WATER_BREATHING("WATER_BREATHING", 9600, true, 0, false, new String[0]),
   WEAKNESS("WEAKNESS", 1800, false, 0, false, new String[0]),
   LONG_WEAKNESS("WEAKNESS", 4800, true, 0, false, new String[0]),
   INFESTED("INFESTED", 3600, false, 0, false, new String[0]),
   OOZING("OOZING", 3600, false, 0, false, new String[0]),
   WEAVING("WEAVING", 3600, false, 0, false, new String[0]),
   WIND_CHARGED("WIND_CHARGED", 3600, false, 0, false, new String[0]);

   private final String type;
   private final int ticks;
   private final boolean extended;
   private final int level;
   private final boolean upgraded;
   private final String[] aliases;
   public static final EnumSet<PotionTypes> VALUES = EnumSet.allOf(PotionTypes.class);

   private PotionTypes(String param3, int param4, boolean param5, int param6, boolean param7, String... param8) {
      this.type = type;
      this.ticks = ticks;
      this.extended = extended;
      this.level = level;
      this.upgraded = upgraded;
      this.aliases = aliases;
   }

   private PotionTypes(String param3, String param4, int param5, boolean param6, int param7, boolean param8, String... param9) {
      this.type = ServerInfo.supportsComponents() ? type : legacyType;
      this.ticks = ticks;
      this.extended = extended;
      this.level = level;
      this.upgraded = upgraded;
      this.aliases = aliases;
   }

   public static Optional<PotionTypes> matchPotionType(String name) {
      name = name.replace(" ", "_").toUpperCase(Locale.ENGLISH);
      Iterator var1 = VALUES.iterator();

      PotionTypes potion;
      do {
         if (!var1.hasNext()) {
            return Optional.empty();
         }

         potion = (PotionTypes)var1.next();
      } while(!potion.name().equalsIgnoreCase(name) && !potion.anyMatchAliases(name));

      return Optional.of(potion);
   }

   public static Optional<PotionTypes> getFromType(String name) {
      name = name.replace(" ", "_").toUpperCase(Locale.ENGLISH);
      Iterator var1 = VALUES.iterator();

      PotionTypes potion;
      do {
         if (!var1.hasNext()) {
            return Optional.empty();
         }

         potion = (PotionTypes)var1.next();
      } while(!potion.type.equalsIgnoreCase(name) && !potion.anyMatchAliases(name));

      return Optional.of(potion);
   }

   public static PotionTypes matchPotionType(PotionEffect effectType) {
      Iterator var1 = VALUES.iterator();

      PotionTypes potionType;
      do {
         if (!var1.hasNext()) {
            return WATER;
         }

         potionType = (PotionTypes)var1.next();
      } while(!potionType.isSupported() || !potionType.hasEffect() || !potionType.parsePotionEffectType().equals(effectType.getType()) || potionType.ticks != effectType.getDuration() || potionType.level != effectType.getAmplifier());

      return potionType;
   }

   public static PotionTypes matchPotionType(PotionType type) {
      Iterator var1 = VALUES.iterator();

      PotionTypes potionType;
      do {
         if (!var1.hasNext()) {
            return WATER;
         }

         potionType = (PotionTypes)var1.next();
      } while(!potionType.isSupported() || !potionType.parsePotionType().equals(type));

      return potionType;
   }

   public static Optional<PotionTypes> matchPotionDataLegacy(PotionType type, boolean extended, boolean upgraded) {
      Iterator var3 = VALUES.iterator();

      PotionTypes potionType;
      do {
         if (!var3.hasNext()) {
            return Optional.empty();
         }

         potionType = (PotionTypes)var3.next();
      } while(!potionType.isSupported() || !potionType.parsePotionType().equals(type) || potionType.extended != extended || potionType.upgraded != upgraded);

      return Optional.of(potionType);
   }

   public static Optional<PotionTypes> matchPotionData(PotionType type) {
      Iterator var1 = VALUES.iterator();

      PotionTypes potionType;
      do {
         if (!var1.hasNext()) {
            return Optional.empty();
         }

         potionType = (PotionTypes)var1.next();
      } while(!potionType.isSupported() || !potionType.name().equals(type.name()));

      return Optional.of(potionType);
   }

   public static boolean canHaveEffects(Material mat) {
      return XMaterial.matchXMaterial(mat).equals(XMaterial.POTION) || XMaterial.matchXMaterial(mat).equals(XMaterial.LINGERING_POTION) || XMaterial.matchXMaterial(mat).equals(XMaterial.SPLASH_POTION) || XMaterial.matchXMaterial(mat).equals(XMaterial.TIPPED_ARROW);
   }

   public PotionEffectType parsePotionEffectType() {
      return this.hasEffect() ? this.parsePotionType().getEffectType() : null;
   }

   public PotionType parsePotionType() {
      return PotionType.valueOf(ServerInfo.supportsComponents() ? this.name() : this.type);
   }

   public PotionEffect parsePotionEffect() {
      return this.hasEffect() ? new PotionEffect(this.parsePotionEffectType(), this.ticks, this.level, false) : null;
   }

   public static List<PotionTypes> getPotionTypes(ItemStack item) {
      List<PotionTypes> potionTypes = new ArrayList();
      if (item == null) {
         return potionTypes;
      } else {
         PotionMeta meta = (PotionMeta)item.getItemMeta();
         if (meta == null) {
            return potionTypes;
         } else {
            if (EconomyShopGUI.getInstance().version >= 19) {
               Optional<PotionTypes> base = EconomyShopGUI.getInstance().versionHandler.getBasePotion(item);
               if (base.isPresent()) {
                  potionTypes.add((PotionTypes)base.get());
               }

               Iterator var4 = meta.getCustomEffects().iterator();

               while(var4.hasNext()) {
                  PotionEffect pe = (PotionEffect)var4.next();
                  potionTypes.add(matchPotionType(pe));
               }
            } else {
               Iterator var6 = meta.getCustomEffects().iterator();

               while(var6.hasNext()) {
                  PotionEffect pe = (PotionEffect)var6.next();
                  potionTypes.add(matchPotionType(pe));
               }
            }

            return potionTypes;
         }
      }
   }

   public boolean isExtended() {
      return this.extended;
   }

   public int getDuration() {
      return this.ticks;
   }

   public int getAmplifier() {
      return this.level;
   }

   public boolean isUpgraded() {
      return this.upgraded;
   }

   public boolean isSupported() {
      try {
         return this.parsePotionType() != null || !this.hasEffect();
      } catch (Exception var2) {
         return false;
      }
   }

   private boolean anyMatchAliases(String name) {
      String[] var2 = this.aliases;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String alias = var2[var4];
         if (name.equals(alias) || name.equals(StringUtils.remove(alias, '_'))) {
            return true;
         }
      }

      return false;
   }

   public static List<String> getNames() {
      List<String> names = new ArrayList();
      PotionTypes[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PotionTypes potionType = var1[var3];
         if (potionType.isSupported()) {
            names.add(potionType.type);
         }
      }

      return names;
   }

   public ItemStack addEffect(ItemStack item, boolean splash) {
      if (EconomyShopGUI.getInstance().version != 18) {
         if (EconomyShopGUI.getInstance().versionHandler != null) {
            item = EconomyShopGUI.getInstance().versionHandler.setPotionType(item, this);
         } else {
            PotionMeta meta = (PotionMeta)item.getItemMeta();
            if (ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_20_R2) && meta.getBasePotionType().getEffectType() == null) {
               meta.setBasePotionType(PotionType.valueOf(this.name()));
            } else {
               if (!this.hasEffect()) {
                  return item;
               }

               meta.addCustomEffect(new PotionEffect(this.parsePotionEffectType(), this.getDuration(), this.getAmplifier(), false, true, true), false);
            }

            item.setItemMeta(meta);
         }
      } else if (this.hasEffect()) {
         this.createOldPotion(item, splash);
      }

      return item;
   }

   private ItemStack createOldPotion(ItemStack item, boolean splash) {
      PotionMeta meta = (PotionMeta)item.getItemMeta();
      if (!this.hasEffect()) {
         return item;
      } else {
         if (meta.hasCustomEffects()) {
            meta.addCustomEffect(new PotionEffect(this.parsePotionEffectType(), this.getDuration(), this.getAmplifier()), false);
         } else {
            Potion pot = new Potion(this.parsePotionType(), this.getAmplifier() + 1, splash, this.isExtended());
            pot.apply(item);
            meta.addCustomEffect(new PotionEffect(this.parsePotionEffectType(), this.getDuration(), this.getAmplifier()), false);
            meta.setMainEffect(this.parsePotionEffectType());
         }

         return item;
      }
   }

   public boolean hasEffect() {
      return this.parsePotionType().getEffectType() != null;
   }

   // $FF: synthetic method
   private static PotionTypes[] $values() {
      return new PotionTypes[]{WATER, MUNDANE, THICK, AWKWARD, FIRE_RESISTANCE, LONG_FIRE_RESISTANCE, HARMING, STRONG_HARMING, HEALING, STRONG_HEALING, INVISIBILITY, LONG_INVISIBILITY, LEAPING, LONG_LEAPING, STRONG_LEAPING, LUCK, NIGHT_VISION, LONG_NIGHT_VISION, POISON, LONG_POISON, STRONG_POISON, REGENERATION, LONG_REGENERATION, STRONG_REGENERATION, SLOW_FALLING, LONG_SLOW_FALLING, SLOWNESS, LONG_SLOWNESS, STRONG_SLOWNESS, STRENGTH, LONG_STRENGTH, STRONG_STRENGTH, SWIFTNESS, LONG_SWIFTNESS, STRONG_SWIFTNESS, TURTLE_MASTER, LONG_TURTLE_MASTER, STRONG_TURTLE_MASTER, WATER_BREATHING, LONG_WATER_BREATHING, WEAKNESS, LONG_WEAKNESS, INFESTED, OOZING, WEAVING, WIND_CHARGED};
   }
}
