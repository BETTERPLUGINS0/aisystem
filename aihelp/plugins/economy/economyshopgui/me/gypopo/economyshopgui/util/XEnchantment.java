package me.gypopo.economyshopgui.util;

import com.google.common.base.Enums;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import me.gypopo.economyshopgui.methodes.SendMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public enum XEnchantment {
   ARROW_DAMAGE(new String[]{"POWER", "ARROW_DAMAGE", "ARROW_POWER", "AD"}),
   ARROW_FIRE(new String[]{"FLAME", "FLAME_ARROW", "FIRE_ARROW", "AF"}),
   ARROW_INFINITE(new String[]{"INFINITY", "INF_ARROWS", "INFINITE_ARROWS", "INFINITE", "UNLIMITED", "UNLIMITED_ARROWS", "AI"}),
   ARROW_KNOCKBACK(new String[]{"PUNCH", "ARROW_KNOCKBACK", "ARROWKB", "ARROW_PUNCH", "AK"}),
   BINDING_CURSE(true, new String[]{"BINDING_CURSE", "BIND_CURSE", "BINDING", "BIND"}),
   CHANNELING(true, new String[]{"CHANNELLING", "CHANELLING", "CHANELING", "CHANNEL"}),
   DAMAGE_ALL(new String[]{"SHARPNESS", "ALL_DAMAGE", "ALL_DMG", "SHARP", "DAL"}),
   DAMAGE_ARTHROPODS(new String[]{"BANE_OF_ARTHROPODS", "ARDMG", "BANE_OF_ARTHROPOD", "ARTHROPOD", "DAR"}),
   DAMAGE_UNDEAD(new String[]{"SMITE", "UNDEAD_DAMAGE", "DU"}),
   DEPTH_STRIDER(true, new String[]{"DEPTH", "STRIDER"}),
   DIG_SPEED(new String[]{"EFFICIENCY", "MINE_SPEED", "CUT_SPEED", "DS", "EFF"}),
   DURABILITY(new String[]{"UNBREAKING", "DURA"}),
   FIRE_ASPECT(true, new String[]{"FIRE", "MELEE_FIRE", "MELEE_FLAME", "FA"}),
   FROST_WALKER(true, new String[]{"FROST", "WALKER"}),
   IMPALING(true, new String[]{"IMPALE", "OCEAN_DAMAGE", "OCEAN_DMG"}),
   SOUL_SPEED(true, new String[]{"SPEED_SOUL", "SOUL_RUNNER"}),
   SWIFT_SNEAK(true, new String[]{"SPEED_SNEAK"}),
   KNOCKBACK(true, new String[]{"K_BACK", "KB"}),
   LOOT_BONUS_BLOCKS(new String[]{"FORTUNE", "BLOCKS_LOOT_BONUS", "FORT", "LBB"}),
   LOOT_BONUS_MOBS(new String[]{"LOOTING", "MOB_LOOT", "MOBS_LOOT_BONUS", "LBM"}),
   LOYALTY(true, new String[]{"LOYAL", "RETURN"}),
   LUCK(new String[]{"LUCK_OF_THE_SEA", "LUCK_OF_SEA", "LUCK_OF_SEAS", "ROD_LUCK"}),
   LURE(true, new String[]{"ROD_LURE"}),
   MENDING(true, new String[0]),
   MULTISHOT(true, new String[]{"TRIPLE_SHOT"}),
   OXYGEN(new String[]{"RESPIRATION", "BREATH", "BREATHING", "O2", "O"}),
   PIERCING(true, new String[0]),
   PROTECTION_ENVIRONMENTAL(new String[]{"PROTECTION", "PROTECT", "PROT"}),
   PROTECTION_EXPLOSIONS(new String[]{"BLAST_PROTECTION", "BLAST_PROTECT", "EXPLOSIONS_PROTECTION", "EXPLOSION_PROTECTION", "BLAST_PROTECTION", "PE"}),
   PROTECTION_FALL(new String[]{"FEATHER_FALLING", "FALL_PROT", "FEATHER_FALL", "FALL_PROTECTION", "FEATHER_FALLING", "PFA"}),
   PROTECTION_FIRE(new String[]{"FIRE_PROTECTION", "FIRE_PROT", "FIRE_PROTECT", "FIRE_PROTECTION", "FLAME_PROTECTION", "FLAME_PROTECT", "FLAME_PROT", "PF"}),
   PROTECTION_PROJECTILE(new String[]{"PROJECTILE_PROTECTION", "PROJECTILE_PROTECTION", "PROJ_PROT", "PP"}),
   QUICK_CHARGE(true, new String[]{"QUICKCHARGE", "QUICK_DRAW", "FAST_CHARGE", "FAST_DRAW"}),
   RIPTIDE(true, new String[]{"RIP", "TIDE", "LAUNCH"}),
   SILK_TOUCH(true, new String[]{"SOFT_TOUCH", "ST"}),
   SWEEPING_EDGE(true, false, new String[]{"SWEEPING", "SWEEP_EDGE"}),
   THORNS(true, new String[]{"HIGHCRIT", "THORN", "HIGHERCRIT", "T"}),
   VANISHING_CURSE(true, new String[]{"VANISHING_CURSE", "VANISH_CURSE", "VANISHING", "VANISH"}),
   WATER_WORKER(new String[]{"AQUA_AFFINITY", "WATER_WORKER", "AQUA_AFFINITY", "WATER_MINE", "WW"}),
   BREACH(true, new String[0]),
   DENSITY(true, new String[0]),
   WIND_BURST(true, new String[0]),
   LUNGE(true, new String[0]);

   public static final EnumSet<XEnchantment> VALUES = EnumSet.allOf(XEnchantment.class);
   private static final boolean ISFLAT;
   private static final Pattern FORMAT_PATTERN = Pattern.compile("\\d+|\\W+");
   private final boolean self;
   private final String[] aliases;

   private XEnchantment(String... param3) {
      this(false, names);
   }

   private XEnchantment(boolean param3, String... param4) {
      this.self = self;
      this.aliases = aliases;
   }

   private XEnchantment(boolean param3, boolean param4, String... param5) {
      this.self = ServerInfo.supportsComponents();
      this.aliases = aliases;
   }

   public static boolean isSmiteEffectiveAgainst(EntityType type) {
      return Arrays.asList(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.WITHER, EntityType.WITHER_SKELETON, EntityType.SKELETON_HORSE, EntityType.STRAY, EntityType.HUSK, EntityType.PHANTOM, EntityType.DROWNED).contains(type);
   }

   public static boolean isArthropodsEffectiveAgainst(EntityType type) {
      if (Arrays.asList(EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.SILVERFISH, EntityType.ENDERMITE).contains(type)) {
         return true;
      } else if (Enums.getIfPresent(EntityType.class, "BEE").isPresent()) {
         return type == EntityType.BEE;
      } else {
         return false;
      }
   }

   @Nonnull
   private static String format(@Nonnull String name) {
      return FORMAT_PATTERN.matcher(name.trim().replace('-', '_').replace(' ', '_')).replaceAll("").toUpperCase(Locale.ENGLISH);
   }

   @Nonnull
   public static Optional<XEnchantment> matchXEnchantment(@Nonnull String enchantment) {
      Validate.notEmpty(enchantment, "Enchantment name cannot be null or empty");
      enchantment = format(enchantment);
      Iterator var1 = VALUES.iterator();

      XEnchantment value;
      do {
         if (!var1.hasNext()) {
            return Optional.empty();
         }

         value = (XEnchantment)var1.next();
      } while(!value.name().equals(enchantment) && !value.anyMatchAliases(enchantment));

      return Optional.of(value);
   }

   public static XEnchantment matchXEnchantment(@Nonnull Enchantment enchantment) {
      Objects.requireNonNull(enchantment, "Cannot parse XEnchantment of a null enchantment");

      try {
         return ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_20_R4) ? matchCorrectEnch(enchantment) : valueOf(enchantment.getName());
      } catch (IllegalArgumentException var2) {
         SendMessage.errorMessage("Could not find enchantment '" + enchantment.getName() + "'");
         return null;
      }
   }

   private static XEnchantment matchCorrectEnch(@Nonnull Enchantment enchantment) {
      String name = enchantment.getKey().getKey().toUpperCase(Locale.ROOT);
      Iterator var2 = VALUES.iterator();

      XEnchantment ench;
      while(true) {
         if (!var2.hasNext()) {
            return null;
         }

         ench = (XEnchantment)var2.next();
         if (!ench.self) {
            if (ench.aliases[0].equals(name)) {
               break;
            }
         } else if (ench.name().equals(name)) {
            break;
         }
      }

      return ench;
   }

   @Nonnull
   public static ItemStack addEnchantFromString(ItemStack item, String enchantment) {
      Objects.requireNonNull(item, "Cannot add enchantment to null ItemStack");
      if (!Strings.isNullOrEmpty(enchantment) && !enchantment.equalsIgnoreCase("none")) {
         String[] split = StringUtils.split(StringUtils.deleteWhitespace(enchantment), ',');
         if (split.length == 0) {
            split = StringUtils.split(enchantment, ' ');
         }

         Optional<XEnchantment> enchantOpt = matchXEnchantment(split[0]);
         if (enchantOpt.isPresent()) {
            return item;
         } else {
            Enchantment enchant = ((XEnchantment)enchantOpt.get()).parseEnchantment();
            if (enchant == null) {
               return null;
            } else {
               int lvl = 1;

               try {
                  if (split.length > 1) {
                     lvl = Integer.parseInt(split[1]);
                  }
               } catch (NumberFormatException var7) {
               }

               item.addUnsafeEnchantment(enchant, lvl);
               return item;
            }
         }
      } else {
         return item;
      }
   }

   @Nonnull
   public ItemStack getBook(int level) {
      ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
      EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
      meta.addStoredEnchant(this.parseEnchantment(), level, true);
      book.setItemMeta(meta);
      return book;
   }

   private boolean anyMatchAliases(String enchantment) {
      String[] var2 = this.aliases;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String alias = var2[var4];
         if (enchantment.equals(alias) || enchantment.equals(StringUtils.remove(alias, '_'))) {
            return true;
         }
      }

      return false;
   }

   @Nonnull
   public String getVanillaName() {
      return this.self ? this.name() : this.aliases[0];
   }

   @Nullable
   public Enchantment parseEnchantment() {
      return ISFLAT ? Enchantment.getByKey(NamespacedKey.minecraft(this.getVanillaName().toLowerCase(Locale.ENGLISH))) : Enchantment.getByName(this.name());
   }

   public boolean isSupported() {
      return this.parseEnchantment() != null;
   }

   @Nonnull
   public String[] getAliases() {
      return this.aliases;
   }

   public String toString() {
      return WordUtils.capitalize(this.name().replace('_', ' ').toLowerCase(Locale.ENGLISH));
   }

   public static List<String> getNames() {
      List<String> names = new ArrayList();
      XEnchantment[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         XEnchantment enchantment = var1[var3];
         if (enchantment.isSupported()) {
            names.add(enchantment.getVanillaName() + ":1");
         }
      }

      return names;
   }

   // $FF: synthetic method
   private static XEnchantment[] $values() {
      return new XEnchantment[]{ARROW_DAMAGE, ARROW_FIRE, ARROW_INFINITE, ARROW_KNOCKBACK, BINDING_CURSE, CHANNELING, DAMAGE_ALL, DAMAGE_ARTHROPODS, DAMAGE_UNDEAD, DEPTH_STRIDER, DIG_SPEED, DURABILITY, FIRE_ASPECT, FROST_WALKER, IMPALING, SOUL_SPEED, SWIFT_SNEAK, KNOCKBACK, LOOT_BONUS_BLOCKS, LOOT_BONUS_MOBS, LOYALTY, LUCK, LURE, MENDING, MULTISHOT, OXYGEN, PIERCING, PROTECTION_ENVIRONMENTAL, PROTECTION_EXPLOSIONS, PROTECTION_FALL, PROTECTION_FIRE, PROTECTION_PROJECTILE, QUICK_CHARGE, RIPTIDE, SILK_TOUCH, SWEEPING_EDGE, THORNS, VANISHING_CURSE, WATER_WORKER, BREACH, DENSITY, WIND_BURST, LUNGE};
   }

   static {
      boolean flat;
      try {
         Class<?> namespacedKeyClass = Class.forName("org.bukkit.NamespacedKey");
         Class<?> enchantmentClass = Class.forName("org.bukkit.enchantments.Enchantment");
         enchantmentClass.getDeclaredMethod("getByKey", namespacedKeyClass);
         flat = true;
      } catch (NoSuchMethodException | ClassNotFoundException var3) {
         flat = false;
      }

      ISFLAT = flat;
   }
}
