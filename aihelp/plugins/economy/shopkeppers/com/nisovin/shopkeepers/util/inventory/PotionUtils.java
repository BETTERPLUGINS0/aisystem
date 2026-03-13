package com.nisovin.shopkeepers.util.inventory;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.bukkit.MinecraftEnumUtils;
import com.nisovin.shopkeepers.util.bukkit.NamespacedKeyUtils;
import com.nisovin.shopkeepers.util.bukkit.RegistryUtils;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class PotionUtils {
   private static final Map<String, PotionType> POTION_TYPE_ALIASES = new HashMap();
   public static Map<? extends String, ? extends PotionType> POTION_TYPE_ALIASES_VIEW;

   @Nullable
   public static PotionType parsePotionType(String input) {
      Validate.notNull(input, (String)"input is null");
      NamespacedKey namespacedKey = NamespacedKeyUtils.parse(input);
      if (namespacedKey == null) {
         return null;
      } else if (!namespacedKey.getNamespace().equals("minecraft")) {
         return null;
      } else {
         String potionName = namespacedKey.getKey();
         PotionType potionType = (PotionType)POTION_TYPE_ALIASES.get(potionName);
         if (potionType != null) {
            return potionType;
         } else {
            String potionEnumName = MinecraftEnumUtils.normalizeEnumName(potionName);

            try {
               return PotionType.valueOf(potionEnumName);
            } catch (IllegalArgumentException var6) {
               return null;
            }
         }
      }
   }

   public static ItemStack parsePotionItem(String input) {
      Validate.notNull(input, (String)"input is null");
      if (input.isEmpty()) {
         return null;
      } else {
         String formattedInput = NamespacedKeyUtils.normalizeMinecraftNamespacedKey(input);
         List<String> words = (List)Unsafe.castNonNull(Arrays.asList(formattedInput.split("_")));
         CollectionUtils.replace(words, "potion", (Object)null);
         CollectionUtils.replace(words, "of", (Object)null);
         Material itemType;
         if (CollectionUtils.replace(words, "arrow", (Object)null)) {
            itemType = Material.TIPPED_ARROW;
            CollectionUtils.replace(words, "tipped", (Object)null);
         } else if (CollectionUtils.replace(words, "splash", (Object)null)) {
            itemType = Material.SPLASH_POTION;
         } else if (CollectionUtils.replace(words, "lingering", (Object)null)) {
            itemType = Material.LINGERING_POTION;
         } else {
            itemType = Material.POTION;
         }

         assert itemType != null;

         boolean isLong = CollectionUtils.replace(words, "long", (Object)null);
         boolean isStrong = CollectionUtils.replace(words, "strong", (Object)null) || CollectionUtils.replace(words, "2", (Object)null) || CollectionUtils.replace(words, "ii", (Object)null);
         String potionTypeInput = (String)words.stream().filter(Objects::nonNull).map(Unsafe::assertNonNull).collect(Collectors.joining("_"));
         PotionType potionType = parsePotionType(potionTypeInput);
         if (potionType == null) {
            return null;
         } else {
            if (isLong && !potionType.isExtendable()) {
               isLong = false;
            }

            if (isStrong && !potionType.isUpgradeable()) {
               isStrong = false;
            }

            if (isLong && isStrong) {
               isStrong = false;
            }

            PotionType strongPotionType;
            if (isLong) {
               strongPotionType = getLongPotionType(potionType);
               if (strongPotionType != null) {
                  potionType = strongPotionType;
               }
            } else if (isStrong) {
               strongPotionType = getStrongPotionType(potionType);
               if (strongPotionType != null) {
                  potionType = strongPotionType;
               }
            }

            ItemStack item = new ItemStack(itemType, 1);
            item = setPotionType(item, potionType);
            return item;
         }
      }
   }

   @Nullable
   public static PotionType getLongPotionType(PotionType potionType) {
      NamespacedKey key = RegistryUtils.getKeyOrThrow(potionType);
      if (key.getKey().startsWith("long_")) {
         return potionType;
      } else if (!potionType.isExtendable()) {
         return null;
      } else {
         Registry<PotionType> potionRegistry = Compat.getProvider().getRegistry(PotionType.class);
         return (PotionType)potionRegistry.get(NamespacedKeyUtils.create(key.getNamespace(), "long_" + key.getKey()));
      }
   }

   @Nullable
   public static PotionType getStrongPotionType(PotionType potionType) {
      NamespacedKey key = RegistryUtils.getKeyOrThrow(potionType);
      if (key.getKey().startsWith("strong_")) {
         return potionType;
      } else if (!potionType.isUpgradeable()) {
         return null;
      } else {
         Registry<PotionType> potionRegistry = Compat.getProvider().getRegistry(PotionType.class);
         return (PotionType)potionRegistry.get(NamespacedKeyUtils.create(key.getNamespace(), "strong_" + key.getKey()));
      }
   }

   public static ItemStack setPotionType(@ReadWrite ItemStack itemStack, PotionType potionType) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      Validate.notNull(potionType, (String)"potionType is null");
      ItemMeta itemMeta = itemStack.getItemMeta();
      if (itemMeta instanceof PotionMeta) {
         PotionMeta potionMeta = (PotionMeta)itemMeta;
         potionMeta.setBasePotionType(potionType);
         itemStack.setItemMeta(potionMeta);
      }

      return itemStack;
   }

   public static boolean equalsIgnoreDuration(PotionEffect effect1, PotionEffect effect2) {
      if (effect1 == effect2) {
         return true;
      } else {
         return effect1.getType().equals(effect2.getType()) && effect1.isAmbient() == effect2.isAmbient() && effect1.getAmplifier() == effect2.getAmplifier() && effect1.hasParticles() == effect2.hasParticles() && effect1.hasIcon() == effect2.hasIcon();
      }
   }

   @Nullable
   public static PotionEffect findIgnoreDuration(Collection<? extends PotionEffect> effects, PotionEffect effect) {
      Iterator var2 = effects.iterator();

      PotionEffect collectionEffect;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         collectionEffect = (PotionEffect)var2.next();
      } while(!equalsIgnoreDuration(collectionEffect, effect));

      return collectionEffect;
   }

   private PotionUtils() {
   }

   static {
      POTION_TYPE_ALIASES_VIEW = Collections.unmodifiableMap(POTION_TYPE_ALIASES);
      POTION_TYPE_ALIASES.put("jump", PotionType.LEAPING);
      POTION_TYPE_ALIASES.put("speed", PotionType.SWIFTNESS);
      POTION_TYPE_ALIASES.put("instant_heal", PotionType.HEALING);
      POTION_TYPE_ALIASES.put("instant_damage", PotionType.HARMING);
      POTION_TYPE_ALIASES.put("regen", PotionType.REGENERATION);
   }
}
