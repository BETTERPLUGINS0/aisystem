package me.PM2.infinitevehicles.xseries;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import me.PM2.infinitevehicles.xseries.base.XBase;
import me.PM2.infinitevehicles.xseries.base.XRegistry;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum XPotion implements XBase<XPotion, PotionEffectType> {
   ABSORPTION(new String[]{"ABSORB"}),
   BAD_OMEN(new String[]{"OMEN_BAD", "PILLAGER"}),
   BLINDNESS(new String[]{"BLIND"}),
   CONDUIT_POWER(new String[]{"CONDUIT", "POWER_CONDUIT"}),
   DARKNESS(new String[0]),
   DOLPHINS_GRACE(new String[]{"DOLPHIN", "GRACE"}),
   FIRE_RESISTANCE(new String[]{"FIRE_RESIST", "RESIST_FIRE", "FIRE_RESISTANCE"}),
   GLOWING(new String[]{"GLOW", "SHINE", "SHINY"}),
   HASTE(new String[]{"FAST_DIGGING", "SUPER_PICK", "DIGFAST", "DIG_SPEED", "QUICK_MINE", "SHARP"}),
   HEALTH_BOOST(new String[]{"BOOST_HEALTH", "BOOST", "HP"}),
   HERO_OF_THE_VILLAGE(new String[]{"HERO", "VILLAGE_HERO"}),
   HUNGER(new String[]{"STARVE", "HUNGRY"}),
   INFESTED(new String[0]),
   INSTANT_DAMAGE(new String[]{"INJURE", "DAMAGE", "HARMING", "INFLICT", "HARM"}),
   INSTANT_HEALTH(new String[]{"HEALTH", "INSTA_HEAL", "INSTANT_HEAL", "INSTA_HEALTH", "HEAL", "HEALING"}),
   INVISIBILITY(new String[]{"INVISIBLE", "VANISH", "INVIS", "DISAPPEAR", "HIDE"}),
   JUMP_BOOST(new String[]{"LEAP", "LEAPING", "JUMP"}),
   LEVITATION(new String[]{"LEVITATE"}),
   LUCK(new String[]{"LUCKY"}),
   MINING_FATIGUE(new String[]{"SLOW_DIGGING", "FATIGUE", "DULL", "DIGGING", "SLOW_DIG", "DIG_SLOW"}),
   NAUSEA(new String[]{"CONFUSION", "SICKNESS", "SICK"}),
   NIGHT_VISION(new String[]{"VISION", "VISION_NIGHT"}),
   OOZING(new String[0]),
   POISON(new String[]{"VENOM"}),
   RAID_OMEN(new String[0]),
   REGENERATION(new String[]{"REGEN"}),
   RESISTANCE(new String[]{"DAMAGE_RESISTANCE", "ARMOR", "DMG_RESIST", "DMG_RESISTANCE"}),
   SATURATION(new String[]{"FOOD"}),
   SLOWNESS(new String[]{"SLOW", "SLUGGISH"}),
   SLOW_FALLING(new String[]{"SLOW_FALL", "FALL_SLOW"}),
   SPEED(new String[]{"SPRINT", "RUNFAST", "SWIFT", "SWIFTNESS", "FAST"}),
   STRENGTH(new String[]{"INCREASE_DAMAGE", "BULL", "STRONG", "ATTACK"}),
   TRIAL_OMEN(new String[0]),
   UNLUCK(new String[]{"UNLUCKY"}),
   WATER_BREATHING(new String[]{"WATER_BREATH", "UNDERWATER_BREATHING", "UNDERWATER_BREATH", "AIR"}),
   WEAKNESS(new String[]{"WEAK"}),
   WEAVING(new String[0]),
   WIND_CHARGED(new String[0]),
   WITHER(new String[]{"DECAY"});

   public static final XPotion[] VALUES = values();
   /** @deprecated */
   @Deprecated
   public static final Set<XPotion> DEBUFFS = Collections.unmodifiableSet(EnumSet.of(BAD_OMEN, BLINDNESS, NAUSEA, INSTANT_DAMAGE, HUNGER, LEVITATION, POISON, SLOWNESS, MINING_FATIGUE, UNLUCK, WEAKNESS, WITHER));
   private static final XPotion[] POTIONEFFECTTYPE_MAPPING = new XPotion[VALUES.length + 1];
   public static final XRegistry<XPotion, PotionEffectType> REGISTRY;
   private final PotionEffectType potionEffectType;
   private final PotionType potionType;

   private XPotion(@NotNull String... param3) {
      PotionEffectType var4 = PotionEffectType.getByName(this.name());
      String[] var5 = var3;
      int var6 = var3.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         if (var4 == null) {
            var4 = PotionEffectType.getByName(var8);
         }
      }

      if (this.name().equals("TURTLE_MASTER")) {
         var4 = findSlowness();
      }

      this.potionEffectType = var4;
      this.potionType = PotionType.getByEffect(this.potionEffectType);
      XPotion.Data.REGISTRY.stdEnum(this, var3, this.potionEffectType);
      if (this.potionType != null) {
         String var11 = this.potionType.name();
         String var12 = "STRONG_" + var11;
         String var13 = "LONG_" + var11;
         XPotion.Data.POTION_TYPE_MAPPING.put(this.potionType, this);

         try {
            XPotion.Data.POTION_TYPE_MAPPING.put(PotionType.valueOf(var12), this);
            XPotion.Data.REGISTRY.registerName(var12, this);
         } catch (IllegalArgumentException var10) {
         }

         try {
            XPotion.Data.POTION_TYPE_MAPPING.put(PotionType.valueOf(var13), this);
            XPotion.Data.REGISTRY.registerName(var13, this);
         } catch (IllegalArgumentException var9) {
         }
      }

   }

   private static PotionEffectType findSlowness() {
      return (PotionEffectType)Stream.of("SLOWNESS", "SLOW", "SLUGGISH").map(PotionEffectType::getByName).filter(Objects::nonNull).findFirst().orElseThrow(() -> {
         return new IllegalStateException("Cannot find slowness potion type");
      });
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public static Optional<XPotion> matchXPotion(@NotNull String var0) {
      return of(var0);
   }

   @NotNull
   public static XPotion of(@NotNull PotionType var0) {
      return (XPotion)XPotion.Data.POTION_TYPE_MAPPING.get(var0);
   }

   public static Optional<XPotion> of(@NotNull String var0) {
      if (var0 != null && !var0.isEmpty()) {
         PotionEffectType var1 = fromId(var0);
         if (var1 != null) {
            Optional var2 = REGISTRY.getByName(var1.getName());
            if (!var2.isPresent()) {
               throw new UnsupportedOperationException("Unsupported potion effect type ID: " + var1);
            } else {
               return var2;
            }
         } else {
            return REGISTRY.getByName(var0);
         }
      } else {
         throw new IllegalArgumentException("Cannot match XPotion of a null or empty potion effect type");
      }
   }

   /** @deprecated */
   @Deprecated
   public static XPotion matchXPotion(@NotNull PotionType var0) {
      return of(var0);
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public static XPotion matchXPotion(@NotNull PotionEffectType var0) {
      return of(var0);
   }

   @NotNull
   public static XPotion of(@NotNull PotionEffectType var0) {
      Objects.requireNonNull(var0, "Cannot match XPotion of a null potion effect type");
      return (XPotion)REGISTRY.getByBukkitForm(var0);
   }

   @Nullable
   private static PotionEffectType fromId(@NotNull String var0) {
      try {
         int var1 = Integer.parseInt(var0);
         return PotionEffectType.getById(var1);
      } catch (NumberFormatException var2) {
         return null;
      }
   }

   private static List<String> split(@NotNull String var0, char var1) {
      ArrayList var2 = new ArrayList(5);
      boolean var3 = false;
      boolean var4 = false;
      int var5 = var0.length();
      int var6 = 0;

      for(int var7 = 0; var7 < var5; ++var7) {
         if (var0.charAt(var7) == var1) {
            if (var3) {
               var2.add(var0.substring(var6, var7));
               var3 = false;
               var4 = true;
            }

            var6 = var7 + 1;
         } else {
            var4 = false;
            var3 = true;
         }
      }

      if (var3 || var4) {
         var2.add(var0.substring(var6, var5));
      }

      return var2;
   }

   @Nullable
   public static XPotion.Effect parseEffect(@Nullable String var0) {
      if (!Strings.isNullOrEmpty(var0) && !var0.equalsIgnoreCase("none")) {
         List var1 = split(var0.replace(" ", ""), ',');
         if (var1.isEmpty()) {
            var1 = split(var0, ' ');
         }

         double var2 = 100.0D;
         int var4 = 0;
         if (var1.size() > 2) {
            var4 = ((String)var1.get(2)).indexOf(37);
            if (var4 != -1) {
               try {
                  var2 = Double.parseDouble(((String)var1.get(2)).substring(var4 + 1));
               } catch (NumberFormatException var9) {
               }
            }
         }

         Optional var5 = of((String)var1.get(0));
         if (!var5.isPresent()) {
            return null;
         } else {
            PotionEffectType var6 = ((XPotion)var5.get()).potionEffectType;
            if (var6 == null) {
               return null;
            } else {
               int var7 = 2400;
               int var8 = 0;
               if (var1.size() > 1) {
                  var7 = toInt((String)var1.get(1), 1) * 20;
                  if (var1.size() > 2) {
                     var8 = toInt(var4 <= 0 ? (String)var1.get(2) : ((String)var1.get(2)).substring(0, var4), 1) - 1;
                  }
               }

               return new XPotion.Effect(new PotionEffect(var6, var7, var8), var2);
            }
         }
      } else {
         return null;
      }
   }

   private static int toInt(String var0, int var1) {
      try {
         return Integer.parseInt(var0);
      } catch (NumberFormatException var3) {
         return var1;
      }
   }

   public static void addEffects(@NotNull LivingEntity var0, @Nullable List<String> var1) {
      Objects.requireNonNull(var0, "Cannot add potion effects to null entity");
      Iterator var2 = parseEffects(var1).iterator();

      while(var2.hasNext()) {
         XPotion.Effect var3 = (XPotion.Effect)var2.next();
         var3.apply(var0);
      }

   }

   public static List<XPotion.Effect> parseEffects(@Nullable List<String> var0) {
      if (var0 != null && !var0.isEmpty()) {
         ArrayList var1 = new ArrayList(var0.size());
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            XPotion.Effect var4 = parseEffect(var3);
            if (var4 != null) {
               var1.add(var4);
            }
         }

         return var1;
      } else {
         return new ArrayList();
      }
   }

   @NotNull
   public static ThrownPotion throwPotion(@NotNull LivingEntity var0, @Nullable Color var1, @Nullable PotionEffect... var2) {
      Objects.requireNonNull(var0, "Cannot throw potion from null entity");
      ItemStack var3 = Material.getMaterial("SPLASH_POTION") == null ? new ItemStack(Material.POTION, 1, (short)16398) : new ItemStack(Material.SPLASH_POTION);
      PotionMeta var4 = (PotionMeta)var3.getItemMeta();
      var4.setColor(var1);
      if (var2 != null) {
         PotionEffect[] var5 = var2;
         int var6 = var2.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            PotionEffect var8 = var5[var7];
            var4.addCustomEffect(var8, true);
         }
      }

      var3.setItemMeta(var4);
      ThrownPotion var9 = (ThrownPotion)var0.launchProjectile(ThrownPotion.class);
      var9.setItem(var3);
      return var9;
   }

   @NotNull
   public static ItemStack buildItemWithEffects(@NotNull Material var0, @Nullable Color var1, @Nullable PotionEffect... var2) {
      Objects.requireNonNull(var0, "Cannot build an effected item with null type");
      if (!canHaveEffects(var0)) {
         throw new IllegalArgumentException("Cannot build item with " + var0.name() + " potion type");
      } else {
         ItemStack var3 = new ItemStack(var0);
         PotionMeta var4 = (PotionMeta)var3.getItemMeta();
         var4.setColor(var1);
         var4.setDisplayName(var0 == Material.POTION ? "Potion" : (var0 == Material.SPLASH_POTION ? "Splash Potion" : (var0 == Material.TIPPED_ARROW ? "Tipped Arrow" : "Lingering Potion")));
         if (var2 != null) {
            PotionEffect[] var5 = var2;
            int var6 = var2.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               PotionEffect var8 = var5[var7];
               var4.addCustomEffect(var8, true);
            }
         }

         var3.setItemMeta(var4);
         return var3;
      }
   }

   public static boolean canHaveEffects(@Nullable Material var0) {
      return var0 != null && (var0.name().endsWith("POTION") || var0.name().startsWith("TIPPED_ARROW"));
   }

   @Nullable
   public PotionEffectType getPotionEffectType() {
      return this.potionEffectType;
   }

   public String[] getNames() {
      return new String[]{this.name()};
   }

   @Nullable
   public PotionEffectType get() {
      return this.potionEffectType;
   }

   @Nullable
   public PotionType getPotionType() {
      return this.potionType;
   }

   @Nullable
   public PotionEffect buildPotionEffect(int var1, int var2) {
      return this.potionEffectType == null ? null : new PotionEffect(this.potionEffectType, var1, var2 - 1);
   }

   public String toString() {
      return this.friendlyName();
   }

   // $FF: synthetic method
   private static XPotion[] $values() {
      return new XPotion[]{ABSORPTION, BAD_OMEN, BLINDNESS, CONDUIT_POWER, DARKNESS, DOLPHINS_GRACE, FIRE_RESISTANCE, GLOWING, HASTE, HEALTH_BOOST, HERO_OF_THE_VILLAGE, HUNGER, INFESTED, INSTANT_DAMAGE, INSTANT_HEALTH, INVISIBILITY, JUMP_BOOST, LEVITATION, LUCK, MINING_FATIGUE, NAUSEA, NIGHT_VISION, OOZING, POISON, RAID_OMEN, REGENERATION, RESISTANCE, SATURATION, SLOWNESS, SLOW_FALLING, SPEED, STRENGTH, TRIAL_OMEN, UNLUCK, WATER_BREATHING, WEAKNESS, WEAVING, WIND_CHARGED, WITHER};
   }

   static {
      XPotion[] var0 = VALUES;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         XPotion var3 = var0[var2];
         if (var3.potionEffectType != null) {
            POTIONEFFECTTYPE_MAPPING[var3.potionEffectType.getId()] = var3;
         }
      }

      REGISTRY = XPotion.Data.REGISTRY;
      REGISTRY.discardMetadata();
   }

   private static final class Data {
      private static final Map<PotionType, XPotion> POTION_TYPE_MAPPING = new EnumMap(PotionType.class);
      private static final XRegistry<XPotion, PotionEffectType> REGISTRY = new XRegistry(PotionEffectType.class, XPotion.class, () -> {
         return Registry.EFFECT;
      }, (BiFunction)null, (var0) -> {
         return new XPotion[var0];
      });
   }

   public static class Effect {
      private PotionEffect effect;
      private double chance;

      public Effect(PotionEffect var1, double var2) {
         this.effect = var1;
         this.chance = var2;
      }

      public XPotion getXPotion() {
         return XPotion.of(this.effect.getType());
      }

      public double getChance() {
         return this.chance;
      }

      public boolean hasChance() {
         return this.chance >= 100.0D || ThreadLocalRandom.current().nextDouble(0.0D, 100.0D) <= this.chance;
      }

      public void setChance(double var1) {
         this.chance = var1;
      }

      public void apply(LivingEntity var1) {
         if (this.hasChance()) {
            var1.addPotionEffect(this.effect);
         }

      }

      public PotionEffect getEffect() {
         return this.effect;
      }

      public void setEffect(PotionEffect var1) {
         this.effect = var1;
      }
   }
}
